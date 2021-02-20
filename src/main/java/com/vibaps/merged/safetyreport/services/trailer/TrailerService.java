package com.vibaps.merged.safetyreport.services.trailer;

import java.sql.SQLException;
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
import com.vibaps.merged.safetyreport.ExeptionHandler;
import com.vibaps.merged.safetyreport.builder.GeoTabRequestBuilder;
import com.vibaps.merged.safetyreport.builder.Uri;
import com.vibaps.merged.safetyreport.common.AppConstants;
import com.vibaps.merged.safetyreport.common.EntityType;
import com.vibaps.merged.safetyreport.dao.gl.CommonGeotabDAO;
import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerResponce;
import com.vibaps.merged.safetyreport.entity.gl.ComDatabase;
import com.vibaps.merged.safetyreport.entity.gl.GlRulelistEntity;
import com.vibaps.merged.safetyreport.repo.gl.ComDatabaseRepository;
import com.vibaps.merged.safetyreport.repo.gl.CommonGeotabRepository;
import com.vibaps.merged.safetyreport.repo.gl.GenDeviceRepository;
import com.vibaps.merged.safetyreport.repo.gl.GenTrailerRepository;
import com.vibaps.merged.safetyreport.services.gl.GeoTabApiService;
import com.vibaps.merged.safetyreport.util.ResponseUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2

public class TrailerService {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private GenDeviceRepository genDeviceRepository;
	@Autowired
	private GenTrailerRepository genTrailerRepository;
	@Autowired
	private CommonGeotabRepository commonGeotabRepository;
	@Autowired
	private ComDatabaseRepository comDatabaseRepository;
	
	@Autowired
	private CommonGeotabDAO commonGeotabDAO;
	
    private TrailerResponce trailerResponce;
	
	
	@Autowired
	private GeoTabApiService geoTabApiService;
	
