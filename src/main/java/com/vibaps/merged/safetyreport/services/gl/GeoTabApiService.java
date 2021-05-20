package com.vibaps.merged.safetyreport.services.gl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import com.vibaps.merged.safetyreport.common.AppMsg;
import com.vibaps.merged.safetyreport.common.EntityType;
import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
import com.vibaps.merged.safetyreport.entity.gl.GlRulelistEntity;
import com.vibaps.merged.safetyreport.entity.gl.ReportRow;
import com.vibaps.merged.safetyreport.util.Assert;
import com.vibaps.merged.safetyreport.util.ResponseUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class GeoTabApiService {

	private static final String PROHIBIT_IDLING = "Prohibit Idling";

	private static final String	ATTR_RESULT	= "result";
	private static final String	ATTR_GROUPS	= "groups";
	private static final String	ATTR_NAME	= "name";
	private static final String	ATTR_ITEM	= "item";

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private UserReportFilterService userReportFilterService;

	/**
	 * Get vehicle exception summary data
	 * 
	 * @param reportParams
	 * @return
	 */
	public List<ReportRow> getVehicleExceptionSummary(ReportParams reportParams) {

		String payload = getReportRequest(reportParams);
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

		JsonObject parsedResponse = ResponseUtil.parseResponse(response);
		return convertParsedReponse(parsedResponse, reportParams);
	}

	/**
	 * Convert JSON response as {@code List<ReportRow>}
	 * 
	 * @param parsedReponse
	 * @param reportParams
	 * @return
	 */
	//TODO: Refactor this method again
	private List<ReportRow> convertParsedReponse(JsonObject parsedReponse, ReportParams reportParams) {
		List<ReportRow>	reportRows		= new ArrayList<>();
		List<String>	columnHeaders	= userReportFilterService
		        .loadReporColumntHeaders(reportParams.getGeotabUserName(), reportParams.getGeotabDatabase());
		
		
		if(EntityType.isDriver(reportParams.getEntityType()))
		{
			JSONArray geotabEventsJOArray = new JSONArray(parsedReponse.getAsJsonArray(ATTR_RESULT).get(0).toString());
		
			
			for (int i = 0; i < geotabEventsJOArray.length(); i++) {
				
				//System.out.println(geotabEventsJOArray.length()+"-----length");
				ReportRow reportRow = new ReportRow();
				

			    JSONObject resultsChild = geotabEventsJOArray.getJSONObject(i);
			    JSONObject itemJO = resultsChild.getJSONObject("item");
			    
			    
			    //driverName
			    String geotabDriverName = itemJO.getString("firstName") + " " + itemJO.getString("lastName");
			   // System.out.println(geotabDriverName+"name");
			    reportRow.setName(geotabDriverName);
				//group
				JSONArray geotabDriverGroups = itemJO.getJSONArray("driverGroups");
				
				
				
				String group = null;
				for(int j = 0; j < geotabDriverGroups.length(); j++) {
					String groupName = "";
					if(!geotabDriverGroups.getJSONObject(j).has("name"))
					{
						groupName="All Vehicles";
					}
					else
					{
						groupName=geotabDriverGroups.getJSONObject(j).getString("name");

					}
					
					if(group == null) {
						group = groupName;
					} else {
						group = group + ", " + groupName;
					}
				}
				reportRow.setGroup(group);
				//Distance
				Object geotabVehicleTotalDistance = resultsChild.get("totalDistance");
				long tDistance = ((Double)geotabVehicleTotalDistance).longValue();
				reportRow.setDistance(tDistance);
				//Geotab exceptions from exceptionSummaries
				Map<String, Integer> geotabExceptionEvents = new HashMap<String, Integer>();
				JSONArray geotabExceptionSummariesJA = resultsChild.getJSONArray("exceptionSummaries");
				for(int k = 0; k < geotabExceptionSummariesJA.length(); k++) {
						//System.out.println(geotabExceptionSummariesJA.isNull(k)+"----=ds");
				
					if(!geotabExceptionSummariesJA.isNull(k))
					{
						//System.out.println(geotabExceptionSummariesJA.length()+"-----length");

					int eventCount = geotabExceptionSummariesJA.getJSONObject(k).getInt("eventCount");
					
					JSONObject geotabExceptionRuleJO = geotabExceptionSummariesJA.getJSONObject(k).getJSONObject("exceptionRule");
					
					String geotabExceptionName = geotabExceptionRuleJO.getString("id");
					
					//System.out.println(geotabExceptionName+"-"+eventCount);
					geotabExceptionEvents.put(geotabExceptionName, geotabExceptionEvents.get(geotabExceptionName)==null?eventCount:geotabExceptionEvents.get(geotabExceptionName)+eventCount);
					}
				}
				for (int m = 3; m < columnHeaders.size(); m++) {
					if (geotabExceptionEvents.get(columnHeaders.get(m)) != null) {
						reportRow.getSelectedRules().put(columnHeaders.get(m),
						        (geotabExceptionEvents.get(columnHeaders.get(m))));
					} else {
						if (reportRow.getSelectedRules().get(columnHeaders.get(m)) == null) {
							reportRow.getSelectedRules().put(columnHeaders.get(m), 0);
						}
					}
				}
			
				reportRows.add(reportRow);
			}
		}
		else
		{

		JSONArray geotabEventsJOArray = new JSONArray(parsedReponse.getAsJsonArray(ATTR_RESULT).get(0).toString());
		for (int i = 0; i < geotabEventsJOArray.length(); i++) {

			ReportRow	reportRow		= new ReportRow();
			JSONObject	resultsChild	= geotabEventsJOArray.getJSONObject(i);
			JSONObject	itemJO			= resultsChild.getJSONObject(ATTR_ITEM);
			// vehicleName
			String geotabVehicleName = itemJO.getString(ATTR_NAME);
			reportRow.setName(geotabVehicleName);
			// group
			JSONArray	geotabVehicleGroups	= itemJO.getJSONArray(ATTR_GROUPS);
			String		group				= null;
			for (int j = 0; j < geotabVehicleGroups.length(); j++) {
				if (group == null) {
					
					if(!geotabVehicleGroups.getJSONObject(j).isNull(ATTR_NAME))
					{
					group = geotabVehicleGroups.getJSONObject(j).getString(ATTR_NAME);
					}
				} else {
					String newGroup = geotabVehicleGroups.getJSONObject(j).getString(ATTR_NAME);
					if (PROHIBIT_IDLING.equalsIgnoreCase(newGroup)) {
						group = newGroup + ", " + group;
					} else {
						group = group + ", " + newGroup;
					}
				}
			}
			reportRow.setGroup(group);
			// Distance
			Object	geotabVehicleTotalDistance	= resultsChild.get("totalDistance");
			long	tDistance					= ((Double) geotabVehicleTotalDistance).longValue();
			reportRow.setDistance(tDistance);
			// Geotab exceptions from exceptionSummaries
			Map<String, Integer> geotabExceptionEvents = new HashMap<String, Integer>();

			JSONArray geotabExceptionSummariesJA = resultsChild.getJSONArray("exceptionSummaries");

			for (int k = 0; k < geotabExceptionSummariesJA.length(); k++) {
				if (!geotabExceptionSummariesJA.isNull(k)) {
					int eventCount = geotabExceptionSummariesJA.getJSONObject(k).getInt("eventCount");

					JSONObject geotabExceptionRuleJO = geotabExceptionSummariesJA.getJSONObject(k)
					        .getJSONObject("exceptionRule");

					String geotabExceptionName = geotabExceptionRuleJO.getString("id");

					geotabExceptionEvents.put(geotabExceptionName,
					        geotabExceptionEvents.get(geotabExceptionName) == null ? eventCount
					                : geotabExceptionEvents.get(geotabExceptionName) + eventCount);
				}

			}

			for (int m = 3; m < columnHeaders.size(); m++) {
				if (geotabExceptionEvents.get(columnHeaders.get(m)) != null) {
					reportRow.getSelectedRules().put(columnHeaders.get(m),
					        (geotabExceptionEvents.get(columnHeaders.get(m))));
				} else {
					if (reportRow.getSelectedRules().get(columnHeaders.get(m)) == null) {
						reportRow.getSelectedRules().put(columnHeaders.get(m), 0);
					}
				}
			}
			reportRows.add(reportRow);
		}
		}
		
		return reportRows;
	}

	/**
	 * Build payload for get report data
	 * 
	 * @param reportParams
	 * @return
	 */
	private String getReportRequest(ReportParams reportParams) {

		List<GlRulelistEntity> ruleList = userReportFilterService.getGeoDropDown(reportParams.getGeotabUserName(),
		        reportParams.getGeotabDatabase());

		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_EXECUTE_MULTI_CALL);

		// bind credentials
		buildCredentials(builder, reportParams);
		if (EntityType.isDriver(reportParams.getEntityType()))
		{
			
			return builder.params().addCalls().method(AppConstants.METHOD_GET_REPORT_DATA).params().argument()
			        .runGroupLevel(-1).isNoDrivingActivityHidden(true).fromUtc(reportParams.getStartDate())
			        .toUtc(reportParams.getEndDate()).entityType(reportParams.getEntityType())
			        .reportArgumentType(AppConstants.PARAM_RISK_MANAGEMENT).groups(Collections.<String>emptyList())
			        .reportSubGroup(AppConstants.PARAM_NONE)
			        .rules(ruleList.stream().map(GlRulelistEntity::getRulevalue).collect(Collectors.toList())).and().and()
			        .done().addCalls().method(AppConstants.METHOD_GET).params().typeName(AppConstants.PARAM_SYSTEM_SETTINGS)
			        .build();
		}
		return builder.params().addCalls().method(AppConstants.METHOD_GET_REPORT_DATA).params().argument()
		        .runGroupLevel(-1).isNoDrivingActivityHidden(true).fromUtc(reportParams.getStartDate())
		        .toUtc(reportParams.getEndDate()).entityType(reportParams.getEntityType())
		        .reportArgumentType(AppConstants.PARAM_RISK_MANAGEMENT).groups(getGroupList(reportParams))
		        .reportSubGroup(AppConstants.PARAM_NONE)
		        .rules(ruleList.stream().map(GlRulelistEntity::getRulevalue).collect(Collectors.toList())).and().and()
		        .done().addCalls().method(AppConstants.METHOD_GET).params().typeName(AppConstants.PARAM_SYSTEM_SETTINGS)
		        .build();
	}

	/**
	 * Construct geotab groups for payload request
	 * 
	 * @param reportParams
	 * @return
	 */
	public List<String> getGroupList(ReportParams reportParams) {

		Assert.isNull(reportParams.getGeotabGroups(), AppMsg.CV001);
		return Stream.of(reportParams.getGeotabGroups().split(",")).collect(Collectors.toList());
	}

	/**
	 * Bind credential details with request payload
	 * 
	 * @param builder
	 * @param reportParams
	 */
	public void buildCredentials(GeoTabRequestBuilder builder, ReportParams reportParams) {

		builder.params().credentials().database(reportParams.getGeotabDatabase())
		        .sessionId(reportParams.getGeotabSessionId()).userName(reportParams.getGeotabUserName());
	}
	
	public void buildvibapsCredentials(GeoTabRequestBuilder builder, ReportParams reportParams) {

		builder.credentials().database(reportParams.getGeotabDatabase())
		        .sessionId(reportParams.getGeotabSessionId()).userName(reportParams.getGeotabUserName());
	}
}
