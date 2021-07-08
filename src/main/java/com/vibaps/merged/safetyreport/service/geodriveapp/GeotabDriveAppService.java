package com.vibaps.merged.safetyreport.service.geodriveapp;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api.EventBehavior;
import org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api_GetEvents_V5.GetEventsResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.lytx.dto.EventsInfoV5;
import com.lytx.dto.GetUsersRequest;
import com.lytx.dto.GetUsersResponse;
import com.lytx.dto.LoginResponse;
import com.lytx.dto.UserInfo;
import com.lytx.services.ISubmissionServiceV5Proxy;
import com.vibaps.merged.safetyreport.builder.GeoTabRequestBuilder;
import com.vibaps.merged.safetyreport.builder.Uri;
import com.vibaps.merged.safetyreport.common.AppConstants;
import com.vibaps.merged.safetyreport.common.EntityType;
import com.vibaps.merged.safetyreport.dto.geodriveapp.GeoDriveAppResponse;
import com.vibaps.merged.safetyreport.dto.geodriveapp.GeoDriveDateResponse;
import com.vibaps.merged.safetyreport.dto.geodriveapp.LytxGroupResponse;
import com.vibaps.merged.safetyreport.dto.geodriveapp.LytxScoreListResponse;
import com.vibaps.merged.safetyreport.dto.geodriveapp.LytxTokenResponse;
import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerResponse;
import com.vibaps.merged.safetyreport.entity.gl.LyUserEntity;
import com.vibaps.merged.safetyreport.repo.gl.CommonGeotabRepository;
import com.vibaps.merged.safetyreport.repo.gl.LyUserRepository;
import com.vibaps.merged.safetyreport.services.gl.CommonGeotabService;
import com.vibaps.merged.safetyreport.services.gl.GeoTabApiService;
import com.vibaps.merged.safetyreport.services.gl.LytxProxyService;
import com.vibaps.merged.safetyreport.util.DateTimeUtil;
import com.vibaps.merged.safetyreport.util.ResponseUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class GeotabDriveAppService {
	
	@Autowired
	 private CommonGeotabRepository commonGeotabRepository;
	
	@Autowired
	private LytxProxyService lytxProxyService;
	
	@Autowired 
	private CommonGeotabService commonGeotabService;
	
	@Autowired
    private Environment env;
	@Autowired
	private GeoTabApiService geoTabApiService;
	@Autowired
	private RestTemplate restTemplate;
	
	

	public LytxScoreListResponse showScore(TrailerParams reportParams) throws RemoteException, ParseException, JsonMappingException, JsonProcessingException, InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		
		
		reportParams.setGeotabSessionId(getAdminLoginSessionId(reportParams));
		String authUrl=authUrlBuild(reportParams);
		
		
		ResponseEntity<LytxTokenResponse> response=getBearerResponseCall(authUrl);
		ResponseEntity<String> groupResponse=getGroupBearerResponseCall(response);
		List<LytxScoreListResponse> parsedResponse=new ArrayList<LytxScoreListResponse>();
		List<LytxScoreListResponse> finalResponse=new ArrayList<LytxScoreListResponse>();
		List<List<LytxScoreListResponse>> responseList=new ArrayList<List<LytxScoreListResponse>>();
		
		JSONObject groupObj=new JSONObject(groupResponse.getBody());
		JSONArray groupArry=groupObj.getJSONArray("groups");
		String groupId=groupArry.getJSONObject(0).getString("id");
		
		reportParams.setDateRange(getDateRangeList());
		
		
		List<CompletableFuture<List<LytxScoreListResponse>>>  listcompletableFuture=new ArrayList<CompletableFuture<List<LytxScoreListResponse>>>();
		
		reportParams.getDateRange().parallelStream().forEach(t->{
			

			CompletableFuture<List<LytxScoreListResponse>> completableFuture = CompletableFuture.supplyAsync(() -> scoreParsedResponse(response,groupId,reportParams,t));
	
	listcompletableFuture.add(completableFuture);		
	
		});
		
		CompletableFuture<?> combined = CompletableFuture.allOf(listcompletableFuture.toArray(new CompletableFuture<?>[0]));

		try
		{
			
			//combined.get();
			listcompletableFuture.parallelStream().forEach(r->{
				try {
					List<LytxScoreListResponse> sresponse =  r.get();
					if(Objects.nonNull(sresponse))
					{
						List<LytxScoreListResponse> listOfScoreResponse=parserFinalResponse(sresponse,reportParams);
						if(listOfScoreResponse.size()>0)
						{
						responseList.add(listOfScoreResponse);
						}
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
		
		
		List<GeoDriveDateResponse> dateRange=getDateRangeList();
		
		return new LytxScoreListResponse(responseList,true,dateRange);
		
	}
	
	public String getAdminLoginSessionId(TrailerParams trailerParams) 
	{
		// TODO Auto-generated method stub
		
		
		String payload =  getLoginPayload(trailerParams);
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
		JSONObject resultobj=obj.getJSONObject("result");
		JSONObject creObj=resultobj.getJSONObject("credentials");
		
		return creObj.getString("sessionId");
	}





	private String getLoginPayload(TrailerParams trailerParams) {
		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method("Authenticate");
		//geoTabApiService.buildCredentials(builder, trailerParams);
		
		return builder.params().userName(env.getProperty("geotab.admin.username"))
				.password(env.getProperty("geotab.admin.password"))
				.database(trailerParams.getGeotabDatabase()).build();
	}

	private List<LytxScoreListResponse> parserFinalResponse(
			List<LytxScoreListResponse> datavalue,TrailerParams reportParams) {
		// TODO Auto-generated method stub
		Integer rank=0;

		List<LytxScoreListResponse> finalResponse=new ArrayList<LytxScoreListResponse>();
				try
					{
						
					
						
					
						Comparator<LytxScoreListResponse> comparator = Comparator.comparing(LytxScoreListResponse::getTotalScore); 
						Collections.sort(datavalue, comparator.reversed());
						
						for(LytxScoreListResponse value:datavalue)
						{
							Float temp = null;
							
								if(!value.getTotalScore().equals(temp))
								{
									rank=rank+1;
									value.setRank(rank);
								}
								else
								{
									value.setRank(rank);
								}
								
								
								 if(reportParams.getGeotabEmployeeNo().equals(value.getEmployeeNum())) 
								 {
								finalResponse.add(value);
								 }
								temp=value.getTotalScore();
						}
					
						
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				
			
				
				
			
		return finalResponse;
	}







	private List<GeoDriveDateResponse> getDateRangeList()
	{
		List<GeoDriveDateResponse> responseList=new ArrayList<GeoDriveDateResponse>();
		for(int i=1;i<5;i++)
		 {
			
			 responseList.add(dateCalculation(i));
		 }

		
		return responseList;
	}
	
	private GeoDriveDateResponse dateCalculation(int data)
	{
		 GeoDriveDateResponse response=new GeoDriveDateResponse();
		 ZonedDateTime input = ZonedDateTime.now();
		 ZonedDateTime startOfLastWeek=null;
		 ZonedDateTime endOfLastWeek=null;
		 
		switch (data) {
		case 1:
			startOfLastWeek = input.minusWeeks(1).with(DayOfWeek.SUNDAY);
			//endOfLastWeek = startOfLastWeek.plusDays(6);
			response.setRange("This Week");
			response.setStartDate(startOfLastWeek.toLocalDate().toString());		 
			response.setEndDate(input.toLocalDate().toString());
			break;
		case 2:
			startOfLastWeek = input.minusWeeks(2).with(DayOfWeek.SUNDAY);
			endOfLastWeek = startOfLastWeek.plusDays(6);
			response.setRange("Last Week");
			response.setStartDate(startOfLastWeek.toLocalDate().toString());		 
			response.setEndDate(endOfLastWeek.toLocalDate().toString());
			break;
		case 3:
			startOfLastWeek = input.withDayOfMonth(1);
			response.setRange("This Month");
			response.setStartDate(startOfLastWeek.toLocalDate().toString());		 
			response.setEndDate(input.toLocalDate().toString());
			break;	
			
		case 4:
	
			startOfLastWeek = input.minusMonths(1).withDayOfMonth(1);
			endOfLastWeek = input.withDayOfMonth(1).minusDays(1);
			response.setRange("Last Month");
			response.setStartDate(startOfLastWeek.toLocalDate().toString());		 
			response.setEndDate(endOfLastWeek.toLocalDate().toString());
			break;	

		default:
			break;
		}
		
		 return response;
	}
	
	private List<LytxScoreListResponse> scoreParsedResponse(ResponseEntity<LytxTokenResponse> response,String groupId,TrailerParams reportParams,GeoDriveDateResponse range) {
		// TODO Auto-generated method stub
		ResponseEntity<String> data=getUserScoreResponse(response, groupId, reportParams,range);

		List<LytxScoreListResponse> responses=new ArrayList<LytxScoreListResponse>();
		JSONObject scoreObj=new JSONObject(data.getBody());
		JSONArray scoreArry=scoreObj.getJSONArray("impacts");
		
		for(int i=0;i<scoreArry.length();i++)
		{
			JSONObject obj=scoreArry.getJSONObject(i);
			
			LytxScoreListResponse enty=new LytxScoreListResponse();
			enty.setEmployeeNum(obj.getString("employeeNum"));
			enty.setFirstName(obj.getString("firstName"));
			enty.setLastName(obj.getString("lastName"));
			enty.setTotalScore(obj.getFloat("totalScore"));
			enty.setRange(range.getRange());
			enty.setStareDate(range.getStartDate());
			enty.setEndDate(range.getEndDate());
			
			responses.add(enty);
			
		}
		
		return responses;
	}

	private ResponseEntity<String> getUserScoreResponse(ResponseEntity<LytxTokenResponse> response,String groupId,TrailerParams trailerParams,GeoDriveDateResponse range) {
		String url=scoreUrlBuild(trailerParams, groupId,range);
		
		if (log.isDebugEnabled()) {
			log.debug("Get report data uri: {}", url);
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Get report data uri: {}", response.getBody().getAccessToken());
		}
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		
        headers.set("Authorization", "Bearer " + response.getBody().getAccessToken()); //accessToken can be the secret key you generate.
		
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
		return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

	}
	
	
	private ResponseEntity<String> getGroupBearerResponseCall(ResponseEntity<LytxTokenResponse> response) {
		String url=response.getBody().getAction()+"/api/v1/core/groups?live=true";
		
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		
        headers.set("Authorization", "Bearer " + response.getBody().getAccessToken()); //accessToken can be the secret key you generate.
		
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
		return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

	}
	private String authUrlBuild(TrailerParams reportParams)
	{
		return env.getProperty("lytx.url.auth")+"sessionId="+reportParams.getGeotabSessionId()+"&username="+env.getProperty("geotab.admin.username")+"&databaseName="+reportParams.getGeotabDatabase()+"&geoTabBaseUrl="+reportParams.getUrl();
	}
	
	private String scoreUrlBuild(TrailerParams reportParams,String groupId,GeoDriveDateResponse range)
	{
		return env.getProperty("lytx.url.scorecard")+"groupIds="+groupId+"&startDate="+range.getStartDate()+"&endDate="+range.getEndDate();
	}
	
	private ResponseEntity<LytxTokenResponse> getBearerResponseCall(String url)
	{
		
		
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
		return restTemplate.exchange(url, HttpMethod.GET, entity, LytxTokenResponse.class);
	}
	
	
	public GeoDriveAppResponse showScore_old(TrailerParams reportParams) throws RemoteException, ParseException {
	
	String startDateForGetDateRange=reportParams.getStartDate();
		String endDateForGetDateRange=reportParams.getEndDate();
		LyUserEntity enty=commonGeotabRepository.getLytxCredentials(reportParams.getGeotabDatabase());
		String sessionId=LytexLogin(enty.getLytxUsername(),enty.getLytxPassword(),enty.getLyEndpoint());
		Map<Long, String> vehicles				=getUserMap(enty.getLyEndpoint(), sessionId);

		Map<Long,Long> lytxData=getLytxExceptionData(reportParams, enty.getLyEndpoint(), sessionId);
		
		Long differentDate=DateTimeUtil.getNumberofDays(startDateForGetDateRange, endDateForGetDateRange);
		String previewsDay=DateTimeUtil.findPrevDay(startDateForGetDateRange).toString();
		String prevStartDay=DateTimeUtil.findPrevStartDay(previewsDay,differentDate.intValue()).toString();
		
		
		reportParams.setStartDate(prevStartDay);
		reportParams.setEndDate(previewsDay);
		
		System.out.println(prevStartDay+"---------"+previewsDay+"---"+differentDate);
		Map<Long,Long> lytxDataPrevies=getLytxExceptionData(reportParams, enty.getLyEndpoint(), sessionId);
		
	
		
		       
	
		List<GeoDriveAppResponse> response=new ArrayList<GeoDriveAppResponse>();
		List<GeoDriveAppResponse> finalresponse=new ArrayList<GeoDriveAppResponse>();

		
		List<Long> listLytxdata = new ArrayList<Long>(lytxData.keySet()); 
		
		

		for(Long data:listLytxdata)
		{
			GeoDriveAppResponse res=new GeoDriveAppResponse();
			Integer per1=0;
			Integer per2=0;
			
			 per1=lytxData.get(data).intValue()*100/differentDate.intValue();
			
			 if(!Objects.isNull(lytxDataPrevies.get(data)))
			 {
			 per2=lytxDataPrevies.get(data).intValue()*100/differentDate.intValue();
			 }
			 
			 if(per2>per1)
			{
				res.setStatus('D');
				res.setPersentage(per2-per1);			}
			else if(per2<per1)
			{
				res.setStatus('U');
				res.setPersentage(per1-per2);
			}
			else
			{
				res.setPersentage(per1-per2);
				res.setStatus('E');
			}
			res.setName(vehicles.get(data));
			res.setScore(lytxData.get(data));
			
			response.add(res);
		}
		
		Comparator<GeoDriveAppResponse> comparator = Comparator.comparing(GeoDriveAppResponse::getScore); 
		Collections.sort(response, comparator.reversed());
		
		
		
		Integer rank=0;
		Long temp = null;
		for(GeoDriveAppResponse value:response)
		{
			if(!value.getScore().equals(temp))
			{
				rank=rank+1;
				value.setRank(rank);
			}
			else
			{
				value.setRank(rank);
			}
			finalresponse.add(value);

			temp=value.getScore();
			
		}
		
		if(reportParams.getGeotabUserName()!=null)
		{
		String name=commonGeotabService.getDriver(reportParams).getName();
		List<GeoDriveAppResponse> filterResponse = finalresponse.stream()
			    .filter(p -> p.getName().equals(name)).collect(Collectors.toList());
		
		return new GeoDriveAppResponse(filterResponse);

		}
		
		
        
		return new GeoDriveAppResponse(finalresponse);
	}
	
	

	
	private String LytexLogin(String username,String password,String endpoint) throws RemoteException
	{
		 ISubmissionServiceV5Proxy er=new ISubmissionServiceV5Proxy(endpoint);
		  LoginResponse value=er.login(username,password);
		  
		  return value.getSessionId();
	}
	
	private Map<Long,String> getUserMap(String endpoint,String sessionId) throws RemoteException
	{
		ISubmissionServiceV5Proxy er=new ISubmissionServiceV5Proxy(endpoint);
		  GetUsersResponse vr=new GetUsersResponse();
		  GetUsersRequest getVehiclesRequest=new GetUsersRequest();
		  getVehiclesRequest.setSessionId(sessionId);
		  vr=er.getUsers(getVehiclesRequest);
		  Map<Long,String> userMap=new HashMap<Long, String>();
		  for(UserInfo data:vr.getUsers())
		  {
			  userMap.put(data.getUserId(), data.getFirstName()+" "+data.getLastName());
		  }
		  
		  return userMap;
	}
	
	public Map<Long,Long> getLytxExceptionData(ReportParams reportParams,String endpoint,String sessionId) throws ParseException, RemoteException {
		Integer	behaviorCount;
		//Boolean status=false;
		
		/*
		 * String behaviors=env.getProperty("lytx.behaviors.filter"); String[]
		 * behaviorsList=behaviors.split(",");
		 */
	
		Map<Long,Long>	lytxVehicleEventsRecord	= new HashMap<Long, Long>();


		String startDateStr = reportParams.getStartDate() + AppConstants.START_UTC;
		String endDateStr = reportParams.getEndDate() + AppConstants.END_UTC;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date newStartDate = sdf.parse(startDateStr);
		
		Date ssdate = sdf.parse(startDateStr);
		Date eedate = sdf.parse(endDateStr);
		int s=0;
		String sdate=startDateStr;
		
		do {

			System.out.println("check---" + s++);
			
			reportParams.setStartDate(sdate);

			reportParams.setLytexSessionid(sessionId);
			reportParams.setEndPoint(endpoint);
			GetEventsResponse eventReponse= lytxProxyService.getLytxExceptionSummary(reportParams);
			
		
		for (EventsInfoV5 event : eventReponse.getEvents()) {
			
//			status=false;
//			
//			for (EventBehavior behavior : event.getBehaviors()) {
//				String exceptionName	= behavior.getBehavior().toString();
//				
//				if(Arrays.stream(behaviorsList).anyMatch(exceptionName::equals))
//				{
//					status=true;
//				}
//					
//				
//			}
			
			Long vehicleName			= event.getDriverId();
			if(vehicleName !=0)
			{
				Long lytxExceptionEvents	= lytxVehicleEventsRecord.get(vehicleName);
	
			if (Objects.isNull(lytxExceptionEvents)) {
				lytxVehicleEventsRecord.put(vehicleName, event.getScore());
			}
			else
			{
				Long score	= lytxVehicleEventsRecord.get(vehicleName)+event.getScore();
				lytxVehicleEventsRecord.put(vehicleName,score);
				
			}
			}
			
				
				
			
		}
		
		log.info("Parse Lytx Event - {}","Stop");
		
		if (eventReponse.getQueryCutoff() != null) {
			String cutoffData = eventReponse.getQueryCutoff().toString();
			System.out.println(cutoffData);

			if (cutoffData != null) {

				newStartDate = DateTimeUtil.getDateFromMilliSeconds(cutoffData);

				String year = (newStartDate.getYear() + 1900) + "";
				String month = (newStartDate.getMonth() + 1) + "";
				String date = newStartDate.getDate() + "";

				if (month.length() == 1) {
					month = "0" + month;
				}
				if (date.length() == 1) {
					date = "0" + date;
				}
				String strNewDate = year + "-" + month + "-" + date;

				if (strNewDate.equals(sdate)) {
					break;
				}
				sdate = strNewDate;

			} else {
				break;
			}
		} else {
			break;
		}
		

	
} while (true);

		if (log.isTraceEnabled()) {
			log.trace("Exception records : {}", lytxVehicleEventsRecord);
		}
		return lytxVehicleEventsRecord;
	}

}
