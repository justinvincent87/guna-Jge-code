package com.vibaps.merged.safetyreport.services.geotabdriveapp;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
import com.vibaps.merged.safetyreport.dto.geodriveapp.GeoDriveDateResponse;
import com.vibaps.merged.safetyreport.dto.geodriveapp.GeotabBehavierResponse;
import com.vibaps.merged.safetyreport.dto.geodriveapp.GeotabDriverCallResponse;
import com.vibaps.merged.safetyreport.dto.geodriveapp.LytxScoreListResponse;
import com.vibaps.merged.safetyreport.dto.gl.Behave;
import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerParams;
import com.vibaps.merged.safetyreport.entity.gl.GlRulelistEntity;
import com.vibaps.merged.safetyreport.entity.gl.LyUserEntity;
import com.vibaps.merged.safetyreport.entity.gl.ReportRow;
import com.vibaps.merged.safetyreport.repo.gl.CommonGeotabRepository;
import com.vibaps.merged.safetyreport.services.gl.GeoTabApiService;
import com.vibaps.merged.safetyreport.services.gl.LytxProxyService;
import com.vibaps.merged.safetyreport.services.gl.UserReportFilterService;
import com.vibaps.merged.safetyreport.util.DateTimeUtil;
import com.vibaps.merged.safetyreport.util.ResponseUtil;

import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
public class GeotabDriveAppServices {
	
	 @Autowired
	 private CommonGeotabRepository commonGeotabRepository;
	 @Autowired
	 private GeoTabApiService geoTabApiService;
	 @Autowired
	 private UserReportFilterService userReportFilterService;
	 @Autowired
	 private LytxProxyService lytxProxyService;
	 
	 @Autowired
	 private RestTemplate restTemplate;

