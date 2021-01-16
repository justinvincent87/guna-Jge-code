package com.vibaps.merged.safetyreport.services.gl;

import static com.vibaps.merged.safetyreport.util.DateTimeUtil.parseUtilDate;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api.EventBehavior;
import org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api_GetEvents_V5.GetEventsResponse;
import org.springframework.stereotype.Service;

import com.lytx.dto.Behavior;
import com.lytx.dto.EventsInfoV5;
import com.lytx.dto.ExistingSessionRequest;
import com.lytx.dto.GetBehaviorsResponse;
import com.lytx.dto.GetEventsByLastUpdateDateRequest;
import com.lytx.dto.GetVehiclesRequest;
import com.lytx.dto.GetVehiclesResponse;
import com.lytx.dto.VehicleInfo;
import com.lytx.services.ISubmissionServiceV5Proxy;
import com.vibaps.merged.safetyreport.common.AppMsg;
import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
import com.vibaps.merged.safetyreport.exception.GeoTabException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class LytxProxyService {

	/**
	 * Get lytx exception details from lytx API
	 * 
	 * @param reportParams
	 * @return
	 */
	public Map<String, Map<String, Integer>> getLytxExceptionData(ReportParams reportParams) {

		Map<String, Map<String, Integer>>	lytxVehicleEventsRecord	= new HashMap<>();
		GetEventsResponse					eventReponse			= getLytxExceptionSummary(reportParams);
		Map<Long, String>					vehicles				= getLytxVehicleDetailMap(reportParams);
		Map<Long, String>					behaviors				= getLytxBehaviorsMap(reportParams);

		if (log.isDebugEnabled()) {
			log.debug("Exception data count for Event record: {}, vehicles: {} and behaviors: {}",
			        eventReponse.getEvents().length, vehicles.size(), behaviors.size());
		}

		for (EventsInfoV5 event : eventReponse.getEvents()) {
			String					vehicleName			= vehicles.get(event.getVehicleId());
			Map<String, Integer>	lytxExceptionEvents	= lytxVehicleEventsRecord.get(vehicleName);
			if (Objects.isNull(lytxExceptionEvents)) {
				lytxExceptionEvents = new HashMap<String, Integer>();
				lytxVehicleEventsRecord.put(vehicleName, lytxExceptionEvents);
			}

			for (EventBehavior behavior : event.getBehaviors()) {
				String	exceptionName	= behaviors.get(behavior.getBehavior());
				Integer	behaviorCount	= lytxExceptionEvents.get(exceptionName);
				if (behaviorCount == null) {
					behaviorCount = 0;
				}
				lytxExceptionEvents.put(exceptionName, ++behaviorCount);
			}
		}

		if (log.isTraceEnabled()) {
			log.trace("Exception records : {}", lytxVehicleEventsRecord);
		}
		return lytxVehicleEventsRecord;
	}

	/**
	 * Get all exception summry records
	 * 
	 * @param reportParams
	 * @return
	 */
	public GetEventsResponse getLytxExceptionSummary(ReportParams reportParams) {

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
