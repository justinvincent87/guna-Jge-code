package com.vibaps.merged.safetyreport.services.trailer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vibaps.merged.safetyreport.builder.GeoTabRequestBuilder;
import com.vibaps.merged.safetyreport.builder.Uri;
import com.vibaps.merged.safetyreport.common.AppConstants;
import com.vibaps.merged.safetyreport.common.EntityType;
import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerResponce;
import com.vibaps.merged.safetyreport.entity.gl.GlRulelistEntity;
import com.vibaps.merged.safetyreport.services.gl.GeoTabApiService;
import com.vibaps.merged.safetyreport.util.ResponseUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2

public class TrailerService {

	@Autowired
	private RestTemplate restTemplate;
	
    private TrailerResponce trailerResponce;
	
	
	@Autowired
	private GeoTabApiService geoTabApiService;
	
	public TrailerResponce showReport(TrailerParams trailerParams) {
		List<JsonObject> lisrResponce=new ArrayList<JsonObject>();
		
		String deviceId = trailerParams.getDeviceId();
		
	    String[] deviceArray = deviceId.split(",");
	    boolean isAllDevice=deviceArray[0].equalsIgnoreCase("selectalldevice");
	    int usecase;
	    
		String payload =null;
		if((trailerParams.getActiveFrom() == null) && (trailerParams.getActiveTo() == null))
		{
			if(isAllDevice)
			{
			usecase=-1;
			}
			else
			{
				usecase=2;
			}
		}
		else if((trailerParams.getActiveFrom() != null) && (trailerParams.getActiveTo() != null) && (isAllDevice))	
		{
			usecase=3;
		}
		else
		{
			usecase=1;
		}
		

		if(!isAllDevice)
		{
		for(int l=0;l<=deviceArray.length;l++)
		{
			trailerParams.setDeviceId(deviceArray[l]);
		
			payload=getReportRequest(trailerParams,usecase);
			
		if (log.isDebugEnabled()) {
			log.debug("Get report data payload: {}", payload);
		}

		String uri = Uri.get().secure().add(trailerParams.getUrl()).add(AppConstants.PATH_VERSION).build();
		if (log.isDebugEnabled()) {
			log.debug("Get report data uri: {}", uri);
		}

		ResponseEntity<String> response = restTemplate.postForEntity(uri, payload, String.class);
		if (log.isDebugEnabled()) {
			log.debug("Get report data response code: {}", response.getStatusCodeValue());
		}

		JsonObject parsedResponse = ResponseUtil.parseResponse(response);
		
		lisrResponce.add(parsedResponse);
		}
		}
		else
		{
			trailerParams.setDeviceId(deviceArray[0]);
			
			payload=getReportRequest(trailerParams,usecase);
			
		if (log.isDebugEnabled()) {
			log.debug("Get report data payload: {}", payload);
		}

		String uri = Uri.get().secure().add(trailerParams.getUrl()).add(AppConstants.PATH_VERSION).build();
		if (log.isDebugEnabled()) {
			log.debug("Get report data uri: {}", uri);
		}

		ResponseEntity<String> response = restTemplate.postForEntity(uri, payload, String.class);
		if (log.isDebugEnabled()) {
			log.debug("Get report data response code: {}", response.getStatusCodeValue());
		}

		JsonObject parsedResponse = ResponseUtil.parseResponse(response);
		
		lisrResponce.add(parsedResponse);
		}
		
		return convertParsedReponseShow(lisrResponce,trailerParams);
	}
	
	private TrailerResponce convertParsedReponseShow(List<JsonObject> parsedResponse,TrailerParams trailerParams) {
		// TODO Auto-generated method stub
		String trailerId = trailerParams.getTrailerId();
		String[] trailerArray = trailerId.split(",");
		boolean trailerCheck=false;
		boolean exist=false;
		//TrailerResponce trailerResponce=null;
	    
		if(!trailerArray[0].equalsIgnoreCase("selectalltrailer"))
		{
			trailerCheck=true;
		}
	    
		
		
		List<TrailerResponce> responcelist=new ArrayList<TrailerResponce>();
		
		for(int t=0;t<parsedResponse.size();t++)
		{
		JsonObject data = new Gson().fromJson(parsedResponse.get(t), JsonObject.class);
	    JsonArray names = data.get("result").getAsJsonArray();
	    
	    
	    for(int i=0;i<names.size();i++){
	    	JsonObject device=names.get(i).getAsJsonObject();
	    	
	    	if(!trailerCheck)
	    	{
	    		 trailerResponce=new TrailerResponce(device.get("activeFrom").getAsString(),device.get("activeTo").getAsString(),device.get("trailer").getAsJsonObject().get("id").
		  				  getAsString(),device.get("device").getAsJsonObject().get("id").
						  getAsString());
	    		
	    	}
	    	else
	    	{
	    		 exist = Arrays.stream(trailerArray).anyMatch(device.get("trailer").getAsJsonObject().get("id").getAsString()::equals);
	    		
	    		if(exist)
	    		{
	    			
	    			trailerResponce=new TrailerResponce(device.get("activeFrom").getAsString(),device.get("activeTo").getAsString(),device.get("trailer").getAsJsonObject().get("id").
			  				  getAsString(),device.get("device").getAsJsonObject().get("id").
							  getAsString());
	    		}
	    	}
	    	if(trailerResponce!=null)
	    	{
	        responcelist.add(trailerResponce);
	    	}
	    }
		}
		
		
		return  new TrailerResponce(responcelist);
	}
	


