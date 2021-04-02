package com.vibaps.merged.safetyreport.services.gl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vibaps.merged.safetyreport.builder.GeoTabRequestBuilder;
import com.vibaps.merged.safetyreport.builder.Uri;
import com.vibaps.merged.safetyreport.common.AppConstants;
import com.vibaps.merged.safetyreport.dao.gl.CommonGeotabDAO;
import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerResponse;
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
	public TrailerResponse getDriver(TrailerParams trailerParams) 
	{
		// TODO Auto-generated method stub
		
		
		String payload =  getReportRequestDriver(trailerParams);
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
		return convertParsedUserReponse(parsedResponse);
	}
	
	private String getReportRequestDriver(TrailerParams trailerParams)  
	{
		
		
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		// bind credentials
		geoTabApiService.buildCredentials(builder, trailerParams);
		
		 return builder.params().typeName("User")
				.build();
		
	}	
	public TrailerResponse getDefects(TrailerParams trailerParams) 
	{
		String payload =  getReportRequestDefacts(trailerParams);
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
		return convertParsedDefectReponse(parsedResponse);
	}
	
	public TrailerResponse convertParsedDefectReponse(JsonObject parsedResponse) {
		
		List<TrailerResponse> responcelist=new ArrayList<>();
		List<String> defactList=new ArrayList<String>();
		JsonObject data = new Gson().fromJson(parsedResponse, JsonObject.class);
	    JsonArray names = data .get("result").getAsJsonArray();
	    JsonObject defacts=names.get(0).getAsJsonObject();
	    JsonArray baseDefacts=defacts.getAsJsonArray("children").getAsJsonArray();
	    
	    System.out.println("Base Size"+baseDefacts.size());
	    for(int i=0;i<baseDefacts.size();i++)
	    {
		    JsonObject baseIndujualDefact=baseDefacts.get(i).getAsJsonObject();
		    String baseDefactsId=baseIndujualDefact.get("id").getAsString();
		    String baseDefactsName=baseIndujualDefact.get("name").getAsString();
	    	JsonArray innerDefacts=baseIndujualDefact.getAsJsonArray("children").getAsJsonArray();
	    	
	    	
	    	for(int j=0;j<innerDefacts.size();j++)
	    	{
	    		JsonObject innerDefactObject=innerDefacts.get(j).getAsJsonObject();
	    		String innerDefactsId=innerDefactObject.get("id").getAsString();
	    		String innerDefactsName=innerDefactObject.get("name").getAsString();
	    		TrailerResponse TrailerResponse = new TrailerResponse(baseDefactsId,baseDefactsName,innerDefactsId,innerDefactsName,false);
		    	
	    		
	    		
		        responcelist.add(TrailerResponse);
	    		
	    		
	    		JsonArray insideinnerDefacts=innerDefactObject.getAsJsonArray("children").getAsJsonArray();
		    	
	    		if(insideinnerDefacts.size()>0)
	    		{
	    			for(int k=0;k<insideinnerDefacts.size();k++)
	    	    	{
	    				JsonObject insideinnerDefactObject=insideinnerDefacts.get(k).getAsJsonObject();
	    	    		String insideinnerDefactsId=insideinnerDefactObject.get("id").getAsString();
	    	    		String insideinnerDefactsName=insideinnerDefactObject.get("name").getAsString();
	    	    		TrailerResponse insideTrailerResponse = new TrailerResponse(innerDefactsId,innerDefactsName,insideinnerDefactsId,insideinnerDefactsName,false);
	    		    	
	    	    		responcelist.add(insideTrailerResponse);
	    	    		

	    	    	}
	    		}
	    		   
	    		
	    	}
	    }
	       
	        
	    
		return new TrailerResponse(responcelist);

	}
	
	private String getReportRequestDefacts(TrailerParams trailerParams)  
	{
		
		
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		// bind credentials
		geoTabApiService.buildCredentials(builder, trailerParams);
		
		 return builder.params().typeName("Group")
				 .search()
				 .id("groupDefectsId")
				.build();
		
	}
	
	public TrailerResponse getDevice(TrailerParams trailerParams) 
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
	public TrailerResponse getMissedDriver(TrailerParams trailerParams,String deviseId) 
	{
		// TODO Auto-generated method stub
		
		
		String payload =  getReportRequestMissedDriver(trailerParams,deviseId);
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
		return convertParsedUserReponse(parsedResponse);
	}
	public TrailerResponse convertParsedUserReponse(JsonObject parsedResponse) {
		
		List<TrailerResponse> responcelist=new ArrayList<>();
		
		JsonObject data = new Gson().fromJson(parsedResponse, JsonObject.class);
	    JsonArray names = data .get("result").getAsJsonArray();
	    
	    
	    for(int i=0;i<names.size();i++)
	    {
	    	JsonObject user=names.get(i).getAsJsonObject();
	    	
	       
	    	TrailerResponse TrailerResponse = new TrailerResponse(user.get("id").getAsString(),user.get("firstName").getAsString()+" "+user.get("lastName").getAsString(),user.get("name").getAsString());
	    	
	   
	        responcelist.add(TrailerResponse);
	        
	    }
		return new TrailerResponse(responcelist);

	}
	private String getReportRequestMissedDriver(TrailerParams trailerParams,String deviceId)  
	{
		
		
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		// bind credentials
		geoTabApiService.buildCredentials(builder, trailerParams);
		
		 return builder.params().typeName("User").search().id(deviceId)
				.build();
		
	}
	public TrailerResponse getTrailer(TrailerParams trailerParams) 
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
	
	public TrailerResponse getMissedTrailer(TrailerParams trailerParams,String trailerId) 
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
	
	public TrailerResponse getMissedDevice(TrailerParams trailerParams,String deviseId) 
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
