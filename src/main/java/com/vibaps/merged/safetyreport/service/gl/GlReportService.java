package com.vibaps.merged.safetyreport.service.gl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api_GetEvents_V5.GetEventsResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

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
import com.vibaps.merged.safetyreport.common.EntityType;
import com.vibaps.merged.safetyreport.dao.gl.CommonGeotabDAO;
import com.vibaps.merged.safetyreport.dto.gl.Behave;
import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
import com.vibaps.merged.safetyreport.entity.gl.GenDevice;
import com.vibaps.merged.safetyreport.entity.gl.GenDriver;
import com.vibaps.merged.safetyreport.entity.gl.GlRulelistEntity;
import com.vibaps.merged.safetyreport.entity.gl.ReportRow;
import com.vibaps.merged.safetyreport.entity.gl.Score;
import com.vibaps.merged.safetyreport.entity.gl.Trip;
import com.vibaps.merged.safetyreport.repo.gl.UserReportFilterRepository;
import com.vibaps.merged.safetyreport.services.gl.GeoTabApiService;
import com.vibaps.merged.safetyreport.services.gl.LytxProxyService;
import com.vibaps.merged.safetyreport.services.gl.UserReportFilterService;
import com.vibaps.merged.safetyreport.util.DateTimeUtil;
import com.vibaps.merged.safetyreport.util.ResponseUtil;

import jdk.internal.org.jline.utils.Log;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Repository
public class GlReportService {
	
	@Autowired
	private UserReportFilterService userReportFilterService;

	@Autowired
	private UserReportFilterRepository userReportFilterRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private GeoTabApiService geoTabApiService;
	
	@Autowired
	private LytxProxyService lytxProxyService;

	@Autowired
	private CommonGeotabDAO commonGeotabDAO;

	private String				vechilelytxlist;
	private Map<Long, String>	vechilemap;
	private int					ROW_OFFSET			= -1;
	private int					FORMULA_START_ROW	= 7;

	String			title;
	LocalDateTime	startDate;
	LocalDateTime	endDate;
	String			reportBy;				// Vehicle or Driver
	int				minimumDistance	= 50;
//	List<String> selectedRuleNames;
	Map<String, Integer>				selectedRules;
	List<ReportRow>						reportRows;
	Map<String, Map<String, Integer>>	lytxVehicleEventsRecord;
	Integer								EXCEPTIONS_START_COLUMN	= 3;
	List<String>						displayReportColumnHeaders;
	Map<String, List<Trip>>				vehicleTrips;
	Map<Long, String>					lytxVehicleList;
	Map<Integer, String>				lytxBehaviors;
	List<Score>							topNRecords;
	List<Score>							bottomNRecords;
	Integer								N_TOP_BOTTOM_RECORDS	= 10;
	List<Map.Entry<String, Integer>>	loadSelectedRuleNames;

	public Object view(String companyid, String db) {
		Map<String, Object>		result	= new HashMap<>();
		List<GlRulelistEntity>	obj		= null;
		List<GlRulelistEntity>	obj1	= null;

		Integer countuser = null;
		countuser = userReportFilterRepository.userCount(companyid, db);

		if (countuser.intValue() == 0) {

			BigInteger newUserId = userReportFilterRepository.userCreation(companyid, db);

			if (newUserId.intValue() != 0) {

				obj1 = userReportFilterRepository.getRuleList(db);

				Iterator it = obj1.iterator();
				while (it.hasNext()) {
					Object[] line = (Object[]) it.next();
					userReportFilterRepository.insertUserRuleList(newUserId, Integer.parseInt(line[0].toString()),
					        Integer.parseInt(line[1].toString()), Integer.parseInt(line[2].toString()));

				}

			}

		}

		return countuser;
	}

	public Object viewadd(String geotabUserid, String db) {

		List<GlRulelistEntity> obj = null;

		obj = userReportFilterRepository.viewadd(geotabUserid, db);

		return obj;
	}

	public List<GlRulelistEntity> getgeodropdown(String geotabUserid, String db) {

		List<GlRulelistEntity> obj = null;

		obj = userReportFilterRepository.getgeodropdown(geotabUserid, db);

		return obj;
	}

	public Object getLybehave(String geouserid, String db) {

		List<GlRulelistEntity> obj = null;

		obj = userReportFilterRepository.getLybehave(geouserid, db);

		return obj;
	}

	public List getallbehave(String geotabUserid, String db) {

		List obj = new ArrayList();

		obj = userReportFilterRepository.getallbehave(geotabUserid, db);

		return obj;
	}

	public List<String> getallbehaveui(String geotabUserid, String db) {

		List<String> obj = new ArrayList();

		obj = userReportFilterRepository.getallbehave(geotabUserid, db);

		return obj;
	}

	public int geoCount(String geotabUserid, String db) {
		int obj = 0;

		obj = userReportFilterRepository.geoCount(geotabUserid, db);

		return obj;
	}

	public int lyCount(String geotabUserid, String db) {
		int obj = 0;

		obj = userReportFilterRepository.lyCount(geotabUserid, db);

		return obj;
	}

	public int getwe(String geotabUserid, String rule, String db) {
		int obj = 0;

		obj = userReportFilterRepository.getWeight(geotabUserid, db, rule);

		return obj;
	}

	public float getminmiles(String geotabUserid, String db) {
		float obj = 0.0F;

		obj = userReportFilterRepository.getminmiles(geotabUserid, db);

		return obj;
	}

	public Object updateresponce(String geotabUserId, String responseJson, String db) {

		userReportFilterRepository.updateresponce(geotabUserId, db, responseJson);

		return "Data Inserted";
	}

	public String selectresponce(String geotabuUserId, String db) {
		// TODO Auto-generated method stub
		String i = "";

		i = userReportFilterRepository.selectresponce(geotabuUserId, db);

		return i;
	}

	public Object insert(ArrayList<GlRulelistEntity> v, String companyid, String minmiles, String db) {
		Map<String, String> result = new HashMap<>();

		int i = 0;
		try {
			i = userReportFilterRepository.getRuleListInsert(companyid, db, Float.valueOf(Float.parseFloat(minmiles)));

		} catch (Exception exception) {
		}

		for (int d = 0; d < v.size(); d++) {

			userReportFilterRepository.updateRuleListValue(companyid, db,
			        Integer.valueOf(((GlRulelistEntity) v.get(d)).getWeight()),
			        ((GlRulelistEntity) v.get(d)).getRulevalue());
			result.put("result", "Rules list saved");
		}

		return result;
	}

	public List<GenDevice> deviceName(String geotabUserId, String db) {
		List<GenDevice> deviceNameList = new ArrayList<GenDevice>();

		List<GenDevice> list = null;

		Session				session	= null;
		Map<String, String>	obj		= new LinkedHashMap<String, String>();
		;
		Transaction transaction = null;

		list = userReportFilterRepository.deviceInfo(geotabUserId, db);

		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[]	line	= (Object[]) it.next();
			GenDevice	eq		= new GenDevice();
			eq.setDeviceId(line[0].toString());
			eq.setDeviceName(line[1].toString());

			deviceNameList.add(eq);
		}

