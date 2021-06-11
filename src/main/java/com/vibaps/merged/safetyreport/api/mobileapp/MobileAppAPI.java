package com.vibaps.merged.safetyreport.api.mobileapp;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vibaps.merged.safetyreport.dto.mobileapp.GeotabDeviceResponse;
import com.vibaps.merged.safetyreport.dto.mobileapp.GeotabDeviceStatusInfoResponse;
import com.vibaps.merged.safetyreport.dto.mobileapp.GeotabLoginResponse;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerParams;
import com.vibaps.merged.safetyreport.services.mobileapp.MobileAppServices;

@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping({ "/mobileApp" })
public class MobileAppAPI {

	@Autowired
	private MobileAppServices mobileAppServices;
	
	@PostMapping(value = "/login",produces=MediaType.APPLICATION_JSON_VALUE)
	public GeotabLoginResponse login(@RequestBody TrailerParams reportParams) throws SQLException, JsonMappingException, JsonProcessingException{
		
		return mobileAppServices.showDvirDefacts(reportParams);
	}
	
	@PostMapping(value = "/get-active-device",produces=MediaType.APPLICATION_JSON_VALUE)
	public GeotabDeviceResponse getActiveDevice(@RequestBody TrailerParams reportParams) throws SQLException, JsonMappingException, JsonProcessingException{
		
		return mobileAppServices.getActiveDevice(reportParams);
	}
	
	@PostMapping(value = "/get-system-settings",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getSystemSetting(@RequestBody TrailerParams reportParams) throws SQLException, JsonMappingException, JsonProcessingException{
		
		return mobileAppServices.getSystemSetting(reportParams);
	}
	
	@PostMapping(value = "/get-device-status-info",produces=MediaType.APPLICATION_JSON_VALUE)
	public GeotabDeviceStatusInfoResponse getDeviceStatusInfo(@RequestBody TrailerParams reportParams) throws SQLException, JsonMappingException, JsonProcessingException{
		
		return mobileAppServices.getDeviceStatusInfo(reportParams);
	}
}