	@Cacheable("Device")
	public TrailerResponce getDevice(TrailerParams trailerParams) {
		// TODO Auto-generated method stub
		
		
		String payload =  getReportRequest(trailerParams,-1);
		if (log.isDebugEnabled()) {
			log.debug("Get report data payload: {}", payload);
		}

		String uri = Uri.get().secure().add(trailerParams.getUrl()).add(AppConstants.PATH_VERSION).build();
		if (log.isDebugEnabled()) {
			log.debug("Get report data uri: {}", uri);
		}

		ResponseEntity<String> response = restTemplate.postForEntity(uri, payload, String.class);
		if (log.isDebugEnabled()) {
			log.debug("Get report data response code: {}", response.getStatusCodeValue());
		}

		//return response;
		JsonObject parsedResponse = ResponseUtil.parseResponse(response);
		return convertParsedReponse(parsedResponse);
	}
	
	private TrailerResponce convertParsedReponse(JsonObject parsedResponse) {
		// TODO Auto-generated method stub
		
		List<TrailerResponce> responcelist=new ArrayList<>();
		
		JsonObject data = new Gson().fromJson(parsedResponse, JsonObject.class);
	    JsonArray names = data .get("result").getAsJsonArray();
	    
	    
	    for(int i=0;i<names.size();i++){
	    	JsonObject device=names.get(i).getAsJsonObject();
	    	
	       
	    	trailerResponce=new TrailerResponce(device.get("id").getAsString(),device.get("name").getAsString());
	    	
	   
	        responcelist.add(trailerResponce);
	        
	    }
	  
		return new TrailerResponce(responcelist);
	}

	@Cacheable("trailer")
	public TrailerResponce getTrailer(TrailerParams trailerParams) {
		// TODO Auto-generated method stub
		
		
		String payload =  getReportRequest(trailerParams,-1);
		if (log.isDebugEnabled()) {
			log.debug("Get report data payload: {}", payload);
		}

		String uri = Uri.get().secure().add(trailerParams.getUrl()).add(AppConstants.PATH_VERSION).build();
		if (log.isDebugEnabled()) {
			log.debug("Get report data uri: {}", uri);
		}

		ResponseEntity<String> response = restTemplate.postForEntity(uri, payload, String.class);
		if (log.isDebugEnabled()) {
			log.debug("Get report data response code: {}", response.getStatusCodeValue());
		}

		JsonObject parsedResponse = ResponseUtil.parseResponse(response);
		return convertParsedReponse(parsedResponse);
	}
	
	private String getReportRequest(TrailerParams trailerParams,int useCase) 
	{
		
		
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		// bind credentials
		geoTabApiService.buildCredentials(builder, trailerParams);
		
		switch(useCase)
		{
		case 1:return builder.params().typeName(trailerParams.getTypeName()).search()
				.activeFrom(trailerParams.getActiveFrom()).
				activeTo(trailerParams.getActiveTo())
				.deviceSearch()
				.id(trailerParams.getDeviceId())
				.build();
		case 2 :return builder.params().typeName(trailerParams.getTypeName()).search()
				.deviceSearch()
				.id(trailerParams.getDeviceId())
				.build();
		case 3 :return builder.params().typeName(trailerParams.getTypeName()).search()
				.activeFrom(trailerParams.getActiveFrom()).
				activeTo(trailerParams.getActiveTo())
				.build();
		default : return builder.params().typeName(trailerParams.getTypeName()).build();
		}
	}		

	
	
	

	
	

}
