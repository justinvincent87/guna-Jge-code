package com.vibaps.merged.safetyreport.services.dvir;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;
import com.vibaps.merged.safetyreport.builder.GeoTabRequestBuilder;
import com.vibaps.merged.safetyreport.builder.Uri;
import com.vibaps.merged.safetyreport.common.AppConstants;
import com.vibaps.merged.safetyreport.dao.gl.CommonGeotabDAO;
import com.vibaps.merged.safetyreport.dto.dvir.DvirDefactsResponse;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerResponce;
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
public class DvirDefactsServices {
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
	
    private TrailerResponce trailerResponce;
	
	public DvirDefactsResponse showDvirDefacts(TrailerParams trailerParams) {
		
		
		
	    
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
		
		
   payload=getReportRequest(trailerParams);
		
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

		return parsedDvirDefacts(ResponseUtil.parseResponseObject(response),trailerParams,comDatabaseId);
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
	private DvirDefactsResponse responceAsync(JSONObject innerJsonObj,TrailerParams trailerParams,Long comDatabaseId,String zoneId)
	{
		String deviceId = trailerParams.getDeviceId();
		String trailerId = trailerParams.getTrailerId();
		String[] trailerArray = trailerId.split(",");
		String[] deviceArray = deviceId.split(",");
		
		
		
		
		String tailerOrDeviceID="-";
		String assetType="-";

		if(!innerJsonObj.isNull("trailer"))
		{
			if(Arrays.stream(trailerArray).anyMatch(innerJsonObj.getJSONObject("trailer").getString("id")::equals))
			{
			Long countTrailer=genTrailerRepository.counttrailerIdAndrefComDatabaseId(innerJsonObj.getJSONObject("trailer").getString("id"),comDatabaseId);
			
			if( countTrailer> 0)
			{
				tailerOrDeviceID=genTrailerRepository.findBytrailerIdAndrefComDatabaseId(innerJsonObj.getJSONObject("trailer").getString("id"),comDatabaseId).getTrailerName();
			}
			else 
			{
				tailerOrDeviceID=commonGeotabDAO.insertGeoTabMissedTrailer(comDatabaseId, trailerParams, innerJsonObj.getJSONObject("trailer").getString("id")).getTrailerName();
			}
			
			tailerOrDeviceID=tailerOrDeviceID;
			assetType="Trailer";
			}
		}
		
		if(!innerJsonObj.isNull("device"))
		{
			if(Arrays.stream(deviceArray).anyMatch(innerJsonObj.getJSONObject("device").getString("id")::equals))
			{
			Long countDevice=genDeviceRepository.countdeviceIdAndrefComDatabaseId(innerJsonObj.getJSONObject("device").getString("id"),comDatabaseId);
			
			if( countDevice > 0)
				
			{
				tailerOrDeviceID=genDeviceRepository.findBydeviceIdAndrefComDatabaseId(innerJsonObj.getJSONObject("device").getString("id"),comDatabaseId).getDeviceName();
			}
			else
			{
				tailerOrDeviceID=	commonGeotabDAO.insertMissedGeoTabDevice(comDatabaseId, trailerParams,innerJsonObj.getJSONObject("device").getString("id")).getDeviceName();
			}
			tailerOrDeviceID=tailerOrDeviceID;
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
		if(countDriver>0)
		{
		 driverInfo=genDriverUsersRepo.findByuserIdAndrefComDatabaseId(innerJsonObj.getJSONObject("driver").getString("id"), comDatabaseId);
		}
		else
		{
			 driverInfo=commonGeotabDAO.insertMissedGeoTabDriver(comDatabaseId, trailerParams, innerJsonObj.getJSONObject("driver").getString("id"));

		}
		
		String driverId=driverInfo.getDriverName()+" ("+driverInfo.getEmpNumber()+")";
		
		
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
				.resultsLimit(AppConstants.RESULTS_LIMIT)
				.build();
	
	}
	
}
