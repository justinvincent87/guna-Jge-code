package com.vibaps.merged.safetyreport.services.dvir;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.jasper.tagplugins.jstl.core.ForEach;
import org.codehaus.jackson.JsonNode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.vibaps.merged.safetyreport.builder.GeoTabRequestBuilder;
import com.vibaps.merged.safetyreport.builder.Uri;
import com.vibaps.merged.safetyreport.common.AppConstants;
import com.vibaps.merged.safetyreport.dao.gl.CommonGeotabDAO;
import com.vibaps.merged.safetyreport.dto.dvir.DvirDefactsResponse;
import com.vibaps.merged.safetyreport.dto.dvir.EventOccurrence;
import com.vibaps.merged.safetyreport.dto.dvir.FaultData;
import com.vibaps.merged.safetyreport.dto.dvir.IdNameSerialization;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerResponse;
import com.vibaps.merged.safetyreport.entity.gl.ComDatabase;
import com.vibaps.merged.safetyreport.entity.gl.GenDefects;
import com.vibaps.merged.safetyreport.entity.gl.GenDriverUsers;
import com.vibaps.merged.safetyreport.repo.gl.ComDatabaseRepository;
import com.vibaps.merged.safetyreport.repo.gl.CommonGeotabRepository;
import com.vibaps.merged.safetyreport.repo.gl.GenDefectsRepo;
import com.vibaps.merged.safetyreport.repo.gl.GenDeviceRepository;
import com.vibaps.merged.safetyreport.repo.gl.GenDriverUsersRepo;
import com.vibaps.merged.safetyreport.repo.gl.GenTrailerRepository;
import com.vibaps.merged.safetyreport.services.gl.GeoTabApiService;
import com.vibaps.merged.safetyreport.services.trailer.TrailerService;
import com.vibaps.merged.safetyreport.util.ResponseUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DvirMaintanenceServices {
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
	@Autowired
	private TrailerService trailerService;
	@Autowired
	private GeoTabApiService geoTabApiService;
	@Autowired
	private GenDefectsRepo genDefectsRepo;
	
	@Autowired
	private GenDriverUsersRepo genDriverUsersRepo;
	
    private TrailerResponse trailerResponce;
	
	public Object showDvirDefacts(TrailerParams trailerParams) {
		String payload =null;
		Long comDatabaseId;
		
		Long dbCount=comDatabaseRepository.countdatabaseName(trailerParams.getGeotabDatabase());
		
		log.info("DB Entry Count: {}",dbCount);
		
		if( dbCount > 0)
		{
		 comDatabaseId=comDatabaseRepository.findBydatabaseName(trailerParams.getGeotabDatabase()).getId();
	}else
	{
		
			comDatabaseId=trailerService.insertDeviceandTrailer(trailerParams);
		}
		
	Map<String,String> controll=new HashMap<String, String>();
	Map<String,String> failureMode=new HashMap<String, String>();	
	Map<String,String> diagnostic=new HashMap<String, String>();	
	Map<String,String> eventRule=new HashMap<String, String>();	
	List<EventOccurrence> eventOccurrenceList= new ArrayList<EventOccurrence>();
	 List<FaultData> faultData=new ArrayList<FaultData>();
	ResponseEntity<String> dvirResponce=getdvir(trailerParams);
//		EventOccurrence[] allReminderResponce=getAllReminder(trailerParams);
//		FaultData[] getAllFault=getAllFault(trailerParams);
//		
//		if(allReminderResponce.length >0)
//		{
//			 eventRule=getEventRule(trailerParams);	
//			 eventOccurrenceList=evenOccurrenceMapping(allReminderResponce,eventRule);
//
//		}
//		
//		if(getAllFault.length > 0)
//		{
//			 controll=getController(trailerParams);
//			 failureMode=getFailureMode(trailerParams);	
//			 diagnostic=getDiagnostic(trailerParams);	
//	         faultData=faultDataMapping(getAllFault,controll,failureMode,diagnostic);
//			 
//			   
//		}
	

		return parsedDvirDefacts(dvirResponce.getBody(),trailerParams,comDatabaseId);
	}
	
	private List<EventOccurrence> evenOccurrenceMapping(EventOccurrence[] allReminderResponce,Map<String,String> eventRule)
	{
		List<EventOccurrence> responce=new ArrayList<EventOccurrence>();
		for(EventOccurrence data:allReminderResponce)
		{
			EventOccurrence entity=new EventOccurrence();
			entity=data;
			entity.setEventRule(eventRule.get(data.getEventRule()));
			responce.add(entity);
		}
		
		return responce;
	}

	private List<FaultData> faultDataMapping(FaultData[] faultData,Map<String,String> controll,Map<String,String> failureMode,Map<String,String> diagnostic)
	{
		List<FaultData> responce=new ArrayList<FaultData>();
		for(FaultData data:faultData)
		{
			FaultData entity=new FaultData();
			entity=data;
			entity.setController(controll.get(data.getController()));
			entity.setDiagnostic(diagnostic.get(data.getDiagnostic()));
			entity.setFailureMode(failureMode.get(data.getFailureMode()));
			responce.add(entity);
		}
		
		return responce;
	}

	public Map<String,String> getController(TrailerParams trailerParams)
	{
		String payload=getControllerRequest(trailerParams);
				
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
			
			return serializingidname(response.getBody());
	}
	
	public Map<String,String> getFailureMode(TrailerParams trailerParams) 
	{
		String payload=getFailureModeRequest(trailerParams);
				
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
			
			return serializingidname(response.getBody().replaceAll("\"NoFailureModeId\",",""));
	}
	
	public Map<String,String> getDiagnostic(TrailerParams trailerParams) 
	{
		String payload=getDiagnosticRequest(trailerParams);
				
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
			
			return serializingidname(response.getBody());
	}
	
	public Map<String,String> getEventRule(TrailerParams trailerParams) 
	{
		String payload=getEventRuletRequest(trailerParams);
				
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
			
			return serializingidname(response.getBody());
	}
	
	private Map<String,String> serializingidname(String response) 
	{
		Map<String,String> dataMap=new HashMap<String,String>();
		
		  JSONObject obj=new JSONObject(response);
		  IdNameSerialization[] responseEntity=null;
		  
		 ObjectMapper mapper=new ObjectMapper();
		try
		{
		   responseEntity =mapper.readValue(obj.getJSONArray("result").toString(), IdNameSerialization[].class);
		}catch(Exception e)
		{
			System.out.println(e);
		}
		
		for(IdNameSerialization var : responseEntity)
		{
			dataMap.put(var.getId(), var.getName());
		}
		    return dataMap;
	}
	
	private ResponseEntity<String> getdvir(TrailerParams trailerParams)
	{
		String payload=getReportRequest(trailerParams);
				
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
	

	
	private FaultData[] getAllFault(TrailerParams trailerParams)
	{
		String payload=getAllFaultRequest(trailerParams);
				
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
			
			return parserFaultData(response);
	}
	
	private FaultData[] parserFaultData(ResponseEntity<String> response)
	{
		JSONObject obj=new JSONObject(response.getBody());		
		FaultData[] responseEntity=null;
		  
		 ObjectMapper mapper=new ObjectMapper();
		try
		{
		   responseEntity =mapper.readValue(obj.getJSONArray("result").toString(), FaultData[].class);
		}catch(Exception e)
		{
			System.out.println(e);
		}
		
		return responseEntity;
	}
	
	private EventOccurrence[] getAllReminder(TrailerParams trailerParams)
	{
		String payload=getAllReminderRequest(trailerParams);
				
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
			
			return parserEventOccurrence(response);
	}
	
	private EventOccurrence[] parserEventOccurrence(ResponseEntity<String> responce)
	{
		JSONObject obj=new JSONObject(responce.getBody());
		EventOccurrence[] responseEntity=null;
		  
		 ObjectMapper mapper=new ObjectMapper();
		try
		{
		   responseEntity =mapper.readValue(obj.getJSONArray("result").toString(), EventOccurrence[].class);
		}catch(Exception e)
		{
			System.out.println(e);
		}
		
		return responseEntity;
	}
	
	private DvirDefactsResponse parsedDvirDefacts(String responsevalue,TrailerParams trailerParams,Long comDatabaseId)
	{
		String zoneId=trailerService.getZoneId(trailerParams);
	List<DvirDefactsResponse> responseList=new ArrayList<DvirDefactsResponse>();
		
		JSONObject jsonObj=new JSONObject(responsevalue);
		JSONArray jsonArry=jsonObj.getJSONArray("result");
		List<CompletableFuture<DvirDefactsResponse>> futureList=new ArrayList<CompletableFuture<DvirDefactsResponse>>();
		
		jsonArry.forEach(t->{
			CompletableFuture<DvirDefactsResponse> future=CompletableFuture.supplyAsync(() -> responceAsync((JSONObject) t,trailerParams,comDatabaseId,zoneId));
			futureList.add(future);
		
		});
		
		CompletableFuture<?> combined = CompletableFuture.allOf(futureList.toArray(new CompletableFuture<?>[0]));
		try
		{
			
			//combined.get();
			futureList.parallelStream().forEach(r->{
				try {
					DvirDefactsResponse response = r.get();
					if(Objects.nonNull(response))
					{
					responseList.add(response);
					}
					
				} catch (InterruptedException e) {
					
					log.error("Error 1 :: {}",e);
				} catch (ExecutionException e) {
					log.error("Error 2 :: {}",e);
				}
			});
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		

		return new DvirDefactsResponse(responseList);
		
	}
	
	private void getAllReminder() {
		
		
	}
	private DvirDefactsResponse responceAsync(JSONObject innerJsonObj,TrailerParams trailerParams,Long comDatabaseId,String zoneId)
	{
		String deviceId = trailerParams.getDeviceId();
		String trailerId = trailerParams.getTrailerId();
		String[] trailerArray = trailerId.split(",");
		String[] deviceArray = deviceId.split(",");
		
		
		
		
		String tailerOrDeviceID="-";
		String assetType="-";


		
		if(!innerJsonObj.isNull("device"))
		{
			if(Arrays.stream(deviceArray).anyMatch(innerJsonObj.getJSONObject("device").getString("id")::equals))
			{
			Long countDevice=genDeviceRepository.countdeviceIdAndrefComDatabaseId(innerJsonObj.getJSONObject("device").getString("id"),comDatabaseId);
			
//			if( countDevice > 0)
//				
//			{
//				tailerOrDeviceID=genDeviceRepository.findBydeviceIdAndrefComDatabaseId(innerJsonObj.getJSONObject("device").getString("id"),comDatabaseId).getDeviceName();
//			}
//			else
//			{
//				tailerOrDeviceID=	commonGeotabDAO.insertMissedGeoTabDevice(comDatabaseId, trailerParams,innerJsonObj.getJSONObject("device").getString("id")).getDeviceName();
//			}
			tailerOrDeviceID=innerJsonObj.getJSONObject("device").getString("id");
			assetType="Vehicle";
			}
		}
		
		
		
		 
			JSONArray remark=innerJsonObj.getJSONArray("defects");
		 
			List<GenDefects> defectName=new ArrayList<GenDefects>();
			
			for(int f=0;f<remark.length();f++)
			{
				defectName.add(genDefectsRepo.findBydefectIdAndrefComDatabaseId(remark.getJSONObject(f).getString("id"), comDatabaseId));
			}
		
		
		String dateTime=trailerService.getZoneTime(zoneId,innerJsonObj.getString("dateTime"));
		String logType=innerJsonObj.getString("logType");
		GenDriverUsers driverInfo=new GenDriverUsers();
		Long countDriver=genDriverUsersRepo.countdriverIdAndrefComDatabaseId(innerJsonObj.getJSONObject("driver").getString("id"), comDatabaseId);
//		if(countDriver>0)
//		{
//		 driverInfo=genDriverUsersRepo.findByuserIdAndrefComDatabaseId(innerJsonObj.getJSONObject("driver").getString("id"), comDatabaseId);
//		}
//		else
//		{
//			 driverInfo=commonGeotabDAO.insertMissedGeoTabDriver(comDatabaseId, trailerParams, innerJsonObj.getJSONObject("driver").getString("id"));
//
//		}
		
		//String driverId=driverInfo.getDriverName()+" ("+driverInfo.getEmpNumber()+")";
		String driverId=innerJsonObj.getJSONObject("driver").getString("id");
		
		if(tailerOrDeviceID.equalsIgnoreCase("-"))
		{
			return null;
		}
		 
		 
		
		return new DvirDefactsResponse(tailerOrDeviceID, dateTime, logType, driverId,defectName,assetType);

		}

	private String getReportRequest(TrailerParams trailerParams)  
	{
		
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		// bind credentials
		geoTabApiService.buildCredentials(builder, trailerParams);
		
	
		
		return builder.params().typeName("DVIRLog")
				.search()
				.fromDate(trailerParams.getFromDate()+builder.FROM_TS_SUFFIX)
				.toDate(trailerParams.getToDate()+builder.TO_TS_SUFFIX)
				.isDefective(true)
				.isRepaired(false)
				.isCertified(false)
				.trailerSearch(null)
				.resultsLimit(AppConstants.RESULTS_LIMIT)
				.build();
	
	}
	
	private String getAllReminderRequest(TrailerParams trailerParams)  
	{
		
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		// bind credentials
		geoTabApiService.buildCredentials(builder, trailerParams);
		
	
		
		return builder.params().typeName("EventOccurrence")
				.search()
				.fromDate(trailerParams.getFromDate()+builder.FROM_TS_SUFFIX)
				.toDate(trailerParams.getToDate()+builder.TO_TS_SUFFIX)
				.build();
	
	}
	
	private String getAllFaultRequest(TrailerParams trailerParams)  
	{
		
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		// bind credentials
		geoTabApiService.buildCredentials(builder, trailerParams);
		
	
		
		return builder.params().typeName("FaultData")
				.search()
				.fromDate(trailerParams.getFromDate()+builder.FROM_TS_SUFFIX)
				.toDate(trailerParams.getToDate()+builder.TO_TS_SUFFIX)
				.build();
	
	}
	
	private String getEventRuletRequest(TrailerParams trailerParams)  
	{
		
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		// bind credentials
		geoTabApiService.buildCredentials(builder, trailerParams);
		
	
		
		return builder.params().typeName("EventRule")
				.build();
	
	}
	
	private String getDiagnosticRequest(TrailerParams trailerParams)  
	{
		
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		// bind credentials
		geoTabApiService.buildCredentials(builder, trailerParams);
		
	
		
		return builder.params().typeName("Diagnostic")
				.build();
	
	}
	
	private String getControllerRequest(TrailerParams trailerParams)  
	{
		
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		// bind credentials
		geoTabApiService.buildCredentials(builder, trailerParams);
		
	
		
		return builder.params().typeName("Controller")
				.build();
	
	}
	
	private String getFailureModeRequest(TrailerParams trailerParams)  
	{
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		// bind credentials
		geoTabApiService.buildCredentials(builder, trailerParams);
		
		return builder.params().typeName("FailureMode")
				.build();
	}
	
}
