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
import com.vibaps.merged.safetyreport.entity.gl.GlRulelistEntity;
import com.vibaps.merged.safetyreport.util.ResponseUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TrailerService {

	@Autowired
	private RestTemplate restTemplate;
	
	public Object showReport(TrailerParams trailerParams) {
		// TODO Auto-generated method stub
		
		List<JsonObject> lisrResponce=new ArrayList<JsonObject>();
		
		String deviceId = trailerParams.getDeviceId();
		
	    String[] deviceArray = deviceId.split(",");
	    
	    int usecase=0;
	    
		String payload =null;
		if((trailerParams.getActiveFrom() == null) && (trailerParams.getActiveTo() == null) && (deviceArray[0].equalsIgnoreCase("selectalldevice")))
		{
			usecase=101;
		}
		else if((trailerParams.getActiveFrom() == null) && (trailerParams.getActiveTo() == null) && (!deviceArray[0].equalsIgnoreCase("selectalldevice")))	
		{
			usecase=2;
		}
		else if((trailerParams.getActiveFrom() != null) && (trailerParams.getActiveTo() != null) && (deviceArray[0].equalsIgnoreCase("selectalldevice")))	
		{
			usecase=3;
		}
		else
		{
			usecase=1;
		}
		
	
		
		
		for(int l=0;l<((deviceArray[0].equalsIgnoreCase("selectalldevice"))?1:deviceArray.length);l++)
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
		return convertParsedReponseShow(lisrResponce,trailerParams);
	}
	
	private Object convertParsedReponseShow(List<JsonObject> parsedResponse,TrailerParams trailerParams) {
		// TODO Auto-generated method stub
		String trailerId = trailerParams.getTrailerId();
		String[] trailerArray = trailerId.split(",");
		boolean trailerCheck=false;
		boolean exist=false;
	    
		if(!trailerArray[0].equalsIgnoreCase("selectalltrailer"))
		{
			trailerCheck=true;
		}
	    
		Map<String,String> deviceMap;
		Map<String,List<Map<String,String>>> fullResponce = new HashMap<String,List<Map<String,String>>>();
		List<Map<String,String>> responcelist=new ArrayList<>();
		
		for(int t=0;t<parsedResponse.size();t++)
		{
		JsonObject data = new Gson().fromJson(parsedResponse.get(t), JsonObject.class);
	    JsonArray names = data.get("result").getAsJsonArray();
	    
	    
	    for(int i=0;i<names.size();i++){
	    	JsonObject device=names.get(i).getAsJsonObject();
	    	deviceMap=new HashMap<String,String>();
	    	if(!trailerCheck)
	    	{
	        deviceMap.put("activeFrom",device.get("activeFrom").getAsString());
	        deviceMap.put("activeTo",device.get("activeTo").getAsString());
	        deviceMap.put("trailer_id",device.get("trailer").getAsJsonObject().get("id").getAsString());
	        deviceMap.put("device_id",device.get("device").getAsJsonObject().get("id").getAsString());
	    	}
	    	else
	    	{
	    		 exist = Arrays.stream(trailerArray).anyMatch(device.get("trailer").getAsJsonObject().get("id").getAsString()::equals);
	    		
	    		if(exist)
	    		{
	    		deviceMap.put("activeFrom",device.get("activeFrom").getAsString());
		        deviceMap.put("activeTo",device.get("activeTo").getAsString());
		        deviceMap.put("trailer_id",device.get("trailer").getAsJsonObject().get("id").getAsString());
		        deviceMap.put("device_id",device.get("device").getAsJsonObject().get("id").getAsString());
	    		}
	    	}
	    	if(!deviceMap.isEmpty())
	    	{
	        responcelist.add(deviceMap);
	    	}
	    }
		}
	    fullResponce.put("result", responcelist);
		return fullResponce;
	}
	
	@Cacheable("Device")
	public Object getDevice(TrailerParams trailerParams) {
		// TODO Auto-generated method stub
		
		
		String payload =  getReportRequest(trailerParams,100);
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
	
	private Object convertParsedReponse(JsonObject parsedResponse) {
		// TODO Auto-generated method stub
		
		Map<String,String> deviceMap;
		Map<String,List<Map<String,String>>> fullResponce = new HashMap<String,List<Map<String,String>>>();
		List<Map<String,String>> responcelist=new ArrayList<>();
		
		JsonObject data = new Gson().fromJson(parsedResponse, JsonObject.class);
	    JsonArray names = data .get("result").getAsJsonArray();
	    
	    
	    for(int i=0;i<names.size();i++){
	    	JsonObject device=names.get(i).getAsJsonObject();
	    	deviceMap=new HashMap<String,String>();
	        
	        deviceMap.put("id",device.get("id").getAsString());
	        deviceMap.put("name",device.get("name").getAsString());
	        
	        responcelist.add(deviceMap);
	        
	    }
	    fullResponce.put("result", responcelist);
		return fullResponce;
	}

	@Cacheable("trailer")
	public Object getTrailer(TrailerParams trailerParams) {
		// TODO Auto-generated method stub
		
		
		String payload =  getReportRequest(trailerParams,100);
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
		buildCredentials(builder, trailerParams);
		
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

	
	
	
	private void buildCredentials(GeoTabRequestBuilder builder, TrailerParams trailerParams) {

		builder.params().credentials().database(trailerParams.getGeotabDatabase())
		        .sessionId(trailerParams.getGeotabSessionId()).userName(trailerParams.getGeotabUserName());
	}
	
	

}
