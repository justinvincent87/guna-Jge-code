package com.vibaps.merged.safetyreport.api.geodriveapp;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
import com.vibaps.merged.safetyreport.dto.geodriveapp.GeoDriveAppResponse;
import com.vibaps.merged.safetyreport.dto.geodriveapp.GeotabDriverCallResponse;
import com.vibaps.merged.safetyreport.dto.geodriveapp.LytxGroupResponse;
import com.vibaps.merged.safetyreport.dto.geodriveapp.LytxScoreListResponse;
import com.vibaps.merged.safetyreport.dto.geodriveapp.LytxTokenResponse;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerParams;
import com.vibaps.merged.safetyreport.entity.gl.LyUserEntity;
import com.vibaps.merged.safetyreport.services.geotabdriveapp.GeotabDriveAppServices;

@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping({ "/geotabDriveApp" })
public class GeotabDriveAppAPI {
	
	@Autowired
	private GeotabDriveAppServices geotabDriveAppService;

	@PostMapping(value = "/show-geotab-drive-score",produces=MediaType.APPLICATION_JSON_VALUE)
	public List<List<GeotabDriverCallResponse>> showScore(@RequestBody TrailerParams reportParams) throws SQLException, RemoteException, ParseException, JsonMappingException, JsonProcessingException, InterruptedException, ExecutionException{
		
		return geotabDriveAppService.showScore(reportParams);
	}
	
	
	
}
