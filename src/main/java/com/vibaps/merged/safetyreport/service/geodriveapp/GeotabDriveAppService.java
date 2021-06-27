package com.vibaps.merged.safetyreport.service.geodriveapp;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api.EventBehavior;
import org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api_GetEvents_V5.GetEventsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lytx.dto.EventsInfoV5;
import com.lytx.dto.GetUsersRequest;
import com.lytx.dto.GetUsersResponse;
import com.lytx.dto.LoginResponse;
import com.lytx.dto.UserInfo;
import com.lytx.services.ISubmissionServiceV5Proxy;
import com.vibaps.merged.safetyreport.common.AppConstants;
import com.vibaps.merged.safetyreport.common.EntityType;
import com.vibaps.merged.safetyreport.dto.geodriveapp.GeoDriveAppResponse;
import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerResponse;
import com.vibaps.merged.safetyreport.entity.gl.LyUserEntity;
import com.vibaps.merged.safetyreport.repo.gl.CommonGeotabRepository;
import com.vibaps.merged.safetyreport.repo.gl.LyUserRepository;
import com.vibaps.merged.safetyreport.services.gl.CommonGeotabService;
import com.vibaps.merged.safetyreport.services.gl.LytxProxyService;
import com.vibaps.merged.safetyreport.util.DateTimeUtil;

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
	
	
	
	

	public GeoDriveAppResponse showScore(TrailerParams reportParams) throws RemoteException, ParseException {
		// TODO Auto-generated method stub
		
		LyUserEntity enty=commonGeotabRepository.getLytxCredentials(reportParams.getLytxuserName());
		String sessionId=LytexLogin(enty.getLytxUsername(),enty.getLytxPassword(),enty.getLyEndpoint());
		Map<Long, String> vehicles				=getUserMap(enty.getLyEndpoint(), sessionId);

		Map<Long,Long> lytxData=getLytxExceptionData(reportParams, enty.getLyEndpoint(), sessionId);
		

	
		
		       
	
		List<GeoDriveAppResponse> response=new ArrayList<GeoDriveAppResponse>();
		List<GeoDriveAppResponse> finalresponse=new ArrayList<GeoDriveAppResponse>();

		
		List<Long> listLytxdata = new ArrayList<Long>(lytxData.keySet()); 
		
		

		for(Long data:listLytxdata)
		{
			System.out.println(data +"----"+lytxData.get(data));
			GeoDriveAppResponse res=new GeoDriveAppResponse();
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