	public TrailerResponce showReport(TrailerParams trailerParams)  {
		List<JsonObject> lisrResponce=new ArrayList<JsonObject>();
		
		String deviceId = trailerParams.getDeviceId();
		
		
	    String[] deviceArray = deviceId.split(",");
	    boolean isAllDevice=deviceArray[0].equalsIgnoreCase("selectalldevice");
	    int usecase;
	    
		String payload =null;
		Long comDatabaseId;
		
		if(comDatabaseRepository.countdatabaseName(trailerParams.getGeotabDatabase()) > 0)
		{
		 comDatabaseId=comDatabaseRepository.findBydatabaseName(trailerParams.getGeotabDatabase()).getId();
		}else
		{
		
			comDatabaseId=insertDeviceandTrailer(trailerParams);
		}
		
		
		
		for(int l=0;l<deviceArray.length;l++)
		{
			
				trailerParams.setDeviceId(deviceArray[l]);
		
			payload=getReportRequest(trailerParams,1);
			
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

		return convertParsedReponseShow(lisrResponce,trailerParams,comDatabaseId);
	}
	
	private TrailerResponce convertParsedReponseShow(List<JsonObject> parsedResponse,TrailerParams trailerParams,Long comDatabaseId) 
	{
		// TODO Auto-generated method stub
		 TrailerResponce trailerResponceReturn = null;
		
		String deviceName;
		String trailerName;
		String trailerId = trailerParams.getTrailerId();
		String[] trailerArray = trailerId.split(",");
		
		

		boolean exist=false;
		TrailerResponce latlngFrom,latlngTo,addresFrom,addresTo;
		//TrailerResponce trailerResponce=null;
	    
		
	    
		
		
		List<TrailerResponce> responcelist=new ArrayList<TrailerResponce>();
		
		for(int t=0;t<parsedResponse.size();t++)
		{
		JsonObject data = new Gson().fromJson(parsedResponse.get(t), JsonObject.class);
	    JsonArray names = data.get("result").getAsJsonArray();
	    
	    
	    for(int i=0;i<names.size();i++){
	    	JsonObject device=names.get(i).getAsJsonObject();

	    		 exist = Arrays.stream(trailerArray).anyMatch(device.get("trailer").getAsJsonObject().get("id").getAsString()::equals);
	    		
	    		if(exist)
	    		{
	    			
	    			latlngFrom= getAddresslat(device.get("activeFrom").getAsString(), trailerParams,device.get("device").getAsJsonObject().get("id").
							  getAsString());
		    		
		    		 latlngTo= getAddresslat(device.get("activeTo").getAsString(), trailerParams,device.get("device").getAsJsonObject().get("id").
							  getAsString());
		    		 
		    		 
		    		 
		    		if(latlngFrom.getLatitude().equals(latlngTo.getLatitude()) && latlngFrom.getLongitude().equals(latlngTo.getLongitude()))
		    		{
		    		 addresFrom=getAddress(latlngFrom, trailerParams);
		    		 addresTo=addresFrom;
		    		}else
		    		{
		    			 addresFrom=getAddress(latlngFrom, trailerParams);
			    		 addresTo=getAddress(latlngTo, trailerParams);
		    			
		    		}
		    		
		    		
		    		if(genDeviceRepository.countdeviceIdAndrefComDatabaseId(device.get("device").getAsJsonObject().get("id").
							  getAsString(),comDatabaseId) > 0)
		    		
		    		{
		    		  deviceName=genDeviceRepository.findBydeviceIdAndrefComDatabaseId(device.get("device").getAsJsonObject().get("id").
							  getAsString(),comDatabaseId).getDeviceName();
		    		}else
		    		{

		    			deviceName=	commonGeotabDAO.insertMissedGeoTabDevice(comDatabaseId, trailerParams, device.get("device").getAsJsonObject().get("id").
								  getAsString()).getDeviceName();
					}
		    		
		    		if(genTrailerRepository.counttrailerIdAndrefComDatabaseId(device.get("trailer").getAsJsonObject().get("id").
			  				  getAsString(),comDatabaseId) > 0)
		    		{
		    		  trailerName=genTrailerRepository.findBytrailerIdAndrefComDatabaseId(device.get("trailer").getAsJsonObject().get("id").
			  				  getAsString(),comDatabaseId).getTrailerName();
		    		}
		    		else 
		    		{
		    			
		    			trailerName=commonGeotabDAO.insertGeoTabMissedTrailer(comDatabaseId, trailerParams, device.get("trailer").getAsJsonObject().get("id").
				  				  getAsString()).getTrailerName();
					}
		    		
		    		trailerResponceReturn=new TrailerResponce(device.get("activeFrom").getAsString(),device.get("activeTo").getAsString(),trailerName,deviceName,addresFrom.getFormattedAddress(),addresTo.getFormattedAddress());
		    		
		    		
		    		if(trailerResponceReturn!=null)
			    	{
			        responcelist.add(trailerResponceReturn);
			    	}
		    		
	    		}
	    		else
	    		{
	    			System.out.println("IDSssss"+device.get("trailer").getAsJsonObject().get("id").getAsString());
	    		}
	    	
	    	
	    }
		}
		
		//System.out.println(responcelist);
		return  new TrailerResponce(responcelist);
	}
	


	
	private Long insertDeviceandTrailer(TrailerParams trailerParams) {
		
		Long comDatabaseId=commonGeotabDAO.insertDevice(trailerParams).getId();
			commonGeotabDAO.insertTrailer(trailerParams);
		
		return comDatabaseId;
		
		
	}

	public TrailerResponce getDevice(TrailerParams trailerParams) {
		// TODO Auto-generated method stub
		
		
		String payload =  getReportRequest(trailerParams,4);
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
	
	public TrailerResponce convertParsedReponse(JsonObject parsedResponse) {
		
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

	public TrailerResponce getTrailer(TrailerParams trailerParams) {
		// TODO Auto-generated method stub
		
		
		String payload =  getReportRequest(trailerParams,5);
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
				//.groups(geoTabApiService.getGroupList(trailerParams))
				.activeFrom(trailerParams.getActiveFrom())
				.activeTo(trailerParams.getActiveTo())
				.deviceSearch()
				.id(trailerParams.getDeviceId())
				.build();
		case 2 :return builder.params().typeName(trailerParams.getTypeName()).search()
				//.groups(geoTabApiService.getGroupList(trailerParams))
				.deviceSearch()
				.id(trailerParams.getDeviceId())
				.build();
		case 3 :return builder.params().typeName(trailerParams.getTypeName()).search()
				.activeFrom(trailerParams.getActiveFrom()).
				activeTo(trailerParams.getActiveTo())
				.build();
		case 4 : return builder.params().typeName(trailerParams.getTypeName())
				.search().groups(geoTabApiService.getGroupList(trailerParams))
				.fromDate(trailerParams.getFromDate()+builder.FROM_TS_SUFFIX).resultsLimit(AppConstants.RESULTS_LIMIT)
				.build();
		case 5 : return builder.params().typeName(trailerParams.getTypeName())
				.search().groups(geoTabApiService.getGroupList(trailerParams))
				.build();
		default : return builder.params().typeName(trailerParams.getTypeName()).build();
				
		}
	}
	
	public TrailerResponce getAddresslat(String timesRange,TrailerParams trailerParams,String deviceid) 
	{
		// TODO Auto-generated method stub
		
		
		String payload =  getLogPayload(timesRange,trailerParams,deviceid);
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
		return convertParsedReponseLatLng(parsedResponse);
		}
	
		public TrailerResponce getAddress(TrailerResponce latlng,TrailerParams trailerParams) 
		{
		
			String payload =  getAddressPayload(latlng,trailerParams);
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
		TrailerResponce address= convertParsedReponseAddress(parsedResponse);
		
		return address;
		
		
	}
	
	private TrailerResponce convertParsedReponseLatLng(JsonObject parsedResponse) {		
		JsonObject data = new Gson().fromJson(parsedResponse, JsonObject.class);
	    JsonArray names = data .get("result").getAsJsonArray();
	    JsonObject device=names.get(0).getAsJsonObject();      
	    trailerResponce=new TrailerResponce(device.get("latitude").getAsDouble(),device.get("longitude").getAsDouble());

		return trailerResponce;
	}
	
	private TrailerResponce convertParsedReponseAddress(JsonObject parsedResponse) {		
		JsonObject data = new Gson().fromJson(parsedResponse, JsonObject.class);
	    JsonArray names = data .get("result").getAsJsonArray();
	    JsonObject device=names.get(0).getAsJsonObject();      
	    trailerResponce=new TrailerResponce(device.get("formattedAddress").getAsString());

		return trailerResponce;
	}
	
	
	private String getLogPayload(String timesRange,TrailerParams trailerParams,String deviceId) 
	{
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		geoTabApiService.buildCredentials(builder, trailerParams);
		
		return builder.params().typeName("LogRecord").search().fromDate(timesRange).toDate(timesRange)
				.deviceSearch()
				.id(deviceId)
				.build();
		
	}
	
	private String getAddressPayload(TrailerResponce latlng,TrailerParams trailerParams) 
	{
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method("GetAddresses");
		geoTabApiService.buildCredentials(builder, trailerParams);
		return builder.params().movingAddresses(true).addcoordinates().
				x(latlng.getLongitude()).
				y(latlng.getLatitude())
				.build();
		
	}
	

	
	
	
	


	
	
	

	
	

}