	public List<List<GeotabDriverCallResponse>> showScore(ReportParams reportParams) throws RemoteException, ParseException {

		
		  List<GeoDriveDateResponse> dateRange=getDateRangeList(); 
		  LyUserEntity enty=commonGeotabRepository.getLytxCredentials(reportParams.getGeotabDatabase()); 
		  String sessionId=LytexLogin(enty.getLytxUsername(),enty.getLytxPassword(),enty.getLyEndpoint());
		 
		  Map<Long,GeotabDriverCallResponse> userMap=getUserMap(enty.getLyEndpoint(), sessionId);
		  
			List<Behave> selectedLytxRuleNames = userReportFilterService.getSelectedLytxRuleNames(reportParams.getGeotabUserName(),reportParams.getGeotabDatabase());

			List<String> lytxBehave=new ArrayList<String>();
			for(Behave data:selectedLytxRuleNames)
			{
				lytxBehave.add(data.getRuleName());
			}
		
			//reportParams.setDateRange(getDateRangeList());
	
			//List<CompletableFuture<List<GeotabDriverCallResponse>>>  listcompletableFuture=new ArrayList<CompletableFuture<List<GeotabDriverCallResponse>>>();
			
			List<GeoDriveDateResponse> items=getDateRangeList();
			
			List<CompletableFuture<List<GeotabDriverCallResponse>>> listcompletableFuture = items.stream()
	                .map(item -> CompletableFuture.supplyAsync(() -> 
	                {
						try {
							return parserdLytxandGeotabResponse(reportParams,enty.getLyEndpoint(), sessionId, userMap, lytxBehave,item);
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						return null;
					}))
	                .collect(Collectors.toList());
			
			
			/*
			 * reportParams.getDateRange().parallelStream().forEach(t->{
			 * 
			 * 
			 * CompletableFuture<List<GeotabDriverCallResponse>> completableFuture =
			 * CompletableFuture.supplyAsync(() -> { try { return
			 * parserdLytxandGeotabResponse(reportParams,enty.getLyEndpoint(), sessionId,
			 * userMap, lytxBehave,t); } catch (Exception e) { // TODO Auto-generated catch
			 * block e.printStackTrace(); } return null; });
			 * 
			 * listcompletableFuture.add(completableFuture);
			 * 
			 * });
			 */
		
			
		//	CompletableFuture<?> combined = CompletableFuture.allOf(listcompletableFuture.toArray(new CompletableFuture<?>[0]));	
			
			
			List<List<GeotabDriverCallResponse>> listResponse=new ArrayList<List<GeotabDriverCallResponse>>();
			
			
			
			
			try
			{
				
				listcompletableFuture.parallelStream().forEach(r->{
					try {
						List<GeotabDriverCallResponse> sresponse =  r.get();
						if(Objects.nonNull(sresponse))
						{
							if(sresponse.size()>0)
							{
								listResponse.add(sresponse);
							}
						}
						else
						{
							log.debug("Null Value :: {}",sresponse.toString());
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
		return listResponse;
	}
	
	private List<GeotabDriverCallResponse> parserdLytxandGeotabResponse(
			ReportParams reportParams,String endpoint,String sessionId,Map<Long,GeotabDriverCallResponse> userMap,List<String> selectedLytxRuleNames,GeoDriveDateResponse range) throws RemoteException, ParseException {
		// TODO Auto-generated method stub
		
		System.out.println(range.getStartDate());
		System.out.println(range.getEndDate());
		
		
		
		Map<String,GeotabDriverCallResponse> geotabResponse=getVehicleExceptionSummary(reportParams,range);
		Map<String,GeotabDriverCallResponse> getLytxResponse=getLytxExceptionData(reportParams, endpoint, sessionId, userMap,selectedLytxRuleNames,range);
		
		
		List<GeotabDriverCallResponse> mergeList=new ArrayList<GeotabDriverCallResponse>();
		Map<String,GeotabDriverCallResponse> mergeResponse=new HashMap<String, GeotabDriverCallResponse>();
		
		List<String> geotabKey=new ArrayList<String>(geotabResponse.keySet());
		List<String> lytxKey=new ArrayList<String>(getLytxResponse.keySet());
		
		
		for(String empNumber:geotabKey)
		{
			mergeResponse.put(empNumber, geotabResponse.get(empNumber));
		}
		
		
		for(String empNumber:lytxKey)
		{
			if(Objects.isNull(mergeResponse.get(empNumber)))
			{
			mergeResponse.put(empNumber, getLytxResponse.get(empNumber));
			}
			else
			{
				GeotabDriverCallResponse enty=mergeResponse.get(empNumber);
				Integer eventcount=enty.getEventCount();
				enty.setEventCount(eventcount+getLytxResponse.get(empNumber).getEventCount());
				
				List<GeotabBehavierResponse> oldlist=enty.getBehavelist();
				List<GeotabBehavierResponse> lyList=getLytxResponse.get(empNumber).getBehavelist();
				List<GeotabBehavierResponse> newList = new ArrayList<GeotabBehavierResponse>();
				newList.addAll(oldlist);
				newList.addAll(lyList);

				enty.setBehavelist(newList);
				
				mergeResponse.put(empNumber, enty);
			}
		}
		
		
		List<String> mergedKeyList=new ArrayList<String>(mergeResponse.keySet());
		
		for(String mergeKey:mergedKeyList)
		{
			mergeList.add(mergeResponse.get(mergeKey));
		}
		
		
		return mergeList;
	}

	private Map<Long,GeotabDriverCallResponse> getUserMap(String endpoint,String sessionId) throws RemoteException
	{
		ISubmissionServiceV5Proxy er=new ISubmissionServiceV5Proxy(endpoint);
		  GetUsersResponse vr=new GetUsersResponse();
		  GetUsersRequest getVehiclesRequest=new GetUsersRequest();
		  getVehiclesRequest.setSessionId(sessionId);
		  vr=er.getUsers(getVehiclesRequest);
		  Map<Long,GeotabDriverCallResponse> userMap=new HashMap<Long, GeotabDriverCallResponse>();
		  
		  for(UserInfo data:vr.getUsers())
		  {
			  GeotabDriverCallResponse userResponse=new GeotabDriverCallResponse();
			  userResponse.setFirstName(data.getFirstName());
			  userResponse.setLastName(data.getLastName());
			  userResponse.setEmployeeNo(data.getEmployeeNumber());
			  
			  userMap.put(data.getUserId(),userResponse);
		  }
		  
		  return userMap;
	}
	
	public Map<String,GeotabDriverCallResponse> getLytxExceptionData(ReportParams reportParams,String endpoint,String sessionId,Map<Long,GeotabDriverCallResponse> userMap,List<String> selectedLytxRuleNames,GeoDriveDateResponse range) throws ParseException, RemoteException {
		Integer	behaviorCount;
		//Boolean status=false;
		
		/*
		 * String behaviors=env.getProperty("lytx.behaviors.filter"); String[]
		 * behaviorsList=behaviors.split(",");
		 */
	
		Map<String,GeotabDriverCallResponse>	lytxVehicleEventsRecord	= new HashMap<String, GeotabDriverCallResponse>();
		
		

		String startDateStr = range.getStartDate() + AppConstants.START_UTC;
		String endDateStr = range.getEndDate() + AppConstants.END_UTC;
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
			List<GeotabBehavierResponse> behaveList=new ArrayList<GeotabBehavierResponse>();

			
			for (EventBehavior behavior : event.getBehaviors()) {
				String exceptionName	= behavior.getBehavior().toString();
				
				if(selectedLytxRuleNames.contains(exceptionName))
				{

					GeotabBehavierResponse behave=new GeotabBehavierResponse();
					behave.setBehavierId(exceptionName);
					behave.setCount(1);
					behave.setEventDate(event.getCreationDate().toString());
					
					behaveList.add(behave);
				}
					
				
			}
		
			
			if(event.getDriverId() !=0)
			{
				String vehicleName			= userMap.get(event.getDriverId()).getFirstName()+" "+userMap.get(event.getDriverId()).getLastName();

				GeotabDriverCallResponse lytxExceptionEvents	= lytxVehicleEventsRecord.get(vehicleName);
	
			if (Objects.isNull(lytxExceptionEvents)) {
				
				GeotabDriverCallResponse enty=new GeotabDriverCallResponse();
				enty.setEmployeeNo(vehicleName);
				//enty.setBehavierId(innserArray.getJSONObject(0).getJSONObject("exceptionRule").getString("id"));
				enty.setStartDate(range.getStartDate());
				enty.setEndDate(range.getEndDate());
				enty.setRange(range.getRange());
				enty.setBehavelist(behaveList);
				
				
				lytxVehicleEventsRecord.put(vehicleName, enty);
			}
			else
			{
				GeotabDriverCallResponse enty=lytxVehicleEventsRecord.get(vehicleName);
				Integer eventcount=enty.getEventCount();
				enty.setEventCount(eventcount+event.getScore().intValue());
				
				List<GeotabBehavierResponse> behaveListOld=enty.getBehavelist();

				behaveListOld.addAll(behaveList);
				enty.setBehavelist(behaveListOld);
				
				
				lytxVehicleEventsRecord.put(vehicleName,enty);
				
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



	
	public Map<String,GeotabDriverCallResponse>  getVehicleExceptionSummary(ReportParams reportParams,GeoDriveDateResponse range) {

		String payload = getReportRequest(reportParams,range);
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

		
		return parsedGeotabDriverResponse(response,range);
				
	}
	
	private Map<String,GeotabDriverCallResponse>  parsedGeotabDriverResponse(ResponseEntity<String> response,GeoDriveDateResponse range ) 
	{
		
		Map<String,GeotabDriverCallResponse> parsedresponse=new HashMap<String, GeotabDriverCallResponse>();
		
		JSONObject obj=new JSONObject(response.getBody());
		
		JSONArray objArray=obj.getJSONArray("result").getJSONArray(0);
		
		for(int i=0;i<objArray.length();i++)
		{
			JSONObject innerObj=objArray.getJSONObject(i);
			//String empNumber=innerObj.getJSONObject("item").getString("name");
			String empNumber=innerObj.getJSONObject("item").getString("firstName")+" "+innerObj.getJSONObject("item").getString("lastName");
			
			JSONArray innserArray=innerObj.getJSONArray("exceptionSummaries");
			
			try
			{
			if(innserArray.length()>0)
			{
				List<GeotabBehavierResponse> behaveList=new ArrayList<GeotabBehavierResponse>();
				for(int s=0;s<innserArray.length();s++)
				{
				
				if(Objects.isNull(parsedresponse.get(empNumber)) && innserArray.getJSONObject(s).getInt("eventCount") !=0)
				{
					GeotabBehavierResponse behave=new GeotabBehavierResponse();
					behave.setBehavierId(innserArray.getJSONObject(s).getJSONObject("exceptionRule").getString("id"));
					behave.setCount(innserArray.getJSONObject(s).getInt("eventCount"));
					behave.setEventDate(innserArray.getJSONObject(s).getString("duration"));
					
					behaveList.add(behave);
					
					GeotabDriverCallResponse enty=new GeotabDriverCallResponse();
					enty.setEmployeeNo(empNumber);
					//enty.setBehavierId(innserArray.getJSONObject(s).getJSONObject("exceptionRule").getString("id"));
					//enty.setEventCount(innserArray.getJSONObject(s).getInt("eventCount"));
					enty.setStartDate(range.getStartDate());
					enty.setEndDate(range.getEndDate());
					enty.setRange(range.getRange());
					enty.setBehavelist(behaveList);					
					parsedresponse.put(empNumber, enty);
				}
				else
				{
					GeotabDriverCallResponse enty=parsedresponse.get(empNumber);
					//Integer eventcount=enty.getEventCount();
					//enty.setEventCount(eventcount+innserArray.getJSONObject(s).getInt("eventCount"));
					
					List<GeotabBehavierResponse> behaveListval=enty.getBehavelist();
					GeotabBehavierResponse behave=new GeotabBehavierResponse();
					behave.setBehavierId(innserArray.getJSONObject(s).getJSONObject("exceptionRule").getString("id"));
					behave.setCount(innserArray.getJSONObject(s).getInt("eventCount"));
					behave.setEventDate(innserArray.getJSONObject(s).getString("duration"));
					
					behaveListval.add(behave);
					enty.setBehavelist(behaveListval);					

					
					parsedresponse.put(empNumber, enty);

				}
				}
				
			}
			
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		return parsedresponse;
	}

	private String getReportRequest(ReportParams reportParams,GeoDriveDateResponse range) {

		List<GlRulelistEntity> ruleList = userReportFilterService.getGeoDropDown(reportParams.getGeotabUserName(),
		        reportParams.getGeotabDatabase());

		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_EXECUTE_MULTI_CALL);

		// bind credentials
		geoTabApiService.buildCredentials(builder, reportParams);
		
			
			return builder.params().addCalls().method(AppConstants.METHOD_GET_REPORT_DATA).params().argument()
			        .runGroupLevel(-1).isNoDrivingActivityHidden(true).fromUtc(range.getStartDate())
			        .toUtc(range.getEndDate()).entityType("Driver")
			        .reportArgumentType(AppConstants.PARAM_RISK_MANAGEMENT).groups(Collections.<String>emptyList())
			        .reportSubGroup(AppConstants.PARAM_NONE)
			        .rules(ruleList.stream().map(GlRulelistEntity::getRulevalue).collect(Collectors.toList())).and().and()
			        .done().addCalls().method(AppConstants.METHOD_GET).params().typeName(AppConstants.PARAM_SYSTEM_SETTINGS)
			        .build();
		

	}
	
	private String LytexLogin(String username,String password,String endpoint) throws RemoteException
	{
		 ISubmissionServiceV5Proxy er=new ISubmissionServiceV5Proxy(endpoint);
		  LoginResponse value=er.login(username,password);
		  
		  return value.getSessionId();
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

}
