package com.vibaps.merged.safetyreport.dao.trending;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api_GetEvents_V5.GetEventsResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lytx.dto.ExistingSessionRequest;
import com.lytx.dto.GetBehaviorsResponse;
import com.lytx.dto.GetEventsByLastUpdateDateRequest;
import com.lytx.dto.GetUsersRequest;
import com.lytx.dto.GetUsersResponse;
import com.lytx.dto.GetVehiclesRequest;
import com.lytx.dto.GetVehiclesResponse;
import com.lytx.services.ISubmissionServiceV5Proxy;
import com.vibaps.merged.safetyreport.builder.GeoTabRequestBuilder;
import com.vibaps.merged.safetyreport.builder.Uri;
import com.vibaps.merged.safetyreport.common.AppConstants;
import com.vibaps.merged.safetyreport.common.EntityType;
import com.vibaps.merged.safetyreport.dao.gl.CommonGeotabDAO;
import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
import com.vibaps.merged.safetyreport.entity.gl.GlRulelistEntity;
import com.vibaps.merged.safetyreport.entity.gl.ReportRow;
import com.vibaps.merged.safetyreport.entity.gl.Trip;
import com.vibaps.merged.safetyreport.service.gl.GlReportService;
import com.vibaps.merged.safetyreport.services.gl.GeoTabApiService;
import com.vibaps.merged.safetyreport.services.gl.LytxProxyService;
import com.vibaps.merged.safetyreport.services.gl.UserReportFilterService;
import com.vibaps.merged.safetyreport.util.DateTimeUtil;
import com.vibaps.merged.safetyreport.util.ResponseUtil;

import lombok.extern.log4j.Log4j2;
@Repository
@Log4j2
public class RestTrendingReportDAO {
	
	@Autowired
	private GlReportService glReportService;
	@Autowired
	private CommonGeotabDAO commonGeotabDAO;
	@Autowired
	private UserReportFilterService userReportFilterService;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private GeoTabApiService geoTabApiService;
	@Autowired
	private LytxProxyService lytxProxyService;
	
	private Map<Long, String> lytxVehicleList;
	private Map<String, List<Trip>> vehicleTrips;
	private int ROW_OFFSET = -1;
	private int FORMULA_START_ROW = 7;
	private int EXCEPTIONS_START_COLUMN = 3;
	private String geotabVehicleExceptionSummariesJson;
	private String lytxExceptionSummariesJson;
	private List<String> displayColumns;
	private Map<Integer, String[]> periods = new LinkedHashMap<Integer, String[]>();
    private Map<Integer, String> lytxBehaviors;
    private String vechilelytxlist; 
    private Map<Long,String> vechilemap;

	
	public Object getReportGeo(ReportParams reportParams) throws ParseException, MalformedURLException, IOException
	{
		String responseJson = "";
		Map<String, Map<String, Integer>> lytxVehicleEventsRecord = new HashMap<String, Map<String, Integer>>();
		String getVehicleResponseJson = "";
		Map<String, Map<String, String>> combinedReport = new HashMap<>();
		
		String sdate="";
			
		JsonObject geotabDriverExceptionSummariesJson=geotabTrendingResponce(reportParams);
			
		String startDateStr = reportParams.getStartDate() + AppConstants.START_UTC;
		String endDateStr = reportParams.getEndDate() + AppConstants.END_UTC;

			
       displayColumns = loadTrendingReporColumntHeaders(reportParams.getGeotabUserName(),reportParams.getGeotabDatabase());

					if (reportParams.getEntityType().equals("Driver")) {
						combinedReport = extractGeotabDriverTrendingData(geotabDriverExceptionSummariesJson, reportParams.getGeotabUserName(),
								reportParams.getGeotabDatabase());
					} else {
						combinedReport = extractGeotabVehicleTrendingData(geotabDriverExceptionSummariesJson, reportParams.getGeotabUserName(),
								reportParams.getGeotabDatabase());

					}

					EXCEPTIONS_START_COLUMN = 6;
							
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

					Date newStartDate = sdf.parse(startDateStr);
					
					Date ssdate = sdf.parse(startDateStr);
					Date eedate = sdf.parse(endDateStr);
					
					
                 //   lytxVehicleEventsRecord =lytxProxyService.getLytxExceptionData(reportParams); 
					int s = 0;
				
					GetBehaviorsResponse response = lytxProxyService.getLytxBehaviors(reportParams);
		
					

					JSONObject jsonObject = new JSONObject(response);
					String lytxBehaviorsJson = toStringValue(jsonObject);
					
					
					lytxVehicleEventsRecord = lytxProxyService.getLytxTrendingExceptionData(reportParams);
					 
				
					updatedCombinedReportWithLytxExceptions(combinedReport, lytxVehicleEventsRecord);

				responseJson=createTrendingResponce(combinedReport,displayColumns);
				

			glReportService.updateresponce(reportParams.getGeotabUserName(), responseJson,  reportParams.getGeotabDatabase());
		
			
		return responseJson; 
	}
	
	public String startDateForQuerryCutOff(String cutoffData)
	{
			String sdate="";
		
			Date newStartDate = getDateFromMilliSeconds(cutoffData);

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
				return "-";
			}
			
