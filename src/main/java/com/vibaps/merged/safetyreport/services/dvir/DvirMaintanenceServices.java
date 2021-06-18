package com.vibaps.merged.safetyreport.services.dvir;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.jasper.tagplugins.jstl.core.ForEach;
import org.apache.poi.ss.formula.functions.T;
import org.codehaus.jackson.JsonNode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import com.vibaps.merged.safetyreport.util.DateTimeUtil;
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
	
	@Autowired
    private Environment env;
	
    private TrailerResponse trailerResponce;
    
    private Map<String,String> deviceMap;
    
    private String deviceNameFromDeviceMap;
	
	public Object showDvirDefacts(TrailerParams trailerParams) {
		String payload =null;
		Long comDatabaseId;
		deviceMap=new HashMap<String, String>();
		deviceNameFromDeviceMap=null;
		
		
		Long dbCount=comDatabaseRepository.countdatabaseName(trailerParams.getGeotabDatabase());
		
		log.info("DB Entry Count: {}",dbCount);
		
		if( dbCount > 0)
		{
		 comDatabaseId=comDatabaseRepository.findBydatabaseName(trailerParams.getGeotabDatabase()).getId();
	}else
	{
		
			comDatabaseId=trailerService.insertDeviceandTrailer(trailerParams);
		}
		
		String zoneId=trailerService.getZoneId(trailerParams);
	
		
	Map<String,String> controll=new HashMap<String, String>();
	Map<String,String> failureMode=new HashMap<String, String>();	
	Map<String,String> diagnostic=new HashMap<String, String>();	
	Map<String,String> eventRule=new HashMap<String, String>();	
	Map<String,List<EventOccurrence>> eventOccurrenceList= new HashMap<String,List<EventOccurrence>>();
	Map<String,List<FaultData>> faultData=new HashMap<String,List<FaultData>>();
	ResponseEntity<String> dvirResponce=getdvir(trailerParams);
	
		EventOccurrence[] allReminderResponce=getAllReminder(trailerParams);
		List<FaultData[]> getAllFault=getAllFault(trailerParams);
		
		if(allReminderResponce.length >0)
		{
			 eventRule=getEventRule(trailerParams);	
			 eventOccurrenceList=evenOccurrenceMapping(allReminderResponce,eventRule,zoneId,comDatabaseId,trailerParams);

		}
		
		
		if(getAllFault.get(0).length > 0)
		{
			 controll=getController(trailerParams);
			 failureMode=getFailureMode(trailerParams);	
			
			 
			 diagnostic=getDiagnostic(trailerParams,getAllFault);
			 
	         faultData=faultDataMapping(getAllFault,controll,failureMode,diagnostic,zoneId,comDatabaseId,trailerParams);
			 
			   
		}
		

	
		//return eventOccurrenceList;
		Map<String,List<DvirDefactsResponse>> dvirDefactsList=parsedDvirDefacts(dvirResponce.getBody(),trailerParams,comDatabaseId,zoneId);
        
		return new DvirDefactsResponse(parsedFullResponce(eventOccurrenceList,faultData,dvirDefactsList),true);
	}
	
	private List  parsedFullResponce(Map<String,List<EventOccurrence>> eventOccurrenceList,Map<String,List<FaultData>> faultData,Map<String,List<DvirDefactsResponse>> dvirDefactsList)
	{
		Map<String,List<?>> responseMap=new HashMap<String,List<?>>();
		List finalResponse=new ArrayList(); 
		
		for (Map.Entry<String,List<EventOccurrence>> entry : eventOccurrenceList.entrySet())
       
		{
			
			if(!responseMap.containsKey(entry.getKey()))
			{
				List newAddFault=new ArrayList();
				newAddFault.add(entry.getValue());
				responseMap.put(entry.getKey(),newAddFault);
				
			}
			else
			{
				List faultMapData=responseMap.get(entry.getKey());
				faultMapData.add(entry.getValue());
				responseMap.put(entry.getKey(), faultMapData);
				
			}
		}
		
		
		for (Map.Entry<String,List<FaultData>> entry : faultData.entrySet())
		       
		{
			
			if(!responseMap.containsKey(entry.getKey()))
			{
				List newAddFault=new ArrayList();
				newAddFault.add(entry.getValue());
				responseMap.put(entry.getKey(),newAddFault);
				
			}
			else
			{
				List faultMapData=responseMap.get(entry.getKey());
				faultMapData.add(entry.getValue());
				responseMap.put(entry.getKey(), faultMapData);
				
			}
		}
		
		for (Map.Entry<String,List<DvirDefactsResponse>> entry : dvirDefactsList.entrySet())
		       
		{
			
			if(!responseMap.containsKey(entry.getKey()))
			{
				List newAddFault=new ArrayList();
				newAddFault.add(entry.getValue());
				responseMap.put(entry.getKey(),newAddFault);
				
			}
			else
			{
				List faultMapData=responseMap.get(entry.getKey());
				faultMapData.add(entry.getValue());
				responseMap.put(entry.getKey(), faultMapData);
				
			}
		}
		

		for (Map.Entry<String,List<?>> entry : responseMap.entrySet())
		       
		{
			List<?> value= responseMap.get(entry.getKey());
			
			finalResponse.add(value);
		}
		
		
		return finalResponse;
	}
	
	
	private Map<String,List<EventOccurrence>> evenOccurrenceMapping(EventOccurrence[] allReminderResponce,Map<String,String> eventRule,String zoonId,Long comDatabaseId,TrailerParams trailerParams)
	{
		String deviceId = trailerParams.getDeviceId();
		String[] deviceArray = deviceId.split(",");
		
		List<EventOccurrence> responce=new ArrayList<EventOccurrence>();
		Map<String,List<EventOccurrence>> responseMap=new HashMap<String,List<EventOccurrence>>();
		for(EventOccurrence data:allReminderResponce)
		{
			if(Arrays.stream(deviceArray).anyMatch(data.getDeviceId()::equals))
			{
				deviceNameFromDeviceMap=deviceMap.get(data.getDeviceId());
				if(deviceNameFromDeviceMap==null || deviceNameFromDeviceMap.isEmpty())
				{
					deviceNameFromDeviceMap=getdeviceName(comDatabaseId,data.getDeviceId(),trailerParams);
					deviceMap.put(data.getDeviceId(), deviceNameFromDeviceMap);			
				}
				
			EventOccurrence entity=new EventOccurrence();
			entity=data;
			entity.setDeviceName(deviceNameFromDeviceMap);
			entity.setEventDate(trailerService.getZoneTime(zoonId,data.getEventDate()));
			entity.setEventRule(eventRule.get(data.getEventRule()));
			responce.add(entity);
			}
		}
		
		
		for(EventOccurrence value:responce)
		{
			
			if(!responseMap.containsKey(value.getDeviceId()))
			{
				List newAddFault=new ArrayList();
				newAddFault.add(value);
				responseMap.put(value.getDeviceId(),newAddFault);
				
			}
			else
			{
				List faultMapData=responseMap.get(value.getDeviceId());
				faultMapData.add(value);
				responseMap.put(value.getDeviceId(), faultMapData);
				
			}
		}
		return responseMap;
	}

	private Map<String,List<FaultData>> faultDataMapping(List<FaultData[]> faultData,Map<String,String> controll,Map<String,String> failureMode,Map<String,String> diagnostic,String zoonId,Long comDatabaseId,TrailerParams trailerParams)
	{
		List<FaultData> responce=new ArrayList<FaultData>();
		String deviceId = trailerParams.getDeviceId();
		String[] deviceArray = deviceId.split(",");
		
		String diagnosticvalue=env.getProperty("fault.search.diagnostic");
		String[] diagnosticList=diagnosticvalue.split(",");
		
		String controllervalue=env.getProperty("fault.search.controller");
		String[] controllerList=controllervalue.split(",");
		
		Map<String,List<FaultData>> responseMap=new HashMap<String,List<FaultData>>();
		Map<String,List<FaultData>> finalResponseMap=new HashMap<String,List<FaultData>>();
		
		for(FaultData[] value:faultData)
		{
		for(FaultData data:value)
		{
			if(Arrays.stream(deviceArray).anyMatch(data.getDeviceId()::equals))
			{
				
				if(Arrays.stream(controllerList).anyMatch(data.getController()::equals))
				{
						if(Arrays.stream(diagnosticList).anyMatch(data.getDiagnostic()::equals))
						{
							deviceNameFromDeviceMap=deviceMap.get(data.getDeviceId());
							if(deviceNameFromDeviceMap==null || deviceNameFromDeviceMap.isEmpty())
							{
								deviceNameFromDeviceMap=getdeviceName(comDatabaseId,data.getDeviceId(),trailerParams);
								deviceMap.put(data.getDeviceId(), deviceNameFromDeviceMap);			
							}
							
					FaultData entity=new FaultData();
					entity=data;
					entity.setDeviceName(deviceNameFromDeviceMap);
					entity.setDateTime(trailerService.getZoneTimeForFault(zoonId,data.getDateTime()));
					entity.setController(controll.get(data.getController()));
					entity.setDiagnostic(diagnostic.get(data.getDiagnostic()));
					entity.setFailureMode(failureMode.get(data.getFailureMode()));
					responce.add(entity);
						}
				}
				else
				{
				
					deviceNameFromDeviceMap=deviceMap.get(data.getDeviceId());
					if(deviceNameFromDeviceMap==null || deviceNameFromDeviceMap.isEmpty())
					{
						deviceNameFromDeviceMap=getdeviceName(comDatabaseId,data.getDeviceId(),trailerParams);
						deviceMap.put(data.getDeviceId(), deviceNameFromDeviceMap);			
					}
					
			FaultData entity=new FaultData();
			entity=data;
			entity.setDeviceName(deviceNameFromDeviceMap);
			entity.setDateTime(trailerService.getZoneTimeForFault(zoonId,data.getDateTime()));
			entity.setController(controll.get(data.getController()));
			entity.setDiagnostic(diagnostic.get(data.getDiagnostic()));
			entity.setFailureMode(failureMode.get(data.getFailureMode()));
			responce.add(entity);
				}
			}
				
		}
		}
		
		for(FaultData value:responce)
		{
			
			if(!responseMap.containsKey(value.getDeviceId()))
			{
				List newAddFault=new ArrayList();
				newAddFault.add(value);
				responseMap.put(value.getDeviceId(),newAddFault);
				
			}
			else
			{
				List faultMapData=responseMap.get(value.getDeviceId());
				faultMapData.add(value);
				responseMap.put(value.getDeviceId(), faultMapData);
				
			}
		}
		
		for (Entry<String, List<FaultData>> entry : responseMap.entrySet()) {
			
			Map<String,List<FaultData>> coutParsed=new HashMap<String, List<FaultData>>();
			Map<String,FaultData> countValue= new TreeMap<String,FaultData>();
			Integer count =1;
			Date startDate=null;
			Date startDateparse=null;
			Date endDate=null;
			
			String k = entry.getKey();
		    List<FaultData> v = entry.getValue();
	    	List<FaultData> faultDataList=new ArrayList<FaultData>();

		    
		    for (FaultData data:v) 
		    {
		    	
		    	if(!countValue.containsKey(data.getDiagnostic()))
		    	{
		    		startDateparse=DateTimeUtil.parseUtilDate(data.getDateTime());
		    		FaultData values=new FaultData(data.getDeviceId(),data.getDiagnostic(),data.getDeviceName(),DateTimeUtil.dateToString(startDateparse),DateTimeUtil.dateToString(startDateparse),count,data.getController());
		    		
		    		countValue.put(data.getDiagnostic(),values);
		    	}
		    	else
		    	{
		    		FaultData updatevalues=countValue.get(data.getDiagnostic());
		    		count=updatevalues.getCount()+1;
		    		
		    		startDateparse=DateTimeUtil.parseUtilDate(data.getDateTime());
		    		
		    		startDate=DateTimeUtil.parseUtilDate(updatevalues.getStartDate()).before(startDateparse)
		    				?DateTimeUtil.parseUtilDate(updatevalues.getStartDate()):startDateparse;
		    		endDate=DateTimeUtil.parseUtilDate(updatevalues.getEndDate()).after(startDateparse)
		    				?DateTimeUtil.parseUtilDate(updatevalues.getEndDate()):startDateparse;		    		
		    		
		    		FaultData values=new FaultData(data.getDeviceId(),data.getDiagnostic(),data.getDeviceName(),DateTimeUtil.dateToString(startDate),DateTimeUtil.dateToString(endDate),count,data.getController());
		    		
		    		countValue.put(data.getDiagnostic(),values);
		    	}
		    	
		    	
		    	
			}
		    
		    List<FaultData> mapParsedfaultDate=new ArrayList<FaultData>();
		    for(Entry<String,FaultData> flParse:countValue.entrySet())
		    {
		    	mapParsedfaultDate.add(countValue.get(flParse.getKey()));
		    	
		    }
		    
		    
		    
		    
		    finalResponseMap.put(k, mapParsedfaultDate);
		}
		
		return finalResponseMap;
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
	
	public Map<String,String> getDiagnostic(TrailerParams trailerParams, List<FaultData[]> getAllFault) 
	{
		String payload=getDiagnosticRequest(trailerParams,getAllFault);
				
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
			
			return serializingidnameMulticall(response.getBody());
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
	
	private Map<String,String> serializingidnameMulticall(String response) 
	{
		Map<String,String> dataMap=new HashMap<String,String>();
		
		  JSONObject obj=new JSONObject(response);
		  
		 ObjectMapper mapper=new ObjectMapper();
		 
		 for(int i=0;i<obj.getJSONArray("result").length();i++)
		 {
		 JSONArray jsonobj=obj.getJSONArray("result").getJSONArray(i);
		 
		 
		try
		{
			IdNameSerialization responseEntity =mapper.readValue(jsonobj.get(0).toString(), IdNameSerialization.class);
			dataMap.put(responseEntity.getId(), responseEntity.getName());
		}catch(Exception e)
		{
			System.out.println(e);
		}
		
		
			
		
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
	

	
	private List<FaultData[]> getAllFault(TrailerParams trailerParams)
	{
		String payload=getFaultMulticallRequest(trailerParams);
				
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
	
	private List<FaultData[]> parserFaultData(ResponseEntity<String> response)
	{
		JSONObject obj=new JSONObject(response.getBody());		
		List<FaultData[]> listResponseEntity=new ArrayList<FaultData[]>();
		
		FaultData[] responseEntity=null;
		  
		 ObjectMapper mapper=new ObjectMapper();
		 
		 for(int i=0;i<obj.getJSONArray("result").length();i++)
		 {
		 
		try
		{
		   responseEntity =mapper.readValue(obj.getJSONArray("result").get(i).toString(), FaultData[].class);
		   listResponseEntity.add(responseEntity);
		}catch(Exception e)
		{
			log.debug(e);
		}
		 }
		return listResponseEntity;
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
	
	private Map<String,List<DvirDefactsResponse>> parsedDvirDefacts(String responsevalue,TrailerParams trailerParams,Long comDatabaseId,String zoonId)
	{
		Map<String,List<DvirDefactsResponse>> responseMap=new HashMap<String,List<DvirDefactsResponse>>();

	    List<DvirDefactsResponse> responseList=new ArrayList<DvirDefactsResponse>();
		
		JSONObject jsonObj=new JSONObject(responsevalue);
		JSONArray jsonArry=jsonObj.getJSONArray("result");
		List<CompletableFuture<DvirDefactsResponse>> futureList=new ArrayList<CompletableFuture<DvirDefactsResponse>>();
		
		jsonArry.forEach(t->{
			CompletableFuture<DvirDefactsResponse> future=CompletableFuture.supplyAsync(() -> responceAsync((JSONObject) t,trailerParams,comDatabaseId,zoonId));
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
		
		
		
		for(DvirDefactsResponse value:responseList)
		{
			if(!responseMap.containsKey(value.getDeviceOrTrailerName()))
			{
				List newAddFault=new ArrayList();
				newAddFault.add(value);
				responseMap.put(value.getDeviceOrTrailerName(),newAddFault);
				
			}
			else
			{
				List faultMapData=responseMap.get(value.getDeviceOrTrailerName());
				faultMapData.add(value);
				responseMap.put(value.getDeviceOrTrailerName(), faultMapData);
				
			}
		}

		return responseMap;
		
		
		
		
	}
	
	private void getAllReminder() {
		
		
	}
	private DvirDefactsResponse responceAsync(JSONObject innerJsonObj,TrailerParams trailerParams,Long comDatabaseId,String zoneId)
	{
		String deviceId = trailerParams.getDeviceId();
	
		String[] deviceArray = deviceId.split(",");
		
		
		
		
		String tailerOrDeviceID="-";
		String assetType="-";
		String devicename="";


		
		if(!innerJsonObj.isNull("device"))
		{
			if(Arrays.stream(deviceArray).anyMatch(innerJsonObj.getJSONObject("device").getString("id")::equals))
			{
				tailerOrDeviceID=innerJsonObj.getJSONObject("device").getString("id");
				deviceNameFromDeviceMap=deviceMap.get(tailerOrDeviceID);
				if(deviceNameFromDeviceMap==null || deviceNameFromDeviceMap.isEmpty())
				{
					deviceNameFromDeviceMap=getdeviceName(comDatabaseId, tailerOrDeviceID, trailerParams);
					deviceMap.put(tailerOrDeviceID, deviceNameFromDeviceMap);			
				}
				
			assetType="Vehicle";
			}
		}
		
		
		
		    List<GenDefects> defectName=new ArrayList<GenDefects>();
			if(!innerJsonObj.isNull(("defects")))
			{
			JSONArray remark=innerJsonObj.getJSONArray("defects");
		 
			
			
			for(int f=0;f<remark.length();f++)
			{
				defectName.add(genDefectsRepo.findBydefectIdAndrefComDatabaseId(remark.getJSONObject(f).getString("id"), comDatabaseId));
			}
			}
			else
			{
				defectName.add(null);
			}
		
		
		String dateTime=trailerService.getZoneTime(zoneId,innerJsonObj.getString("dateTime"));
		String logType="-";
		if(!innerJsonObj.isNull("logType"))
		{
			logType=innerJsonObj.getString("logType");
		}
		GenDriverUsers driverInfo=new GenDriverUsers();
		Long countDriver=genDriverUsersRepo.countdriverIdAndrefComDatabaseId(innerJsonObj.getJSONObject("driver").getString("id"), comDatabaseId);
		String driverId=innerJsonObj.getJSONObject("driver").getString("id");
		
		if(tailerOrDeviceID.equalsIgnoreCase("-"))
		{
			return null;
		}
		 
		 
		
		return new DvirDefactsResponse(tailerOrDeviceID, dateTime, logType, driverId,defectName,assetType,deviceNameFromDeviceMap);

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
//				.isRepaired(false)
//				.isCertified(false)
				.trailerSearch(null)
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
	
	private String getDiagnosticRequest(TrailerParams trailerParams, List<FaultData[]> getAllFault)  
	{
		
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_EXECUTE_MULTI_CALL);
		// bind credentials
		geoTabApiService.buildCredentials(builder, trailerParams);
		List<String> uniqeDiagnosticId=new ArrayList<String>();
		
		for(FaultData[] value:getAllFault)
		{
		for(FaultData data:value)
		{
			uniqeDiagnosticId.add(data.getDiagnostic());
			
		}
		}
		List<String> newList = uniqeDiagnosticId.stream()
                .distinct()
                .collect(Collectors.toList());

        for(String value:newList)
		{
			getDiagnosticMulticalRequest(builder, value);
		}
        
        String payload = builder.build();
		return payload;
	
	}
	
	private String getFaultMulticallRequest(TrailerParams trailerParams)  
	{
		
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_EXECUTE_MULTI_CALL);
		// bind credentials
		geoTabApiService.buildCredentials(builder, trailerParams);
		
	//	String diagnostic=env.getProperty("fault.search.diagnostic");
		
//		String[] diagnosticList=diagnostic.split(",");
	

        //for(String value:diagnosticList)
		//{
        	getFaultMulticalRequest(builder,trailerParams);
		//}
        
        String payload = builder.build();
		return payload;
	
	}
	
	private void getDiagnosticMulticalRequest(GeoTabRequestBuilder builder,String diagnosticId)
	{

		builder.params()
		.addCalls()
			.method(AppConstants.METHOD_GET)
			.params()
				.typeName("Diagnostic")
				.search()
						.id(diagnosticId);
	}
	
	private void getFaultMulticalRequest(GeoTabRequestBuilder builder,TrailerParams trailerParams)
	{

		builder.params()
		.addCalls()
			.method(AppConstants.METHOD_GET)
			.params()
				.typeName("FaultData")
				.search()
				.fromDate(trailerParams.getFromDate()+builder.FROM_TS_SUFFIX)
				.toDate(trailerParams.getToDate()+builder.TO_TS_SUFFIX)
				//.diagnosticSearch().id(diagnosticId)
				;
	}
	
	private List getDiagnosticMultiCallRequest(TrailerParams trailerParams,FaultData[] getAllFault)
	{
		
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_GET);
		// bind credentials
		geoTabApiService.buildCredentials(builder, trailerParams);
		
		List payload=new ArrayList<String>();
		
		
		for(FaultData result : getAllFault)
		{
			payload.add(builder.params().typeName("Diagnostic")
					.search()
					.id(result.getDiagnostic()));
		}
		
		return payload;
		
		
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
	
	private String getdeviceName(Long comDatabaseId,String deviceId,TrailerParams trailerParams)
	{
		Long countDevice=genDeviceRepository.countdeviceIdAndrefComDatabaseId(deviceId,comDatabaseId);
		String deviceName="";
		if( countDevice > 0)
			
		{
			deviceName=genDeviceRepository.findBydeviceIdAndrefComDatabaseId(deviceId,comDatabaseId).getDeviceName();
		}
		else
		{
			deviceName=	commonGeotabDAO.insertMissedGeoTabDevice(comDatabaseId, trailerParams,deviceId).getDeviceName();
		}
		
		return deviceName;
	}
	
}