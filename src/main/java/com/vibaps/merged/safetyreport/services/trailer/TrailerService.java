package com.vibaps.merged.safetyreport.services.trailer;

import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
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
		List<TrailerResponce> lisrResponce=new ArrayList<TrailerResponce>();
		
		String deviceId = trailerParams.getDeviceId();
		
		
	    String[] deviceArray = deviceId.split(",");
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
		
		JsonObject data = new Gson().fromJson(parsedResponse, JsonObject.class);
	    JsonArray names = data.get("result").getAsJsonArray();
	    
	    for(int i=0;i<names.size();i++)
	    {
	    	JsonObject device=names.get(i).getAsJsonObject();
	    
		lisrResponce.add(new TrailerResponce(device.get("activeFrom").getAsString(),device.get("activeTo").getAsString(),device.get("trailer").getAsJsonObject().get("id").
				  getAsString(),device.get("device").getAsJsonObject().get("id").
				  getAsString()));

	    }
		}

		return convertParsedReponseShow(lisrResponce,trailerParams,comDatabaseId);
	}
	
	private TrailerResponce convertParsedReponseShow(List<TrailerResponce> parsedResponse,TrailerParams trailerParams,Long comDatabaseId) 
	{
		DecimalFormat df = new DecimalFormat("###.###");
		df.setRoundingMode(RoundingMode.DOWN);
		List<TrailerResponce> getAddressFromAndToResponce=new ArrayList<TrailerResponce>();
		
		String getZoneId=getZoneId(trailerParams);
		
		
		// TODO Auto-generated method stub
		 TrailerResponce trailerResponceReturn = null;
		 List<TrailerResponce> latlongResponce = null;
		 
		 
		
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
/*		JsonObject data = new Gson().fromJson(parsedResponse.get(t), JsonObject.class);
	    JsonArray names = data.get("result").getAsJsonArray();
	    
	    
	    for(int i=0;i<names.size();i++){
	    	JsonObject device=names.get(i).getAsJsonObject();*/

	    		 exist = Arrays.stream(trailerArray).anyMatch(parsedResponse.get(t).getTrailerId()::equals);
	    		
	    		if(exist)
	    		{
	    			
//	    			latlngFrom= getAddresslat(parsedResponse.get(t).getActiveFrom(), trailerParams,parsedResponse.get(t).getDeviceId());
//		    		latlngTo= getAddresslat(parsedResponse.get(t).getActiveTo(),trailerParams,parsedResponse.get(t).getDeviceId());
//		    		 
		    		latlongResponce=getAddresslatlng(parsedResponse.get(t).getActiveFrom(),parsedResponse.get(t).getActiveTo(), trailerParams,parsedResponse.get(t).getDeviceId());	
		    		
		    		latlngFrom=latlongResponce.get(0);
		    		latlngTo=latlongResponce.get(1);
		    		 
		    		if(df.format(latlngFrom.getLatitude()).equals(df.format(latlngTo.getLatitude())) && df.format(latlngFrom.getLongitude()).equals(df.format(latlngTo.getLongitude())))
		    		{
		    		 addresFrom=getAddress(latlngFrom, trailerParams);
		    		 addresTo=addresFrom;
		    		}else
		    		{
						/*
						 * addresFrom=getAddress(latlngFrom, trailerParams);
						 * addresTo=getAddress(latlngTo, trailerParams);
						 */
		    			 getAddressFromAndToResponce=getAddressFromAndTo(latlngFrom,latlngTo,trailerParams);
		    			addresFrom=getAddressFromAndToResponce.get(0);
		    			addresTo=getAddressFromAndToResponce.get(1);
		    			
		    		}
		    		
		    		
		    		if(genDeviceRepository.countdeviceIdAndrefComDatabaseId(parsedResponse.get(t).getDeviceId(),comDatabaseId) > 0)
		    		
		    		{
		    		  deviceName=genDeviceRepository.findBydeviceIdAndrefComDatabaseId(parsedResponse.get(t).getDeviceId(),comDatabaseId).getDeviceName();
		    		}else
		    		{

		    			deviceName=	commonGeotabDAO.insertMissedGeoTabDevice(comDatabaseId, trailerParams, parsedResponse.get(t).getDeviceId()).getDeviceName();
					}
		    		
		    		if(genTrailerRepository.counttrailerIdAndrefComDatabaseId(parsedResponse.get(t).getTrailerId(),comDatabaseId) > 0)
		    		{
		    		  trailerName=genTrailerRepository.findBytrailerIdAndrefComDatabaseId(parsedResponse.get(t).getTrailerId(),comDatabaseId).getTrailerName();
		    		}
		    		else 
		    		{
		    			
		    			trailerName=commonGeotabDAO.insertGeoTabMissedTrailer(comDatabaseId, trailerParams, parsedResponse.get(t).getTrailerId()).getTrailerName();
					}
		    		
		    		String totime=getZoneTime(getZoneId,parsedResponse.get(t).getActiveTo());
		    		String toaddress="-";
		    		if(!totime.equals("-"))
		    		{
		    			toaddress=addresTo.getFormattedAddress();	
		    		}
		    		
		    		trailerResponceReturn=new TrailerResponce(getZoneTime(getZoneId,parsedResponse.get(t).getActiveFrom()),totime,trailerName,deviceName,addresFrom.getFormattedAddress(),toaddress);
		    		
		    		
		    		if(trailerResponceReturn!=null)
			    	{
			        responcelist.add(trailerResponceReturn);
			    	}
		    		
	    		}
	    		
	    	
	    	
	    }
		/* } */
		
		//System.out.println(responcelist);
		return  new TrailerResponce(responcelist);
	}
	
	@Cacheable(value = "getAddresslatlng", unless = "#ts0=='getAddresslatlng'")
	public List<TrailerResponce> getAddresslatlng(String activeFrom,String activeTo,TrailerParams trailerParams,String deviceid) 
	{
		// TODO Auto-generated method stub
		
		
		String payload =  getLogPayloadFromTo(activeFrom,activeTo,trailerParams,deviceid);
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
		return convertParsedReponseLatLngnew(parsedResponse);
		}
	
	
	@Cacheable(value = "getZoneTime", unless = "#ts1=='getZoneTime'")
	private String getZoneTime(String timeZoneId,String utcTime)
	{
		
	    DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
	    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy hh.mm a");

	    
	     ZonedDateTime parsed = ZonedDateTime.parse(utcTime, formatter.withZone(ZoneId.of(timeZoneId)));
	     if(parsed.getYear()>Calendar.getInstance().get(Calendar.YEAR))
	     {
	    	 return "-";
	     }
	     return  parsed.format(formatter1);
	}
	
	
	private String getZoneId(TrailerParams trailerParams)
	{
		String payload =  getReportRequest(trailerParams,6);
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
		
		
		
		return  parsezoneId(parsedResponse);
	}

	public String parsezoneId(JsonObject parsedResponse) 
	{
		
		List<TrailerResponce> responcelist=new ArrayList<>();
		
		JsonObject data = new Gson().fromJson(parsedResponse, JsonObject.class);
	    JsonArray names = data .get("result").getAsJsonArray();
	    
	    String zoneId=names.get(0).getAsJsonObject().get("timeZoneId").getAsString();
	    	  
		return zoneId;
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
		case 6 : return builder.params().typeName("User")
				.search().name(trailerParams.getGeotabUserName())
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
	
		@Cacheable(value = "getAddress", unless = "#ts2=='getAddress'")
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
		
		@Cacheable(value = "getAddressFromAndTo", unless = "#ts3=='getAddressFromAndTo'")
		public List<TrailerResponce> getAddressFromAndTo(TrailerResponce fromlatlng,TrailerResponce tolatlng,TrailerParams trailerParams) 
		{
		
			String payload =  getAddressPayloadFromAndTo(fromlatlng,tolatlng,trailerParams);
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
		 
		
		return convertParsedReponseAddressFromAndTo(parsedResponse);
		
		
	}	
	
	private TrailerResponce convertParsedReponseLatLng(JsonObject parsedResponse) {		
		JsonObject data = new Gson().fromJson(parsedResponse, JsonObject.class);
	    JsonArray names = data .get("result").getAsJsonArray();
	    JsonObject device=names.get(0).getAsJsonObject(); 
	   return new TrailerResponce(names.get(0).getAsJsonObject().get("latitude").getAsDouble(),names.get(0).getAsJsonObject().get("longitude").getAsDouble());
	}
	
	private List<TrailerResponce> convertParsedReponseLatLngnew(JsonObject parsedResponse) {		
		List<TrailerResponce> responce=new ArrayList<TrailerResponce>();
		JsonObject data = new Gson().fromJson(parsedResponse, JsonObject.class);
	    JsonArray names = data .get("result").getAsJsonArray();
	    
	    responce.add(new TrailerResponce(names.get(0).getAsJsonArray().get(0).getAsJsonObject().get("latitude").getAsDouble(),names.get(0).getAsJsonArray().get(0).getAsJsonObject().get("longitude").getAsDouble()));
	    responce.add(new TrailerResponce(names.get(1).getAsJsonArray().get(0).getAsJsonObject().get("latitude").getAsDouble(),names.get(1).getAsJsonArray().get(0).getAsJsonObject().get("longitude").getAsDouble()));

	    
	    
		return responce;
	}
	
	private TrailerResponce convertParsedReponseAddress(JsonObject parsedResponse) {		
		JsonObject data = new Gson().fromJson(parsedResponse, JsonObject.class);
	    JsonArray names = data .get("result").getAsJsonArray();
	    JsonObject device=names.get(0).getAsJsonObject();      
	    trailerResponce=new TrailerResponce(device.get("formattedAddress").getAsString());

		return trailerResponce;
	}
	
	private List<TrailerResponce> convertParsedReponseAddressFromAndTo(JsonObject parsedResponse) {		
		List<TrailerResponce> address=new ArrayList<TrailerResponce>();
		
		JsonObject data = new Gson().fromJson(parsedResponse, JsonObject.class);
	    JsonArray names = data .get("result").getAsJsonArray();
	    address.add(new TrailerResponce(names.get(0).getAsJsonObject().get("formattedAddress").getAsString()));
	    address.add(new TrailerResponce(names.get(0).getAsJsonObject().get("formattedAddress").getAsString()));
		return address;
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
	
	private String getLogPayloadFromTo(String activeFrom,String activeTo,TrailerParams trailerParams,String deviceId) 
	{
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_EXECUTE_MULTI_CALL);
		geoTabApiService.buildCredentials(builder, trailerParams);
		
		return builder.params().addCalls().method(AppConstants.METHOD_GET).params().typeName("LogRecord").search().fromDate(activeFrom).toDate(activeFrom)
				.deviceSearch()
				.id(deviceId).and().and().and().done().addCalls().method(AppConstants.METHOD_GET).params().typeName("LogRecord").search().fromDate(activeTo).toDate(activeTo)
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
	
	private String getAddressPayloadFromAndTo(TrailerResponce fromLatlng,TrailerResponce toLatlng,TrailerParams trailerParams) 
	{
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method("GetAddresses");
		geoTabApiService.buildCredentials(builder, trailerParams);
		return builder.params().movingAddresses(true).addcoordinates()
				.x(fromLatlng.getLongitude())
				.y(fromLatlng.getLatitude()).and().addcoordinates()
				.x(toLatlng.getLongitude())
				.y(toLatlng.getLatitude())
				.build();
		
	}
	

	
	
	
	


	
	
	

	
	

}
