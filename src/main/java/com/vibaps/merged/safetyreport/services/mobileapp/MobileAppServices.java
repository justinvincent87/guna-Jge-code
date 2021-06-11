package com.vibaps.merged.safetyreport.services.mobileapp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
import com.vibaps.merged.safetyreport.dto.mobileapp.GeotabDeviceStatusInfoResponse;
import com.vibaps.merged.safetyreport.dto.mobileapp.GeotabLoginResponse;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerParams;
import com.vibaps.merged.safetyreport.services.gl.GeoTabApiService;
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
	
	private String systemSettingsParam(TrailerParams trailerParams)
	{
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		geoTabApiService.buildCredentials(builder, trailerParams);
		
		return builder.params().typeName("SystemSettings")
				.build();
	}



	public GeotabDeviceStatusInfoResponse getDeviceStatusInfo(TrailerParams trailerParams) throws JsonMappingException, JsonProcessingException, JSONException {
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
		
		for(int i=0;i<result.length();i++)
		{
		ObjectMapper mapper = new ObjectMapper();
		JSONObject innerresult= result.getJSONArray(i).getJSONObject(0);

		
		GeotabDeviceStatusInfoResponse responsevalue=mapper.readValue(innerresult.toString(),GeotabDeviceStatusInfoResponse.class);
		responseList.add(responsevalue);
		}
		
		return new GeotabDeviceStatusInfoResponse(responseList);
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
	


}
