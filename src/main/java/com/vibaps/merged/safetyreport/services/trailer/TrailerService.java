package com.vibaps.merged.safetyreport.services.trailer;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.h2.util.DateTimeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.graphbuilder.math.func.LgFunction;
import com.vibaps.merged.safetyreport.builder.GeoTabRequestBuilder;
import com.vibaps.merged.safetyreport.builder.Uri;
import com.vibaps.merged.safetyreport.common.AppConstants;
import com.vibaps.merged.safetyreport.dao.gl.CommonGeotabDAO;
import com.vibaps.merged.safetyreport.dto.trailer.DeviceResponse;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerAttachementResponce;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerListResponse;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerResponse;
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
    @Autowired
    private Environment env;
	
	
    private TrailerResponse trailerResponce;
    
    private Map<String, String> addressMap=new HashMap<String, String>();
    private Long countDevice,countTrailer;

	private static final DateTimeFormatter DF_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private GeoTabApiService geoTabApiService;
	
	public TrailerResponse showReport(TrailerParams trailerParams) throws JsonMappingException, JsonProcessingException  {

		StopWatch sw=new StopWatch();
		List<TrailerResponse> list=new ArrayList<TrailerResponse>();
		sw.start();
		String payload=getTrailerRequestPayload(trailerParams);
			
			if (log.isDebugEnabled()) {
				log.debug("Get report data payload: {}", payload);
			}
	
			String uri = env.getProperty("datamonster.base.url")+AppConstants.DATA_MONSTER_TRAILER_SEARCH_URL;
			
			if (log.isDebugEnabled()) {
				log.debug("Get report data uri: {}", uri);
			}
	
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<String>(payload,headers);

			String response = restTemplate.postForObject(uri, entity, String.class);

	sw.stop();
	
	log.info("Time :: {}",sw.getTotalTimeMillis());
	
	sw.start();
			//ResponseEntity<String> response = restTemplate.postForEntity(uri,payload.toString(),String.class);
			
			/*
			 * if (log.isDebugEnabled()) { log.debug("Get report data response code: {}",
			 * response); }
			 */

			JSONObject obj=new JSONObject(response);
			JSONArray jsonarray=obj.getJSONArray("data");
			  
		//	SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy hh:mm a");
	       // df.setTimeZone(TimeZone.getDefault());
	      String zoneId=  getZoneId(trailerParams);
	        
			//  ObjectMapper mapper = new ObjectMapper();
			  
			 // mapper.setTimeZone(TimeZone.getTimeZone(getZoneId(trailerParams)));
		     // mapper.setDateFormat(df);
for(int i=0;i<jsonarray.length();i++)
{
	
	
	list.add(new TrailerResponse(jsonarray.getJSONObject(i).getString("id"),getZoneDateTime(zoneId,jsonarray.getJSONObject(i).getString(
			  "activeFrom")),getZoneDateTime(zoneId,jsonarray.getJSONObject(i).getString("activeTo")),jsonarray.getJSONObject(i).getString("trailerName"),jsonarray.getJSONObject(i).getString("deviceName"),jsonarray.getJSONObject(i).getString("attachedLocation"),jsonarray.getJSONObject(i).getString("detachedLocation")));	

	 
}
			  
			  
			 // TrailerAttachementResponce[] result= mapper.readValue(jsonarray.toString(),  TrailerAttachementResponce[].class );
		     
	sw.stop();
	log.info("Time End:: {}",sw.getTotalTimeMillis());

	return new TrailerResponse(list);
	
			 // return new TrailerResponse(result);
	}
	
	
	public String showReportCount(TrailerParams trailerParams) throws JsonMappingException, JsonProcessingException  {

		StopWatch sw=new StopWatch();
		
		sw.start();
		String payload=getTrailerRequestPayload(trailerParams);
			
			if (log.isDebugEnabled()) {
				log.debug("Get report data payload: {}", payload);
			}
	
			String uri = env.getProperty("datamonster.base.url")+AppConstants.DATA_MONSTER_TRAILER_SEARCH_COUNT_URL;
			
			if (log.isDebugEnabled()) {
				log.debug("Get report data uri: {}", uri);
			}
	
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<String>(payload,headers);

			String response = restTemplate.postForObject(uri, entity, String.class);

	  return response;
	}
	
	
	
	
	public String showDevice(TrailerParams trailerParams) throws JsonMappingException, JsonProcessingException  {

	
				
		
		String payload=getDeviceRequestPayload(trailerParams);
			
			if (log.isDebugEnabled()) {
				log.debug("Get report data payload: {}", payload);
			}
	
			String uri = env.getProperty("datamonster.base.url")+AppConstants.DATA_MONSTER_DEVICE_SEARCH_URL;
			
			if (log.isDebugEnabled()) {
				log.debug("Get report data uri: {}", uri);
			}
	
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<String>(payload,headers);

			return restTemplate.postForObject(uri, entity, String.class);
	}

	public String showTrailer(TrailerParams trailerParams) throws JsonMappingException, JsonProcessingException  {
		String payload=getTrailerListRequestPayload(trailerParams);
			
			if (log.isDebugEnabled()) {
				log.debug("Get report data payload: {}", payload);
			}
	
			String uri = env.getProperty("datamonster.base.url")+AppConstants.DATA_MONSTER_TRAILERLIST_SEARCH_URL;
			
			if (log.isDebugEnabled()) {
				log.debug("Get report data uri: {}", uri);
			}
	
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<String>(payload,headers);

			return restTemplate.postForObject(uri, entity, String.class);
	}



	
	private String getTrailerRequestPayload(TrailerParams trailerParams)
	{
		String deviceId = trailerParams.getDeviceId();
		String trailerId = trailerParams.getTrailerId();
		String[] trailerArray = trailerId.split(",");
		String[] deviceArray = deviceId.split(",");
		String timeZone=getZoneId(trailerParams);
		
		
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		geoTabApiService.buildvibapsCredentials(builder, trailerParams);
	
		if(!Objects.isNull(trailerParams.getPage()) && !Objects.isNull(trailerParams.getSize())) 
			{
				return builder.deviceIds(Arrays.asList(deviceArray)).trailerIds(Arrays.asList(trailerArray)).activeFrom(utcDateParse(timeZone,trailerParams.getActiveFrom())).activeTo(utcDateParse(timeZone,trailerParams.getActiveTo())).page(trailerParams.getPage()).size(trailerParams.getSize()).build();

			}	
		
return builder.deviceIds(Arrays.asList(deviceArray)).trailerIds(Arrays.asList(trailerArray)).activeFrom(utcDateParse(timeZone,trailerParams.getActiveFrom())).activeTo(utcDateParse(timeZone,trailerParams.getActiveTo())).build();

	}
	
	private String utcDateParse(String timeZone,String dateValue)
	{
		return LocalDateTime.parse(dateValue, DF_PATTERN)
				.atZone(ZoneId.of(timeZone))
				.toOffsetDateTime().toString();
		
	}
	
	private String getDeviceRequestPayload(TrailerParams trailerParams)
	{
GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
return builder.historicalFromDate(trailerParams.getFromDate()).url(trailerParams.getUrl()).groups(trailerParams.getGeotabGroups()).database(trailerParams.getGeotabDatabase()).build();
	}
	
	private String getTrailerListRequestPayload(TrailerParams trailerParams)
	{
GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
return builder.url(trailerParams.getUrl()).groups(trailerParams.getGeotabGroups()).database(trailerParams.getGeotabDatabase()).build();
	}
	
	public TrailerResponse convertParsedReponseShow(TrailerParams trailerParams) 
	{
		
		Long comDatabaseId=comDatabaseRepository.findBydatabaseName(trailerParams.getGeotabDatabase()).getId();
		List<TrailerResponse> responseList=new ArrayList<TrailerResponse>();
		List<CompletableFuture<TrailerResponse>> futureList=new ArrayList<>();  
		trailerParams.getTrailerParsedInput().parallelStream().forEach(t->{
			
			CompletableFuture<TrailerResponse> future= CompletableFuture.supplyAsync(() -> getResponseList(t, trailerParams, comDatabaseId));

			futureList.add(future);
			
		});
		
		CompletableFuture<?> combined = CompletableFuture.allOf(futureList.toArray(new CompletableFuture<?>[0]));
		try
		{
			
			//combined.get();
			futureList.parallelStream().forEach(r->{
				try {
					TrailerResponse response = r.get();
					if(Objects.nonNull(response))
					{
					responseList.add(response);
					}
					else
					{
						log.debug("Null Value :: {}",response.toString());
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
		
		return  new TrailerResponse(responseList);
	}
	
	private TrailerResponse getResponseList(TrailerResponse parsedResponse,TrailerParams trailerParams,Long comDatabaseId)
	{
	    
		DecimalFormat df = new DecimalFormat("###.###");
		df.setRoundingMode(RoundingMode.DOWN);
		List<TrailerResponse> getAddressFromAndToResponce=new ArrayList<TrailerResponse>();
		String getZoneId=getZoneId(trailerParams);
		TrailerResponse trailerResponceReturn = null;
		List<TrailerResponse> latlongResponce = null;
		String deviceName;
		String trailerName;
		TrailerResponse latlngFrom,latlngTo;
		String addresFrom,addressTo;
	    
		
		latlongResponce=getAddresslatlng(parsedResponse.getActiveFrom(),parsedResponse.getActiveTo(), trailerParams,parsedResponse.getDeviceId());	
		
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
		   
			addressTo=addresFrom;
		 
		 
		}
		else
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
				addressTo=addressMap.get(df.format(latlngTo.getLatitude())+"&"+df.format(latlngTo.getLongitude()));
				log.debug("MAP To Address: {}", addressTo);
			}
			else
			{
				addressTo=getAddress(Double.valueOf(df.format(latlngTo.getLatitude())),Double.valueOf(df.format(latlngTo.getLongitude())),trailerParams).getFormattedAddress();
    			addressMap.put(df.format(latlngTo.getLatitude())+"&"+df.format(latlngTo.getLongitude()), addressTo);
    			log.debug("To Address: {}", addressTo);
			}
			
			
			
		}
		 countDevice=genDeviceRepository.countdeviceIdAndrefComDatabaseId(parsedResponse.getDeviceId(),comDatabaseId);
		 countTrailer=genTrailerRepository.counttrailerIdAndrefComDatabaseId(parsedResponse.getTrailerId(),comDatabaseId);
		
		if( countDevice > 0)
		
		{
		  deviceName=genDeviceRepository.findBydeviceIdAndrefComDatabaseId(parsedResponse.getDeviceId(),comDatabaseId).getDeviceName();
		}
		else
		{
			deviceName=	commonGeotabDAO.insertMissedGeoTabDevice(comDatabaseId, trailerParams, parsedResponse.getDeviceId()).getDeviceName();
		}
		
		if( countTrailer> 0)
		{
			
		  trailerName=genTrailerRepository.findBytrailerIdAndrefComDatabaseId(parsedResponse.getTrailerId(),comDatabaseId).getTrailerName();
		
		}
		else 
		{
			trailerName=commonGeotabDAO.insertGeoTabMissedTrailer(comDatabaseId, trailerParams, parsedResponse.getTrailerId()).getTrailerName();
		}
		
		String totime=getZoneTime(getZoneId,parsedResponse.getActiveTo());
		String toaddress="-";
		if(!totime.equals("-"))
		{
			toaddress=addressTo;	
		}
		
		return new TrailerResponse(getZoneTime(getZoneId,parsedResponse.getActiveFrom()),totime,trailerName,deviceName,addresFrom,toaddress);
		
		

	}
	
	@Cacheable(value = "getAddresslatlng")
	public List<TrailerResponse> getAddresslatlng(String activeFrom,String activeTo,TrailerParams trailerParams,String deviceid) 
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
	public String getZoneTime(String timeZoneId,String utcTime)
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
	
	@Cacheable(value = "getZoneTimeForFault")
	public String getZoneTimeForFault(String timeZoneId,String utcTime)
	{
		
	    DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
	    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	    
	     ZonedDateTime parsed = ZonedDateTime.parse(utcTime, formatter.withZone(ZoneId.of(timeZoneId)));
	     if(parsed.getYear()>Calendar.getInstance().get(Calendar.YEAR))
	     {
	    	 return "-";
	     }
	     return  parsed.format(formatter1);
	}
	
	@Cacheable(value = "getZoneTime")
	public String getZoneDateTime(String timeZoneId,String utcTime)
	{

	    DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
	    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a");

	    
	     ZonedDateTime parsed = ZonedDateTime.parse(utcTime.substring(0, utcTime.indexOf("+"))+"Z", formatter.withZone(ZoneId.of(timeZoneId)));
	     if(parsed.getYear()>Calendar.getInstance().get(Calendar.YEAR))
	     {
	    	 return "-";
	     }
	     return  parsed.format(formatter1);
	}
	

	
	
	public String getZoneId(TrailerParams trailerParams)
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
		
		List<TrailerResponse> responcelist=new ArrayList<>();
		
		JsonObject data = new Gson().fromJson(parsedResponse, JsonObject.class);
	    JsonArray names = data .get("result").getAsJsonArray();
	    
	    String zoneId=names.get(0).getAsJsonObject().get("timeZoneId").getAsString();
	   
	    System.out.println(zoneId);
		return zoneId;
	}
	
	public String getEmpId(TrailerParams trailerParams)
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
		
		
		
		return  parseEmpId(parsedResponse);
	}
	public String parseEmpId(JsonObject parsedResponse) 
	{

		JsonObject data = new Gson().fromJson(parsedResponse, JsonObject.class);
	    JsonArray names = data .get("result").getAsJsonArray();
	    
	    
	    String zoneId=names.get(0).getAsJsonObject().get("firstName").getAsString()+" "+names.get(0).getAsJsonObject().get("lastName").getAsString();
	    
	  
	    System.out.println(zoneId);
		return zoneId;
	}
	

	
	public Long insertDeviceandTrailer(TrailerParams trailerParams) {
		
		Long comDatabaseId=commonGeotabDAO.insertDevice(trailerParams).getId();
			
			commonGeotabDAO.insertTrailer(trailerParams,comDatabaseId);
			commonGeotabDAO.insertDriver(trailerParams,comDatabaseId);
			commonGeotabDAO.insertDefects(trailerParams,comDatabaseId);
		return comDatabaseId;
		
		
	}

	@Cacheable(value="getDevice")
	public TrailerResponse getDevice(TrailerParams trailerParams) {
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
	
	public String getUserTimeZone(TrailerParams trailerParams) {
		return getTodayDate(getZoneId(trailerParams));
	}
	
	   private String getTodayDate(String timeZone)
	   {
		   SimpleDateFormat dateTimeInGMT = new SimpleDateFormat("yyyy-MM-dd");
			dateTimeInGMT.setTimeZone(TimeZone.getTimeZone(timeZone));
			
			return dateTimeInGMT.format(new Date());
	   }
	
	public TrailerResponse convertParsedReponse(JsonObject parsedResponse) {
		
		List<TrailerResponse> responcelist=new ArrayList<>();
		
		JsonObject data = new Gson().fromJson(parsedResponse, JsonObject.class);
	    JsonArray names = data .get("result").getAsJsonArray();
	    
	    
	    for(int i=0;i<names.size();i++){
	    	JsonObject device=names.get(i).getAsJsonObject();
	    	
	       
	    	trailerResponce=new TrailerResponse(device.get("id").getAsString(),device.get("name").getAsString());
	    	
	   
	        responcelist.add(trailerResponce);
	        
	    }
	  
		return new TrailerResponse(responcelist);
	}

	@Cacheable(value="getTrailer")
	public TrailerResponse getTrailer(TrailerParams trailerParams) {
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
				.activeFrom(trailerParams.getActiveFrom())
				.activeTo(trailerParams.getActiveTo())
				.deviceSearch()
				.id(trailerParams.getDeviceId())
				.build();
		case 2 :return builder.params().typeName(trailerParams.getTypeName()).search()
				.deviceSearch()
				.id(trailerParams.getDeviceId())
				.build();
		case 3 :return builder.params().typeName(trailerParams.getTypeName()).search()
				.activeFrom(trailerParams.getActiveFrom()).
				activeTo(trailerParams.getActiveTo())
				.build();
		case 4 : return builder.params().typeName(trailerParams.getTypeName())
				.search().groups(geoTabApiService.getGroupList(trailerParams))
				.fromDate(trailerParams.getFromDate()+builder.FROM_TS_SUFFIX)
				.build();
		case 5 : return builder.params().typeName(trailerParams.getTypeName())
				.search().groups(geoTabApiService.getGroupList(trailerParams))
				.build();
		case 6 : return builder.params().typeName("User")
				.search().name(trailerParams.getGeotabUserName())
				.build();
		case 7:return builder.params().typeName(trailerParams.getTypeName()).search()
				//.groups(geoTabApiService.getGroupList(trailerParams))
				.activeFrom(trailerParams.getActiveFrom())
				.activeTo(trailerParams.getActiveTo())
				.build();
		case 8:return builder.params().typeName(trailerParams.getTypeName())	
				.build();
		default : return builder.params().typeName(trailerParams.getTypeName()).build();
				
		}
	}
	
	public TrailerResponse getAddresslat(String timesRange,TrailerParams trailerParams,String deviceid) 
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
		public TrailerResponse getAddress(Double fromLat,Double fromLng,TrailerParams trailerParams) 
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
		TrailerResponse address= convertParsedReponseAddress(parsedResponse);
		
		return address;
		
		
	}
		

	
	private TrailerResponse convertParsedReponseLatLng(JsonObject parsedResponse) {		
		JsonObject data = new Gson().fromJson(parsedResponse, JsonObject.class);
	    JsonArray names = data .get("result").getAsJsonArray();
	    JsonObject device=names.get(0).getAsJsonObject(); 
	   return new TrailerResponse(names.get(0).getAsJsonObject().get("latitude").getAsDouble(),names.get(0).getAsJsonObject().get("longitude").getAsDouble());
	}
	
	private List<TrailerResponse> convertParsedReponseLatLngnew(JsonObject parsedResponse) {		
		List<TrailerResponse> responce=new ArrayList<TrailerResponse>();
		JsonObject data = new Gson().fromJson(parsedResponse, JsonObject.class);
	    JsonArray names = data .get("result").getAsJsonArray();
	    
	    
	    if(names.get(0).getAsJsonArray().size()!=0)
	    {
	    responce.add(new TrailerResponse(names.get(0).getAsJsonArray().get(0).getAsJsonObject().get("latitude").getAsDouble(),names.get(0).getAsJsonArray().get(0).getAsJsonObject().get("longitude").getAsDouble()));
	    }
	    else
	    {
	    	responce.add(new TrailerResponse(0.000,0.000));
	    }
	    
	    if(names.get(1).getAsJsonArray().size()!=0)
	    {
	    responce.add(new TrailerResponse(names.get(1).getAsJsonArray().get(0).getAsJsonObject().get("latitude").getAsDouble(),names.get(1).getAsJsonArray().get(0).getAsJsonObject().get("longitude").getAsDouble()));
	    }
	    else
	    {
	    	responce.add(new TrailerResponse(0.000,0.000));
	    }
	    
	    
		return responce;
	}
	
	private TrailerResponse convertParsedReponseAddress(JsonObject parsedResponse) {		
		JsonObject data = new Gson().fromJson(parsedResponse, JsonObject.class);
	    JsonArray names = data .get("result").getAsJsonArray();
	    JsonObject device=names.get(0).getAsJsonObject();      
	    trailerResponce=new TrailerResponse(device.get("formattedAddress").getAsString());

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
	

	public TrailerResponse getTrailerAttachementData(TrailerParams trailerParams)  {
		List<TrailerResponse> lisrResponce=new ArrayList<TrailerResponse>();
		

		
		

	
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
		
	
	
				
			if(trailerParams.getActiveFrom() !=null && trailerParams.getActiveTo() !=null)
			{
			payload=getReportRequest(trailerParams,7);
			}
			else
			{
				payload=getReportRequest(trailerParams,8);	
			}
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
					/*
					 * exist =
					 * Arrays.stream(trailerArray).anyMatch(device.get("trailer").getAsJsonObject().
					 * get("id"). getAsString()::equals); if(exist) {
					 */
		    			lisrResponce.add(new TrailerResponse(device.get("activeFrom").getAsString(),device.get("activeTo").getAsString(),device.get("trailer").getAsJsonObject().get("id").
						  getAsString(),device.get("device").getAsJsonObject().get("id").
						  getAsString(),device.get("id").getAsString()));
						/* } */
		    }
			
		
		return convertParsedGetTrailerAttachement(lisrResponce,trailerParams,comDatabaseId);
	}
	
	
	private TrailerResponse convertParsedGetTrailerAttachement(List<TrailerResponse> parsedResponse,TrailerParams trailerParams,Long comDatabaseId) 
	{
		List<TrailerResponse> responseList=new ArrayList<TrailerResponse>();
		List<CompletableFuture<TrailerResponse>> futureList=new ArrayList<>();  
		parsedResponse.parallelStream().forEach(t->{
			
			CompletableFuture<TrailerResponse> future= CompletableFuture.supplyAsync(() -> getTrailerAttachementResponseList(t, trailerParams, comDatabaseId));
			futureList.add(future);
		});
		CompletableFuture<?> combined = CompletableFuture.allOf(futureList.toArray(new CompletableFuture<?>[0]));
		try
		{
			//combined.get();
			futureList.parallelStream().forEach(r->{
				try {
					TrailerResponse response = r.get();
					if(Objects.nonNull(response))
					{
					responseList.add(response);
					}
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				} catch (ExecutionException e) {
					
					e.printStackTrace();
				}
			});
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return  new TrailerResponse(responseList);
	}
	
	
	private TrailerResponse getTrailerAttachementResponseList(TrailerResponse parsedResponse,TrailerParams trailerParams,Long comDatabaseId)
	{
	    
		DecimalFormat df = new DecimalFormat("###.###");
		df.setRoundingMode(RoundingMode.DOWN);
		List<TrailerResponse> getAddressFromAndToResponce=new ArrayList<TrailerResponse>();
		String getZoneId=getZoneId(trailerParams);
		TrailerResponse trailerResponceReturn = null;
		List<TrailerResponse> latlongResponce = null;
		String deviceName;
		String trailerName;
		TrailerResponse latlngFrom,latlngTo;
		String addressFrom,addressTo;
	    
		
		latlongResponce=getAddresslatlng(parsedResponse.getActiveFrom(),parsedResponse.getActiveTo(), trailerParams,parsedResponse.getDeviceId());	
		
		latlngFrom=latlongResponce.get(0);
		latlngTo=latlongResponce.get(1);
		 
		if(df.format(latlngFrom.getLatitude()).equals(df.format(latlngTo.getLatitude())) && df.format(latlngFrom.getLongitude()).equals(df.format(latlngTo.getLongitude())))
		{
			if(latlngFrom.getLongitude()!=0.000 && latlngFrom.getLatitude()!=0.000 && latlngTo.getLongitude()!=0.000 && latlngTo.getLatitude()!=0.000)
			{
			if(addressMap.get(df.format(latlngFrom.getLatitude())+"&"+df.format(latlngFrom.getLongitude()))!=null)
			{
				addressFrom=	addressMap.get(df.format(latlngFrom.getLatitude())+"&"+df.format(latlngFrom.getLongitude()));
			}
			else
			{
				 addressFrom=getAddress(Double.valueOf(df.format(latlngFrom.getLatitude())),Double.valueOf(df.format(latlngFrom.getLongitude())), trailerParams).getFormattedAddress();
				 addressMap.put(df.format(latlngFrom.getLatitude())+"&"+df.format(latlngFrom.getLongitude()), addressFrom);
		    		
			}
			
			addressTo=addressFrom;
			}
			else
			{
				addressFrom="-";
				addressTo="-";
			}
		 
		}
		else
		{
			
			if(addressMap.get(df.format(latlngFrom.getLatitude())+"&"+df.format(latlngFrom.getLongitude()))!=null)
			{
				addressFrom=addressMap.get(df.format(latlngFrom.getLatitude())+"&"+df.format(latlngFrom.getLongitude()));
			}
			else
			{
				
    			addressFrom=getAddress(Double.valueOf(df.format(latlngFrom.getLatitude())),Double.valueOf(df.format(latlngFrom.getLongitude())),trailerParams).getFormattedAddress();
    			addressMap.put(df.format(latlngFrom.getLatitude())+"&"+df.format(latlngFrom.getLongitude()), addressFrom);
    			
			}
			
			if(addressMap.get(df.format(latlngTo.getLatitude())+"&"+df.format(latlngTo.getLongitude()))!=null)
			{
				addressTo=addressMap.get(df.format(latlngTo.getLatitude())+"&"+df.format(latlngTo.getLongitude()));
				log.debug("MAP To Address: {}", addressTo);
			}
			else
			{
				addressTo=getAddress(Double.valueOf(df.format(latlngTo.getLatitude())),Double.valueOf(df.format(latlngTo.getLongitude())),trailerParams).getFormattedAddress();
    			addressMap.put(df.format(latlngTo.getLatitude())+"&"+df.format(latlngTo.getLongitude()), addressTo);
    			log.debug("To Address: {}", addressTo);
			}
			
			
			
		}
		 countDevice=genDeviceRepository.countdeviceIdAndrefComDatabaseId(parsedResponse.getDeviceId(),comDatabaseId);
		 countTrailer=genTrailerRepository.counttrailerIdAndrefComDatabaseId(parsedResponse.getTrailerId(),comDatabaseId);
		
		if( countDevice > 0)
		
		{
		  deviceName=genDeviceRepository.findBydeviceIdAndrefComDatabaseId(parsedResponse.getDeviceId(),comDatabaseId).getDeviceName();
		}
		else
		{
			deviceName=	commonGeotabDAO.insertMissedGeoTabDevice(comDatabaseId, trailerParams, parsedResponse.getDeviceId()).getDeviceName();
		}
		
		if( countTrailer> 0)
		{
			
		  trailerName=genTrailerRepository.findBytrailerIdAndrefComDatabaseId(parsedResponse.getTrailerId(),comDatabaseId).getTrailerName();
		
		}
		else 
		{
			trailerName=commonGeotabDAO.insertGeoTabMissedTrailer(comDatabaseId, trailerParams, parsedResponse.getTrailerId()).getTrailerName();
		}
		
		
		
		return new TrailerResponse(parsedResponse.getActiveFrom(),parsedResponse.getActiveTo(),trailerName,deviceName,addressFrom,addressTo,parsedResponse.getTrailerattachedId());
		
		

	}


	
	
	

	
	

}
