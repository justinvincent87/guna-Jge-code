package com.vibaps.merged.safetyreport.services.gl;

import static com.vibaps.merged.safetyreport.util.DateTimeUtil.parseUtilDate;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api.EventBehavior;
import org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api_GetEvents_V5.GetEventsResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.lytx.dto.Behavior;
import com.lytx.dto.EventsInfoV5;
import com.lytx.dto.ExistingSessionRequest;
import com.lytx.dto.GetBehaviorsResponse;
import com.lytx.dto.GetEventsByLastUpdateDateRequest;
import com.lytx.dto.GetUsersRequest;
import com.lytx.dto.GetUsersResponse;
import com.lytx.dto.GetVehiclesRequest;
import com.lytx.dto.GetVehiclesResponse;
import com.lytx.dto.UserInfo;
import com.lytx.dto.VehicleInfo;
import com.lytx.services.ISubmissionServiceV5Proxy;
import com.vibaps.merged.safetyreport.common.AppConstants;
import com.vibaps.merged.safetyreport.common.AppMsg;
import com.vibaps.merged.safetyreport.common.EntityType;
import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
import com.vibaps.merged.safetyreport.exception.GeoTabException;
import com.vibaps.merged.safetyreport.util.DateTimeUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class LytxProxyService {

	/**
	 * Get lytx exception details from lytx API
	 * 
	 * @param reportParams
	 * @return
	 * @throws ParseException 
	 */
	
	public Map<String, Map<String, Integer>> getLytxExceptionData(ReportParams reportParams) throws ParseException {
		Integer	behaviorCount;
		Map<Long, String>					vehicles;
		Map<String, Map<String, Integer>>	lytxVehicleEventsRecord	= new HashMap<>();
	


	if(EntityType.isDriver(reportParams.getEntityType()))
	{
		
		vehicles				= getLytxUserDetailMap(reportParams);
	}
	else
	{
		vehicles				= getLytxVehicleDetailMap(reportParams);
	}
	
	
//	JSONObject lytxuserJO = new JSONObject(vehicles);
//	
//	log.info("User :: {}",lytxuserJO.toString());
	
		Map<Long, String>					behaviors				= getLytxBehaviorsMap(reportParams);

		
		String startDateStr = reportParams.getStartDate() + AppConstants.START_UTC;
		String endDateStr = reportParams.getEndDate() + AppConstants.END_UTC;
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date newStartDate = DateTimeUtil.parseUtilDate(startDateStr);
		
		Date ssdate = DateTimeUtil.parseUtilDate(startDateStr);
		Date eedate = DateTimeUtil.parseUtilDate(endDateStr);
		int s=0;
		String sdate=startDateStr;
		
		do {

			System.out.println("check---" + s++);
			
			reportParams.setStartDate(sdate);
			log.info("Get Lytx Event- {}","Start");
			
						
			
			GetEventsResponse eventReponse= getLytxExceptionSummaryForDriverScore(reportParams,newStartDate,eedate);
			
			log.info("Get Lytx Event- {}","Stop");
//			JSONObject lytxEventsJO = new JSONObject(eventReponse);
//			
//			log.info("LyData{}",lytxEventsJO.toString());
			
		
		if (log.isDebugEnabled()) {
			log.debug("Exception data count for Event record: {}, vehicles: {} and behaviors: {}",
			        eventReponse.getEvents().length, vehicles.size(), behaviors.size());
		}
		
		

		
		for (EventsInfoV5 event : eventReponse.getEvents()) {
			
			
			String vehicleName;
			if(EntityType.isDriver(reportParams.getEntityType()))
			{
				
				vehicleName			= vehicles.get(event.getDriverId());
			}
			else
			{
				vehicleName			= vehicles.get(event.getVehicleId());

			}
			Map<String, Integer>	lytxExceptionEvents	= lytxVehicleEventsRecord.get(vehicleName);
			if (Objects.isNull(lytxExceptionEvents)) {
				lytxExceptionEvents = new HashMap<String, Integer>();
				lytxVehicleEventsRecord.put(vehicleName, lytxExceptionEvents);
			}

			for (EventBehavior behavior : event.getBehaviors()) {
				String	exceptionName	= behavior.getBehavior().toString();
					behaviorCount	= lytxExceptionEvents.get(exceptionName);
				if (behaviorCount == null) {
					behaviorCount = 0;
				}
				
				lytxExceptionEvents.put(exceptionName, ++behaviorCount);
			}
		}
		
		log.info("Parse Lytx Event - {}","Stop");
		

			//System.out.println(eventReponse.getQueryCutoff().getTime());

			if (!Objects.isNull(eventReponse.getQueryCutoff())) 
			{
				System.out.println(eventReponse.getQueryCutoff().getTime());

				newStartDate = eventReponse.getQueryCutoff().getTime();

			

			} else {
				break;
			}
		
		

	
} while (true);

		if (log.isTraceEnabled()) {
			log.trace("Exception records : {}", lytxVehicleEventsRecord);
		}
		return lytxVehicleEventsRecord;
	}
	
	
	
	
	
	public Map<String, Map<String, Integer>> getLytxTrendingExceptionData(ReportParams reportParams,Map<Integer, String[]> periods) throws ParseException {
		Integer	behaviorCount;
		Map<Long, String>					vehicles;
		Map<String, Map<String, Integer>>	lytxVehicleEventsRecord	= new HashMap<>();
	if(EntityType.isDriver(reportParams.getEntityType()))
	{
		
		vehicles				= getLytxUserDetailMap(reportParams);
	}
	else
	{
		vehicles				= getLytxVehicleDetailMap(reportParams);
	}
		Map<Long, String>					behaviors				= getLytxBehaviorsMap(reportParams);

		
		/*
		 * if (log.isDebugEnabled()) { log.
		 * debug("Exception data count for Event record: {}, vehicles: {} and behaviors: {}"
		 * , eventReponse.getEvents().length, vehicles.size(), behaviors.size()); }
		 */
		
		//
		String startDateStr = reportParams.getStartDate() + AppConstants.START_UTC;
		String endDateStr = reportParams.getEndDate() + AppConstants.END_UTC;
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date newStartDate = DateTimeUtil.parseUtilDate(startDateStr);
		
		Date ssdate = DateTimeUtil.parseUtilDate(startDateStr);
		Date eedate = DateTimeUtil.parseUtilDate(endDateStr);
		int s=0;
		String sdate=startDateStr;
		do {

			System.out.println("check---" + s++);
			
			reportParams.setStartDate(sdate);
				
			GetEventsResponse eventReponse= getLytxExceptionSummaryForDriverScore(reportParams,newStartDate,eedate);

				
				JSONObject lytxEventsJO = new JSONObject(eventReponse);

				for (EventsInfoV5 event : eventReponse.getEvents()) {
					String vehicleName;
					if(EntityType.isDriver(reportParams.getEntityType()))
					{
						
						vehicleName			= vehicles.get(event.getDriverId());
					}
					else
					{
						vehicleName			= vehicles.get(event.getVehicleId());

					}
					
					Map<String, Integer>	lytxExceptionEvents	= lytxVehicleEventsRecord.get(vehicleName);
					if (Objects.isNull(lytxExceptionEvents)) {
						
						String recordDateUTC =event.getRecordDateUTC().toString();
						Date recordDate = null;

						if (recordDateUTC.contains("java")) {
							recordDate = getDateFromMilliSeconds(recordDateUTC);
						} else {
							recordDate = getDate(recordDateUTC);
						}
						if (recordDate.before(ssdate) || recordDate.after(eedate)) {
							continue;
						}
						Integer periodNumber = getPeriodNumberForDate(recordDate,periods);
						
						
						
						lytxExceptionEvents = new HashMap<String, Integer>();
						lytxVehicleEventsRecord.put( periodNumber + "|" +vehicleName, lytxExceptionEvents);
					}

					for (EventBehavior behavior : event.getBehaviors()) {
						String	exceptionName	= behavior.getBehavior().toString();
						behaviorCount	= lytxExceptionEvents.get(exceptionName);
						if (behaviorCount == null) {
							behaviorCount = 0;
						}
						lytxExceptionEvents.put(exceptionName, ++behaviorCount);
					}
				}
				
				if (eventReponse.getQueryCutoff() != null) {
					System.out.println(eventReponse.getQueryCutoff().getTime());

					if (!Objects.isNull(eventReponse.getQueryCutoff().getTime())) {

						newStartDate = eventReponse.getQueryCutoff().getTime();

					

					} else {
						break;
					}
				} else {
					break;
				}

			
		} while (true);

	
		
		//end

		if (log.isTraceEnabled()) {
			log.trace("Exception records : {}", lytxVehicleEventsRecord);
		}

		return lytxVehicleEventsRecord;
	}
	public Integer getPeriodNumberForDate(Date date,Map<Integer, String[]> periods) throws ParseException {
		for (Map.Entry<Integer, String[]> entry : periods.entrySet()) {
			Integer key = entry.getKey();
			String[] value = entry.getValue();
			Date startDate = getDate(value[0]);
			Date endDate = getDate(value[1]);
			if (!(date.before(startDate) || date.after(endDate))) {
				return key;
			}
		}
		return null;
	}
	
	private Date getDateFromMilliSeconds(String ms) {
		Date stdate = new Date(Long.parseLong(ms.substring(33, ms.indexOf(','))));

		return stdate;
	}
	
	private Date getDate(String dateStr) throws ParseException {
		dateStr = dateStr.substring(0, dateStr.indexOf('.'));
		TimeZone timeZone = TimeZone.getTimeZone("UTC");
		Calendar calendar = Calendar.getInstance(timeZone);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//					simpleDateFormat.setTimeZone(timeZone);
		Date date = null;
		
			date = simpleDateFormat.parse(dateStr);
		
		return date;
	}
	/**
	 * Get all exception summry records
	 * 
	 * @param reportParams
	 * @return
	 */
	public GetEventsResponse getLytxExceptionSummary(ReportParams reportParams) {

		log.info("Start - Date :: {}",parseUtilDate(reportParams.getStartDate()));
		log.info("End - Date :: {}",parseUtilDate(reportParams.getEndDate()));
		
		GetEventsByLastUpdateDateRequest getEventsRequest = new GetEventsByLastUpdateDateRequest();
		getEventsRequest.setSessionId(reportParams.getLytexSessionid());
		getEventsRequest.setStartDate(parseUtilDate(reportParams.getStartDate()));
		getEventsRequest.setEndDate(parseUtilDate(reportParams.getEndDate()));
		if (StringUtils.isNotBlank(reportParams.getGroupId())) {
			getEventsRequest.setGroupId(Long.valueOf(reportParams.getGroupId()));
		}

		try {
			return getProxy(reportParams).getEventsByLastUpdateDate(getEventsRequest);
		} catch (RemoteException e) {
			log.error("Error while fetching lytx exception summary", e);
			throw new GeoTabException(AppMsg.ER002);
		}
	}
	
	public GetEventsResponse getLytxExceptionSummaryForDriverScore(ReportParams reportParams,Date startDate,Date endDate) {

		log.info("Start - Date :: {}",parseUtilDate(reportParams.getStartDate()));
		log.info("End - Date :: {}",parseUtilDate(reportParams.getEndDate()));
		
		GetEventsByLastUpdateDateRequest getEventsRequest = new GetEventsByLastUpdateDateRequest();
		getEventsRequest.setSessionId(reportParams.getLytexSessionid());
		getEventsRequest.setStartDate(startDate);
		getEventsRequest.setEndDate(endDate);
		if (StringUtils.isNotBlank(reportParams.getGroupId())) {
			getEventsRequest.setGroupId(Long.valueOf(reportParams.getGroupId()));
		}

		try {
			return getProxy(reportParams).getEventsByLastUpdateDate(getEventsRequest);
		} catch (RemoteException e) {
			log.error("Error while fetching lytx exception summary", e);
			throw new GeoTabException(AppMsg.ER002);
		}
	}

	/**
	 * Get all vehicles map from lytx
	 * 
	 * @param reportParams
	 * @return
	 */
	public Map<Long, String> getLytxVehicleDetailMap(ReportParams reportParams) {

		GetVehiclesResponse response = getLytxVehicleDetail(reportParams);
		if (Objects.nonNull(response)) {
			return Stream.of(response.getVehicles())
			        .collect(Collectors.toMap(VehicleInfo::getVehicleId, VehicleInfo::getName));
		}
		
		return Collections.emptyMap();
	}
	
	public Map<Long, String> getLytxUserDetailMap(ReportParams reportParams) {
		Map<Long, String> userMap = new HashMap<>();
		GetUsersResponse response = getLytxUserDetail(reportParams);
	
			
			
			JSONObject jsonObject2 = new JSONObject(response);

			
			  String vechilelytxlist = jsonObject2.toString(); 
			  JSONObject lytxVechileJO = new JSONObject(vechilelytxlist);
			 
					JSONArray lytxVechileArray = jsonObject2.getJSONArray("users");
					
					
					for(int i=0;i<lytxVechileArray.length();i++)
					{
						JSONObject lytxObjValue=lytxVechileArray.getJSONObject(i);
						
						userMap.put(lytxObjValue.getLong("userId"),lytxObjValue.getString("firstName")+" "+lytxObjValue.getString("lastName"));
					}
		
		
		return userMap;
	}
	
	
	/**
	 * Get all behaviors map from lytx
	 * 
	 * @param reportParams
	 * @return
	 */
	public Map<Long, String> getLytxBehaviorsMap(ReportParams reportParams) {

		GetBehaviorsResponse response = getLytxBehaviors(reportParams);
		if (Objects.nonNull(response)) {
			return Stream.of(response.getBehaviors())
			        .collect(Collectors.toMap(Behavior::getBehaviorId, Behavior::getDescription));
		}
		return Collections.emptyMap();
	}

	/**
	 * Get all vehicles from lytx
	 * 
	 * @param reportParams
	 * @return
	 */
	public GetVehiclesResponse getLytxVehicleDetail(ReportParams reportParams) {

		GetVehiclesRequest getVehiclesRequest = new GetVehiclesRequest();
		getVehiclesRequest.setIncludeSubgroups(Boolean.valueOf(true));
		getVehiclesRequest.setSessionId(reportParams.getLytexSessionid());
		try {
			return getProxy(reportParams).getVehicles(getVehiclesRequest);
		} catch (RemoteException e) {
			log.error("Error while fetching lytx vehicle", e);
			throw new GeoTabException(AppMsg.ER002);
		}
	}
	
	/**
	 * Get all User from lytx
	 * 
	 * @param reportParams
	 * @return
	 */
	public GetUsersResponse getLytxUserDetail(ReportParams reportParams) {

		GetUsersRequest getUserRequest = new GetUsersRequest();
		getUserRequest.setSessionId(reportParams.getLytexSessionid());
		try {
			return getProxy(reportParams).getUsers(getUserRequest);
		} catch (RemoteException e) {
			log.error("Error while fetching lytx user", e);
			throw new GeoTabException(AppMsg.ER002);
		}
	}

	/**
	 * Get all behaviors from lytx
	 * 
	 * @param reportParams
	 * @return
	 */
	public GetBehaviorsResponse getLytxBehaviors(ReportParams reportParams) {

		ExistingSessionRequest request = new ExistingSessionRequest();
		request.setSessionId(reportParams.getLytexSessionid());
		try {
			return getProxy(reportParams).getBehaviors(request);
		} catch (RemoteException e) {
			log.error("Error while fetching lytx behavior", e);
			throw new GeoTabException(AppMsg.ER002);
		}
	}

	/**
	 * Get lytx proxy gateway using endpoint
	 * 
	 * @param reportParams
	 * @return
	 */
	private ISubmissionServiceV5Proxy getProxy(ReportParams reportParams) {
		return new ISubmissionServiceV5Proxy(reportParams.getEndPoint());
	}
}
