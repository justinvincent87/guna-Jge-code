package com.vibaps.merged.safetyreport.services.gl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;
import com.vibaps.merged.safetyreport.builder.GeoTabRequestBuilder;
import com.vibaps.merged.safetyreport.builder.Uri;
import com.vibaps.merged.safetyreport.common.AppConstants;
import com.vibaps.merged.safetyreport.dao.gl.CommonGeotabDAO;
import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerResponce;
import com.vibaps.merged.safetyreport.entity.gl.ComDatabase;
import com.vibaps.merged.safetyreport.repo.gl.ComDatabaseRepository;
import com.vibaps.merged.safetyreport.services.trailer.TrailerService;
import com.vibaps.merged.safetyreport.util.ResponseUtil;

import lombok.extern.log4j.Log4j2;
@Service
@Log4j2
public class CommonGeotabService {
	
     @Autowired
	private CommonGeotabDAO dao;
     
     @Autowired
 	private GeoTabApiService geoTabApiService;
     
     @Autowired
 	private RestTemplate restTemplate;
     
     @Autowired
     private TrailerService trailerService;
     
     @Autowired
 	private ComDatabaseRepository comDatabaseRepository;

	public ComDatabase insertDevice(TrailerParams reportParams) throws SQLException
	{
		return dao.insertDevice(reportParams);
	}
	
	public ComDatabase insertTrailer(TrailerParams reportParams) throws SQLException
	{
		return dao.insertTrailer(reportParams);
	}
	
	public TrailerResponce getDevice(TrailerParams trailerParams) 
	{
		// TODO Auto-generated method stub
		
		
		String payload =  getReportRequestDevice(trailerParams);
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
		return trailerService.convertParsedReponse(parsedResponse);
	}
	
	public TrailerResponce getTrailer(TrailerParams trailerParams) 
	{
		// TODO Auto-generated method stub
		
		
		String payload =  getReportRequestTrailer(trailerParams);
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
		return trailerService.convertParsedReponse(parsedResponse);
	}
	
	public TrailerResponce getMissedTrailer(TrailerParams trailerParams,String trailerId) 
	{
		// TODO Auto-generated method stub
		
		
		String payload =  getReportRequestMissedTrailer(trailerParams,trailerId);
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
		return trailerService.convertParsedReponse(parsedResponse);
	}
	
	
	private String getReportRequestDevice(TrailerParams trailerParams)  
	{
		
		
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		// bind credentials
		geoTabApiService.buildCredentials(builder, trailerParams);
		
		 return builder.params().typeName("Device")
				.build();
		
	}
	
	public TrailerResponce getMissedDevice(TrailerParams trailerParams,String deviseId) 
	{
		// TODO Auto-generated method stub
		
		
		String payload =  getReportRequestMissedDevice(trailerParams,deviseId);
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
		return trailerService.convertParsedReponse(parsedResponse);
	}
	private String getReportRequestMissedDevice(TrailerParams trailerParams,String deviceId)  
	{
		
		
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		// bind credentials
		geoTabApiService.buildCredentials(builder, trailerParams);
		
		 return builder.params().typeName("Device").search().id(deviceId)
				.build();
		
	}
	
	private String getReportRequestTrailer(TrailerParams trailerParams)  
	{
		
		
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		// bind credentials
		geoTabApiService.buildCredentials(builder, trailerParams);
		
		 return builder.params().typeName("Trailer")
				.build();
		
	}
	
	private String getReportRequestMissedTrailer(TrailerParams trailerParams,String trailerId)  
	{
		
		
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		// bind credentials
		geoTabApiService.buildCredentials(builder, trailerParams);
		
		 return builder.params().typeName("Trailer").search().id(trailerId)
				.build();
		
	}

	public String uploadFile(MultipartFile file,String filePath) throws IOException {
		// TODO Auto-generated method stub
		byte[] bytes = file.getBytes();
        Path path = Paths.get(filePath+file.getOriginalFilename());
        Files.write(path, bytes);

    
		return "You successfully uploaded '" + file.getOriginalFilename();
	}

	public ResponseEntity<String> apiCallUsingURL(String url) {
		
		ResponseEntity<String> response = restTemplate.postForEntity(url,null, String.class);
		
		return response;
	}

	public ResponseEntity<String> getGeotabDataBasedType(TrailerParams reportParams) {
		String payload =  getpayloadGeotabResponceUsingTypeName(reportParams);
		if (log.isDebugEnabled()) {
			log.debug("Get report data payload: {}", payload);
		}

		String uri = Uri.get().secure().add(reportParams.getUrl()).add(AppConstants.PATH_VERSION).build();
		if (log.isDebugEnabled()) {
			log.debug("Get report data uri: {}", uri);
		}

		ResponseEntity<String> response = restTemplate.postForEntity(uri, payload, String.class);
		if (log.isDebugEnabled()) {
			log.debug("Get report data response code: {}", response.getStatusCodeValue());
		}

		//return response;
		//JsonObject parsedResponse = ResponseUtil.parseResponse(response);
		return response;
	}

	private String getpayloadGeotabResponceUsingTypeName(TrailerParams reportParams)  
	{
		
		
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		// bind credentials
		geoTabApiService.buildCredentials(builder, reportParams);
		
		 return builder.params().typeName(reportParams.getTypeName())
				.build();
		
	}

	public Long getdatabaceid(TrailerParams reportParams) {
		// TODO Auto-generated method stub
		
		Long dbCount=comDatabaseRepository.getDatabaseId(reportParams.getGeotabDatabase());
		
		return dbCount;
	}

	




}