		return deviceNameList;
	}

	public Object getReportGeo(String startDate, String endDate, String geosees, String geotabgroups, String userName,
	        String geodatabase, String url, String filename, String templect, String enttype)
	        throws MalformedURLException, IOException, ParseException {
		String					responseJson	= "";
		List<GlRulelistEntity>	getl			= getgeodropdown(userName, geodatabase);
		// ArrayList<String> getl = (ArrayList<String>) getgeodropdown;
		String								value			= "";
		Map<String, Map<String, String>>	combinedReport	= new HashMap<>();
		List<String>						displayColumns	= null;
		Map<Integer, String>				lytxBehaviors	= null;

		String		groupvalue		= "";
		String[]	geotabgroupsval	= geotabgroups.split(",");

		for (int i = 0; i < geotabgroupsval.length; i++) {
			if (i != geotabgroupsval.length - 1) {
				groupvalue = groupvalue + "{\"id\":\"" + (String) geotabgroupsval[i] + "\"},";
			} else {
				groupvalue = groupvalue + "{\"id\":\"" + (String) geotabgroupsval[i] + "\"}";
			}
		}

		JsonObject geotabDriverExceptionJson = geotabVechileDriverResponce(startDate, endDate, getl, url, groupvalue,
		        enttype, userName, geodatabase, geosees);

		String geotabDriverExceptionSummariesJson = "{\"result\":"
		        + geotabDriverExceptionJson.getAsJsonArray("result").get(0).toString() + "}";

		// load the header for report data (from the database based on the userName in
		// actual application)
		displayColumns = loadReporColumntHeaders(userName, geodatabase);

		// Load Lytx vehicle map with vehicleId and names

		// Map<Long, String> lytxVehicleList = loadLytxVehicleIDNameMap();
		// lytxBehaviors= loadLytxBehaviors();
		// String[] lytxBehaviorsArray = new String[lytxBehaviors.size()];

		int bCount = 0;

//	        	//create report object with Geotab VEHICLE data:
//	    		Map<String, Map<String, String>> combinedReport = extractGeotabVehicleData(geotabVehicleExceptionSummariesJson);

		// create report object with Geotab DRIVER data:
		if (enttype.equals("Driver")) {
			combinedReport = extractGeotabDriverData(geotabDriverExceptionSummariesJson, userName, geodatabase);
		} else {

			combinedReport = extractGeotabVehicleData(geotabDriverExceptionSummariesJson, userName, geodatabase);

			// System.out.println(combinedReport+"-fdf---");
		}

		responseJson = createJsonForGeotabResponce(displayColumns, combinedReport);

		updateresponce(userName, responseJson, geodatabase);

		return responseJson;

	}

	public String createJsonForGeotabResponce(List<String> displayColumns,
	        Map<String, Map<String, String>> combinedReport) {
		String			responseJson	= "";
		List<Integer>	totals			= new ArrayList<>();

		StringBuffer combinedReportResponseJson = new StringBuffer();

		// create a json response
		totals = new ArrayList<Integer>();
		for (int q = 0; q < displayColumns.size(); q++) {
			totals.add(0);
		}
		combinedReportResponseJson = new StringBuffer();
		combinedReportResponseJson.append("\"information\": [");
		boolean	firstRow		= true;
		int		rulesRecords	= displayColumns.size() - 3;
		for (Map.Entry<String, Map<String, String>> combinedReportRows : combinedReport.entrySet()) {
			if (!firstRow) {
				combinedReportResponseJson.append(",");
			} else {
				firstRow = false;
			}
			combinedReportResponseJson.append("{");
			boolean				rulesHeadedAdded	= false;
			int					headerCount			= 0;
			int					rowCount			= 0;
			Map<String, String>	rowData				= combinedReportRows.getValue();
			for (Map.Entry<String, String> data : rowData.entrySet()) {
				if (headerCount++ > 0 && headerCount < displayColumns.size() + 1) {
					combinedReportResponseJson.append(",");
				}
				if (rowCount++ < 3) {
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

	public JsonObject geotabVechileDriverResponce(String startDate, String endDate, List<GlRulelistEntity> getl,
	        String url, String groupval, String enttype, String userName, String geodatabase, String geosees)
	        throws MalformedURLException, IOException, ParseException {
		SimpleDateFormat	sdf		= new SimpleDateFormat("yyyy-MM-dd");
		String				sDate	= startDate;
		String				eDate	= endDate;
		Date				ssdate	= sdf.parse(sDate);
		Date				eedate	= sdf.parse(eDate);
		String				gvalue	= "";
		for (int j = 0; j < getl.size(); j++) {
			if (j != getl.size() - 1) {
				gvalue = gvalue + "{\"id\":\"" + getl.get(j).getRulevalue() + "\"},";
			} else {
				gvalue = gvalue + "{\"id\":\"" + getl.get(j).getRulevalue() + "\"}";
			}
		}

		String	uri				= "https://" + url + "/apiv1";
		String	urlParameters	= "{\"method\":\"ExecuteMultiCall\",\"params\":{\"calls\":[{\"method\":\"GetReportData\",\"params\":{\"argument\":{\"runGroupLevel\":-1,\"isNoDrivingActivityHidden\":true,\"fromUtc\":\""
		        + sDate + "T01:00:00.000Z\",\"toUtc\":\"" + eDate + "T03:59:59.000Z\",\"entityType\":\"" + enttype
		        + "\",\"reportArgumentType\":\"RiskManagement\",\"groups\":[" + groupval
		        + "],\"reportSubGroup\":\"None\",\"rules\":[" + gvalue
		        + "]}}},{\"method\":\"Get\",\"params\":{\"typeName\":\"SystemSettings\"}}],\"credentials\":{\"database\":\""
		        + geodatabase + "\",\"sessionId\":\"" + geosees + "\",\"userName\":\"" + userName + "\"}}}";

		String serverurl = uri;

		// System.out.println(uri+urlParameters);

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
		InputStream		is			= con.getInputStream();
		BufferedReader	rd			= new BufferedReader(new InputStreamReader(is));
		StringBuilder	response	= new StringBuilder();
		String			line;
		while ((line = rd.readLine()) != null) {
			response.append(line);
			response.append('\r');
		}
		rd.close();
		JsonParser	parser	= new JsonParser();
		JsonObject	o		= parser.parse(response.toString()).getAsJsonObject();

		return o;
	}

	@SuppressWarnings("unchecked")
	public List<GenDriver> driverName(String geotabUserId, String db) {

		Map<String, String>	obj				= new LinkedHashMap<String, String>();
		List<GenDriver>		driverNameList	= new ArrayList<GenDriver>();
		List<GenDriver>		list			= new ArrayList();

		Transaction transaction = null;

		list = userReportFilterRepository.getDriverInfo(geotabUserId, db);

		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				GenDriver eq = new GenDriver();
				eq.setDriverId(list.get(i).getDriverId());
				eq.setDriverName(list.get(i).getDriverName());
				driverNameList.add(eq);
			}
		}

		return driverNameList;
	}
	// Report Data

	public String processforNonLytx(String sdate, String edate, String geosees, String geotabgroups, String geouname,
	        String geodatabase, String url, String filename, String templect, String enttype)
	        throws ParseException, MalformedURLException, IOException {

		/*
		 * reportBy = enttype;
		 * 
		 * displayReportColumnHeaders = loadReporColumntHeaders(geouname, geodatabase);
		 * 
		 * title = "";
		 * 
		 * Map<String, Integer> selectedRules = new LinkedHashMap<String, Integer>();
		 * reportRows = new ArrayList<ReportRow>();
		 * 
		 * List<String> displayReportColumnHeaders = new ArrayList<String>();
		 * vehicleTrips = new LinkedHashMap<String, List<Trip>>(); lytxVehicleList = new
		 * LinkedHashMap<Long, String>(); lytxBehaviors = new LinkedHashMap<Integer,
		 * String>(); topNRecords = new ArrayList<Score>(); bottomNRecords = new
		 * ArrayList<Score>();
		 * 
		 * String groupvalue = "";
		 * 
		 * String[] geotabgroupsval = geotabgroups.split(",");
		 * 
		 * for (int i = 0; i < geotabgroupsval.length; i++) { if (i !=
		 * geotabgroupsval.length - 1) { groupvalue = groupvalue + "{\"id\":\"" +
		 * (String) geotabgroupsval[i] + "\"},"; } else { groupvalue = groupvalue +
		 * "{\"id\":\"" + (String) geotabgroupsval[i] + "\"}"; } } if
		 * (reportBy.equalsIgnoreCase("Driver")) { // Load Trips data to get driver data
		 * corresponding to Vehicles; vehicleTrips = loadVehicleTripsMap(geouname,
		 * geodatabase, geosees, url, sdate, edate); }
		 * 
		 * int geoRuleCount = 0; geoRuleCount = geoCount(geouname, geodatabase);
		 * 
		 * System.out.println("Geoci---" + geoRuleCount);
		 * 
		 * if (geoRuleCount > 0) { if (reportBy.equalsIgnoreCase("Driver")) {
		 * System.out.println("COMBINED REPORT - DRIVER");
		 * 
		 * String geotabDriverExceptionSummariesResponseJson = "{\"result\":" +
		 * getGeotabDriverExceptionSummariesResponseJson(sdate, edate, geouname,
		 * groupvalue, geodatabase, geosees, url,
		 * enttype).getAsJsonArray("result").get(0).toString() + "}";
		 * 
		 * extractGeotabDriverData(geotabDriverExceptionSummariesResponseJson,
		 * geouname); } else { System.out.println("COMBINED REPORT - VEHICLE"); String
		 * geotabVechileExceptionSummariesJson = "{\"result\":" +
		 * getGeotabVehicleExceptionSummariesResponseJson(sdate, edate, geouname,
		 * groupvalue, geodatabase, geosees, url,
		 * enttype).getAsJsonArray("result").get(0).toString() + "}";
		 * 
		 * extractGeotabVehicleData(geotabVechileExceptionSummariesJson, geouname); } }
		 * 
		 * // process GEOTAB exceptions response if
		 * (reportBy.equalsIgnoreCase("Driver")) { JsonObject o =
		 * getGeotabDriverExceptionSummariesResponseJson( sdate, edate, geouname,
		 * geotabgroups, geodatabase, geosees, url, enttype); String
		 * geotabDriverExceptionSummariesResponseJson = "{\"result\":" +
		 * o.getAsJsonArray("result").get(0).toString() + "}";
		 * 
		 * System.out.println("COMBINED REPORT - DRIVER");
		 * extractGeotabDriverData(geotabDriverExceptionSummariesResponseJson,
		 * geouname); } else { System.out.println("COMBINED REPORT - VEHICLE");
		 * JsonObject o = getGeotabVehicleExceptionSummariesResponseJson(sdate, edate,
		 * geouname, geotabgroups, geodatabase, geosees, url, enttype);
		 * 
		 * String geotabVehileExceptionSummariesJson = "{\"result\":" +
		 * o.getAsJsonArray("result").get(0).toString() + "}";
		 * 
		 * extractGeotabVehicleData(geotabVehileExceptionSummariesJson, geouname); }
		 * 
		 * String reportResponseJson = createReportReponseJson(geouname);
		 * 
		 * updateresponce(geouname, reportResponseJson, geodatabase);
		 * 
		 * System.out.println(reportResponseJson);
		 */
		return null;
	}

	public String process(ReportParams reportParams) throws ParseException, MalformedURLException, IOException {

		reportBy = reportParams.getEntityType();

		displayReportColumnHeaders = loadReporColumntHeaders(reportParams.getGeotabUserName(),reportParams.getGeotabDatabase());

		title = "";
		reportRows = new ArrayList<ReportRow>();

		vehicleTrips	= new LinkedHashMap<String, List<Trip>>();
		lytxVehicleList	= new LinkedHashMap<Long, String>();
		lytxBehaviors	= new LinkedHashMap<Integer, String>();
		topNRecords		= new ArrayList<Score>();
		bottomNRecords	= new ArrayList<Score>();

		/*
		 * if (EntityType.isDriver(reportParams.getEntityType())) { // Load Trips data
		 * to get driver data corresponding to Vehicles; vehicleTrips =
		 * loadVehicleTripsMap(geouname, geodatabase, geosees, url, sdate, edate); }
		 */

		// process GEOTAB exceptions response
	
		int geoRuleCount=0;
		geoRuleCount=geoCount(reportParams.getGeotabUserName(),reportParams.getGeotabDatabase());
	


		if(geoRuleCount > 0)
		{
			reportRows = geoTabApiService.getVehicleExceptionSummary(reportParams);
		}

		// process LYTX exceptions response
		// Create a Map of lytx vehicleIds to exception map

		
			lytxVehicleEventsRecord = lytxProxyService.getLytxExceptionData(reportParams);
  
				//log.info("Lytx Responce::{}",lytxVehicleEventsRecord);			
		
				// combine Lytx exceptions data with the geotab exception report
			if(geoRuleCount > 0)
			{
				reportRows=updateCombinedReportWithLytxExceptions(lytxVehicleEventsRecord, reportParams.getGeotabUserName(),reportRows);
			}
			else
			{
				reportRows=createLytxExceptionsReport(lytxVehicleEventsRecord,reportParams.getGeotabUserName(),reportRows);
			}
			
		String reportResponseJson = createReportReponseJson(reportParams.getGeotabUserName(),reportRows);

		updateresponce(reportParams.getGeotabUserName(), reportResponseJson, reportParams.getGeotabDatabase());

		return reportResponseJson;
	}
	
	
	private List<ReportRow> createLytxExceptionsReport(Map<String, Map<String, Integer>> lytxVehicleEventsRecord,String userName,List<ReportRow> reportRows) {
		//for every vehicle in lytxVehicleEventsRecord
		// displayReportColumnHeaders=loadReporColumntHeaders(userName);
		
		for (Map.Entry<String, Map<String, Integer>> lytxVehiclesEventsMapEntry : lytxVehicleEventsRecord.entrySet()) {
			//Get the report row corresponding to that vehicle.
			String lytxVehicleName = lytxVehiclesEventsMapEntry.getKey();
			
			//possible performance issue here.  Better to use Maps.
			ReportRow reportRow = new ReportRow();
			reportRow.setDistance(0L);
			reportRow.setGroup("-");
		    reportRow.setName(lytxVehicleName);
			

			Map<String, Integer> lytxVehExceptions = lytxVehiclesEventsMapEntry.getValue();
			for(int m=EXCEPTIONS_START_COLUMN; m < displayReportColumnHeaders.size(); m++) {
				if(lytxVehExceptions.get(displayReportColumnHeaders.get(m)) != null) {
					//System.out.println(lytxVehicleName+"-"+displayReportColumnHeaders.get(m)+"--"+lytxVehExceptions.get(displayReportColumnHeaders.get(m)));
					
					reportRow.getSelectedRules().put(displayReportColumnHeaders.get(m),lytxVehExceptions.get(displayReportColumnHeaders.get(m)));
				}
				else {
					if(reportRow.getSelectedRules().get(displayReportColumnHeaders.get(m)) == null){
						reportRow.getSelectedRules().put(displayReportColumnHeaders.get(m), 0);
					}	
				}
				
			}
			reportRows.add(reportRow);
		}
		
		return reportRows;
	}

	/**
	 * @param geotabDriverExceptionSummariesResponseJson
	 */
	private void extractGeotabDriverData(String geotabDriverExceptionSummariesResponseJson, String userName) {

		// List<String> displayReportColumnHeaders = loadReporColumntHeaders(userName);
		JSONObject	geotabEventsJO		= new JSONObject(geotabDriverExceptionSummariesResponseJson);
		JSONArray	geotabEventsJOArray	= geotabEventsJO.getJSONArray("result");

		for (int i = 0; i < geotabEventsJOArray.length(); i++) {

			// System.out.println(geotabEventsJOArray.length()+"-----length");
			ReportRow reportRow = new ReportRow();

			JSONObject	resultsChild	= geotabEventsJOArray.getJSONObject(i);
			JSONObject	itemJO			= resultsChild.getJSONObject("item");

			// driverName
			String geotabDriverName = itemJO.getString("firstName") + " " + itemJO.getString("lastName");
			// System.out.println(geotabDriverName+"name");
			reportRow.setName(geotabDriverName);
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
			reportRow.setGroup(group);
			// Distance
			Object	geotabVehicleTotalDistance	= resultsChild.get("totalDistance");
			long	tDistance					= ((Double) geotabVehicleTotalDistance).longValue();
			reportRow.setDistance(tDistance);
			// Geotab exceptions from exceptionSummaries
			Map<String, Integer>	geotabExceptionEvents		= new HashMap<String, Integer>();
			JSONArray				geotabExceptionSummariesJA	= resultsChild.getJSONArray("exceptionSummaries");
			for (int k = 0; k < geotabExceptionSummariesJA.length(); k++) {
				// System.out.println(geotabExceptionSummariesJA.isNull(k)+"----=ds");

				if (!geotabExceptionSummariesJA.isNull(k)) {
					// System.out.println(geotabExceptionSummariesJA.length()+"-----length");

					int eventCount = geotabExceptionSummariesJA.getJSONObject(k).getInt("eventCount");

					JSONObject geotabExceptionRuleJO = geotabExceptionSummariesJA.getJSONObject(k)
					        .getJSONObject("exceptionRule");

					String geotabExceptionName = "G-" + geotabExceptionRuleJO.getString("name");

					// System.out.println(geotabExceptionName+"-"+eventCount);
					geotabExceptionEvents.put(geotabExceptionName,
					        geotabExceptionEvents.get(geotabExceptionName) == null ? eventCount
					                : geotabExceptionEvents.get(geotabExceptionName) + eventCount);
				}
			}
			for (int m = EXCEPTIONS_START_COLUMN; m < displayReportColumnHeaders.size(); m++) {
				if (geotabExceptionEvents.get(displayReportColumnHeaders.get(m)) != null) {
					reportRow.getSelectedRules().put(displayReportColumnHeaders.get(m),
					        (geotabExceptionEvents.get(displayReportColumnHeaders.get(m))));
				} else {
					if (reportRow.getSelectedRules().get(displayReportColumnHeaders.get(m)) == null) {
						reportRow.getSelectedRules().put(displayReportColumnHeaders.get(m), 0);
					}
				}
			}
			reportRows.add(reportRow);
		}

	}

	/**
	 * @param geotabVehicleExceptionSummariesResponseJson
	 */
	private void extractGeotabVehicleData(String geotabVehicleExceptionSummariesResponseJson, String userName) {
		// GEOTAB Events processing

		// System.out.println(geotabVehicleExceptionSummariesResponseJson);

		// displayReportColumnHeaders=loadReporColumntHeaders(userName);

		// System.out.println(displayReportColumnHeaders.toString());
		// System.out.println("res : "+geotabVehicleExceptionSummariesResponseJson);
		JSONObject	geotabEventsJO		= new JSONObject(geotabVehicleExceptionSummariesResponseJson);
		JSONArray	geotabEventsJOArray	= geotabEventsJO.getJSONArray("result");

		for (int i = 0; i < geotabEventsJOArray.length(); i++) {

			ReportRow	reportRow		= new ReportRow();
			JSONObject	resultsChild	= geotabEventsJOArray.getJSONObject(i);
			JSONObject	itemJO			= resultsChild.getJSONObject("item");
			// vehicleName
			String geotabVehicleName = itemJO.getString("name");
			reportRow.setName(geotabVehicleName);
			// group
			JSONArray	geotabVehicleGroups	= itemJO.getJSONArray("groups");
			String		group				= null;
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

					String geotabExceptionName = "G-" + geotabExceptionRuleJO.getString("name");

					geotabExceptionEvents.put(geotabExceptionName,
					        geotabExceptionEvents.get(geotabExceptionName) == null ? eventCount
					                : geotabExceptionEvents.get(geotabExceptionName) + eventCount);
				}

			}

			for (int m = 3; m < displayReportColumnHeaders.size(); m++) {
				if (geotabExceptionEvents.get(displayReportColumnHeaders.get(m)) != null) {
					reportRow.getSelectedRules().put(displayReportColumnHeaders.get(m),
					        (geotabExceptionEvents.get(displayReportColumnHeaders.get(m))));
				} else {
					if (reportRow.getSelectedRules().get(displayReportColumnHeaders.get(m)) == null) {
						reportRow.getSelectedRules().put(displayReportColumnHeaders.get(m), 0);
					}
				}
			}
			reportRows.add(reportRow);

		}

	}

	// cleaned - in use
	/**
	 * @param getLytxExceptionSummariesResponseJson
	 * @throws RemoteException
	 */
	@Deprecated(forRemoval = true)
	private Map<String, Map<String, Integer>> extractExceptionDataFromLytxResponse(
	        String getLytxExceptionSummariesResponseJson, String lytxSess, String endpoint) throws RemoteException {
		// Load Lytx vehicle map with vehicleId and names

		lytxBehaviors	= null;
		lytxVehicleList	= null;
		if (lytxVehicleList == null) {
			lytxVehicleList	= new LinkedHashMap<Long, String>();
			lytxVehicleList	= loadLytxVehicleID_NameMap(getLytxVehicleID_NameResponseJson(lytxSess, endpoint));
		}
		// Load Lytx Behaviours map
		if (lytxBehaviors == null) {
			lytxBehaviors	= new LinkedHashMap<Integer, String>();
			lytxBehaviors	= loadLytxBehaviors(getLytxBehaviorsResponseJson(lytxSess, endpoint));

		}
		// Process lytxExceptionSummariesJson
		lytxVehicleEventsRecord = new HashMap<String, Map<String, Integer>>();

		JSONObject	lytxEventsJO	= new JSONObject(getLytxExceptionSummariesResponseJson);
		JSONArray	lytxEventsArray	= lytxEventsJO.getJSONArray("events");
		for (int i = 0; i < lytxEventsArray.length(); i++) {
			Long	eventsVehicleId	= lytxEventsArray.getJSONObject(i).getLong("vehicleId");
			String	vehicleName		= lytxVehicleList.get(eventsVehicleId);

			Map<String, Integer> lytxExceptionEvents = lytxVehicleEventsRecord.get(vehicleName);
			if (lytxExceptionEvents == null) {
				lytxExceptionEvents = new HashMap<String, Integer>();

				lytxVehicleEventsRecord.put(vehicleName, lytxExceptionEvents);

			}
			JSONArray lytxBehavioursArray = lytxEventsArray.getJSONObject(i).getJSONArray("behaviors");
			for (int j = 0; j < lytxBehavioursArray.length(); j++) {
				int		behavior		= lytxBehavioursArray.getJSONObject(j).getInt("behavior");
				String	exceptionName	= lytxBehaviors.get(behavior);
				Integer	behaviorCount	= lytxExceptionEvents.get(exceptionName);
				if (behaviorCount == null) {
					behaviorCount = 0;
				}

				lytxExceptionEvents.put(exceptionName, ++behaviorCount);
			}
		}
		return lytxVehicleEventsRecord;
	}

	/**
	 * @param reportRows
	 * @param lytxVehicleEventsRecord
	 * @return 
	 */
	private List<ReportRow> updateCombinedReportWithLytxExceptions(Map<String, Map<String, Integer>> lytxVehicleEventsRecord,
	        String userName,List<ReportRow> reportRows) {

		for (Map.Entry<String, Map<String, Integer>> lytxVehiclesEventsMapEntry : lytxVehicleEventsRecord.entrySet()) {
			String lytxVehicleName = lytxVehiclesEventsMapEntry.getKey();
			if(lytxVehicleName !=null)
			{
			Optional<ReportRow> reportRow = reportRows.stream()
										.filter(r -> lytxVehicleName.equalsIgnoreCase(r.getName()))
										.findFirst();
			if (reportRow.isEmpty()) {
				continue;
			}
			
			Map<String, Integer> lytxVehExceptions = lytxVehiclesEventsMapEntry.getValue();
			for (int m = EXCEPTIONS_START_COLUMN; m < displayReportColumnHeaders.size(); m++) {
				if (lytxVehExceptions.get(displayReportColumnHeaders.get(m)) != null) {
					reportRow.get().getSelectedRules().put(displayReportColumnHeaders.get(m),
					        lytxVehExceptions.get(displayReportColumnHeaders.get(m)));
				}
			}
		}
		}
		return reportRows;
	}

	public String createReportReponseJson(String userName,List<ReportRow> reportRows) {
		// create a json response

		// displayReportColumnHeaders=loadReporColumntHeaders(userName,db);
		String			responseJson				= null;
		StringBuffer	combinedReportResponseJson	= new StringBuffer();

		List<Integer> totals = new ArrayList<Integer>();
		for (int q = 0; q < displayReportColumnHeaders.size(); q++) {
			totals.add(0);
		}
		combinedReportResponseJson.append("\"information\": [");
		boolean	firstRow		= true;
		int		rulesRecords	= displayReportColumnHeaders.size() - EXCEPTIONS_START_COLUMN;
//	          for (Map.Entry<String, Map<String, String>> combinedReportRows : combinedReport.entrySet()) {
		for (ReportRow row : reportRows) {
			if (!firstRow) {
				combinedReportResponseJson.append(",");
			} else {
				firstRow = false;
			}
			combinedReportResponseJson.append("{");
			boolean	rulesHeadedAdded	= false;
			int		headerCount			= 0;
			int		rowItemCount		= 0;
			rulesHeadedAdded = false;
			combinedReportResponseJson.append("\"" + "VehicleName" + "\": \"" + row.getName() + "\"");
			combinedReportResponseJson.append(",");
			combinedReportResponseJson.append("\"" + "Group" + "\": \"" + row.getGroup() + "\"");
			combinedReportResponseJson.append(",");
			combinedReportResponseJson.append("\"" + "Distance" + "\": \"" + row.getDistance() + "\"");
			combinedReportResponseJson.append(",");
			rowItemCount = 4;

			int counter = 2;

			for (Map.Entry<String, Integer> data : row.getSelectedRules().entrySet()) {
				counter++;
				if (headerCount++ > 0 && headerCount < displayReportColumnHeaders.size() + 1) {
					combinedReportResponseJson.append(",");
				}
				if (!rulesHeadedAdded) {
					combinedReportResponseJson.append("\"Behave\": [");
					rulesHeadedAdded = true;
				}
				combinedReportResponseJson.append("{");
				combinedReportResponseJson.append("\"Rule\": \"" + data.getValue() + "\"}");
				// System.out.println(row.getName()+displayReportColumnHeaders.get(counter)+"--"+data.getValue());

				totals.set(rowItemCount - 1, (totals.get(rowItemCount - 1) + data.getValue()));
				if (rowItemCount++ == displayReportColumnHeaders.size()) {
					combinedReportResponseJson.append("]");
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
			if (ruleCounter != displayReportColumnHeaders.size()) {
				totalsJson.append(",");
			}
		}
		totalsJson.append("],");

		responseJson = totalsJson.toString() + combinedReportResponseJson.toString();
		System.out.println(responseJson);

		return responseJson;
	}
	/*
	 * returns the list of top n number of vehicles or drivers whose score is the
	 * least. param: top number of records needed.
	 */
	/*
	 * public void calculateTopBottomNRecords() { Map<String, Score> scores = new
	 * HashMap<String, Score>(); List<Score> zeroScores = new ArrayList<Score>();
	 * List<Score> topScores = new ArrayList<Score>(); int countOfRules =
	 * loadSelectedRuleNames().size(); for (ReportRow row : reportRows ) { Score
	 * score = new Score(); //row.setScore(score); score.setName(row.getName());
	 * Map<String, Double> weightedAvgScore = new LinkedHashMap<String, Double>();
	 * score.setWeightedAvgScores(weightedAvgScore); double totalWeightedAvgScores =
	 * 0d; for(Map.Entry<String, Integer> selectedRulesEntry :
	 * loadSelectedRuleNames()) { String ruleName = selectedRulesEntry.getKey();
	 * Integer weight = this.selectedRules.get(ruleName); Integer ruleVal =
	 * row.getSelectedRules().get(ruleName); if(ruleVal > 0) { double weightedAvg =
	 * (double)(ruleVal * weight)/(this.minimumDistance * 100);
	 * score.getWeightedAvgScores().put(ruleName, weightedAvg);
	 * totalWeightedAvgScores += weightedAvg;
	 * score.setMeanWeightedAvgScores((double)(totalWeightedAvgScores/countOfRules))
	 * ; } else { score.getWeightedAvgScores().put(ruleName, 0d); } }
	 * if(score.getMeanWeightedAvgScores() == null) {
	 * score.setMeanWeightedAvgScores(0d); }
	 * 
	 * if(score.getMeanWeightedAvgScores() == 0d) {
	 * score.setMeanWeightedAvgScores((double)row.getDistance());
	 * zeroScores.add(score); } else { topScores.add(score); } }
	 * 
	 * int topScoresCount = topScores.size(); int zeroScoresCount =
	 * zeroScores.size();
	 * 
	 * 
	 * 
	 * topNRecords = new ArrayList<Score>(); bottomNRecords = new
	 * ArrayList<Score>(); if(zeroScoresCount > 0) {
	 * zeroScores.sort(Comparator.comparing(Score::getMeanWeightedAvgScores).
	 * reversed()); for(int i = 0; i < zeroScoresCount && i < N_TOP_BOTTOM_RECORDS;
	 * i++) { topNRecords.add(zeroScores.get(i)); } }
	 * 
	 * topScores.sort(Comparator.comparing(Score::getMeanWeightedAvgScores));
	 * for(int i = 0; (i < topScoresCount) && (i < (N_TOP_BOTTOM_RECORDS -
	 * zeroScoresCount)); i++) { topNRecords.add(topScores.get(i)); } //BOTTOM N
	 * RECORDS
	 * topScores.sort(Comparator.comparing(Score::getMeanWeightedAvgScores).reversed
	 * ()); for(int i = 0; (i < topScoresCount) && (i < (N_TOP_BOTTOM_RECORDS));
	 * i++) { bottomNRecords.add(topScores.get(i)); } }
	 */

	public Map<String, List<Trip>> loadVehicleTripsMap(String geouserid, String databaseName, String geosess,
	        String url, String sdate, String edate) throws MalformedURLException, IOException {
		// Run the api calls to get the trips, users etc data from Geotab and Store the
		// values in the VehicleDriverMap object.
		/*
		 * api.call("Get", { "typeName": "Trip", search: { fromDate:
		 * '2020-05-01T00:00:00.000Z', //this.getStartDate() toDate:
		 * "2020-05-30T00:00:00.000Z", //this.getEndDate() } }, function(result) { for
		 * (var i = 0; i < result.length; i++) { if(result[i].driver!="UnknownDriverId")
		 * { //console.log(result[i]); getDeviceData(result[i]); } } }, function(e) {
		 * console.error("Failed:", e); });
		 * 
		 * function getDeviceData(deviceId) { var a=""; api.call("Get", { "typeName":
		 * "Device", "search": { id: deviceId.device.id } }, function(results) {
		 * api.call("Get", { "typeName": "User", search: { id: deviceId.driver.id } },
		 * function(resultss) {
		 * console.log(results[0].name+"---"+resultss[0].firstName+" "+resultss[0].
		 * lastName+"-"+deviceId.start+"-"+deviceId.stop); }, function(e) {
		 * console.error("Failed:", e); }); //console.log (results[0].name); },
		 * function(e) { //console.error("Failed:", e); }); }
		 */
		// THE FOLLOWING METHOD CALL 'loadSampleTrips()' SHOULD BE REPLACED WITH ACTUAL
		// CALL AND VALUE RETURNED AS STRING ARRAY.
		ArrayList<String> tripsData = commonGeotabDAO.getTrip(geouserid, databaseName, geosess, url, sdate, edate);
		// END METHOD CALL 'loadSampleTrips()'

		Map<String, List<Trip>> vehicleTrips = new HashMap<String, List<Trip>>();
		for (String tripData : tripsData) {
			String[]	tripVars		= tripData.split("\\|");
			String		vehicleName		= tripVars[0];
			String		driverName[]	= tripVars[1].split(" ");	// ASSUMPTION: NO SPACES IN FIRSTNAME AND LASTNAMES.
			                                                        // THE DRIVER NAME WILL HAVE ONLY ONE SPACE BETWEEN
			                                                        // FIRST AND LAST NAMES.
			String		driverFirstName	= driverName[0];
			String		driverLastName	= "";

			driverLastName = driverName[1];

			LocalDateTime	tripStartDate	= DateTimeUtil.convertToLocalDateTime(tripVars[2]);
			LocalDateTime	tripEndDate		= DateTimeUtil.convertToLocalDateTime(tripVars[3]);
			Trip			trip			= new Trip(vehicleName, driverFirstName, driverLastName, tripStartDate,
			        tripEndDate);
			List<Trip>		trips			= vehicleTrips.get(vehicleName);
			if (trips == null) {
				trips = new ArrayList<Trip>();
				vehicleTrips.put(vehicleName, trips);
			}
			trips.add(trip);
		}
		return vehicleTrips;
	}

	// Load lytx behavior to map - Key=lytxBehaviorId, Value=BehaviorName
	public Map<Integer, String> loadLytxBehaviors(String lytxBehaviorsResponseJson) {
		Map<Integer, String>	lBehaviors			= new HashMap<Integer, String>();
		JSONObject				lytxBehaviorsJO		= new JSONObject(lytxBehaviorsResponseJson);
		JSONArray				lytxBehaviorsArray	= lytxBehaviorsJO.getJSONArray("behaviors");
		for (int i = 0; i < lytxBehaviorsArray.length(); i++) {
			JSONObject	behaviorJO		= lytxBehaviorsArray.getJSONObject(i);
			int			lytxBehaviorId	= behaviorJO.getInt("behaviorId");
			String		lytxDescription	= "L-" + behaviorJO.getString("description");
			lBehaviors.put(lytxBehaviorId, lytxDescription);
		}
//	        printLytxBehaviors(lBehaviors);
		return lBehaviors;
	}

	public Map<Long, String> loadLytxVehicleID_NameMap(String getVehicleResponseJson) {
		Map<Long, String>	lytxVehicleList		= new HashMap<Long, String>();
		JSONObject			lytxVehiclesJO		= new JSONObject(getVehicleResponseJson);
		JSONArray			lytxVehiclesArray	= lytxVehiclesJO.getJSONArray("vehicles");
		for (int i = 0; i < lytxVehiclesArray.length(); i++) {
			String	vehicleName	= lytxVehiclesArray.getJSONObject(i).getString("name");
			Long	vehicleId	= lytxVehiclesArray.getJSONObject(i).getLong("vehicleId");
			lytxVehicleList.put(vehicleId, vehicleName);
		}
		return lytxVehicleList;
	}

	public void printLytxBehaviors(Map<Integer, String> lBehaviors) {

		for (Map.Entry<Integer, String> lBehavior : lBehaviors.entrySet()) {
			System.out.println(lBehavior.getKey() + " | " + lBehavior.getValue());
		}

	}

	public List<String> loadReporColumntHeaders(String userName, String db) {
		List<String> reportColumnHeader = new ArrayList<String>();
		reportColumnHeader.add("Vehicle Name");
		reportColumnHeader.add("Group");
		reportColumnHeader.add("Distance");
		List<Behave> selectedRuleNames = userReportFilterService.getSelectedRuleNames(userName, db);
		for (Behave header : selectedRuleNames) {
			reportColumnHeader.add(header.getRuleName());
		}
		return reportColumnHeader;
	}

	// FOR TESTING ONLY: This method should make the actual call to Geotab and get
	// the exceptionSummariesJson
	// Guna todo: copy the request here (commented) to get the response below;
	public JsonObject getGeotabDriverExceptionSummariesResponseJson(String sdate, String edate, String geouname,
	        String groupvalue, String geodatabase, String geosees, String url, String enttype)
	        throws ParseException, MalformedURLException, IOException {

		List<GlRulelistEntity> getl = getgeodropdown(geouname, geodatabase);
		// ArrayList<String> getl = (ArrayList<String>)getgeodropdown;
		SimpleDateFormat	sdf		= new SimpleDateFormat("yyyy-MM-dd");
		String				sDate	= sdate;
		String				eDate	= edate;
		Date				ssdate	= sdf.parse(sDate);
		Date				eedate	= sdf.parse(eDate);
		String				gvalue	= "";
		for (int j = 0; j < getl.size(); j++) {
			if (j != getl.size() - 1) {
				gvalue = gvalue + "{\"id\":\"" + getl.get(j).getRulevalue() + "\"},";
			} else {
				gvalue = gvalue + "{\"id\":\"" + getl.get(j).getRulevalue() + "\"}";
			}
		}

		String	uri				= "https://" + url + "/apiv1";
		String	urlParameters	= "{\"method\":\"ExecuteMultiCall\",\"params\":{\"calls\":[{\"method\":\"GetReportData\",\"params\":{\"argument\":{\"runGroupLevel\":-1,\"isNoDrivingActivityHidden\":true,\"fromUtc\":\""
		        + sdate + "T01:00:00.000Z\",\"toUtc\":\"" + edate + "T03:59:59.000Z\",\"entityType\":\"" + enttype
		        + "\",\"reportArgumentType\":\"RiskManagement\",\"groups\":[" + groupvalue
		        + "],\"reportSubGroup\":\"None\",\"rules\":[" + gvalue
		        + "]}}},{\"method\":\"Get\",\"params\":{\"typeName\":\"SystemSettings\"}}],\"credentials\":{\"database\":\""
		        + geodatabase + "\",\"sessionId\":\"" + geosees + "\",\"userName\":\"" + geouname + "\"}}}";

		// System.out.println(uri+"-"+urlParameters);

		String				serverurl	= uri;
		HttpURLConnection	con			= (HttpURLConnection) (new URL(serverurl)).openConnection();
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
		InputStream		is			= con.getInputStream();
		BufferedReader	rd			= new BufferedReader(new InputStreamReader(is));
		StringBuilder	response	= new StringBuilder();
		String			line;
		while ((line = rd.readLine()) != null) {
			response.append(line);
			response.append('\r');
		}
		rd.close();
		JsonParser	parser	= new JsonParser();
		JsonObject	o		= parser.parse(response.toString()).getAsJsonObject();

		return o;
	}

	// FOR TESTING ONLY: This method should make the actual call to Geotab and get
	// the exceptionSummariesJson
	// Guna todo: copy the request here (commented) to get the response below;
	@Deprecated(forRemoval = true)
	private JsonObject getGeotabVehicleExceptionSummariesResponseJson(String sdate, String edate, String geouname,
	        String groupvalue, String geodatabase, String geosees, String url, String enttype)
	        throws ParseException, MalformedURLException, IOException {
		List<GlRulelistEntity> getl = getgeodropdown(geouname, geodatabase);
		
		String request = GeoTabRequestBuilder.getInstance()
		.method("ExecuteMultiCall")
		.params()
			.credentials()
				.database(geodatabase)
				.sessionId(geosees)
				.userName(geouname)
			.and()
			.addCalls()
				.method("GetReportData")
				.params()
					.argument()
						.runGroupLevel(-1)
						.isNoDrivingActivityHidden(true)
						.fromUtc(sdate)
						.toUtc(edate)
						.entityType("Device")
						.reportArgumentType("RiskManagement")
						.groups(Stream.of(groupvalue.split(",")).collect(Collectors.toList()))
						.reportSubGroup("None")
						.rules(getl.stream().map(GlRulelistEntity::getRulevalue).collect(Collectors.toList()))
						.and()
					.and()
				.done()
			.addCalls()
				.method("Get")
				.params()
					.typeName("SystemSettings").build();
		
		String	uri				= "https://" + url + "/apiv1";
		ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
		return ResponseUtil.parseResponse(response);
	}

	// FOR TESTING ONLY: This method should make the actual call to Lytx and get the
	// exceptionSummariesJson with startDate and EndDate as search parameters.
	// Guna todo: copy the request here (commented) to get the response below;
	public String getLytxExceptionSummariesResponseJson(String sdate, String edate, String lytxSess, String groupid,
	        String endpoint) throws ParseException, RemoteException {

		SimpleDateFormat					sdf				= new SimpleDateFormat("yyyy-MM-dd");
		String								sDate			= sdate;
		String								eDate			= edate;
		Date								ssdate			= sdf.parse(sDate);
		Date								eedate			= sdf.parse(eDate);
		ISubmissionServiceV5Proxy			dr				= new ISubmissionServiceV5Proxy(endpoint);
		GetEventsResponse					qr				= new GetEventsResponse();
		GetEventsByLastUpdateDateRequest	geteventbyid	= new GetEventsByLastUpdateDateRequest();
		geteventbyid.setSessionId(lytxSess);
		geteventbyid.setStartDate(ssdate);
		geteventbyid.setEndDate(eedate);
		if (!groupid.equalsIgnoreCase("null")) {
			geteventbyid.setGroupId(Long.valueOf(Long.parseLong(groupid)));
		}

		qr = dr.getEventsByLastUpdateDate(geteventbyid);

		JSONObject	jsonObject3							= new JSONObject(qr);
		String		lytxExceptionSummariesResponseJson	= toStringValue(jsonObject3);

		return lytxExceptionSummariesResponseJson;
	}

	// FOR TESTING ONLY: This method should make the actual call to (Lytx OR Geotab
	// ?) and get the vehicles and the vehicleIDs. Request search parameters are
	// .....
	// Guna todo: copy the request here (commented) to get the response below;
	@Deprecated
	public String getLytxVehicleID_NameResponseJson(String lytxSess, String endpoint) throws RemoteException {

		ISubmissionServiceV5Proxy	pr					= new ISubmissionServiceV5Proxy(endpoint);
		GetVehiclesResponse			lr					= new GetVehiclesResponse();
		GetVehiclesRequest			getVehiclesRequest	= new GetVehiclesRequest();
		getVehiclesRequest.setIncludeSubgroups(Boolean.valueOf(true));
		getVehiclesRequest.setSessionId(lytxSess);
		lr = pr.getVehicles(getVehiclesRequest);

		JSONObject jsonObject2 = new JSONObject(lr);

		String lytxVehicleID_NameResponseJson = toStringValue(jsonObject2);
		return lytxVehicleID_NameResponseJson;
	}

	// FOR TESTING ONLY: This method should make the actual call to Lytx and get the
	// Lytx behavious and their Ids.
	// Guna todo: copy the request here (commented) to get the response below;
	@Deprecated
	public String getLytxBehaviorsResponseJson(String lytxSess, String endpoint) throws RemoteException {
		ISubmissionServiceV5Proxy	er	= new ISubmissionServiceV5Proxy(endpoint);
		ExistingSessionRequest		re	= new ExistingSessionRequest();
		re.setSessionId(lytxSess);
		GetBehaviorsResponse getb = new GetBehaviorsResponse();
		getb = er.getBehaviors(re);

		JSONObject	jsonObject					= new JSONObject(getb);
		String		lytxBehaviorsResponseJson	= toStringValue(jsonObject);

		return lytxBehaviorsResponseJson.toString();
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

	/*
	 * returns the list of top n number of vehicles or drivers whose score is the
	 * least. param: top number of records needed.
	 */
	public void calculateTopBottomNRecords(String userName, String responce, Workbook workbook, String db) {
		reportRows = new ArrayList<ReportRow>();

		loadReportRowsFromJson(responce, userName, db);
		Map<String, Score>	scores				= new HashMap<String, Score>();
		List<Score>			zeroScores			= new ArrayList<Score>();
		List<Score>			topScores			= new ArrayList<Score>();
		List<Behave>		selectedRuleNames	= userReportFilterService.getSelectedRuleNames(userName, db);
		int					countOfRules		= selectedRuleNames.size();
		for (ReportRow row : reportRows) {
			Score score = new Score();
			score.setName(row.getName());
			Map<String, Double> weightedAvgScore = new LinkedHashMap<String, Double>();
			score.setWeightedAvgScores(weightedAvgScore);
			double totalWeightedAvgScores = 0d;
			for (Behave selectedRulesEntry : selectedRuleNames) {
				String	ruleName	= selectedRulesEntry.getRuleName();
				Integer	weight		= selectedRules.get(ruleName);
				Integer	ruleVal		= row.getSelectedRules().get(ruleName);
				if (ruleVal == null) {
					continue;
				}
				if (ruleVal > 0) {
					double weightedAvg = (double) (ruleVal * weight) / (minimumDistance * 100);
					score.getWeightedAvgScores().put(ruleName, weightedAvg);
					totalWeightedAvgScores += weightedAvg;
					score.setMeanWeightedAvgScores((double) (totalWeightedAvgScores / countOfRules));
				} else {
					score.getWeightedAvgScores().put(ruleName, 0d);
				}
			}
			if (score.getMeanWeightedAvgScores() == null) {
				score.setMeanWeightedAvgScores(0d);
			}

			if (score.getMeanWeightedAvgScores() == 0d) {
				score.setMeanWeightedAvgScores((double) row.getDistance());
				zeroScores.add(score);
			} else {
				topScores.add(score);
			}
		}

		int	topScoresCount	= topScores.size();
		int	zeroScoresCount	= zeroScores.size();

		System.out.println("zeroscores size = " + zeroScores.size());
		System.out.println("topscores size = " + topScores.size());

		topNRecords		= new ArrayList<Score>();
		bottomNRecords	= new ArrayList<Score>();
		if (zeroScoresCount > 0) {
			zeroScores.sort(Comparator.comparing(Score::getMeanWeightedAvgScores).reversed());
			for (int i = 0; i < zeroScoresCount && i < N_TOP_BOTTOM_RECORDS; i++) {
				topNRecords.add(zeroScores.get(i));
			}
		}

		topScores.sort(Comparator.comparing(Score::getMeanWeightedAvgScores));
		for (int i = 0; (i < topScoresCount) && (i < (N_TOP_BOTTOM_RECORDS - zeroScoresCount)); i++) {
			topNRecords.add(topScores.get(i));
		}
		// BOTTOM N RECORDS
		topScores.sort(Comparator.comparing(Score::getMeanWeightedAvgScores).reversed());
		for (int i = 0; (i < topScoresCount) && (i < (N_TOP_BOTTOM_RECORDS)); i++) {
			bottomNRecords.add(topScores.get(i));
		}
		/*
		 * //Print the top scores for(int i = 0; i < topNRecords.size(); i++) {
		 * System.out.println("Top " + (i+1)+" - " + topNRecords.get(i).getName() + ", "
		 * + topNRecords.get(i).getMeanWeightedAvgScores()); } //Print the bottom scores
		 * for(int i = 0; i < bottomNRecords.size(); i++) { System.out.println("Bottom "
		 * + (i+1)+" - " + bottomNRecords.get(i) + ", " +
		 * bottomNRecords.get(i).getMeanWeightedAvgScores()); }
		 */

		// Wrire To Excel

		Sheet reportNthRecord = workbook.getSheetAt(4);

		int startingNthrecord = 5;
		reportNthRecord.createRow(4);

		Row rowsNthRecord = reportNthRecord.getRow(4);
		rowsNthRecord.createCell(0);
		Cell cellsNthRecord = rowsNthRecord.getCell(0);
		cellsNthRecord.setCellValue("Top Ten Records");
		rowsNthRecord.createCell(1);

		cellsNthRecord = rowsNthRecord.getCell(1);
		cellsNthRecord.setCellValue("Value");
		rowsNthRecord.createCell(5);

		cellsNthRecord = rowsNthRecord.getCell(5);
		cellsNthRecord.setCellValue("Botton Ten Records");
		rowsNthRecord.createCell(6);

		cellsNthRecord = rowsNthRecord.getCell(6);
		cellsNthRecord.setCellValue("Value");

		for (int i = 0; i < topNRecords.size(); i++) {
			reportNthRecord.createRow(i + startingNthrecord);

			rowsNthRecord = reportNthRecord.getRow(i + startingNthrecord);
			rowsNthRecord.createCell(0);

			cellsNthRecord = rowsNthRecord.getCell(0);

			cellsNthRecord.setCellValue(topNRecords.get(i).getName());
			rowsNthRecord.createCell(1);

			cellsNthRecord = rowsNthRecord.getCell(1);

			cellsNthRecord.setCellValue(topNRecords.get(i).getMeanWeightedAvgScores());

		}

		// Print the bottom scores
		for (int i = 0; i < bottomNRecords.size(); i++) {

			rowsNthRecord = reportNthRecord.getRow(i + startingNthrecord);
			rowsNthRecord.createCell(5);

			cellsNthRecord = rowsNthRecord.getCell(5);

			cellsNthRecord.setCellValue(bottomNRecords.get(i).getName());

			rowsNthRecord.createCell(6);

			cellsNthRecord = rowsNthRecord.getCell(6);

			cellsNthRecord.setCellValue(bottomNRecords.get(i).getMeanWeightedAvgScores());

		}

	}

	public List<ReportRow> loadReportRowsFromJson(String reportDataJson, String userName, String db) {
		List<Behave>	ruleList		= userReportFilterService.getSelectedRuleNames(userName, db);
		JSONObject		reportDataJO	= new JSONObject(reportDataJson);
		JSONArray		reportDataArray	= reportDataJO.getJSONArray("information");
		for (int i = 0; i < reportDataArray.length(); i++) {
			JSONObject	reportRowJO	= reportDataArray.getJSONObject(i);
			ReportRow	reportRow	= new ReportRow();
			reportRow.setSelectedRules(new LinkedHashMap<String, Integer>());
			reportRow.setName(reportRowJO.getString("VehicleName"));
			reportRow.setGroup(reportRowJO.getString("Group"));
			reportRow.setDistance(Long.parseLong(reportRowJO.getString("Distance")));
			JSONArray behaviorArray = reportRowJO.getJSONArray("Behave");
			for (int j = 0; j < behaviorArray.length(); j++) {
				JSONObject behaveItem = behaviorArray.getJSONObject(j);
				reportRow.getSelectedRules().put(ruleList.get(j).getRuleName(), behaveItem.getInt("Rule"));
			}
			reportRows.add(reportRow);
		}
		return reportRows;
	}

	public List<String> loadReporColumntHeadersTrending(String userName, String db) {
		List<String> reportColumnHeader = new ArrayList<>();
		reportColumnHeader.add("VehicleName");
		reportColumnHeader.add("Group");
		reportColumnHeader.add("Distance");
		GlReportService		da		= new GlReportService();
		List<String>	gval	= new ArrayList();
		gval = da.getallbehave(userName, db);
		for (int j = 0; j < gval.size(); j++) {
			// System.out.println(j + "-----" + gval.get(j));
			reportColumnHeader.add(gval.get(j));
		}
		// System.out.println(reportColumnHeader.size() + "-----");
		return reportColumnHeader;
	}

	private Map<String, Map<String, String>> extractGeotabDriverData(String geotabDriverExceptionSummariesJson,
	        String userName, String db) {

		List<String> displayColumns = loadReporColumntHeadersTrending(userName, db);

		// create report object:
		Map<String, Map<String, String>> combinedReport = new LinkedHashMap<String, Map<String, String>>();
		// GEOTAB Events processing
		JSONObject	geotabEventsJO		= new JSONObject(geotabDriverExceptionSummariesJson);
		JSONArray	geotabEventsJOArray	= geotabEventsJO.getJSONArray("result");
		for (int i = 0; i < geotabEventsJOArray.length(); i++) {
			JSONObject	resultsChild	= geotabEventsJOArray.getJSONObject(i);
			JSONObject	itemJO			= resultsChild.getJSONObject("item");
			// driverName
			String				geotabVehicleName	= itemJO.getString("firstName") + " "
			        + itemJO.getString("lastName");
			Map<String, String>	newReportRow		= new LinkedHashMap<String, String>();	// getNewReportRow();

			newReportRow.put(displayColumns.get(0), geotabVehicleName);
			// group
			JSONArray	geotabDriverGroups	= itemJO.getJSONArray("driverGroups");
			String		group				= null;
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
			Map<String, Integer>	geotabExceptionEvents		= new HashMap<String, Integer>();
			JSONArray				geotabExceptionSummariesJA	= resultsChild.getJSONArray("exceptionSummaries");
			for (int k = 0; k < geotabExceptionSummariesJA.length(); k++) {

				int			eventCount				= geotabExceptionSummariesJA.getJSONObject(k).getInt("eventCount");
				JSONObject	geotabExceptionRuleJO	= geotabExceptionSummariesJA.getJSONObject(k)
				        .getJSONObject("exceptionRule");
				String		geotabExceptionName		= "G-" + geotabExceptionRuleJO.getString("name");
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

	private Map<String, Map<String, String>> extractGeotabVehicleData(String geotabVehicleExceptionSummariesJson,
	        String userName, String db) {
		Map<String, Map<String, String>> combinedReport = new LinkedHashMap<String, Map<String, String>>();

		List<String> displayColumns = loadReporColumntHeadersTrending(userName, db);

		// create report object:
		// GEOTAB Events processing
		JSONObject	geotabEventsJO		= new JSONObject(geotabVehicleExceptionSummariesJson);
		JSONArray	geotabEventsJOArray	= geotabEventsJO.getJSONArray("result");
		for (int i = 0; i < geotabEventsJOArray.length(); i++) {
			JSONObject	resultsChild	= geotabEventsJOArray.getJSONObject(i);
			JSONObject	itemJO			= resultsChild.getJSONObject("item");

			// vehicleName
			String				geotabVehicleName	= itemJO.getString("name");
			Map<String, String>	newReportRow		= new LinkedHashMap<String, String>();	// getNewReportRow();
			newReportRow.put(displayColumns.get(0), geotabVehicleName);
			// group
			JSONArray	geotabVehicleGroups	= itemJO.getJSONArray("groups");
			String		group				= null;
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
			Map<String, Integer>	geotabExceptionEvents		= new HashMap<String, Integer>();
			JSONArray				geotabExceptionSummariesJA	= resultsChild.getJSONArray("exceptionSummaries");
			for (int k = 0; k < geotabExceptionSummariesJA.length(); k++) {

				int			eventCount				= geotabExceptionSummariesJA.getJSONObject(k).getInt("eventCount");
				JSONObject	geotabExceptionRuleJO	= geotabExceptionSummariesJA.getJSONObject(k)
				        .getJSONObject("exceptionRule");
				String		geotabExceptionName		= "G-" + geotabExceptionRuleJO.getString("name");

				// System.out.println("-----guna"+geotabExceptionName);

				geotabExceptionEvents.put(geotabExceptionName,
				        geotabExceptionEvents.get(geotabExceptionName) == null ? eventCount
				                : geotabExceptionEvents.get(geotabExceptionName) + eventCount);

			}
			for (int m = 3; m < displayColumns.size(); m++) {
				if (geotabExceptionEvents.get(displayColumns.get(m)) != null) {
					System.out.println(displayColumns.get(m) + "----notnull");

					newReportRow.put(displayColumns.get(m),
					        (geotabExceptionEvents.get(displayColumns.get(m))).toString());
					System.out.println(displayColumns.get(m) + "----not111111null");

				} else {

					if (newReportRow.get(displayColumns.get(m)) == null) {

						newReportRow.put(displayColumns.get(m), "0");
					}
				}
			}

			System.out.println(geotabVehicleName + "----" + newReportRow);

			combinedReport.put(geotabVehicleName, newReportRow);
		}

		return combinedReport;
	}

	public String createExcelReport(String sdate, String edate, String geouname, String geodatabase, String url,
	        String filename, String templect) throws EncryptedDocumentException, IOException {
		String responseJson = "";

		responseJson = selectresponce(geouname, geodatabase);

		List<String> displayColumns = loadReporColumntHeaders(geouname, geodatabase);

		File	source	= new File(
		        "/usr/local/apache-tomcat-8.5.51/webapps/GL_Driver_Safety_Report_Template_" + templect + ".xlsx");
		File	dest	= new File("/usr/local/apache-tomcat-8.5.51/webapps/" + geodatabase + "/report/excel/as.xlsx");
		try {
			copyFileUsingStream(source, dest);
		} catch (IOException e3) {
			e3.printStackTrace();
		}
		Workbook	workbook	= WorkbookFactory
		        .create(new File("/usr/local/apache-tomcat-8.5.51/webapps/" + geodatabase + "/report/excel/as.xlsx"));
		Sheet		sheet		= workbook.getSheetAt(0);
		DateFormat	df			= new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Calendar	calobj		= Calendar.getInstance();
		for (int j3 = 0; j3 < 8; j3++) {
			String	name	= "";
			String	val		= "";
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
			Row		row		= sheet.getRow(j3);
			Cell	cell	= row.getCell(0);
			if (cell == null)
				cell = row.createCell(0);
			cell.setCellValue(name);
			cell = row.createCell(1);
			cell.setCellValue(val);
		}
		sheet.createRow(4);
		Row row2 = sheet.getRow(4);
		for (int h = 0; h < displayColumns.size(); h++) {
			Cell cell2 = row2.createCell(h);
			if (h == 0) {
				cell2.setCellValue("Weight");
			} else if (h == 1 || h == 2) {
				cell2.setCellValue("");
			} else {
				int D = 0;
				D = getwe(geouname, ((String) displayColumns.get(h)).toString().trim(), geodatabase);
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
		JSONObject	excelvd	= new JSONObject(responseJson);
		JSONArray	info	= excelvd.getJSONArray("information");
		int			s		= 0;
		for (s = 0; s < info.length(); s++) {
			sheet.createRow(s + 6);
			Row		row4	= sheet.getRow(s + 6);
			Cell	cell4	= row4.getCell(0);
			if (cell4 == null)
				cell4 = row4.createCell(0);
			cell4.setCellValue(info.getJSONObject(s).getString("VehicleName"));
			cell4 = row4.createCell(1);
			cell4.setCellValue(info.getJSONObject(s).getString("Group"));
			cell4 = row4.createCell(2);
			cell4.setCellValue(Integer.parseInt(info.getJSONObject(s).getString("Distance")));
			JSONArray behave = info.getJSONObject(s).getJSONArray("Behave");
			for (int h2 = 0; h2 < behave.length(); h2++) {
				cell4 = row4.createCell(h2 + 3);
				cell4.setCellValue(Integer.parseInt(behave.getJSONObject(h2).getString("Rule")));
			}
		}
		Sheet	report	= workbook.getSheetAt(1);
		Row		rows	= report.getRow(5);
		Cell	cells	= rows.getCell(2);
		float	min		= 0.0F;
		try {
			min = getminmiles(geouname, geodatabase);
		} catch (Exception exception) {
		}
		cells.setCellValue(min);
		int statrpoint = s + 7;

		String[] formulas = { "Data!A@", "Data!B@", "Data!C@", "IF(C#>$C$6,TRUE,FALSE)", "Data!E@", "Data!F@",
		        "Data!G@", "Data!H@", "Data!I@", "Data!J@", "Data!K@", "Data!L@", "Data!M@", "Data!N@",
		        "IFERROR((E#*E$6)/($C#/100),0)", "IFERROR((F#*F$6)/($C#/100),0)", "IFERROR((G#*G$6)/($C#/100),0)",
		        "IFERROR((H#*H$6)/($C#/100),0)", "IFERROR((I#*I$6)/($C#/100),0)", "IFERROR((J#*J$6)/($C#/100),0)",
		        "IFERROR((K#*K$6)/($C#/100),0)", "IFERROR((L#*L$6)/($C#/100),0)", "IFERROR((M#*M$6)/($C#/100),0)",
		        "IFERROR((N#*N$6)/($C#/100),0)", "AVERAGE(OFFSET($O#,0,0,1,$Y$5))" };
		// String[] formulas =
		// {"Data!A@","Data!B@","Data!C@","IF(C#>$C$6,TRUE,FALSE)","Data!E@","Data!F@","Data!G@","Data!H@","Data!I@","Data!J@","Data!K@","Data!L@","Data!M@","Data!N@","IFERROR((E#*E$6)/($C8/100),0)","IFERROR((F#*F$6)/($C8/100),0)","IFERROR((G#*G$6)/($C8/100),0)","IFERROR((H#*H$6)/($C8/100),0)","IFERROR((I#*I$6)/($C8/100),0)","IFERROR((J#*J$6)/($C8/100),0)","IFERROR((K#*K$6)/($C8/100),0)","IFERROR((L#*L$6)/($C8/100),0)","IFERROR((M#*M$6)/($C8/100),0)","IFERROR((N#*N$6)/($C8/100),0)","AVERAGE(OFFSET($O#,0,0,1,$Y$5))"};
		// String[] formulas =
		// {"Data!A@","Data!B@","Data!C@","IF(C#>$C$6,TRUE,FALSE)","Data!E@","Data!F@","Data!G@","Data!H@","Data!I@","Data!J@","Data!K@","Data!L@","Data!M@","Data!N@","Data!O@","Data!P@","Data!Q@","Data!R@","Data!S@","Data!T@","Data!U@","Data!V@","Data!W@","Data!X@","IFERROR((E#*E$6)/($C8/100),0)","IFERROR((F#*F$6)/($C8/100),0)","IFERROR((G#*G$6)/($C8/100),0)","IFERROR((H#*H$6)/($C8/100),0)","IFERROR((I#*I$6)/($C8/100),0)","IFERROR((J#*J$6)/($C8/100),0)","IFERROR((K#*K$6)/($C8/100),0)","IFERROR((L#*L$6)/($C8/100),0)","IFERROR((M#*M$6)/($C8/100),0)","IFERROR((N#*N$6)/($C8/100),0)","IFERROR((O#*O$6)/($C8/100),0)","IFERROR((P#*P$6)/($C8/100),0)","IFERROR((Q#*Q$6)/($C8/100),0)","IFERROR((R#*R$6)/($C8/100),0)","IFERROR((S#*S$6)/($C8/100),0)","IFERROR((T#*T$6)/($C8/100),0)","IFERROR((U#*U$6)/($C8/100),0)","IFERROR((V#*V$6)/($C8/100),0)","IFERROR((W#*W$6)/($C8/100),0)","IFERROR((X#*X$6)/($C8/100),0)","AVERAGE(OFFSET($Y#,0,0,1,$AS$5))"};
		updateFormulaForReport(report, FORMULA_START_ROW, s, ROW_OFFSET, formulas);
//				

		/*
		 * for (int i = statrpoint; i < Integer.parseInt(templect); i++) { try { Row row
		 * = report.getRow(i); report.removeRow(row); } catch (Exception exception) {} }
		 */

		String			newAllDataNamedRange	= "Report!$A$7:$Y$" + statrpoint;
		XSSFWorkbook	glDSRWorkbook			= (XSSFWorkbook) workbook;
		XSSFSheet		reportSheet				= glDSRWorkbook.getSheet("Report");
		XSSFName		allDataNamedRange		= glDSRWorkbook.getName("AllData");
		allDataNamedRange.setRefersToFormula(newAllDataNamedRange);

		/*
		 * try { dao.calculateTopBottomNRecords(geouname, responseJson,workbook); }catch
		 * (Exception e) { // TODO: handle exception }
		 */

		try (FileOutputStream outputStream = new FileOutputStream(
		        "/usr/local/apache-tomcat-8.5.51/webapps/" + geodatabase + "/report/excel/" + filename + ".xlsx")) {
			XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
			workbook.write(outputStream);
		}
		workbook.close();

		return "{\"url\":\"" + url + geodatabase + "/report/excel/" + filename + ".xlsx\"}";
	}

	public void copyFileUsingStream(File source, File dest) throws IOException {
		InputStream		is	= null;
		OutputStream	os	= null;

		is	= new FileInputStream(source);
		os	= new FileOutputStream(dest);
		byte[]	buffer	= new byte[1024];
		int		length;
		while ((length = is.read(buffer)) > 0) {
			os.write(buffer, 0, length);
		}
		is.close();
		os.close();

	}

	private void updateFormulaForReport(Sheet sheet, int startRow, int numberOfRows, int rowOffset,
	        String[] formulaList) {

		for (int i = startRow; i < startRow + numberOfRows; i++) {

			Row curRow = sheet.createRow(i);
			for (int col = 0; col < formulaList.length; col++) {
				Cell	newCell	= curRow.createCell(col);
				String	formula	= formulaList[col];
				formula	= formula.replace("@", Integer.toString(i));
				formula	= formula.replace("#", Integer.toString(i + 1));
				newCell.setCellFormula(formula);
			}
		}
	}

	private Map<String, Map<String, Integer>> extractExceptionDataFromLytxResponseforDriver(
	        String getLytxExceptionSummariesResponseJson, String lytxSess, String endpoint) throws RemoteException {
		// Load Lytx vehicle map with vehicleId and names

		lytxBehaviors	= null;
		vechilelytxlist	= null;
		if (vechilelytxlist == null) {
			vechilemap = new LinkedHashMap<Long, String>();
			ISubmissionServiceV5Proxy	er				= new ISubmissionServiceV5Proxy(endpoint);
			GetUsersResponse			vr				= new GetUsersResponse();
			GetUsersRequest				getusersrequest	= new GetUsersRequest();
			getusersrequest.setSessionId(lytxSess);
			vr = er.getUsers(getusersrequest);
			JSONObject jsonObject2 = new JSONObject(vr);

			vechilelytxlist = jsonObject2.toString();
			JSONObject lytxVechileJO = new JSONObject(vechilelytxlist);

			JSONArray lytxVechileArray = jsonObject2.getJSONArray("users");

			for (int i = 0; i < lytxVechileArray.length(); i++) {
				JSONObject lytxObjValue = lytxVechileArray.getJSONObject(i);

				// System.out.println(lytxObjValue.getLong("userId")+"-"+lytxObjValue.getString("firstName")+"-"+lytxObjValue.getString("lastName"));

				vechilemap.put(lytxObjValue.getLong("userId"),
				        lytxObjValue.getString("firstName") + " " + lytxObjValue.getString("lastName"));
			}

			// lytxVehicleList =
			// loadLytxVehicleID_NameMap(getLytxVehicleID_NameResponseJson(lytxSess,endpoint));
		}
		// Load Lytx Behaviours map
		if (lytxBehaviors == null) {
			lytxBehaviors	= new LinkedHashMap<Integer, String>();
			lytxBehaviors	= loadLytxBehaviors(getLytxBehaviorsResponseJson(lytxSess, endpoint));

		}
		// Process lytxExceptionSummariesJson
		lytxVehicleEventsRecord = new HashMap<String, Map<String, Integer>>();

		JSONObject	lytxEventsJO	= new JSONObject(getLytxExceptionSummariesResponseJson);
		JSONArray	lytxEventsArray	= lytxEventsJO.getJSONArray("events");
		for (int i = 0; i < lytxEventsArray.length(); i++) {
			Long	driverId	= lytxEventsArray.getJSONObject(i).getLong("driverId");
			String	vehicleName	= vechilemap.get(driverId);

			Map<String, Integer> lytxExceptionEvents = lytxVehicleEventsRecord.get(vehicleName);
			if (lytxExceptionEvents == null) {
				lytxExceptionEvents = new HashMap<String, Integer>();

				lytxVehicleEventsRecord.put(vehicleName, lytxExceptionEvents);

			}
			JSONArray lytxBehavioursArray = lytxEventsArray.getJSONObject(i).getJSONArray("behaviors");
			for (int j = 0; j < lytxBehavioursArray.length(); j++) {
				int		behavior		= lytxBehavioursArray.getJSONObject(j).getInt("behavior");
				String	exceptionName	= lytxBehaviors.get(behavior);
				Integer	behaviorCount	= lytxExceptionEvents.get(exceptionName);
				if (behaviorCount == null) {
					behaviorCount = 0;
				}
				lytxExceptionEvents.put(exceptionName, ++behaviorCount);
			}
		}
		return lytxVehicleEventsRecord;
	}

}