			sdate = strNewDate;

		
	return sdate;
	}
	
	
	public String createTrendingResponce(Map<String, Map<String, String>> combinedReport,List<String> displayColumns)
	{
		String responseJson = "";
		List<Integer> totals = new ArrayList<>();
		StringBuffer combinedReportResponseJson = new StringBuffer();

		for (int q = 0; q < displayColumns.size(); q++) {
			totals.add(0);
		}
		combinedReportResponseJson = new StringBuffer();
		combinedReportResponseJson.append("\"information\": [");
		boolean firstRow = true;
		int rulesRecords = displayColumns.size() - 3;
		for (Map.Entry<String, Map<String, String>> combinedReportRows : combinedReport.entrySet()) {
			if (!firstRow) {
				combinedReportResponseJson.append(",");
			} else {
				firstRow = false;
			}
			combinedReportResponseJson.append("{");
			boolean rulesHeadedAdded = false;
			int headerCount = 0;
			int rowCount = 0;
			Map<String, String> rowData = combinedReportRows.getValue();
			for (Map.Entry<String, String> data : rowData.entrySet()) {
				if (headerCount++ > 0 && headerCount < displayColumns.size() + 1) {
					combinedReportResponseJson.append(",");
				}
				if (rowCount++ < EXCEPTIONS_START_COLUMN) {
					rulesHeadedAdded = false;
					combinedReportResponseJson.append("\"" + data.getKey() + "\": \"" + data.getValue() + "\"");
				} else {
					if (!rulesHeadedAdded) {
						combinedReportResponseJson.append("\"Behave\": [");
						rulesHeadedAdded = true;
					}
					combinedReportResponseJson.append("{");
					combinedReportResponseJson.append("\"Rule\": \"" + data.getValue() + "\"}");
					totals.set(rowCount - 1, (totals.get(rowCount - 1) + Integer.parseInt(data.getValue())));
					if (rowCount == displayColumns.size()) {
						combinedReportResponseJson.append("]");
					}
				}

			}
			combinedReportResponseJson.append("}");
		}
		combinedReportResponseJson.append("]}");

		StringBuffer totalsJson = new StringBuffer();
		totalsJson.append("{\"totals\": [");
		int ruleCounter = 0;
		for (int totalVal : totals) {
			totalsJson.append("{ \"Rule\": \"" + totalVal + "\" }");
			ruleCounter++;
			if (ruleCounter != displayColumns.size()) {
				totalsJson.append(",");
			}
		}
		totalsJson.append("],");

		responseJson = totalsJson.toString() + combinedReportResponseJson.toString();
		
		return responseJson;
	}
	
	
	public JsonObject geotabTrendingResponce(ReportParams reportParams) throws MalformedURLException, IOException
	{
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
			return parsedResponse;
		
	}
	private String getReportRequest(ReportParams reportParams) {

		List<GlRulelistEntity> ruleList = userReportFilterService.getGeoDropDown(reportParams.getGeotabUserName(),
		        reportParams.getGeotabDatabase());

		GeoTabRequestBuilder builder = GeoTabRequestBuilder.getInstance();
		builder.method(AppConstants.METHOD_EXECUTE_MULTI_CALL);

		// bind credentials
		geoTabApiService.buildCredentials(builder, reportParams);

		return builder.params().addCalls().method(AppConstants.METHOD_GET_REPORT_DATA).params().argument()
		        .runGroupLevel(-1).isNoDrivingActivityHidden(true).fromUtc(reportParams.getStartDate())
		        .toUtc(reportParams.getEndDate()).entityType(reportParams.getEntityType())
		        .reportArgumentType(AppConstants.PARAM_RISK_MANAGEMENT).groups(geoTabApiService.getGroupList(reportParams))
		        .reportSubGroup(reportParams.getPeriod())
		        .rules(ruleList.stream().map(GlRulelistEntity::getRulevalue).collect(Collectors.toList())).and().and()
		        .done().addCalls().method(AppConstants.METHOD_GET).params().typeName(AppConstants.PARAM_SYSTEM_SETTINGS)
		        .build();
	}
	

	public String toStringValue(Object object) {
		String value = "";
		if (object != null)
			if (object instanceof String) {
				value = (String) object;
			} else {
				
					value = "" + object.toString().trim();
				
			}
		return value;
	}
	
	public Map<Long, String> loadLytxVehicleIDNameMap(String getVehicleResponseJson) {
		Map<Long, String> lytxVehicleList = new HashMap<Long, String>();
		JSONObject lytxVehiclesJO = new JSONObject(getVehicleResponseJson);
		JSONArray lytxVehiclesArray = lytxVehiclesJO.getJSONArray("vehicles");
		for (int i = 0; i < lytxVehiclesArray.length(); i++) {
			String vehicleName = lytxVehiclesArray.getJSONObject(i).getString("name");
			Long vehicleId = lytxVehiclesArray.getJSONObject(i).getLong("vehicleId");
			lytxVehicleList.put(vehicleId, vehicleName);
		}
		return lytxVehicleList;
	}
	
	public Map<Integer, String> loadLytxBehaviors(String lytxBehaviorsJson) {
		Map<Integer, String> lBehaviors = new HashMap<Integer, String>();
		JSONObject lytxBehaviorsJO = new JSONObject(lytxBehaviorsJson);
		JSONArray lytxBehaviorsArray = lytxBehaviorsJO.getJSONArray("behaviors");
		for (int i = 0; i < lytxBehaviorsArray.length(); i++) {
			JSONObject behaviorJO = lytxBehaviorsArray.getJSONObject(i);
			int lytxBehaviorId = behaviorJO.getInt("behaviorId");
			String lytxDescription = "L-" + behaviorJO.getString("description");
			lBehaviors.put(lytxBehaviorId, lytxDescription);
		}
		return lBehaviors;
	}
	

	public List<String> loadTrendingReporColumntHeaders(String userName, String db) {
		List<String> reportColumnHeader = new ArrayList<String>();
		reportColumnHeader.add("VehicleName");
		reportColumnHeader.add("Group");
		reportColumnHeader.add("Distance");
		reportColumnHeader.add("PeriodNumber");
		reportColumnHeader.add("StartDate");
		reportColumnHeader.add("EndDate");
		// TODO: Remaining columns to be populated from database selected columns. using
		
		List<String> gval = new ArrayList();
		gval = glReportService.getallbehave(userName, db);
		for (int j = 0; j < gval.size(); j++) {
			// System.out.println(j + "-----" + gval.get(j));
			reportColumnHeader.add(gval.get(j));
		}
		return reportColumnHeader;
	}
	
	private Map<String, Map<String, String>> extractGeotabDriverTrendingData(
			JsonObject geotabDriverTrendingReportResponseJson, String userName, String db) {
		displayColumns = loadTrendingReporColumntHeaders(userName, db);

		
		// create report object:
		Map<String, Map<String, String>> combinedReport = new LinkedHashMap<String, Map<String, String>>();
		// GEOTAB Events processing
		try
		{
		JsonArray geotabEventsJOArray = geotabDriverTrendingReportResponseJson.getAsJsonArray("result");
		JsonArray resultChildArray = geotabEventsJOArray.get(0).getAsJsonArray();
		for (int i = 0; i < resultChildArray.size(); i++) {
			JsonObject resultsChild = resultChildArray.get(i).getAsJsonObject();
			int periodNumber = resultsChild.get("periodNumber").getAsInt();

			String periodStartDate = resultsChild.get("periodStartDate").getAsString();
			String periodEndDate = resultsChild.get("periodEndDate").getAsString();
			if (periods.get(periodNumber) == null) {
				String[] periodRange = new String[2];
				periodRange[0] = periodStartDate;
				periodRange[1] = periodEndDate;
				periods.put(periodNumber, periodRange);
			}

			System.out.print("exceptionSummary - Period Number: " + periodNumber + ", Period Start: " + periodStartDate
					+ ", Period End: " + periodEndDate + " - ");
			JsonObject itemJO = resultsChild.getAsJsonObject("item");
			// driverName
			String geotabVehicleName = itemJO.get("firstName").getAsString() + " " + itemJO.get("lastName").getAsString();
			Map<String, String> newReportRow = new LinkedHashMap<String, String>();// getNewReportRow();
			newReportRow.put(displayColumns.get(0), geotabVehicleName);
			// group
			JsonArray geotabDriverGroups = itemJO.getAsJsonArray("driverGroups");
			String group = null;
			for (int j = 0; j < geotabDriverGroups.size(); j++) {
				String groupName = "";
				if (!geotabDriverGroups.get(j).getAsJsonObject().has("name")) {
					groupName = "All Vehicles";
				} else {
					groupName = geotabDriverGroups.get(j).getAsJsonObject().get("name").getAsString();

				}
				if (group == null) {
					group = groupName;
				} else {
					group = group + ", " + groupName;
				}
			}
			newReportRow.put(displayColumns.get(1), group);
			// Distance
			long tDistance = resultsChild.get("totalDistance").getAsLong();


			newReportRow.put(displayColumns.get(2), Long.toString(tDistance));
			newReportRow.put(displayColumns.get(3), Integer.toString(periodNumber));
			newReportRow.put(displayColumns.get(4), periodStartDate);
			newReportRow.put(displayColumns.get(5), periodEndDate);
			// Geotab exceptions from exceptionSummaries
			Map<String, Integer> geotabExceptionEvents = new HashMap<String, Integer>();
			JsonArray geotabExceptionSummariesJA = resultsChild.getAsJsonArray("exceptionSummaries");
			for (int k = 0; k < geotabExceptionSummariesJA.size(); k++) {
				if (!geotabExceptionSummariesJA.get(k).isJsonObject()) {
					int eventCount = geotabExceptionSummariesJA.get(k).getAsJsonObject().getAsJsonObject("eventCount").getAsInt();
					JsonObject geotabExceptionRuleJO = geotabExceptionSummariesJA.get(k).getAsJsonObject().getAsJsonObject("exceptionRule");
					String geotabExceptionName = "G-" + geotabExceptionRuleJO.get("name").getAsString();
					geotabExceptionEvents.put(geotabExceptionName, eventCount);
				}
			}
			for (int m = EXCEPTIONS_START_COLUMN; m < displayColumns.size(); m++) {
				if (geotabExceptionEvents.get(displayColumns.get(m)) != null) {
					newReportRow.put(displayColumns.get(m),
							(geotabExceptionEvents.get(displayColumns.get(m))).toString());
				} else {
					if (newReportRow.get(displayColumns.get(m)) == null) {
						newReportRow.put(displayColumns.get(m), "0");
					}
				}
			}
			String key = periodNumber + "|" + geotabVehicleName;
			combinedReport.put(geotabVehicleName, newReportRow);
		}
		}catch (Exception e) {
			// TODO: handle exception
			
			System.out.println(e);
		}
		return combinedReport;
	}
	
	private Map<String, Map<String, String>> extractGeotabVehicleTrendingData(
			JsonObject geotabTrendingReportResponseJson, String userName, String db) {

		// System.out.println(geotabTrendingReportResponseJson);

		// create report object:
		displayColumns = loadTrendingReporColumntHeaders(userName, db);

		Map<String, Map<String, String>> combinedReport = new LinkedHashMap<String, Map<String, String>>();
		// GEOTAB Events processing
		//JSONObject geotabEventsJO = new JSONObject(geotabTrendingReportResponseJson);
		JsonArray geotabEventsJOArray = geotabTrendingReportResponseJson.getAsJsonArray("result");
		JsonArray resultChildArray = geotabEventsJOArray.get(0).getAsJsonArray();
		for (int j = 0; j < resultChildArray.size(); j++) {
			
			try
			{
			JsonObject exceptionSummary = resultChildArray.get(j).getAsJsonObject();
			int periodNumber = exceptionSummary.get("periodNumber").getAsInt();
			String periodStartDate = exceptionSummary.get("periodStartDate").getAsString();
			String periodEndDate = exceptionSummary.get("periodEndDate").getAsString();
			if (periods.get(periodNumber) == null) {
				String[] periodRange = new String[2];
				periodRange[0] = periodStartDate;
				periodRange[1] = periodEndDate;
				periods.put(periodNumber, periodRange);
			}
			JsonObject itemJO = exceptionSummary.get("item").getAsJsonObject();
			// vehicleName
			String geotabVehicleName = itemJO.get("name").getAsString();
			Map<String, String> newReportRow = new LinkedHashMap<String, String>();// getNewReportRow();

			newReportRow.put(displayColumns.get(0), geotabVehicleName);
			// group
			JsonArray geotabVehicleGroups = itemJO.getAsJsonArray("groups");
			String group = null;
			for (int k = 0; k < geotabVehicleGroups.size(); k++) {
				if (group == null) {
					group = geotabVehicleGroups.get(k).getAsJsonObject().get("name").getAsString();
				} else {
					String newGroup = geotabVehicleGroups.get(k).getAsJsonObject().get("name").getAsString();
					if ("Prohibit Idling".equalsIgnoreCase(newGroup)) {
						group = newGroup + ", " + group;
					} else {
						group = group + ", " + newGroup;
					}
				}
			}
			newReportRow.put(displayColumns.get(1), group);
			// Distance
			long tDistance = exceptionSummary.get("totalDistance").getAsLong();

		

			newReportRow.put(displayColumns.get(2), Long.toString(tDistance));
			newReportRow.put(displayColumns.get(3), Integer.toString(periodNumber));
			newReportRow.put(displayColumns.get(4), periodStartDate);
			newReportRow.put(displayColumns.get(5), periodEndDate);

			// Geotab exceptions from exceptionSummaries
			Map<String, Integer> geotabExceptionEvents = new HashMap<String, Integer>();
			JsonArray geotabExceptionSummariesJA = exceptionSummary.getAsJsonArray("exceptionSummaries");
			for (int m = 0; m < geotabExceptionSummariesJA.size(); m++) {
				if (!geotabExceptionSummariesJA.get(m).isJsonNull()) {
					int eventCount = geotabExceptionSummariesJA.get(m).getAsJsonObject().get("eventCount").getAsInt();
					JsonObject geotabExceptionRuleJO = geotabExceptionSummariesJA.get(m).getAsJsonObject().get("exceptionRule").getAsJsonObject();
					String geotabExceptionName = "G-" + geotabExceptionRuleJO.get("name").getAsString();
					geotabExceptionEvents.put(geotabExceptionName,
							geotabExceptionEvents.get(geotabExceptionName) == null ? eventCount
									: geotabExceptionEvents.get(geotabExceptionName) + eventCount);
				}
			}
			for (int n = 3; n < displayColumns.size(); n++) {
				if (geotabExceptionEvents.get(displayColumns.get(n)) != null) {
					newReportRow.put(displayColumns.get(n),
							(geotabExceptionEvents.get(displayColumns.get(n))).toString());
				} else {
					if (newReportRow.get(displayColumns.get(n)) == null) {
						newReportRow.put(displayColumns.get(n), "0");
					}
				}
			}
			String key = periodNumber + "|" + geotabVehicleName;
			combinedReport.put(key, newReportRow);
		}catch (Exception e) {
		System.out.println(e);
		}
			}
		return combinedReport;
	}
	
	private Map<String, Map<String, String>> extractGeotabDriverData(JsonObject geotabDriverExceptionSummariesJson) {

		// create report object:
		Map<String, Map<String, String>> combinedReport = new LinkedHashMap<String, Map<String, String>>();
		// GEOTAB Events processing
		//JsonObject geotabEventsJO = new JSONObject(geotabDriverExceptionSummariesJson);
		JsonArray geotabEventsJOArray = geotabDriverExceptionSummariesJson.getAsJsonArray("result");
		for (int i = 0; i < geotabEventsJOArray.size(); i++) {
			JsonObject resultsChild = geotabEventsJOArray.get(i).getAsJsonObject();
			JsonObject itemJO = resultsChild.getAsJsonObject("item");
			// driverName
			String geotabVehicleName = itemJO.getAsJsonObject("firstName").getAsString() + " " + itemJO.getAsJsonObject("lastName").getAsString();
			Map<String, String> newReportRow = new LinkedHashMap<String, String>();// getNewReportRow();
			newReportRow.put(displayColumns.get(0), geotabVehicleName);
			// group
			JsonArray geotabDriverGroups = itemJO.getAsJsonArray("driverGroups");
			String group = null;
			for (int j = 0; j < geotabDriverGroups.size(); j++) {
				if (group == null) {
					group = geotabDriverGroups.get(j).getAsJsonObject().getAsJsonObject("name").getAsString();
				} else {
					group = group + ", " + geotabDriverGroups.get(j).getAsJsonObject().getAsJsonObject("name").getAsString();
					;
				}
			}
			newReportRow.put(displayColumns.get(1), group);
			// Distance
			Object geotabVehicleTotalDistance = resultsChild.get("totalDistance");

			long tDistance = ((Double) geotabVehicleTotalDistance).longValue();

			newReportRow.put(displayColumns.get(2), Long.toString(tDistance));

			// Geotab exceptions from exceptionSummaries
			Map<String, Integer> geotabExceptionEvents = new HashMap<String, Integer>();
			JsonArray geotabExceptionSummariesJA = resultsChild.getAsJsonArray("exceptionSummaries");
			for (int k = 0; k < geotabExceptionSummariesJA.size(); k++) {
				int eventCount = geotabExceptionSummariesJA.get(k).getAsJsonObject().getAsJsonObject("eventCount").getAsInt();
				JsonObject geotabExceptionRuleJO = geotabExceptionSummariesJA.get(k).getAsJsonObject().getAsJsonObject("exceptionRule");
				String geotabExceptionName = "G-" + geotabExceptionRuleJO.getAsJsonObject("name").getAsString();
				geotabExceptionEvents.put(geotabExceptionName, eventCount);
			}
			for (int m = 3; m < displayColumns.size(); m++) {
				if (geotabExceptionEvents.get(displayColumns.get(m)) != null) {
					newReportRow.put(displayColumns.get(m),
							(geotabExceptionEvents.get(displayColumns.get(m))).toString());
				} else {
					if (newReportRow.get(displayColumns.get(m)) == null) {
						newReportRow.put(displayColumns.get(m), "0");
					}
				}
			}
			combinedReport.put(geotabVehicleName, newReportRow);
		}
		return combinedReport;
	}
	
	private Map<String, Map<String, String>> extractGeotabVehicleData(
			String geotabVehicleExceptionSummariesJson) {

		System.out.println("test" + geotabVehicleExceptionSummariesJson);
		// create report object:
		Map<String, Map<String, String>> combinedReport = new LinkedHashMap<String, Map<String, String>>();
		// GEOTAB Events processing
		JSONObject geotabEventsJO = new JSONObject(geotabVehicleExceptionSummariesJson);
		JSONArray geotabEventsJOArray = geotabEventsJO.getJSONArray("results");
		for (int i = 0; i < geotabEventsJOArray.length(); i++) {
			JSONObject resultsChild = geotabEventsJOArray.getJSONObject(i);
			JSONObject itemJO = resultsChild.getJSONObject("item");
			// vehicleName
			String geotabVehicleName = itemJO.getString("name");
			Map<String, String> newReportRow = new LinkedHashMap<String, String>();// getNewReportRow();
			newReportRow.put(displayColumns.get(0), geotabVehicleName);
			// group
			JSONArray geotabVehicleGroups = itemJO.getJSONArray("groups");
			String group = null;
			for (int j = 0; j < geotabVehicleGroups.length(); j++) {
				if (group == null) {
					group = geotabVehicleGroups.getJSONObject(j).getString("name");
				} else {
					String newGroup = geotabVehicleGroups.getJSONObject(j).getString("name");
					if ("Prohibit Idling".equalsIgnoreCase(newGroup)) {
						group = newGroup + ", " + group;
					} else {
						group = group + ", " + newGroup;
					}
				}
			}
			newReportRow.put(displayColumns.get(1), group);
			// Distance
			Object geotabVehicleTotalDistance = resultsChild.get("totalDistance");

			long tDistance = ((Double) geotabVehicleTotalDistance).longValue();

			newReportRow.put(displayColumns.get(2), Long.toString(tDistance));

			// Geotab exceptions from exceptionSummaries
			Map<String, Integer> geotabExceptionEvents = new HashMap<String, Integer>();
			JSONArray geotabExceptionSummariesJA = resultsChild.getJSONArray("exceptionSummaries");
			for (int k = 0; k < geotabExceptionSummariesJA.length(); k++) {
				int eventCount = geotabExceptionSummariesJA.getJSONObject(k).getInt("eventCount");
				JSONObject geotabExceptionRuleJO = geotabExceptionSummariesJA.getJSONObject(k)
						.getJSONObject("exceptionRule");
				String geotabExceptionName = "G-" + geotabExceptionRuleJO.getString("name");
				geotabExceptionEvents.put(geotabExceptionName,
						geotabExceptionEvents.get(geotabExceptionName) == null ? eventCount
								: geotabExceptionEvents.get(geotabExceptionName) + eventCount);
			}
			for (int m = 3; m < displayColumns.size(); m++) {
				if (geotabExceptionEvents.get(displayColumns.get(m)) != null) {
					newReportRow.put(displayColumns.get(m),
							(geotabExceptionEvents.get(displayColumns.get(m))).toString());
				} else {
					if (newReportRow.get(displayColumns.get(m)) == null) {
						newReportRow.put(displayColumns.get(m), "0");
					}
				}
			}
			combinedReport.put(geotabVehicleName, newReportRow);
		}
		return combinedReport;
	}
	
	private String sendLytxRequest(String groupid, String sdate, String edate, String sees, String endpoint)
			throws ParseException {

		//System.out.println(sdate + "---" + edate+ "---" + sees+ "---" + endpoint+"---"+groupid);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sDate = sdate;
		String eDate = edate;
		Date ssdate = sdf.parse(sDate);
		Date eedate = sdf.parse(eDate);
		ISubmissionServiceV5Proxy dr = new ISubmissionServiceV5Proxy(endpoint);
		GetEventsResponse qr = new GetEventsResponse();
		GetEventsByLastUpdateDateRequest geteventbyid = new GetEventsByLastUpdateDateRequest();
		geteventbyid.setSessionId(sees);
		geteventbyid.setStartDate(ssdate);
		geteventbyid.setEndDate(eedate);
		if (!groupid.equalsIgnoreCase("0"))
			geteventbyid.setGroupId(Long.valueOf(Long.parseLong(groupid)));
		try {
			qr = dr.getEventsByLastUpdateDate(geteventbyid);
		} catch (Exception e) {
			System.out.println(e);
		}

		JSONObject jsonObject3 = new JSONObject(qr);
		lytxExceptionSummariesJson = toStringValue(jsonObject3);
//System.out.println(lytxExceptionSummariesJson);
		return lytxExceptionSummariesJson;
	}


	private Map<String, Map<String, Integer>> extractExceptionDataFromLytxResponse(
			JSONArray lytxExceptionSummariesJson, Map<Long, String> lytxVehicleList, Date startDate,
			Date endDate, String lytxBehaviorsJson) throws ParseException {

		Map<String, Map<String, Integer>> lytxVehicleEventsRecord = new HashMap<String, Map<String, Integer>>();
	
		lytxBehaviors = loadLytxBehaviors(lytxBehaviorsJson);

		for (int i = 0; i < lytxExceptionSummariesJson.length(); i++) {
			Long eventsVehicleId = lytxExceptionSummariesJson.getJSONObject(i).getLong("vehicleId");
			String vehicleName = lytxVehicleList.get(eventsVehicleId);
			Map<String, Integer> lytxExceptionEvents = lytxVehicleEventsRecord.get(vehicleName);
			if (lytxExceptionEvents == null) {
				lytxExceptionEvents = new HashMap<String, Integer>();
				String key = vehicleName;
				
					String recordDateUTC = lytxExceptionSummariesJson.getJSONObject(i).getString("recordDateUTC");
					Date recordDate = null;

					if (recordDateUTC.contains("java")) {
						recordDate = getDateFromMilliSeconds(recordDateUTC);
					} else {
						recordDate = getDate(recordDateUTC);
					}
					if (recordDate.before(startDate) || recordDate.after(endDate)) {
						continue;
					}
					Integer periodNumber = getPeriodNumberForDate(recordDate);
					key = periodNumber + "|" + key;
				
				lytxVehicleEventsRecord.put(key, lytxExceptionEvents);
			}
			JSONArray lytxBehavioursArray = lytxExceptionSummariesJson.getJSONObject(i).getJSONArray("behaviors");

			for (int j = 0; j < lytxBehavioursArray.length(); j++) {
				int behavior = lytxBehavioursArray.getJSONObject(j).getInt("behavior");
				String exceptionName = lytxBehaviors.get(behavior);
				Integer behaviorCount = lytxExceptionEvents.get(exceptionName);
				if (behaviorCount == null) {
					behaviorCount = 0;
				}
				lytxExceptionEvents.put(exceptionName, ++behaviorCount);
			}
		}
		return lytxVehicleEventsRecord;
	}

	public static Date getDateFromMilliSeconds(String ms) {
		Date stdate = new Date(Long.parseLong(ms.substring(33, ms.indexOf(','))));

		return stdate;
	}
	
	private void updatedCombinedReportWithLytxExceptions(Map<String, Map<String, String>> combinedReport,
			Map<String, Map<String, Integer>> lytxVehicleEventsRecord) {
		// for every vehicle in lytxVehicleEventsRecord
		for (Map.Entry<String, Map<String, Integer>> lytxVehiclesEventsMapEntry : lytxVehicleEventsRecord.entrySet()) {
			// Get the report row corresponding to that vehicle.
			String lytxVehicleName = lytxVehiclesEventsMapEntry.getKey();
			Map<String, String> reportHeader = combinedReport.get(lytxVehicleName);
			// if the lytxVehicle is not in the Geotab's vehicle list, then skip.
			if (reportHeader == null) {
				continue;
			}
			Map<String, Integer> lytxVehExceptions = lytxVehiclesEventsMapEntry.getValue();
			for (int m = 3; m < displayColumns.size(); m++) {
				if (lytxVehExceptions.get(displayColumns.get(m)) != null) {
					reportHeader.put(displayColumns.get(m), (lytxVehExceptions.get(displayColumns.get(m))).toString());
				}
			}
		}
	}
	
	public LocalDateTime convertToLocalDateTime(String strDate) {
		LocalDateTime ldt = null;
		if (strDate != null) {
			if (strDate.contains("java")) {
				long dateInMilliSec = Long.parseLong(strDate.substring(33, strDate.indexOf(',')));
				ldt = LocalDateTime.ofInstant(Instant.ofEpochSecond(dateInMilliSec), TimeZone.getDefault().toZoneId());
			} else {
				ldt = LocalDateTime.parse(strDate.substring(0, strDate.indexOf('.')));
			}
		}
		return ldt;
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
	public Integer getPeriodNumberForDate(Date date) throws ParseException {
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
	

	
	public String createExcelReport(String sdate,String edate,
			String geouname,String geodatabase,String url,
			String filename,String templect,String entityType) throws EncryptedDocumentException, IOException
	{
		String responseJson = "";
		
			responseJson = glReportService.selectresponce(geouname, geodatabase);
		



		List<String> displayColumns = loadTrendingReporColumntHeaders(geouname, geodatabase);

		File source = new File(AppConstants.SOURCE_FILE_TRENDING_REPORT);
		File dest = new File("/usr/local/apache-tomcat-8.5.51/webapps/" + geodatabase + "/report/excel/as.xlsx");
		
			glReportService.copyFileUsingStream(source, dest);
		
		Workbook workbook = WorkbookFactory
				.create(new File("/usr/local/apache-tomcat-8.5.51/webapps/" + geodatabase + "/report/excel/as.xlsx"));
		Sheet sheet = workbook.getSheetAt(0);
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Calendar calobj = Calendar.getInstance();
		for (int j3 = 0; j3 < 8; j3++) {
			String name = "";
			String val = "";
			switch (j3) {
			case 0:
				name = "CompanyName";
				val = geodatabase;
				break;
			case 1:
				name = "RunDate";
				val = df.format(calobj.getTime());
				break;
			case 2:
				name = "FromDate";
				val = sdate;
				break;
			case 3:
				name = "ToDate";
				val = edate;
				break;
			}
			sheet.createRow(j3);
			Row row = sheet.getRow(j3);
			Cell cell = row.getCell(0);
			if (cell == null)
				cell = row.createCell(0);
			cell.setCellValue(name);
			cell = row.createCell(1);
			cell.setCellValue(val);

			if (j3 == 0) {
				cell = row.createCell(3);
				cell.setCellValue("Trending Report");

				cell = row.createCell(4);
				if (entityType.equals("Device")) {
					cell.setCellValue("Vehicle");
				} else {
					cell.setCellValue("Driver");
				}
			}
		}
		sheet.createRow(4);
		Row row2 = sheet.getRow(4);
		for (int h = 0; h < displayColumns.size(); h++) {
			Cell cell2 = row2.createCell(h);
			if (h == 0) {
				cell2.setCellValue("Weight");
			} else if (h == 1 || h == 2 || h == 3 || h == 4 || h == 5) {
				cell2.setCellValue("");
			} else {
				int D = 0;
				D = glReportService.getwe(geouname, ((String) displayColumns.get(h)).toString().trim(), geodatabase);
				cell2.setCellValue(D);
			}
		}
		sheet.createRow(5);
		Row row3 = sheet.getRow(5);
		for (int j4 = 0; j4 < displayColumns.size(); j4++) {
			Cell cell3 = row3.getCell(j4);
			if (cell3 == null)
				cell3 = row3.createCell(j4);
			cell3.setCellValue(((String) displayColumns.get(j4)).toString());
		}
		JSONObject excelvd = new JSONObject(responseJson);
		JSONArray info = excelvd.getJSONArray("information");
		int s = 0;
		for (s = 0; s < info.length(); s++) {
			sheet.createRow(s + 6);
			Row row4 = sheet.getRow(s + 6);
			Cell cell4 = row4.getCell(0);
			if (cell4 == null)
				cell4 = row4.createCell(0);
			cell4.setCellValue(info.getJSONObject(s).getString("VehicleName"));
			cell4 = row4.createCell(1);
			cell4.setCellValue(info.getJSONObject(s).getString("Group"));
			cell4 = row4.createCell(2);
			cell4.setCellValue(Integer.parseInt(info.getJSONObject(s).getString("Distance")));
			cell4 = row4.createCell(3);
			cell4.setCellValue(Integer.parseInt(info.getJSONObject(s).getString("PeriodNumber")));
			cell4 = row4.createCell(4);
			cell4.setCellValue((info.getJSONObject(s).getString("StartDate")));
			cell4 = row4.createCell(5);
			cell4.setCellValue((info.getJSONObject(s).getString("EndDate")));

			JSONArray behave = info.getJSONObject(s).getJSONArray("Behave");

			for (int h2 = 0; h2 < behave.length(); h2++) {
				cell4 = row4.createCell(h2 + 6);
				cell4.setCellValue(Integer.parseInt(behave.getJSONObject(h2).getString("Rule")));
			}

		}
		Sheet report = workbook.getSheetAt(1);
		Row rows = report.getRow(5);
		Cell cells = rows.getCell(2);
		float min = 0.0F;
		
			min = glReportService.getminmiles(geouname, geodatabase);

			
		cells.setCellValue(min);
		int statrpoint = s + 7;

		String[] formulas = AppConstants.TRENDING_REPORT_FORMULA;
		
		
		updateFormulaForReport(report, FORMULA_START_ROW, s, ROW_OFFSET, formulas);
					
		updateDateFormatFormulaForReport(workbook, report, FORMULA_START_ROW, s, ROW_OFFSET, formulas);

		

		String newAllDataNamedRange = "Report!$A$7:$AA$" + statrpoint;
		XSSFWorkbook glDSRWorkbook = (XSSFWorkbook) workbook;
		XSSFSheet reportSheet = glDSRWorkbook.getSheet("Report");
		XSSFName allDataNamedRange = glDSRWorkbook.getName("AllData");
		allDataNamedRange.setRefersToFormula(newAllDataNamedRange);

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		CellStyle dateCellStyle = workbook.createCellStyle();
		short displayDateFormat = workbook.createDataFormat().getFormat("yyyy-MM-dd");
		dateCellStyle.setDataFormat(displayDateFormat);

		try (FileOutputStream outputStream = new FileOutputStream(
				"/usr/local/apache-tomcat-8.5.51/webapps/" + geodatabase + "/report/excel/" + filename + ".xlsx")) 
		{
			XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
			workbook.write(outputStream);
		}
		workbook.close();

		return "{\"url\":\"" + url + geodatabase + "/report/excel/" + filename + ".xlsx\"}";
	}
	
	
	private void updateFormulaForReport(Sheet sheet, int startRow, int numberOfRows, int rowOffset,
			String[] formulaList) {

		for (int i = startRow; i < startRow + numberOfRows; i++) {

			Row curRow = sheet.createRow(i);
			for (int col = 0; col < formulaList.length; col++) {
				Cell newCell = curRow.createCell(col);
				String formula = formulaList[col];
				formula = formula.replace("@", Integer.toString(i));
				formula = formula.replace("#", Integer.toString(i + 1));
				newCell.setCellFormula(formula);
			}
		}
	}
		private void updateDateFormatFormulaForReport(Workbook workbook,Sheet sheet, int startRow, int numberOfRows, int rowOffset, String[] formulaList) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			CellStyle dateCellStyle = workbook.createCellStyle();
			short displayDateFormat = workbook.createDataFormat().getFormat("yyyy-MM-dd");
			dateCellStyle.setDataFormat(displayDateFormat);

			for(int i = startRow; i < startRow + numberOfRows ; i++) {

			Row curRow = sheet.createRow(i);
			for(int col = 0; col < formulaList.length; col++) {
			Cell newCell = curRow.createCell(col);
			String formula = formulaList[col];
			formula = formula.replace("@", Integer.toString(i));
			formula = formula.replace("#", Integer.toString(i+1));
			newCell.setCellFormula(formula);
			if(col == 4 || col == 5)
			{
				newCell.setCellStyle(dateCellStyle);
			}
			}
			}
			}
		
	    private Map<String, Map<String, Integer>> extractExceptionDataFromLytxResponseDriver(String endpoint,String lytxSess,JSONArray lytxExceptionSummariesJson, Map<Long, String> lytxVehicleList, boolean trending, Date startDate, Date endDate, String lytxBehaviorsJson) throws RemoteException, ParseException {
	         Map<String, Map<String, Integer>> lytxVehicleEventsRecord = new HashMap<String, Map<String, Integer>>();
	        lytxBehaviors = loadLytxBehaviors(lytxBehaviorsJson);
			vechilelytxlist=null;

			/*
			 * System.out.println("EndPoint"+endpoint);
			 * System.out.println("lytex"+lytxSess);
			 */
	    	vechilemap=new LinkedHashMap<Long, String>();
			ISubmissionServiceV5Proxy er=new ISubmissionServiceV5Proxy(endpoint);
			  GetUsersResponse vr=new GetUsersResponse();
			  GetUsersRequest getusersrequest=new GetUsersRequest();
			  getusersrequest.setSessionId(lytxSess);
				  vr=er.getUsers(getusersrequest);
				JSONObject jsonObject2 = new JSONObject(vr);


		  vechilelytxlist=jsonObject2.toString(); 
		  JSONObject lytxVechileJO = new JSONObject(vechilelytxlist);

				JSONArray lytxVechileArray = jsonObject2.getJSONArray("users");

				//System.out.println("vlist"+vechilelytxlist);

				for(int i=0;i<lytxVechileArray.length();i++)
				{
					JSONObject lytxObjValue=lytxVechileArray.getJSONObject(i);

					//System.out.println(lytxObjValue.getLong("userId")+"-"+lytxObjValue.getString("firstName")+" "+lytxObjValue.getString("lastName"));

					vechilemap.put(lytxObjValue.getLong("userId"),lytxObjValue.getString("firstName")+" "+lytxObjValue.getString("lastName"));
				}
	        for (int i = 0; i < lytxExceptionSummariesJson.length(); ++i) {
	             Long eventsVehicleId = lytxExceptionSummariesJson.getJSONObject(i).getLong("driverId");
	             String vehicleName = vechilemap.get(eventsVehicleId);
	            System.out.println(vehicleName);

	            Map<String, Integer> lytxExceptionEvents = lytxVehicleEventsRecord.get(vehicleName);
	            if (lytxExceptionEvents == null) {
	                lytxExceptionEvents = new HashMap<String, Integer>();
	                String key = vehicleName;
	                if (trending) {
	                    final String recordDateUTC = lytxExceptionSummariesJson.getJSONObject(i).getString("recordDateUTC");
	                    Date recordDate = null;
	                    if (recordDateUTC.contains("java")) {
	                        recordDate = getDateFromMilliSeconds(recordDateUTC);
	                    }
	                    else {
	                        recordDate = getDate(recordDateUTC);
	                    }
	                    if (recordDate.before(startDate)) {
	                        continue;
	                    }
	                    if (recordDate.after(endDate)) {
	                        continue;
	                    }
	                     Integer periodNumber = getPeriodNumberForDate(recordDate);
	                    key = periodNumber + "|" + key;
	                }
	                lytxVehicleEventsRecord.put(key, lytxExceptionEvents);
	            }
	             JSONArray lytxBehavioursArray = lytxExceptionSummariesJson.getJSONObject(i).getJSONArray("behaviors");
	            for (int j = 0; j < lytxBehavioursArray.length(); ++j) {
	                 int behavior = lytxBehavioursArray.getJSONObject(j).getInt("behavior");
	                 String exceptionName = lytxBehaviors.get(behavior);
	                Integer behaviorCount = lytxExceptionEvents.get(exceptionName);
	                if (behaviorCount == null) {
	                    behaviorCount = 0;
	                }
	                lytxExceptionEvents.put(exceptionName, ++behaviorCount);
	            }
	        }
	        return lytxVehicleEventsRecord;
	    }

}
