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
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

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
import org.springframework.stereotype.Repository;

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
import com.vibaps.merged.safetyreport.dao.gl.CommonGeotabDAO;
import com.vibaps.merged.safetyreport.entity.gl.GlRulelistEntity;
import com.vibaps.merged.safetyreport.entity.gl.Trip;
import com.vibaps.merged.safetyreport.service.gl.GlReportService;
@Repository
public class RestTrendingReportDAO {
	
	@Autowired
	private GlReportService glReportService;
	@Autowired
	private CommonGeotabDAO commonGeotabDAO;
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

	
	public Object getReportGeo(String groupId,String startDate,String endDate,
			String lytxSessionId,String geotabSessionId,String geotabGroups,
		String userName,String geotabDatabase,String url,
		String enttype,String period,String endPoint) throws ParseException, MalformedURLException, IOException
	{
		String responseJson = "";
		Map<String, Map<String, Integer>> lytxVehicleEventsRecord = new HashMap<String, Map<String, Integer>>();
		String getVehicleResponseJson = "";
		List<GlRulelistEntity> getl = glReportService.getgeodropdown(userName,geotabDatabase);
		//ArrayList<String> getl = (ArrayList<String>) getgeodropdown;
		String value = "";
		Map<String, Map<String, String>> combinedReport = new HashMap<>();
		List<String> displayColumns = null;
		Map<Integer, String> lytxBehaviors = null;
		
		  String groupvalue = "";
      	String[] geotabgroupsval = geotabGroups.split(",");
		      
		      for (int i = 0; i < geotabgroupsval.length; i++) {
		        if (i != geotabgroupsval.length - 1) {
		          groupvalue = groupvalue + "{\"id\":\"" + (String)geotabgroupsval[i] + "\"},";
		        } else {
		          groupvalue = groupvalue + "{\"id\":\"" + (String)geotabgroupsval[i] + "\"}";
		        } 
		      }
		
		
		
		 getVehicleResponseJson=lytexVechileResponce(startDate, endDate, groupId, lytxSessionId, endPoint);
		
		
			String lytxBehaviorsJson = glReportService.getLytxBehaviorsResponseJson(lytxSessionId, endPoint);
			String sdate="";

	
			// geotabDriverExceptionSummariesJson = "{\"result\":" +
			// o.getAsJsonArray("result").get(0).toString() + "}";

			// System.out.println(geotabDriverExceptionSummariesJson);

			
			String geotabDriverExceptionSummariesJson=geotabTrendingResponce(getl, groupvalue, endDate, startDate, url, geotabDatabase, geotabSessionId, enttype, userName, period).toString();
			String startDateStr = startDate + "T01:00:00";
			String endDateStr = endDate + "T59:59:59";

			

				if (!groupId.equalsIgnoreCase("0")) {
					int EXCEPTIONS_START_COLUMN = 3;

					// Load Lytx vehicle map with vehicleId and names
					lytxVehicleList = loadLytxVehicleIDNameMap(getVehicleResponseJson);
					lytxBehaviors = loadLytxBehaviors(lytxBehaviorsJson);
					String[] lytxBehaviorsArray = new String[lytxBehaviors.size()];
					int bCount = 0;

					if (enttype.equalsIgnoreCase("Driver")) {
						// Load Trips data to get driver data from Vehicles;

						
							vehicleTrips = loadVehicleTripsMap(enttype, userName, geotabDatabase,geotabSessionId, url,startDateStr, endDateStr);
						
					}
				}
				boolean trending = true;

//		        	//create report object with Geotab VEHICLE data:
//		    		Map<String, Map<String, String>> combinedReport = extractGeotabVehicleData(geotabVehicleExceptionSummariesJson);

				// create report object with Geotab DRIVER data:

				if (trending) {
					// load the header for report data (from the database based on the userName in
					// actual application)
					displayColumns = loadTrendingReporColumntHeaders(userName, geotabDatabase);

					if (enttype.equals("Driver")) {
						combinedReport = extractGeotabDriverTrendingData(geotabDriverExceptionSummariesJson, userName,
								geotabDatabase);
					} else {
						combinedReport = extractGeotabVehicleTrendingData(geotabDriverExceptionSummariesJson, userName,
								geotabDatabase);

					}

					EXCEPTIONS_START_COLUMN = 6;
				} else {
					// load the header for report data (from the database based on the userName in
					// actual application)
					if (enttype.equalsIgnoreCase("Driver")) {
						System.out.println("COMBINED REPORT - DRIVER");
						combinedReport = extractGeotabDriverData(geotabDriverExceptionSummariesJson);
					} else {
						System.out.println("COMBINED REPORT - VEHICLE");
						combinedReport = extractGeotabVehicleData(geotabVehicleExceptionSummariesJson);
					}
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				Date newStartDate = sdf.parse(startDate);
				
				Date ssdate = sdf.parse(startDate);
				Date eedate = sdf.parse(endDate);
				// Create a Map of lytx vehicleIds to exception map
				int s = 0;
				if (!groupId.equalsIgnoreCase("0")) {
					do {

						System.out.println("check---" + s++);

			
							lytxExceptionSummariesJson = sendLytxRequest(groupId, startDate, endDate, lytxSessionId, endPoint);

							JSONObject lytxEventsJO = new JSONObject(lytxExceptionSummariesJson);
							JSONArray lytxEventsArray = lytxEventsJO.getJSONArray("events");
							 if (enttype.equals("Driver")) 
	                            {
	                            	System.out.println("Driver");
	                            lytxVehicleEventsRecord = extractExceptionDataFromLytxResponseDriver(endPoint,lytxSessionId,lytxEventsArray, this.lytxVehicleList, trending, ssdate, eedate, lytxBehaviorsJson);
	                            }
	                            else
	                            {
	                                lytxVehicleEventsRecord = extractExceptionDataFromLytxResponse(lytxEventsArray, this.lytxVehicleList, trending, ssdate, eedate, lytxBehaviorsJson);

	                            }
							if (lytxEventsJO.has("queryCutoff")) {
								String cutoffData = lytxEventsJO.getString("queryCutoff");
								System.out.println(cutoffData);

								if (cutoffData != null) {

									newStartDate = getDateFromMilliSeconds(cutoffData);

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

					updatedCombinedReportWithLytxExceptions(combinedReport, lytxVehicleEventsRecord);
				}
				// create a json response
				
				responseJson=createTrendingResponce(combinedReport,displayColumns);
				

			glReportService.updateresponce(userName, responseJson, geotabDatabase);
		

		return responseJson; 
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
	
	
	public JsonObject geotabTrendingResponce(List<GlRulelistEntity> getl,String groupvalue,String endDate,String startDate,String url,String geotabDatabase,String geotabSessionId,String enttype,String userName,String period) throws MalformedURLException, IOException
	{
		String gvalue = "";
		for (int j = 0; j < getl.size(); j++) {
			if (j != getl.size() - 1) {
				gvalue = gvalue + "{\"id\":\"" +getl.get(j).getRulevalue() + "\"},";
			} else {
				gvalue = gvalue + "{\"id\":\"" +getl.get(j).getRulevalue() + "\"}";
			}
		}
	
		String uri = "https://" + url + "/apiv1";
		String urlParameters = "{\"method\":\"ExecuteMultiCall\",\"params\":{\"calls\":[{\"method\":\"GetReportData\",\"params\":{\"argument\":{\"runGroupLevel\":-1,\"isNoDrivingActivityHidden\":true,\"fromUtc\":\""
				+ startDate + "T01:00:00.000Z\",\"toUtc\":\"" + endDate + "T03:59:59.000Z\",\"entityType\":\"" + enttype
				+ "\",\"reportArgumentType\":\"RiskManagement\",\"groups\":[" + groupvalue
				+ "],\"reportSubGroup\":\"" + period + "\",\"rules\":[" + gvalue
				+ "]}}},{\"method\":\"Get\",\"params\":{\"typeName\":\"SystemSettings\"}}],\"credentials\":{\"database\":\""
				+ geotabDatabase + "\",\"sessionId\":\"" + geotabSessionId + "\",\"userName\":\"" + userName + "\"}}}";

		String serverurl = uri;
		HttpURLConnection con = (HttpURLConnection) (new URL(serverurl)).openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", " application/json; charset=utf-8");
		con.setRequestProperty("Content-Language", "en-US");
		con.setDoOutput(true);
		con.setUseCaches(false);
		con.setDoInput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
		InputStream is = con.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		StringBuilder response = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			response.append(line);
			response.append('\r');
		}
		rd.close();
		JsonParser parser = new JsonParser();
		JsonObject o = parser.parse(response.toString()).getAsJsonObject();
		
		return o;
	}
	
	public String lytexVechileResponce(String startDate,String endDate,String groupId,String lytxSessionId,String endPoint) throws ParseException, RemoteException
	{
		String lytxBehaviorsJson="";
		String getVehicleResponseJson="";
		Date ssdate = null;
		Date eedate = null;
		if (!groupId.equalsIgnoreCase("0")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String sDate = startDate;
			String eDate = endDate;
			ssdate = sdf.parse(sDate);
			eedate = sdf.parse(eDate);

			ISubmissionServiceV5Proxy er = new ISubmissionServiceV5Proxy(endPoint);
			ExistingSessionRequest re = new ExistingSessionRequest();
			re.setSessionId(lytxSessionId);
			GetBehaviorsResponse getb = new GetBehaviorsResponse();
			getb = er.getBehaviors(re);

			JSONObject jsonObject = new JSONObject(getb);
			lytxBehaviorsJson = toStringValue(jsonObject);
			ISubmissionServiceV5Proxy pr = new ISubmissionServiceV5Proxy(endPoint);
			GetVehiclesResponse lr = new GetVehiclesResponse();
			GetVehiclesRequest getVehiclesRequest = new GetVehiclesRequest();
			getVehiclesRequest.setIncludeSubgroups(Boolean.valueOf(true));
			getVehiclesRequest.setSessionId(lytxSessionId);
			lr = pr.getVehicles(getVehiclesRequest);

			JSONObject jsonObject2 = new JSONObject(lr);
			getVehicleResponseJson = toStringValue(jsonObject2);
		}
		
		return getVehicleResponseJson;
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
//		        printLytxBehaviors(lBehaviors);
		return lBehaviors;
	}
	
	public Map<String, List<Trip>> loadVehicleTripsMap(String entity, String geouname, String geodatabase,
			String geosees, String url, String fromDate, String toDate) throws MalformedURLException, IOException {
		// THE FOLLOWING METHOD CALL 'loadSampleTrips()' SHOULD BE REPLACED WITH ACTUAL
		// CALL AND VALUE RETURNED AS STRING ARRAY.

		ArrayList<String> tripsData = commonGeotabDAO.getTrip(geouname, geodatabase, geosees, url, fromDate, toDate);

		for (String tripData : tripsData) {
			String[] tripVars = tripData.split("\\|");
			String vehicleName = tripVars[0];
			String driverName[] = tripVars[1].split(" "); // ASSUMPTION: NO SPACES IN FIRSTNAME AND LASTNAMES. THE
															// DRIVER NAME WILL HAVE ONLY ONE SPACE BETWEEN FIRST AND
															// LAST NAMES.
			String driverFirstName = driverName[0];
			String driverLastName = driverName[1];
			LocalDateTime tripStartDate = convertToLocalDateTime(tripVars[2]);
			LocalDateTime tripEndDate = convertToLocalDateTime(tripVars[3]);
			Trip trip = new Trip(vehicleName, driverFirstName, driverLastName, tripStartDate, tripEndDate);
			List<Trip> trips = vehicleTrips.get(vehicleName);
			if (trips == null) {
				trips = new ArrayList<Trip>();
				vehicleTrips.put(vehicleName, trips);
			}
			trips.add(trip);
		}
		return vehicleTrips;
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
			String geotabDriverTrendingReportResponseJson, String userName, String db) {
		displayColumns = loadTrendingReporColumntHeaders(userName, db);

		// create report object:
		Map<String, Map<String, String>> combinedReport = new LinkedHashMap<String, Map<String, String>>();
		// GEOTAB Events processing
		JSONObject geotabEventsJO = new JSONObject(geotabDriverTrendingReportResponseJson);
		JSONArray geotabEventsJOArray = geotabEventsJO.getJSONArray("result");
		JSONArray resultChildArray = geotabEventsJOArray.getJSONArray(0);
		for (int i = 0; i < resultChildArray.length(); i++) {
			JSONObject resultsChild = resultChildArray.getJSONObject(i);
			Integer periodNumber = resultsChild.getInt("periodNumber");

			String periodStartDate = resultsChild.getString("periodStartDate");
			String periodEndDate = resultsChild.getString("periodEndDate");
			if (periods.get(periodNumber) == null) {
				String[] periodRange = new String[2];
				periodRange[0] = periodStartDate;
				periodRange[1] = periodEndDate;
				periods.put(periodNumber, periodRange);
			}

			System.out.print("exceptionSummary - Period Number: " + periodNumber + ", Period Start: " + periodStartDate
					+ ", Period End: " + periodEndDate + " - ");
			JSONObject itemJO = resultsChild.getJSONObject("item");
			// driverName
			String geotabVehicleName = itemJO.getString("firstName") + " " + itemJO.getString("lastName");
			Map<String, String> newReportRow = new LinkedHashMap<String, String>();// getNewReportRow();
			newReportRow.put(displayColumns.get(0), geotabVehicleName);
			// group
			JSONArray geotabDriverGroups = itemJO.getJSONArray("driverGroups");
			String group = null;
			for (int j = 0; j < geotabDriverGroups.length(); j++) {
				String groupName = "";
				if (!geotabDriverGroups.getJSONObject(j).has("name")) {
					groupName = "All Vehicles";
				} else {
					groupName = geotabDriverGroups.getJSONObject(j).getString("name");

				}
				if (group == null) {
					group = groupName;
				} else {
					group = group + ", " + groupName;
				}
			}
			newReportRow.put(displayColumns.get(1), group);
			// Distance
			Object geotabVehicleTotalDistance = resultsChild.get("totalDistance");

			long tDistance = ((Double) geotabVehicleTotalDistance).longValue();

			newReportRow.put(displayColumns.get(2), Long.toString(tDistance));
			newReportRow.put(displayColumns.get(3), Integer.toString(periodNumber));
			newReportRow.put(displayColumns.get(4), periodStartDate);
			newReportRow.put(displayColumns.get(5), periodEndDate);
			// Geotab exceptions from exceptionSummaries
			Map<String, Integer> geotabExceptionEvents = new HashMap<String, Integer>();
			JSONArray geotabExceptionSummariesJA = resultsChild.getJSONArray("exceptionSummaries");
			for (int k = 0; k < geotabExceptionSummariesJA.length(); k++) {
				if (!geotabExceptionSummariesJA.isNull(k)) {
					int eventCount = geotabExceptionSummariesJA.getJSONObject(k).getInt("eventCount");
					JSONObject geotabExceptionRuleJO = geotabExceptionSummariesJA.getJSONObject(k)
							.getJSONObject("exceptionRule");
					String geotabExceptionName = "G-" + geotabExceptionRuleJO.getString("name");
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

		return combinedReport;
	}
	
	private Map<String, Map<String, String>> extractGeotabVehicleTrendingData(
			String geotabTrendingReportResponseJson, String userName, String db) {

		// System.out.println(geotabTrendingReportResponseJson);

		// create report object:
		displayColumns = loadTrendingReporColumntHeaders(userName, db);

		Map<String, Map<String, String>> combinedReport = new LinkedHashMap<String, Map<String, String>>();
		// GEOTAB Events processing
		JSONObject geotabEventsJO = new JSONObject(geotabTrendingReportResponseJson);
		JSONArray geotabEventsJOArray = geotabEventsJO.getJSONArray("result");
		JSONArray resultChildArray = geotabEventsJOArray.getJSONArray(0);
		for (int j = 0; j < resultChildArray.length(); j++) {

			JSONObject exceptionSummary = resultChildArray.getJSONObject(j);
			Integer periodNumber = exceptionSummary.getInt("periodNumber");
			String periodStartDate = exceptionSummary.getString("periodStartDate");
			String periodEndDate = exceptionSummary.getString("periodEndDate");
			if (periods.get(periodNumber) == null) {
				String[] periodRange = new String[2];
				periodRange[0] = periodStartDate;
				periodRange[1] = periodEndDate;
				periods.put(periodNumber, periodRange);
			}
			JSONObject itemJO = exceptionSummary.getJSONObject("item");
			// vehicleName
			String geotabVehicleName = itemJO.getString("name");
			Map<String, String> newReportRow = new LinkedHashMap<String, String>();// getNewReportRow();

			newReportRow.put(displayColumns.get(0), geotabVehicleName);
			// group
			JSONArray geotabVehicleGroups = itemJO.getJSONArray("groups");
			String group = null;
			for (int k = 0; k < geotabVehicleGroups.length(); k++) {
				if (group == null) {
					group = geotabVehicleGroups.getJSONObject(k).getString("name");
				} else {
					String newGroup = geotabVehicleGroups.getJSONObject(k).getString("name");
					if ("Prohibit Idling".equalsIgnoreCase(newGroup)) {
						group = newGroup + ", " + group;
					} else {
						group = group + ", " + newGroup;
					}
				}
			}
			newReportRow.put(displayColumns.get(1), group);
			// Distance
			Object geotabVehicleTotalDistance = exceptionSummary.get("totalDistance");

			long tDistance = ((Double) geotabVehicleTotalDistance).longValue();

			newReportRow.put(displayColumns.get(2), Long.toString(tDistance));
			newReportRow.put(displayColumns.get(3), Integer.toString(periodNumber));
			newReportRow.put(displayColumns.get(4), periodStartDate);
			newReportRow.put(displayColumns.get(5), periodEndDate);

			// Geotab exceptions from exceptionSummaries
			Map<String, Integer> geotabExceptionEvents = new HashMap<String, Integer>();
			JSONArray geotabExceptionSummariesJA = exceptionSummary.getJSONArray("exceptionSummaries");
			for (int m = 0; m < geotabExceptionSummariesJA.length(); m++) {
				if (!geotabExceptionSummariesJA.isNull(m)) {
					int eventCount = geotabExceptionSummariesJA.getJSONObject(m).getInt("eventCount");
					JSONObject geotabExceptionRuleJO = geotabExceptionSummariesJA.getJSONObject(m)
							.getJSONObject("exceptionRule");
					String geotabExceptionName = "G-" + geotabExceptionRuleJO.getString("name");
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
		}
		return combinedReport;
	}
	
	private Map<String, Map<String, String>> extractGeotabDriverData(String geotabDriverExceptionSummariesJson) {

		// create report object:
		Map<String, Map<String, String>> combinedReport = new LinkedHashMap<String, Map<String, String>>();
		// GEOTAB Events processing
		JSONObject geotabEventsJO = new JSONObject(geotabDriverExceptionSummariesJson);
		JSONArray geotabEventsJOArray = geotabEventsJO.getJSONArray("result");
		for (int i = 0; i < geotabEventsJOArray.length(); i++) {
			JSONObject resultsChild = geotabEventsJOArray.getJSONObject(i);
			JSONObject itemJO = resultsChild.getJSONObject("item");
			// driverName
			String geotabVehicleName = itemJO.getString("firstName") + " " + itemJO.getString("lastName");
			Map<String, String> newReportRow = new LinkedHashMap<String, String>();// getNewReportRow();
			newReportRow.put(displayColumns.get(0), geotabVehicleName);
			// group
			JSONArray geotabDriverGroups = itemJO.getJSONArray("driverGroups");
			String group = null;
			for (int j = 0; j < geotabDriverGroups.length(); j++) {
				if (group == null) {
					group = geotabDriverGroups.getJSONObject(j).getString("name");
				} else {
					group = group + ", " + geotabDriverGroups.getJSONObject(j).getString("name");
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
			JSONArray geotabExceptionSummariesJA = resultsChild.getJSONArray("exceptionSummaries");
			for (int k = 0; k < geotabExceptionSummariesJA.length(); k++) {
				int eventCount = geotabExceptionSummariesJA.getJSONObject(k).getInt("eventCount");
				JSONObject geotabExceptionRuleJO = geotabExceptionSummariesJA.getJSONObject(k)
						.getJSONObject("exceptionRule");
				String geotabExceptionName = "G-" + geotabExceptionRuleJO.getString("name");
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

		System.out.println(sdate + "---" + edate+ "---" + sees+ "---" + endpoint+"---"+groupid);

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
System.out.println(lytxExceptionSummariesJson);
		return lytxExceptionSummariesJson;
	}


	private Map<String, Map<String, Integer>> extractExceptionDataFromLytxResponse(
			JSONArray lytxExceptionSummariesJson, Map<Long, String> lytxVehicleList, boolean trending, Date startDate,
			Date endDate, String lytxBehaviorsJson) throws ParseException {

		Map<String, Map<String, Integer>> lytxVehicleEventsRecord = new HashMap<String, Map<String, Integer>>();
		// Process lytxExceptionSummariesJson
		/*
		 * JSONObject lytxEventsJO = new JSONObject(lytxExceptionSummariesJson);
		 * JSONArray lytxEventsArray = lytxEventsJO.getJSONArray("events");
		 */
		lytxBehaviors = loadLytxBehaviors(lytxBehaviorsJson);

		for (int i = 0; i < lytxExceptionSummariesJson.length(); i++) {
			Long eventsVehicleId = lytxExceptionSummariesJson.getJSONObject(i).getLong("vehicleId");
			String vehicleName = lytxVehicleList.get(eventsVehicleId);
			Map<String, Integer> lytxExceptionEvents = lytxVehicleEventsRecord.get(vehicleName);
			if (lytxExceptionEvents == null) {
				lytxExceptionEvents = new HashMap<String, Integer>();
				String key = vehicleName;
				if (trending) {
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
				}
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

	private Date getDateFromMilliSeconds(String ms) {
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
	
	public Object getReportGeoLytx(String sdate,String edate,String geosees,
			String geotabgroups,String userName,
			String geodatabase,String url,String enttype, String period) throws MalformedURLException, IOException
		{
		String responseJson = "";
		Map<String, Map<String, Integer>> lytxVehicleEventsRecord = new HashMap<String, Map<String, Integer>>();
		String getVehicleResponseJson = "";
		List<Integer> totals = new ArrayList<>();
		List<GlRulelistEntity> getl = glReportService.getgeodropdown(userName,geodatabase);
		//ArrayList<String> getl = (ArrayList<String>) getgeodropdown;
		String value = "";
		Map<String, Map<String, String>> combinedReport = new HashMap<>();
		List<String> displayColumns = null;
		Map<Integer, String> lytxBehaviors = null;
		StringBuffer combinedReportResponseJson = new StringBuffer();
		
			String lytxBehaviorsJson = "";
			Date ssdate = null;
			Date eedate = null;

			String gvalue = "";
			for (int j = 0; j < getl.size(); j++) {
				if (j != getl.size() - 1) {
					gvalue = gvalue + "{\"id\":\"" +getl.get(j).getRulevalue()+ "\"},";
				} else {
					gvalue = gvalue + "{\"id\":\"" +getl.get(j).getRulevalue()+ "\"}";
				}
			}
			String groupvalue = "";
        	String[] geotabgroupsval = geotabgroups.split(",");
		      
		      for (int i = 0; i < geotabgroupsval.length; i++) {
		        if (i != geotabgroupsval.length - 1) {
		          groupvalue = groupvalue + "{\"id\":\"" + (String)geotabgroupsval[i] + "\"},";
		        } else {
		          groupvalue = groupvalue + "{\"id\":\"" + (String)geotabgroupsval[i] + "\"}";
		        } 
		      } 
			String uri = "https://" + url + "/apiv1";
			String urlParameters = "{\"method\":\"ExecuteMultiCall\",\"params\":{\"calls\":[{\"method\":\"GetReportData\",\"params\":{\"argument\":{\"runGroupLevel\":-1,\"isNoDrivingActivityHidden\":true,\"fromUtc\":\""
					+ sdate + "T01:00:00.000Z\",\"toUtc\":\"" + edate + "T03:59:59.000Z\",\"entityType\":\"" + enttype
					+ "\",\"reportArgumentType\":\"RiskManagement\",\"groups\":[" + groupvalue
					+ "],\"reportSubGroup\":\"" + period + "\",\"rules\":[" + gvalue
					+ "]}}},{\"method\":\"Get\",\"params\":{\"typeName\":\"SystemSettings\"}}],\"credentials\":{\"database\":\""
					+ geodatabase + "\",\"sessionId\":\"" + geosees + "\",\"userName\":\"" + userName + "\"}}}";

			String serverurl = uri;
			HttpURLConnection con = (HttpURLConnection) (new URL(serverurl)).openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", " application/json; charset=utf-8");
			con.setRequestProperty("Content-Language", "en-US");
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setDoInput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			InputStream is = con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			JsonParser parser = new JsonParser();
			JsonObject o = parser.parse(response.toString()).getAsJsonObject();
			String geotabDriverExceptionSummariesJson = o.toString();
			// geotabDriverExceptionSummariesJson = "{\"result\":" +
			// o.getAsJsonArray("result").get(0).toString() + "}";

			// System.out.println(geotabDriverExceptionSummariesJson);

			String startDateStr = sdate + "T01:00:00";
			String endDateStr = edate + "T59:59:59";

			

				boolean trending = true;

//		        	//create report object with Geotab VEHICLE data:
//		    		Map<String, Map<String, String>> combinedReport = extractGeotabVehicleData(geotabVehicleExceptionSummariesJson);

				// create report object with Geotab DRIVER data:

				if (trending) {
					// load the header for report data (from the database based on the userName in
					// actual application)
					displayColumns = loadTrendingReporColumntHeaders(userName, geodatabase);

					if (enttype.equals("Driver")) {
						combinedReport = extractGeotabDriverTrendingData(geotabDriverExceptionSummariesJson, userName,
								geodatabase);
					} else {
						combinedReport = extractGeotabVehicleTrendingData(geotabDriverExceptionSummariesJson, userName,
								geodatabase);

					}

					EXCEPTIONS_START_COLUMN = 6;
				} else {
					// load the header for report data (from the database based on the userName in
					// actual application)
					if (enttype.equalsIgnoreCase("Driver")) {
						System.out.println("COMBINED REPORT - DRIVER");
						combinedReport = extractGeotabDriverData(geotabDriverExceptionSummariesJson);
					} else {
						System.out.println("COMBINED REPORT - VEHICLE");
						combinedReport = extractGeotabVehicleData(geotabVehicleExceptionSummariesJson);
					}
				}
				Date newStartDate = ssdate;
				// Create a Map of lytx vehicleIds to exception map
				int s = 0;

				// create a json response
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

			

		
			glReportService.updateresponce(userName, responseJson, geodatabase);
		
		return responseJson;
			
		}
	
	public String createExcelReport(String sdate,String edate,
			String geouname,String geodatabase,String url,
			String filename,String templect,String entityType) throws EncryptedDocumentException, IOException
	{
		String responseJson = "";
		
			responseJson = glReportService.selectresponce(geouname, geodatabase);
		

		/*
		 * List<Score> topNRecords=new ArrayList<Score>();
		 * 
		 * try { topNRecords=dao.calculateTopRecords(geouname); }catch (Exception e) {
		 * // TODO: handle exception }
		 * 
		 * //bootom List<Score> bottomNRecords=new ArrayList<Score>(); try {
		 * bottomNRecords=dao.calculateBottomNRecords(geouname); }catch (Exception e) {
		 * // TODO: handle exception
		 * 
		 * 
		 * }
		 */

		List<String> displayColumns = loadTrendingReporColumntHeaders(geouname, geodatabase);

		File source = new File(
				"/usr/local/apache-tomcat-8.5.51/webapps/GL_Driver_Safety_Report_Template_" + templect + ".xlsx");
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

		// String[] formulas =
		// {"Data!A@","Data!B@","Data!C@","IF(C#>$C$6,TRUE,FALSE)","Data!E@","Data!F@","Data!G@","Data!H@","Data!I@","Data!J@","Data!K@","Data!L@","Data!M@","Data!N@","IFERROR((E#*E$6)/($C#/100),0)","IFERROR((F#*F$6)/($C#/100),0)","IFERROR((G#*G$6)/($C#/100),0)","IFERROR((H#*H$6)/($C#/100),0)","IFERROR((I#*I$6)/($C#/100),0)","IFERROR((J#*J$6)/($C#/100),0)","IFERROR((K#*K$6)/($C#/100),0)","IFERROR((L#*L$6)/($C#/100),0)","IFERROR((M#*M$6)/($C#/100),0)","IFERROR((N#*N$6)/($C#/100),0)","AVERAGE(OFFSET($O#,0,0,1,$Y$5))"};
		// String[] formulas =
		// {"Data!A@","Data!B@","Data!C@","IF(C#>$C$6,TRUE,FALSE)","Data!E@","Data!F@","Data!G@","Data!H@","Data!I@","Data!J@","Data!K@","Data!L@","Data!M@","Data!N@","Data!O@","Data!P@","IFERROR((G#*G$6)/($C#/100),0)","IFERROR((H#*H$6)/($C#/100),0)","IFERROR((I#*I$6)/($C#/100),0)","IFERROR((J#*J$6)/($C#/100),0)","IFERROR((K#*K$6)/($C#/100),0)","IFERROR((L#*L$6)/($C#/100),0)","IFERROR((M#*M$6)/($C#/100),0)","IFERROR((N#*N$6)/($C#/100),0)","IFERROR((O#*O$6)/($C#/100),0)","IFERROR((P#*P$6)/($C#/100),0)","AVERAGE(OFFSET($Q#,0,0,1,$AA$5))"};
		String[] formulas = { "Data!A@", "Data!B@", "Data!C@", "IF(C#>$C$6,TRUE,FALSE)",
				"DATE(LEFT(Data!E@,4),MID(Data!E@,6,2),MID(Data!E@,9,2))",
				"DATE(LEFT(Data!F@,4),MID(Data!F@,6,2),MID(Data!F@,9,2))", "Data!G@", "Data!H@", "Data!I@", "Data!J@",
				"Data!K@", "Data!L@", "Data!M@", "Data!N@", "Data!O@", "Data!P@", "IFERROR((G#*G$6)/($C#/100),0)",
				"IFERROR((H#*H$6)/($C#/100),0)", "IFERROR((I#*I$6)/($C#/100),0)", "IFERROR((J#*J$6)/($C#/100),0)",
				"IFERROR((K#*K$6)/($C#/100),0)", "IFERROR((L#*L$6)/($C#/100),0)", "IFERROR((M#*M$6)/($C#/100),0)",
				"IFERROR((N#*N$6)/($C#/100),0)", "IFERROR((O#*O$6)/($C#/100),0)", "IFERROR((P#*P$6)/($C#/100),0)",
				"AVERAGE(OFFSET($Q#,0,0,1,$AA$5))" };
		// String[] formulas =
		// {"Data!A@","Data!B@","Data!C@","IF(C#>$C$6,TRUE,FALSE)","Data!E@","Data!F@","Data!G@","Data!H@","Data!I@","Data!J@","Data!K@","Data!L@","Data!M@","Data!N@","IFERROR((E#*E$6)/($C8/100),0)","IFERROR((F#*F$6)/($C8/100),0)","IFERROR((G#*G$6)/($C8/100),0)","IFERROR((H#*H$6)/($C8/100),0)","IFERROR((I#*I$6)/($C8/100),0)","IFERROR((J#*J$6)/($C8/100),0)","IFERROR((K#*K$6)/($C8/100),0)","IFERROR((L#*L$6)/($C8/100),0)","IFERROR((M#*M$6)/($C8/100),0)","IFERROR((N#*N$6)/($C8/100),0)","AVERAGE(OFFSET($O#,0,0,1,$Y$5))"};
		// String[] formulas =
		// {"Data!A@","Data!B@","Data!C@","IF(C#>$C$6,TRUE,FALSE)","Data!E@","Data!F@","Data!G@","Data!H@","Data!I@","Data!J@","Data!K@","Data!L@","Data!M@","Data!N@","Data!O@","Data!P@","Data!Q@","Data!R@","Data!S@","Data!T@","Data!U@","Data!V@","Data!W@","Data!X@","IFERROR((E#*E$6)/($C8/100),0)","IFERROR((F#*F$6)/($C8/100),0)","IFERROR((G#*G$6)/($C8/100),0)","IFERROR((H#*H$6)/($C8/100),0)","IFERROR((I#*I$6)/($C8/100),0)","IFERROR((J#*J$6)/($C8/100),0)","IFERROR((K#*K$6)/($C8/100),0)","IFERROR((L#*L$6)/($C8/100),0)","IFERROR((M#*M$6)/($C8/100),0)","IFERROR((N#*N$6)/($C8/100),0)","IFERROR((O#*O$6)/($C8/100),0)","IFERROR((P#*P$6)/($C8/100),0)","IFERROR((Q#*Q$6)/($C8/100),0)","IFERROR((R#*R$6)/($C8/100),0)","IFERROR((S#*S$6)/($C8/100),0)","IFERROR((T#*T$6)/($C8/100),0)","IFERROR((U#*U$6)/($C8/100),0)","IFERROR((V#*V$6)/($C8/100),0)","IFERROR((W#*W$6)/($C8/100),0)","IFERROR((X#*X$6)/($C8/100),0)","AVERAGE(OFFSET($Y#,0,0,1,$AS$5))"};
		updateFormulaForReport(report, FORMULA_START_ROW, s, ROW_OFFSET, formulas);
//						
		updateDateFormatFormulaForReport(workbook, report, FORMULA_START_ROW, s, ROW_OFFSET, formulas);

		/*
		 * for (int i = statrpoint; i < Integer.parseInt(templect); i++) { try { Row row
		 * = report.getRow(i); report.removeRow(row); } catch (Exception exception) {} }
		 */

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
		
	    private Map<String, Map<String, Integer>> extractExceptionDataFromLytxResponseDriver(String endpoint,String lytxSess,final JSONArray lytxExceptionSummariesJson, final Map<Long, String> lytxVehicleList, final boolean trending, final Date startDate, final Date endDate, final String lytxBehaviorsJson) throws RemoteException, ParseException {
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
