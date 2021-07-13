package com.vibaps.merged.safetyreport.services.mobileapp;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.catalina.mapper.Mapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.vibaps.merged.safetyreport.builder.GeoTabRequestBuilder;
import com.vibaps.merged.safetyreport.builder.Uri;
import com.vibaps.merged.safetyreport.common.AppConstants;
import com.vibaps.merged.safetyreport.common.AppMsg;
import com.vibaps.merged.safetyreport.dto.dvir.FaultData;
import com.vibaps.merged.safetyreport.dto.gl.GeoTabReponse;
import com.vibaps.merged.safetyreport.dto.mobileapp.GeotabDeviceResponse;
import com.vibaps.merged.safetyreport.dto.mobileapp.GeotabDeviceStatusInfoBaseResponse;
import com.vibaps.merged.safetyreport.dto.mobileapp.GeotabDeviceStatusInfoResponse;
import com.vibaps.merged.safetyreport.dto.mobileapp.GeotabLoginResponse;
import com.vibaps.merged.safetyreport.dto.mobileapp.GeotabUserResponce;
import com.vibaps.merged.safetyreport.dto.trailer.DeviceResponse;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerParams;
import com.vibaps.merged.safetyreport.services.gl.GeoTabApiService;
import com.vibaps.merged.safetyreport.services.trailer.TrailerService;
import com.vibaps.merged.safetyreport.util.DateTimeUtil;
import com.vibaps.merged.safetyreport.util.ResponseUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class MobileAppServices {

	@Autowired
	private GeoTabApiService geoTabApiService;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private TrailerService trailerService; 
	
	private GeoTabReponse responseBody;
	private AppMsg appMsg;
	
	public GeotabLoginResponse showDvirDefacts(TrailerParams trailerParams) throws JsonMappingException, JsonProcessingException {
		// TODO Auto-generated method stub
		String payload=loginRequestParam(trailerParams);
		
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
		
		JSONObject obj=new JSONObject(response.getBody());
		JSONObject result=obj.getJSONObject("result");
		JSONObject credentials=result.getJSONObject("credentials");
		
		ObjectMapper mapper = new ObjectMapper();
		
		GeotabLoginResponse responsevalue=mapper.readValue(credentials.toString(),GeotabLoginResponse.class);
		
		
		return responsevalue;
	}
	
	
	
	private String loginRequestParam(TrailerParams trailerParams)
	{
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method("Authenticate")
		.params()
		.database(trailerParams.getGeotabDatabase())
		.password(trailerParams.getGeotabPassword())
		.userName(trailerParams.getGeotabUserName());
		
		// bind credentials
	//	geoTabApiService.buildCredentials(builder, trailerParams);
		
	
		
		return builder.params()
				.build();
	}



	public GeotabDeviceResponse getActiveDevice(TrailerParams trailerParams) throws JsonMappingException, JsonProcessingException {
		String payload =  deviceParam(trailerParams);
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

		JSONObject obj=new JSONObject(response.getBody());
		JSONArray result=obj.getJSONArray("result");
		
		ObjectMapper mapper = new ObjectMapper();
		
		GeotabDeviceResponse[] responsevalue=mapper.readValue(result.toString(),GeotabDeviceResponse[].class);
		
		
		return new GeotabDeviceResponse(responsevalue);
	}
	
	private String deviceParam(TrailerParams trailerParams)
	{
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		geoTabApiService.buildCredentials(builder, trailerParams);
		
		return builder.params().typeName("Device")
				.search()
				.fromDate(trailerParams.getFromDate()+builder.FROM_TS_SUFFIX)
				.build();
	}



	public ResponseEntity<String> getSystemSetting(TrailerParams trailerParams) 
	{
		String payload =  systemSettingsParam(trailerParams);
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
		
		return response;
	}
	
	public Integer getEnginData(TrailerParams trailerParams) 
	{
		String payload =  enginDataParam(trailerParams);
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
		
		return parsedEnginHours(response);
	}
	
	private Integer parsedEnginHours(ResponseEntity<String> response)
	{
		JSONObject obj=new JSONObject(response.getBody());
		JSONArray ary=obj.getJSONArray("result");
		
		Integer hours=0;
		if(ary.getJSONObject(0).get("data") != null)
		{
		    double engineHourString = Double.valueOf(ary.getJSONObject(0).get("data").toString());
		    hours = (int) Math.round(engineHourString/3600);
		}
		
		return hours;
		
	}
	
	
	
	private String enginDataParam(TrailerParams trailerParams)
	{
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		geoTabApiService.buildCredentials(builder, trailerParams);
		
		return builder.params()
				.typeName("StatusData")
				.search()
					.deviceSearch().id(trailerParams.getDeviceId()).and()
					.diagnosticSearch().id("DiagnosticEngineHoursAdjustmentId").and()
					.fromDate(trailerParams.getFromDate())
					.toDate(trailerParams.getToDate())
				.build();
	}
	public Double getOdaMeterData(TrailerParams trailerParams) 
	{
		String payload =  odameeterDataParam(trailerParams);
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
		
		return parsedOdameter(response);
	}
	
	private Double parsedOdameter(ResponseEntity<String> response)
	{
		JSONObject obj=new JSONObject(response.getBody());
		JSONArray ary=obj.getJSONArray("result");
		
		Double odameter=0.00;
		if(ary.getJSONObject(0).get("data") != null)
		{
		     odameter = Double.valueOf(ary.getJSONObject(0).get("data").toString());
		    
		}
		
		return odameter;
		
	}
	
	private String odameeterDataParam(TrailerParams trailerParams)
	{
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		geoTabApiService.buildCredentials(builder, trailerParams);
		
		return builder.params()
				.typeName("StatusData")
				.search()
					.deviceSearch().id(trailerParams.getDeviceId()).and()
					.diagnosticSearch().id("DiagnosticOdometerAdjustmentId").and()
					.fromDate(trailerParams.getFromDate())
					.toDate(trailerParams.getToDate())
				.build();
	}
	private String systemSettingsParam(TrailerParams trailerParams)
	{
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		geoTabApiService.buildCredentials(builder, trailerParams);
		
		return builder.params().typeName("SystemSettings")
				.build();
	}



	public GeotabDeviceStatusInfoResponse getDeviceStatusInfo(TrailerParams trailerParams) throws JsonMappingException, JsonProcessingException, JSONException, ParseException {
		String payload=getDeviceStatusInfoRequest(trailerParams);
		
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
		List<GeotabDeviceStatusInfoResponse> responseList=new ArrayList<GeotabDeviceStatusInfoResponse>();
		
		JSONObject obj=new JSONObject(response.getBody());
		JSONArray result=obj.getJSONArray("result");
		
		
		
	String zoneId=trailerService.getZoneId(trailerParams);
		
		for(int i=0;i<result.length();i++)
		{
		ObjectMapper mapper = new ObjectMapper();
//		mapper.setTimeZone(TimeZone.getTimeZone(zoneId));
		
		JSONObject innerresult= result.getJSONArray(i).getJSONObject(0);

		
		try
		{
		
		GeotabDeviceStatusInfoResponse responsevalue=mapper.readValue(innerresult.toString(),GeotabDeviceStatusInfoResponse.class);
		responseList.add(responsevalue);
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		}
		
		List<GeotabDeviceStatusInfoResponse> finalresponseList=new ArrayList<GeotabDeviceStatusInfoResponse>();

		for(GeotabDeviceStatusInfoResponse data:responseList)
		{
			String driverName="-";
			
			if(!data.getDriver().equalsIgnoreCase("-"))
			{
			GeotabUserResponce driverIdval=getUserdata(trailerParams, data.getDriver())[0];
			driverName=driverIdval.getFirstName()+" "+driverIdval.getLastName();
			}
			
			trailerParams.setFromDate(data.getDateTime());
			trailerParams.setToDate(data.getDateTime());
			
			Integer enginHours=getEnginData(trailerParams);
			Double odaMeter=getOdaMeterData(trailerParams);
			
			DecimalFormat df=new DecimalFormat("0.00");
			String formate = df.format(odaMeter); 
			Double finalValue = (Double)df.parse(formate) ;
			
			data.setEnginHours(enginHours);
			data.setOdaMeter(finalValue);
			data.setDeviceAddress(trailerService.getAddress(data.getLatitude(), data.getLongitude(), trailerParams).getFormattedAddress());
			data.setDriverName(driverName);			
			finalresponseList.add(data);
		}
		
		return new GeotabDeviceStatusInfoResponse(finalresponseList);
	}
	
		
	public GeotabDeviceStatusInfoBaseResponse getDeviceStatusBaseInfo(TrailerParams trailerParams) throws JsonMappingException, JsonProcessingException, JSONException {
		String payload=getDeviceStatusInfoRequest(trailerParams);
		
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
		List<GeotabDeviceStatusInfoBaseResponse> responseList=new ArrayList<GeotabDeviceStatusInfoBaseResponse>();
		
		JSONObject obj=new JSONObject(response.getBody());
		JSONArray result=obj.getJSONArray("result");

		for(int i=0;i<result.length();i++)
		{
		ObjectMapper mapper = new ObjectMapper();
		JSONObject innerresult= result.getJSONArray(i).getJSONObject(0);

		
		try
		{
		
			GeotabDeviceStatusInfoBaseResponse responsevalue=mapper.readValue(innerresult.toString(),GeotabDeviceStatusInfoBaseResponse.class);
		responseList.add(responsevalue);
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		}
		
		
		return new GeotabDeviceStatusInfoBaseResponse(responseList);
	}
	
	private String getDeviceStatusInfoRequest(TrailerParams trailerParams)  
	{
		
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_EXECUTE_MULTI_CALL);
		// bind credentials
		geoTabApiService.buildCredentials(builder, trailerParams);
		List<String> deviceId=new ArrayList<String>();
		
		String[] elements = trailerParams.getDeviceId().split(",");

		List<String> newList = Arrays.asList(elements);
		
		for(String value:newList)
		{
			getDeviceInfoRequest(builder, value);
		}
        
        String payload = builder.build();
		return payload;
	
	}
	
	private void getDeviceInfoRequest(GeoTabRequestBuilder builder,String deviceId)
	{

		builder.params()
		.addCalls()
			.method(AppConstants.METHOD_GET)
			.params()
				.typeName("DeviceStatusInfo")
				.search()
				.deviceSearch()
						.id(deviceId);
	}
	
	
	public GeotabUserResponce[] getUserdata(TrailerParams trailerParams,String driverId) throws JsonMappingException, JsonProcessingException
	{
		String payload =  getUserRequest(trailerParams,driverId);
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
		
		JSONObject obj=new JSONObject(response.getBody());
		JSONArray result=obj.getJSONArray("result");
		
		ObjectMapper mapper=new ObjectMapper();
		
		GeotabUserResponce[] responce=mapper.readValue(result.toString(), GeotabUserResponce[].class);
		
		return responce;
	}
	
	private String getUserRequest(TrailerParams trailerParams,String driverId) 
	{
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		// bind credentials
		geoTabApiService.buildCredentials(builder, trailerParams);
		
		return builder.params().typeName("User")
				.search().id(driverId)
				.build();
	}


}
