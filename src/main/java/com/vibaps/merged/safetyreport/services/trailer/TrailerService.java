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
		Map<String, String> addressMap=new HashMap<String, String>();
		
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
		TrailerResponce latlngFrom,latlngTo;
		String addresFrom,addresTo;
		//TrailerResponce trailerResponce=null;
	    
		
	    
		
		
		List<TrailerResponce> responcelist=new ArrayList<TrailerResponce>();
		
		for(int t=0;t<parsedResponse.size();t++)
		{


	    		 exist = Arrays.stream(trailerArray).anyMatch(parsedResponse.get(t).getTrailerId()::equals);
	    		
	    		if(exist)
	    		{
	    			
			
		    		latlongResponce=getAddresslatlng(parsedResponse.get(t).getActiveFrom(),parsedResponse.get(t).getActiveTo(), trailerParams,parsedResponse.get(t).getDeviceId());	
		    		
		    		latlngFrom=latlongResponce.get(0);
		    		latlngTo=latlongResponce.get(1);
		    		 
		    		if(df.format(latlngFrom.getLatitude()).equals(df.format(latlngTo.getLatitude())) && df.format(latlngFrom.getLongitude()).equals(df.format(latlngTo.getLongitude())))
		    		{
		    			if(addressMap.get(df.format(latlngFrom.getLatitude())+"&"+df.format(latlngFrom.getLongitude()))!=null)
		    			{
		    				addresFrom=	addressMap.get(df.format(latlngFrom.getLatitude())+"&"+df.format(latlngFrom.getLongitude()));
		    				log.debug("MAP From Address: {}", addresFrom);
		    			}
		    			else
		    			{
		    				 addresFrom=getAddress(Double.valueOf(df.format(latlngFrom.getLatitude())),Double.valueOf(df.format(latlngFrom.getLongitude())), trailerParams).getFormattedAddress();
		    				 addressMap.put(df.format(latlngFrom.getLatitude())+"&"+df.format(latlngFrom.getLongitude()), addresFrom);
		 		    		
		    				 log.debug("From Address: {}", addresFrom);
		    			}
		    		 addresTo=addresFrom;
		    		 
		    		 
		    		}else
		    		{
						
		    			if(addressMap.get(df.format(latlngFrom.getLatitude())+"&"+df.format(latlngFrom.getLongitude()))!=null)
		    			{
		    				addresFrom=addressMap.get(df.format(latlngFrom.getLatitude())+"&"+df.format(latlngFrom.getLongitude()));
		    			}
		    			else
		    			{
		    				
			    			addresFrom=getAddress(Double.valueOf(df.format(latlngFrom.getLatitude())),Double.valueOf(df.format(latlngFrom.getLongitude())),trailerParams).getFormattedAddress();
			    			addressMap.put(df.format(latlngFrom.getLatitude())+"&"+df.format(latlngFrom.getLongitude()), addresFrom);
			    			
		    			}
		    			
		    			if(addressMap.get(df.format(latlngTo.getLatitude())+"&"+df.format(latlngTo.getLongitude()))!=null)
		    			{
		    				addresTo=addressMap.get(df.format(latlngTo.getLatitude())+"&"+df.format(latlngTo.getLongitude()));
		    				log.debug("MAP To Address: {}", addresTo);
		    			}
		    			else
		    			{
		    				addresTo=getAddress(Double.valueOf(df.format(latlngTo.getLatitude())),Double.valueOf(df.format(latlngTo.getLongitude())),trailerParams).getFormattedAddress();
			    			addressMap.put(df.format(latlngTo.getLatitude())+"&"+df.format(latlngTo.getLongitude()), addresTo);
			    			log.debug("To Address: {}", addresTo);
		    			}
		    			
		    			
		    			
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
		    			toaddress=addresTo;	
		    		}
		    		
		    		trailerResponceReturn=new TrailerResponce(getZoneTime(getZoneId,parsedResponse.get(t).getActiveFrom()),totime,trailerName,deviceName,addresFrom,toaddress);
		    		
		    		
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
	
	@Cacheable(value = "getAddresslatlng")
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
	
	
	@Cacheable(value = "getZoneTime")
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

	@Cacheable(value="getDevice")
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

	@Cacheable(value="getTrailer")
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
	
		@Cacheable(value = "getAddress")
		public TrailerResponce getAddress(Double fromLat,Double fromLng,TrailerParams trailerParams) 
		{
		
			String payload =  getAddressPayload(fromLat,fromLng,trailerParams);
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
	
	private String getAddressPayload(Double fromlat,Double fromlng,TrailerParams trailerParams) 
	{
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method("GetAddresses");
		geoTabApiService.buildCredentials(builder, trailerParams);
		return builder.params().movingAddresses(true).addcoordinates().
				x(fromlng).
				y(fromlat)
				.build();
		
	}
	
	private String getAddressPayloadFromAndTo(Double fromLat,Double fromlng,Double toLat,Double tolng,TrailerParams trailerParams) 
	{
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method("GetAddresses");
		geoTabApiService.buildCredentials(builder, trailerParams);
		return builder.params().movingAddresses(true).addcoordinates()
				.x(fromlng)
				.y(fromLat).and().addcoordinates()
				.x(tolng)
				.y(toLat)
				.build();
		
	}
	

	
	
	
	


	
	
	

	
	

}
