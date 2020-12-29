// 
// Decompiled by Procyon v0.5.36
// 

package com.vibaps.merged.safetyreport.api.trending;

import java.time.Instant;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import com.vibaps.merged.safetyreport.dao.gl.Common_Geotab_DAO;
import java.io.FileNotFoundException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import java.text.DateFormat;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.OutputStream;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import java.io.FileOutputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import com.vibaps.merged.safetyreport.api.gl.RestDriverSafetyReport;
import java.io.File;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.text.ParseException;
import com.lytx.dto.GetEventsByLastUpdateDateRequest;
import com.lytx.dto.GetUsersRequest;
import com.lytx.dto.GetUsersResponse;

import org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api_GetEvents_V5.GetEventsResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.EncryptedDocumentException;
import java.util.Iterator;
import org.json.JSONArray;
import com.google.gson.JsonObject;
import java.io.InputStream;
import java.util.Date;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.net.HttpURLConnection;
import com.lytx.dto.GetVehiclesRequest;
import com.lytx.dto.GetVehiclesResponse;
import org.json.JSONObject;
import com.lytx.dto.GetBehaviorsResponse;
import com.lytx.dto.ExistingSessionRequest;
import com.lytx.services.ISubmissionServiceV5Proxy;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.RequestParam;
import com.vibaps.merged.safetyreport.dao.gl.GL_Report_DAO;
import com.vibaps.merged.safetyreport.entity.gl.Trip;
import java.util.List;
import java.util.Map;
import com.vibaps.merged.safetyreport.services.gl.GL_Report_SER;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping({ "/trendingreport" })
public class RestTrendingReport
{
    private static final int ROW_OFFSET = -1;
    private static final int FORMULA_START_ROW = 7;
    public final GL_Report_SER ser;
    static Map<Integer, String> lytxBehaviors;
    static Map<Integer, String[]> periods;
    static List<String> displayColumns;
    static String lytxExceptionSummariesJson;
    static int EXCEPTIONS_START_COLUMN;
    static String geotabVehicleExceptionSummariesJson;
    static Map<String, List<Trip>> vehicleTrips;
    static GL_Report_DAO dao;
    Map<Long, String> lytxVehicleList;
    static String vechilelytxlist; 
    static Map<Long,String> vechilemap;
    public RestTrendingReport() {
        this.ser = new GL_Report_SER();
    }
    
    @RequestMapping(value = { "/getTrendingReport" }, method = { RequestMethod.GET }, produces = { "application/json" })
    @ResponseBody
    public Object getReportGeo(@RequestParam final String groupid, @RequestParam String sdate, @RequestParam final String edate, @RequestParam final String sees, @RequestParam final String geosees, @RequestParam final String geotabgroups, @RequestParam final String userName, @RequestParam final String geodatabase, @RequestParam final String url, @RequestParam final String enttype, @RequestParam final String period, @RequestParam final String endpoint) throws EncryptedDocumentException, InvalidFormatException, IOException {
        String responseJson = "";
        Map<String, Map<String, Integer>> lytxVehicleEventsRecord = new HashMap<String, Map<String, Integer>>();
        String getVehicleResponseJson = "";
        final List<Integer> totals = new ArrayList<Integer>();
        final Object getgeodropdown = this.ser.getgeodropdown(userName,geodatabase);
        final ArrayList<String> getl = (ArrayList<String>)getgeodropdown;
        final String value = "";
        Map<String, Map<String, String>> combinedReport = new HashMap<String, Map<String, String>>();
        List<String> displayColumns = null;
        Map<Integer, String> lytxBehaviors = null;
        StringBuffer combinedReportResponseJson = new StringBuffer();
        try {
            String lytxBehaviorsJson = "";
            Date ssdate = null;
            Date eedate = null;
            if (!groupid.equalsIgnoreCase("0")) {
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                final String sDate = sdate;
                final String eDate = edate;
                ssdate = sdf.parse(sDate);
                eedate = sdf.parse(eDate);
                final ISubmissionServiceV5Proxy er = new ISubmissionServiceV5Proxy(endpoint);
                final ExistingSessionRequest re = new ExistingSessionRequest();
                re.setSessionId(sees);
                GetBehaviorsResponse getb = new GetBehaviorsResponse();
                getb = er.getBehaviors(re);
                final JSONObject jsonObject = new JSONObject((Object)getb);
                lytxBehaviorsJson = toStringValue(jsonObject);
                final ISubmissionServiceV5Proxy pr = new ISubmissionServiceV5Proxy(endpoint);
                GetVehiclesResponse lr = new GetVehiclesResponse();
                final GetVehiclesRequest getVehiclesRequest = new GetVehiclesRequest();
                getVehiclesRequest.setIncludeSubgroups(Boolean.valueOf(true));
                getVehiclesRequest.setSessionId(sees);
                lr = pr.getVehicles(getVehiclesRequest);
                final JSONObject jsonObject2 = new JSONObject((Object)lr);
                getVehicleResponseJson = toStringValue(jsonObject2);
            }
            String gvalue = "";
            for (int j = 0; j < getl.size(); ++j) {
                if (j != getl.size() - 1) {
                    gvalue = gvalue + "{\"id\":\"" + getl.get(j) + "\"},";
                }
                else {
                    gvalue = gvalue + "{\"id\":\"" + getl.get(j) + "\"}";
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
            final String uri = "https://" + url + "/apiv1";
            final String urlParameters = "{\"method\":\"ExecuteMultiCall\",\"params\":{\"calls\":[{\"method\":\"GetReportData\",\"params\":{\"argument\":{\"runGroupLevel\":-1,\"isNoDrivingActivityHidden\":true,\"fromUtc\":\"" + sdate + "T01:00:00.000Z\",\"toUtc\":\"" + edate + "T03:59:59.000Z\",\"entityType\":\"" + enttype + "\",\"reportArgumentType\":\"RiskManagement\",\"groups\":[" + groupvalue + "],\"reportSubGroup\":\"" + period + "\",\"rules\":[" + gvalue + "]}}},{\"method\":\"Get\",\"params\":{\"typeName\":\"SystemSettings\"}}],\"credentials\":{\"database\":\"" + geodatabase + "\",\"sessionId\":\"" + geosees + "\",\"userName\":\"" + userName + "\"}}}";
            final String serverurl = uri;
            final HttpURLConnection con = (HttpURLConnection)new URL(serverurl).openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", " application/json; charset=utf-8");
            con.setRequestProperty("Content-Language", "en-US");
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setDoInput(true);
            final DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            final InputStream is = con.getInputStream();
            final BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            final StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            final JsonParser parser = new JsonParser();
            final JsonObject o = parser.parse(response.toString()).getAsJsonObject();
            final String geotabDriverExceptionSummariesJson = o.toString();
            
   System.out.println("report"+geotabDriverExceptionSummariesJson);         
            
            final String startDateStr = sdate + "T01:00:00";
            final String endDateStr = edate + "T59:59:59";
            try {
                if (!groupid.equalsIgnoreCase("0")) {
                    final int EXCEPTIONS_START_COLUMN = 3;
                    this.lytxVehicleList = loadLytxVehicleIDNameMap(getVehicleResponseJson);
                    lytxBehaviors = loadLytxBehaviors(lytxBehaviorsJson);
                    final String[] lytxBehaviorsArray = new String[lytxBehaviors.size()];
                    final int bCount = 0;
                    if (enttype.equalsIgnoreCase("Driver")) {
                        try {
                            RestTrendingReport.vehicleTrips = loadVehicleTripsMap(enttype, userName, geodatabase, geosees, url, startDateStr, endDateStr);
                        }
                        catch (Exception ex) {}
                    }
                }
                final boolean trending = true;
                if (trending) {
                    displayColumns = loadTrendingReporColumntHeaders(userName, geodatabase);
                    if (enttype.equals("Driver")) {
                        combinedReport = extractGeotabDriverTrendingData(geotabDriverExceptionSummariesJson, userName, geodatabase);
                    }
                    else {
                        combinedReport = extractGeotabVehicleTrendingData(geotabDriverExceptionSummariesJson, userName, geodatabase);
                    }
                    RestTrendingReport.EXCEPTIONS_START_COLUMN = 6;
                }
                else if (enttype.equalsIgnoreCase("Driver")) {
                    System.out.println("COMBINED REPORT - DRIVER");
                    combinedReport = extractGeotabDriverData(geotabDriverExceptionSummariesJson);
                }
                else {
                    System.out.println("COMBINED REPORT - VEHICLE");
                    combinedReport = extractGeotabVehicleData(RestTrendingReport.geotabVehicleExceptionSummariesJson);
                }
                Date newStartDate = ssdate;
                int s = 0;
                if (!groupid.equalsIgnoreCase("0")) {
                	
                    while (true) {
                        System.out.println("check---" + s++);
                        try {
                            RestTrendingReport.lytxExceptionSummariesJson = this.sendLytxRequest(groupid, sdate, edate, sees, endpoint);
                            final JSONObject lytxEventsJO = new JSONObject(RestTrendingReport.lytxExceptionSummariesJson);
                            final JSONArray lytxEventsArray = lytxEventsJO.getJSONArray("events");
                            if (enttype.equals("Driver")) 
                            {
                            	System.out.println("Driver");
                            lytxVehicleEventsRecord = extractExceptionDataFromLytxResponseDriver(endpoint,sees,lytxEventsArray, this.lytxVehicleList, trending, ssdate, eedate, lytxBehaviorsJson);
                            }
                            else
                            {
                                lytxVehicleEventsRecord = extractExceptionDataFromLytxResponse(lytxEventsArray, this.lytxVehicleList, trending, ssdate, eedate, lytxBehaviorsJson);
	
                            }
                            if (lytxEventsJO.has("queryCutoff")) {
                                final String cutoffData = lytxEventsJO.getString("queryCutoff");
                                System.out.println(cutoffData);
                                if (cutoffData != null) {
                                    newStartDate = getDateFromMilliSeconds(cutoffData);
                                    final String year = newStartDate.getYear() + 1900 + "";
                                    String month = newStartDate.getMonth() + 1 + "";
                                    String date = newStartDate.getDate() + "";
                                    if (month.length() == 1) {
                                        month = "0" + month;
                                    }
                                    if (date.length() == 1) {
                                        date = "0" + date;
                                    }
                                    final String strNewDate = year + "-" + month + "-" + date;
                                    if (!strNewDate.equals(sdate)) {
                                        sdate = strNewDate;
                                        continue;
                                    }
                                }
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    updatedCombinedReportWithLytxExceptions(combinedReport, lytxVehicleEventsRecord);
                }
                for (int q = 0; q < displayColumns.size(); ++q) {
                    totals.add(0);
                }
                combinedReportResponseJson = new StringBuffer();
                combinedReportResponseJson.append("\"information\": [");
                boolean firstRow = true;
                final int rulesRecords = displayColumns.size() - 3;
                for (final Map.Entry<String, Map<String, String>> combinedReportRows : combinedReport.entrySet()) {
                    if (!firstRow) {
                        combinedReportResponseJson.append(",");
                    }
                    else {
                        firstRow = false;
                    }
                    combinedReportResponseJson.append("{");
                    boolean rulesHeadedAdded = false;
                    int headerCount = 0;
                    int rowCount = 0;
                    final Map<String, String> rowData = combinedReportRows.getValue();
                    for (final Map.Entry<String, String> data : rowData.entrySet()) {
                        if (headerCount++ > 0 && headerCount < displayColumns.size() + 1) {
                            combinedReportResponseJson.append(",");
                        }
                        if (rowCount++ < RestTrendingReport.EXCEPTIONS_START_COLUMN) {
                            rulesHeadedAdded = false;
                            combinedReportResponseJson.append("\"" + data.getKey() + "\": \"" + data.getValue() + "\"");
                        }
                        else {
                            if (!rulesHeadedAdded) {
                                combinedReportResponseJson.append("\"Behave\": [");
                                rulesHeadedAdded = true;
                            }
                            combinedReportResponseJson.append("{");
                            combinedReportResponseJson.append("\"Rule\": \"" + data.getValue() + "\"}");
                            totals.set(rowCount - 1, totals.get(rowCount - 1) + Integer.parseInt(data.getValue()));
                            if (rowCount != displayColumns.size()) {
                                continue;
                            }
                            combinedReportResponseJson.append("]");
                        }
                    }
                    combinedReportResponseJson.append("}");
                }
                combinedReportResponseJson.append("]}");
                final StringBuffer totalsJson = new StringBuffer();
                totalsJson.append("{\"totals\": [");
                int ruleCounter = 0;
                for (final int totalVal : totals) {
                    totalsJson.append("{ \"Rule\": \"" + totalVal + "\" }");
                    if (++ruleCounter != displayColumns.size()) {
                        totalsJson.append(",");
                    }
                }
                totalsJson.append("],");
                responseJson = totalsJson.toString() + combinedReportResponseJson.toString();
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        catch (Exception ex2) {}
        try {
            this.ser.updateresponce(userName, responseJson, geodatabase);
        }
        catch (Exception ex3) {}
        return responseJson;
    }
    
    @RequestMapping(value = { "/getTrendingReportNonLytx" }, method = { RequestMethod.GET }, produces = { "application/json" })
    @ResponseBody
    public Object getReportGeoLytx(@RequestParam final String sdate, @RequestParam final String edate, @RequestParam final String geosees, @RequestParam final String geotabgroups, @RequestParam final String userName, @RequestParam final String geodatabase, @RequestParam final String url, @RequestParam final String enttype, final String period) throws EncryptedDocumentException, InvalidFormatException, IOException {
        String responseJson = "";
        final Map<String, Map<String, Integer>> lytxVehicleEventsRecord = new HashMap<String, Map<String, Integer>>();
        final String getVehicleResponseJson = "";
        final List<Integer> totals = new ArrayList<Integer>();
        final Object getgeodropdown = this.ser.getgeodropdown(userName,geodatabase);
        final ArrayList<String> getl = (ArrayList<String>)getgeodropdown;
        final String value = "";
        Map<String, Map<String, String>> combinedReport = new HashMap<String, Map<String, String>>();
        List<String> displayColumns = null;
        final Map<Integer, String> lytxBehaviors = null;
        StringBuffer combinedReportResponseJson = new StringBuffer();
        try {
            final String lytxBehaviorsJson = "";
            final Date ssdate = null;
            final Date eedate = null;
            String gvalue = "";
            for (int j = 0; j < getl.size(); ++j) {
                if (j != getl.size() - 1) {
                    gvalue = gvalue + "{\"id\":\"" + getl.get(j) + "\"},";
                }
                else {
                    gvalue = gvalue + "{\"id\":\"" + getl.get(j) + "\"}";
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
            final String uri = "https://" + url + "/apiv1";
            final String urlParameters = "{\"method\":\"ExecuteMultiCall\",\"params\":{\"calls\":[{\"method\":\"GetReportData\",\"params\":{\"argument\":{\"runGroupLevel\":-1,\"isNoDrivingActivityHidden\":true,\"fromUtc\":\"" + sdate + "T01:00:00.000Z\",\"toUtc\":\"" + edate + "T03:59:59.000Z\",\"entityType\":\"" + enttype + "\",\"reportArgumentType\":\"RiskManagement\",\"groups\":[" + groupvalue + "],\"reportSubGroup\":\"" + period + "\",\"rules\":[" + gvalue + "]}}},{\"method\":\"Get\",\"params\":{\"typeName\":\"SystemSettings\"}}],\"credentials\":{\"database\":\"" + geodatabase + "\",\"sessionId\":\"" + geosees + "\",\"userName\":\"" + userName + "\"}}}";
            final String serverurl = uri;
            final HttpURLConnection con = (HttpURLConnection)new URL(serverurl).openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", " application/json; charset=utf-8");
            con.setRequestProperty("Content-Language", "en-US");
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setDoInput(true);
            final DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            final InputStream is = con.getInputStream();
            final BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            final StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            final JsonParser parser = new JsonParser();
            final JsonObject o = parser.parse(response.toString()).getAsJsonObject();
            final String geotabDriverExceptionSummariesJson = o.toString();
            final String startDateStr = sdate + "T01:00:00";
            final String endDateStr = edate + "T59:59:59";
            try {
                final boolean trending = true;
                if (trending) {
                    displayColumns = loadTrendingReporColumntHeaders(userName, geodatabase);
                    if (enttype.equals("Driver")) {
                        combinedReport = extractGeotabDriverTrendingData(geotabDriverExceptionSummariesJson, userName, geodatabase);
                    }
                    else {
                        combinedReport = extractGeotabVehicleTrendingData(geotabDriverExceptionSummariesJson, userName, geodatabase);
                    }
                    RestTrendingReport.EXCEPTIONS_START_COLUMN = 6;
                }
                else if (enttype.equalsIgnoreCase("Driver")) {
                    System.out.println("COMBINED REPORT - DRIVER");
                    combinedReport = extractGeotabDriverData(geotabDriverExceptionSummariesJson);
                }
                else {
                    System.out.println("COMBINED REPORT - VEHICLE");
                    combinedReport = extractGeotabVehicleData(RestTrendingReport.geotabVehicleExceptionSummariesJson);
                }
                final Date newStartDate = ssdate;
                final int s = 0;
                for (int q = 0; q < displayColumns.size(); ++q) {
                    totals.add(0);
                }
                combinedReportResponseJson = new StringBuffer();
                combinedReportResponseJson.append("\"information\": [");
                boolean firstRow = true;
                final int rulesRecords = displayColumns.size() - 3;
                for (final Map.Entry<String, Map<String, String>> combinedReportRows : combinedReport.entrySet()) {
                    if (!firstRow) {
                        combinedReportResponseJson.append(",");
                    }
                    else {
                        firstRow = false;
                    }
                    combinedReportResponseJson.append("{");
                    boolean rulesHeadedAdded = false;
                    int headerCount = 0;
                    int rowCount = 0;
                    final Map<String, String> rowData = combinedReportRows.getValue();
                    for (final Map.Entry<String, String> data : rowData.entrySet()) {
                        if (headerCount++ > 0 && headerCount < displayColumns.size() + 1) {
                            combinedReportResponseJson.append(",");
                        }
                        if (rowCount++ < RestTrendingReport.EXCEPTIONS_START_COLUMN) {
                            rulesHeadedAdded = false;
                            combinedReportResponseJson.append("\"" + data.getKey() + "\": \"" + data.getValue() + "\"");
                        }
                        else {
                            if (!rulesHeadedAdded) {
                                combinedReportResponseJson.append("\"Behave\": [");
                                rulesHeadedAdded = true;
                            }
                            combinedReportResponseJson.append("{");
                            combinedReportResponseJson.append("\"Rule\": \"" + data.getValue() + "\"}");
                            totals.set(rowCount - 1, totals.get(rowCount - 1) + Integer.parseInt(data.getValue()));
                            if (rowCount != displayColumns.size()) {
                                continue;
                            }
                            combinedReportResponseJson.append("]");
                        }
                    }
                    combinedReportResponseJson.append("}");
                }
                combinedReportResponseJson.append("]}");
                final StringBuffer totalsJson = new StringBuffer();
                totalsJson.append("{\"totals\": [");
                int ruleCounter = 0;
                for (final int totalVal : totals) {
                    totalsJson.append("{ \"Rule\": \"" + totalVal + "\" }");
                    if (++ruleCounter != displayColumns.size()) {
                        totalsJson.append(",");
                    }
                }
                totalsJson.append("],");
                responseJson = totalsJson.toString() + combinedReportResponseJson.toString();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception ex) {}
        try {
            this.ser.updateresponce(userName, responseJson, geodatabase);
        }
        catch (Exception ex2) {}
        return responseJson;
    }
    
    private String sendLytxRequest(final String groupid, final String sdate, final String edate, final String sees, final String endpoint) throws ParseException {
        System.out.println(sdate + "---" + edate);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String sDate = sdate;
        final String eDate = edate;
        final Date ssdate = sdf.parse(sDate);
        final Date eedate = sdf.parse(eDate);
        final ISubmissionServiceV5Proxy dr = new ISubmissionServiceV5Proxy(endpoint);
        GetEventsResponse qr = new GetEventsResponse();
        final GetEventsByLastUpdateDateRequest geteventbyid = new GetEventsByLastUpdateDateRequest();
        geteventbyid.setSessionId(sees);
        geteventbyid.setStartDate(ssdate);
        geteventbyid.setEndDate(eedate);
        if (!groupid.equalsIgnoreCase("null")) {
            geteventbyid.setGroupId(Long.valueOf(Long.parseLong(groupid)));
        }
        try {
            qr = dr.getEventsByLastUpdateDate(geteventbyid);
        }
        catch (Exception e) {
            System.out.println(e);
        }
        final JSONObject jsonObject3 = new JSONObject((Object)qr);
        return RestTrendingReport.lytxExceptionSummariesJson = toStringValue(jsonObject3);
    }
    
    private static void updatedCombinedReportWithLytxExceptions(final Map<String, Map<String, String>> combinedReport, final Map<String, Map<String, Integer>> lytxVehicleEventsRecord) {
        for (final Map.Entry<String, Map<String, Integer>> lytxVehiclesEventsMapEntry : lytxVehicleEventsRecord.entrySet()) {
            final String lytxVehicleName = lytxVehiclesEventsMapEntry.getKey();
            final Map<String, String> reportHeader = combinedReport.get(lytxVehicleName);
            if (reportHeader == null) {
                continue;
            }
            final Map<String, Integer> lytxVehExceptions = lytxVehiclesEventsMapEntry.getValue();
            for (int m = 3; m < RestTrendingReport.displayColumns.size(); ++m) {
                if (lytxVehExceptions.get(RestTrendingReport.displayColumns.get(m)) != null) {
                    reportHeader.put(RestTrendingReport.displayColumns.get(m), lytxVehExceptions.get(RestTrendingReport.displayColumns.get(m)).toString());
                }
            }
        }
    }
    
    private static Map<String, Map<String, Integer>> extractExceptionDataFromLytxResponse(final JSONArray lytxExceptionSummariesJson, final Map<Long, String> lytxVehicleList, final boolean trending, final Date startDate, final Date endDate, final String lytxBehaviorsJson) {
        final Map<String, Map<String, Integer>> lytxVehicleEventsRecord = new HashMap<String, Map<String, Integer>>();
        RestTrendingReport.lytxBehaviors = loadLytxBehaviors(lytxBehaviorsJson);
        for (int i = 0; i < lytxExceptionSummariesJson.length(); ++i) {
            final Long eventsVehicleId = lytxExceptionSummariesJson.getJSONObject(i).getLong("vehicleId");
            final String vehicleName = lytxVehicleList.get(eventsVehicleId);
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
                    final Integer periodNumber = getPeriodNumberForDate(recordDate);
                    key = periodNumber + "|" + key;
                }
                lytxVehicleEventsRecord.put(key, lytxExceptionEvents);
            }
            final JSONArray lytxBehavioursArray = lytxExceptionSummariesJson.getJSONObject(i).getJSONArray("behaviors");
            for (int j = 0; j < lytxBehavioursArray.length(); ++j) {
                final int behavior = lytxBehavioursArray.getJSONObject(j).getInt("behavior");
                final String exceptionName = RestTrendingReport.lytxBehaviors.get(behavior);
                Integer behaviorCount = lytxExceptionEvents.get(exceptionName);
                if (behaviorCount == null) {
                    behaviorCount = 0;
                }
                lytxExceptionEvents.put(exceptionName, ++behaviorCount);
            }
        }
        return lytxVehicleEventsRecord;
    }
    
    private static Map<String, Map<String, Integer>> extractExceptionDataFromLytxResponseDriver(String endpoint,String lytxSess,final JSONArray lytxExceptionSummariesJson, final Map<Long, String> lytxVehicleList, final boolean trending, final Date startDate, final Date endDate, final String lytxBehaviorsJson) throws RemoteException {
        final Map<String, Map<String, Integer>> lytxVehicleEventsRecord = new HashMap<String, Map<String, Integer>>();
        RestTrendingReport.lytxBehaviors = loadLytxBehaviors(lytxBehaviorsJson);
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
            final Long eventsVehicleId = lytxExceptionSummariesJson.getJSONObject(i).getLong("driverId");
            final String vehicleName = vechilemap.get(eventsVehicleId);
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
                    final Integer periodNumber = getPeriodNumberForDate(recordDate);
                    key = periodNumber + "|" + key;
                }
                lytxVehicleEventsRecord.put(key, lytxExceptionEvents);
            }
            final JSONArray lytxBehavioursArray = lytxExceptionSummariesJson.getJSONObject(i).getJSONArray("behaviors");
            for (int j = 0; j < lytxBehavioursArray.length(); ++j) {
                final int behavior = lytxBehavioursArray.getJSONObject(j).getInt("behavior");
                final String exceptionName = RestTrendingReport.lytxBehaviors.get(behavior);
                Integer behaviorCount = lytxExceptionEvents.get(exceptionName);
                if (behaviorCount == null) {
                    behaviorCount = 0;
                }
                lytxExceptionEvents.put(exceptionName, ++behaviorCount);
            }
        }
        return lytxVehicleEventsRecord;
    }
    
    public static Integer getPeriodNumberForDate(final Date date) {
        for (final Map.Entry<Integer, String[]> entry : RestTrendingReport.periods.entrySet()) {
            final Integer key = entry.getKey();
            final String[] value = entry.getValue();
            final Date startDate = getDate(value[0]);
            final Date endDate = getDate(value[1]);
            if (!date.before(startDate) && !date.after(endDate)) {
                return key;
            }
        }
        return null;
    }
    
    private static Map<String, Map<String, String>> extractGeotabVehicleTrendingData(final String geotabTrendingReportResponseJson, final String userName, final String db) {
        RestTrendingReport.displayColumns = loadTrendingReporColumntHeaders(userName, db);
        final Map<String, Map<String, String>> combinedReport = new LinkedHashMap<String, Map<String, String>>();
        final JSONObject geotabEventsJO = new JSONObject(geotabTrendingReportResponseJson);
        final JSONArray geotabEventsJOArray = geotabEventsJO.getJSONArray("result");
        final JSONArray resultChildArray = geotabEventsJOArray.getJSONArray(0);
        for (int j = 0; j < resultChildArray.length(); ++j) {
            final JSONObject exceptionSummary = resultChildArray.getJSONObject(j);
            final Integer periodNumber = exceptionSummary.getInt("periodNumber");
            final String periodStartDate = exceptionSummary.getString("periodStartDate");
            final String periodEndDate = exceptionSummary.getString("periodEndDate");
            if (RestTrendingReport.periods.get(periodNumber) == null) {
                final String[] periodRange = { periodStartDate, periodEndDate };
                RestTrendingReport.periods.put(periodNumber, periodRange);
            }
            final JSONObject itemJO = exceptionSummary.getJSONObject("item");
            final String geotabVehicleName = itemJO.getString("name");
            final Map<String, String> newReportRow = new LinkedHashMap<String, String>();
            newReportRow.put(RestTrendingReport.displayColumns.get(0), geotabVehicleName);
            final JSONArray geotabVehicleGroups = itemJO.getJSONArray("groups");
            String group = null;
            for (int k = 0; k < geotabVehicleGroups.length(); ++k) {
                if (group == null) {
                    group = geotabVehicleGroups.getJSONObject(k).getString("name");
                }
                else {
                    final String newGroup = geotabVehicleGroups.getJSONObject(k).getString("name");
                    if ("Prohibit Idling".equalsIgnoreCase(newGroup)) {
                        group = newGroup + ", " + group;
                    }
                    else {
                        group = group + ", " + newGroup;
                    }
                }
            }
            newReportRow.put(RestTrendingReport.displayColumns.get(1), group);
            final Object geotabVehicleTotalDistance = exceptionSummary.get("totalDistance");
            final long tDistance = ((Double)geotabVehicleTotalDistance).longValue();
            newReportRow.put(RestTrendingReport.displayColumns.get(2), Long.toString(tDistance));
            newReportRow.put(RestTrendingReport.displayColumns.get(3), Integer.toString(periodNumber));
            newReportRow.put(RestTrendingReport.displayColumns.get(4), periodStartDate);
            newReportRow.put(RestTrendingReport.displayColumns.get(5), periodEndDate);
            final Map<String, Integer> geotabExceptionEvents = new HashMap<String, Integer>();
            final JSONArray geotabExceptionSummariesJA = exceptionSummary.getJSONArray("exceptionSummaries");
            for (int m = 0; m < geotabExceptionSummariesJA.length(); ++m) {
                if (!geotabExceptionSummariesJA.isNull(m)) {
                    final int eventCount = geotabExceptionSummariesJA.getJSONObject(m).getInt("eventCount");
                    final JSONObject geotabExceptionRuleJO = geotabExceptionSummariesJA.getJSONObject(m).getJSONObject("exceptionRule");
                    final String geotabExceptionName = "G-" + geotabExceptionRuleJO.getString("name");
                    geotabExceptionEvents.put(geotabExceptionName, (geotabExceptionEvents.get(geotabExceptionName) == null) ? eventCount : (geotabExceptionEvents.get(geotabExceptionName) + eventCount));
                }
            }
            for (int n = 3; n < RestTrendingReport.displayColumns.size(); ++n) {
                if (geotabExceptionEvents.get(RestTrendingReport.displayColumns.get(n)) != null) {
                    newReportRow.put(RestTrendingReport.displayColumns.get(n), geotabExceptionEvents.get(RestTrendingReport.displayColumns.get(n)).toString());
                }
                else if (newReportRow.get(RestTrendingReport.displayColumns.get(n)) == null) {
                    newReportRow.put(RestTrendingReport.displayColumns.get(n), "0");
                }
            }
            final String key = periodNumber + "|" + geotabVehicleName;
            combinedReport.put(key, newReportRow);
        }
        return combinedReport;
    }
    
    private static Map<String, Map<String, String>> extractGeotabVehicleData(final String geotabVehicleExceptionSummariesJson) {
        System.out.println("test" + geotabVehicleExceptionSummariesJson);
        final Map<String, Map<String, String>> combinedReport = new LinkedHashMap<String, Map<String, String>>();
        final JSONObject geotabEventsJO = new JSONObject(geotabVehicleExceptionSummariesJson);
        final JSONArray geotabEventsJOArray = geotabEventsJO.getJSONArray("results");
        for (int i = 0; i < geotabEventsJOArray.length(); ++i) {
            final JSONObject resultsChild = geotabEventsJOArray.getJSONObject(i);
            final JSONObject itemJO = resultsChild.getJSONObject("item");
            final String geotabVehicleName = itemJO.getString("name");
            final Map<String, String> newReportRow = new LinkedHashMap<String, String>();
            newReportRow.put(RestTrendingReport.displayColumns.get(0), geotabVehicleName);
            final JSONArray geotabVehicleGroups = itemJO.getJSONArray("groups");
            String group = null;
            for (int j = 0; j < geotabVehicleGroups.length(); ++j) {
                if (group == null) {
                    group = geotabVehicleGroups.getJSONObject(j).getString("name");
                }
                else {
                    final String newGroup = geotabVehicleGroups.getJSONObject(j).getString("name");
                    if ("Prohibit Idling".equalsIgnoreCase(newGroup)) {
                        group = newGroup + ", " + group;
                    }
                    else {
                        group = group + ", " + newGroup;
                    }
                }
            }
            newReportRow.put(RestTrendingReport.displayColumns.get(1), group);
            final Object geotabVehicleTotalDistance = resultsChild.get("totalDistance");
            final long tDistance = ((Double)geotabVehicleTotalDistance).longValue();
            newReportRow.put(RestTrendingReport.displayColumns.get(2), Long.toString(tDistance));
            final Map<String, Integer> geotabExceptionEvents = new HashMap<String, Integer>();
            final JSONArray geotabExceptionSummariesJA = resultsChild.getJSONArray("exceptionSummaries");
            for (int k = 0; k < geotabExceptionSummariesJA.length(); ++k) {
                final int eventCount = geotabExceptionSummariesJA.getJSONObject(k).getInt("eventCount");
                final JSONObject geotabExceptionRuleJO = geotabExceptionSummariesJA.getJSONObject(k).getJSONObject("exceptionRule");
                final String geotabExceptionName = "G-" + geotabExceptionRuleJO.getString("name");
                geotabExceptionEvents.put(geotabExceptionName, (geotabExceptionEvents.get(geotabExceptionName) == null) ? eventCount : (geotabExceptionEvents.get(geotabExceptionName) + eventCount));
            }
            for (int m = 3; m < RestTrendingReport.displayColumns.size(); ++m) {
                if (geotabExceptionEvents.get(RestTrendingReport.displayColumns.get(m)) != null) {
                    newReportRow.put(RestTrendingReport.displayColumns.get(m), geotabExceptionEvents.get(RestTrendingReport.displayColumns.get(m)).toString());
                }
                else if (newReportRow.get(RestTrendingReport.displayColumns.get(m)) == null) {
                    newReportRow.put(RestTrendingReport.displayColumns.get(m), "0");
                }
            }
            combinedReport.put(geotabVehicleName, newReportRow);
        }
        return combinedReport;
    }
    
    private static Map<String, Map<String, String>> extractGeotabDriverData(final String geotabDriverExceptionSummariesJson) {
        final Map<String, Map<String, String>> combinedReport = new LinkedHashMap<String, Map<String, String>>();
        final JSONObject geotabEventsJO = new JSONObject(geotabDriverExceptionSummariesJson);
        final JSONArray geotabEventsJOArray = geotabEventsJO.getJSONArray("result");
        for (int i = 0; i < geotabEventsJOArray.length(); ++i) {
            final JSONObject resultsChild = geotabEventsJOArray.getJSONObject(i);
            final JSONObject itemJO = resultsChild.getJSONObject("item");
            final String geotabVehicleName = itemJO.getString("firstName") + " " + itemJO.getString("lastName");
            final Map<String, String> newReportRow = new LinkedHashMap<String, String>();
            newReportRow.put(RestTrendingReport.displayColumns.get(0), geotabVehicleName);
            final JSONArray geotabDriverGroups = itemJO.getJSONArray("driverGroups");
            String group = null;
            for (int j = 0; j < geotabDriverGroups.length(); ++j) {
                if (group == null) {
                    group = geotabDriverGroups.getJSONObject(j).getString("name");
                }
                else {
                    group = group + ", " + geotabDriverGroups.getJSONObject(j).getString("name");
                }
            }
            newReportRow.put(RestTrendingReport.displayColumns.get(1), group);
            final Object geotabVehicleTotalDistance = resultsChild.get("totalDistance");
            final long tDistance = ((Double)geotabVehicleTotalDistance).longValue();
            newReportRow.put(RestTrendingReport.displayColumns.get(2), Long.toString(tDistance));
            final Map<String, Integer> geotabExceptionEvents = new HashMap<String, Integer>();
            final JSONArray geotabExceptionSummariesJA = resultsChild.getJSONArray("exceptionSummaries");
            for (int k = 0; k < geotabExceptionSummariesJA.length(); ++k) {
                final int eventCount = geotabExceptionSummariesJA.getJSONObject(k).getInt("eventCount");
                final JSONObject geotabExceptionRuleJO = geotabExceptionSummariesJA.getJSONObject(k).getJSONObject("exceptionRule");
                final String geotabExceptionName = "G-" + geotabExceptionRuleJO.getString("name");
                geotabExceptionEvents.put(geotabExceptionName, eventCount);
            }
            for (int m = 3; m < RestTrendingReport.displayColumns.size(); ++m) {
                if (geotabExceptionEvents.get(RestTrendingReport.displayColumns.get(m)) != null) {
                    newReportRow.put(RestTrendingReport.displayColumns.get(m), geotabExceptionEvents.get(RestTrendingReport.displayColumns.get(m)).toString());
                }
                else if (newReportRow.get(RestTrendingReport.displayColumns.get(m)) == null) {
                    newReportRow.put(RestTrendingReport.displayColumns.get(m), "0");
                }
            }
            combinedReport.put(geotabVehicleName, newReportRow);
        }
        return combinedReport;
    }
    
    private static Map<String, Map<String, String>> extractGeotabDriverTrendingData(final String geotabDriverTrendingReportResponseJson, final String userName, final String db) {
        RestTrendingReport.displayColumns = loadTrendingReporColumntHeaders(userName, db);
        final Map<String, Map<String, String>> combinedReport = new LinkedHashMap<String, Map<String, String>>();
        final JSONObject geotabEventsJO = new JSONObject(geotabDriverTrendingReportResponseJson);
        final JSONArray geotabEventsJOArray = geotabEventsJO.getJSONArray("result");
        final JSONArray resultChildArray = geotabEventsJOArray.getJSONArray(0);
        for (int i = 0; i < resultChildArray.length(); ++i) {
            final JSONObject resultsChild = resultChildArray.getJSONObject(i);
            final Integer periodNumber = resultsChild.getInt("periodNumber");
            final String periodStartDate = resultsChild.getString("periodStartDate");
            final String periodEndDate = resultsChild.getString("periodEndDate");
            if (RestTrendingReport.periods.get(periodNumber) == null) {
                final String[] periodRange = { periodStartDate, periodEndDate };
                RestTrendingReport.periods.put(periodNumber, periodRange);
            }
            System.out.print("exceptionSummary - Period Number: " + periodNumber + ", Period Start: " + periodStartDate + ", Period End: " + periodEndDate + " - ");
            final JSONObject itemJO = resultsChild.getJSONObject("item");
            final String geotabVehicleName = itemJO.getString("firstName") + " " + itemJO.getString("lastName");
            final Map<String, String> newReportRow = new LinkedHashMap<String, String>();
            newReportRow.put(RestTrendingReport.displayColumns.get(0), geotabVehicleName);
            final JSONArray geotabDriverGroups = itemJO.getJSONArray("driverGroups");
            String group = null;
            for (int j = 0; j < geotabDriverGroups.length(); ++j) {
                String groupName = "";
                if (!geotabDriverGroups.getJSONObject(j).has("name")) {
                    groupName = "All Vehicles";
                }
                else {
                    groupName = geotabDriverGroups.getJSONObject(j).getString("name");
                }
                if (group == null) {
                    group = groupName;
                }
                else {
                    group = group + ", " + groupName;
                }
            }
            newReportRow.put(RestTrendingReport.displayColumns.get(1), group);
            final Object geotabVehicleTotalDistance = resultsChild.get("totalDistance");
            final long tDistance = ((Double)geotabVehicleTotalDistance).longValue();
            newReportRow.put(RestTrendingReport.displayColumns.get(2), Long.toString(tDistance));
            newReportRow.put(RestTrendingReport.displayColumns.get(3), Integer.toString(periodNumber));
            newReportRow.put(RestTrendingReport.displayColumns.get(4), periodStartDate);
            newReportRow.put(RestTrendingReport.displayColumns.get(5), periodEndDate);
            final Map<String, Integer> geotabExceptionEvents = new HashMap<String, Integer>();
            final JSONArray geotabExceptionSummariesJA = resultsChild.getJSONArray("exceptionSummaries");
            for (int k = 0; k < geotabExceptionSummariesJA.length(); ++k) {
                if (!geotabExceptionSummariesJA.isNull(k)) {
                    final int eventCount = geotabExceptionSummariesJA.getJSONObject(k).getInt("eventCount");
                    final JSONObject geotabExceptionRuleJO = geotabExceptionSummariesJA.getJSONObject(k).getJSONObject("exceptionRule");
                    final String geotabExceptionName = "G-" + geotabExceptionRuleJO.getString("name");
                    geotabExceptionEvents.put(geotabExceptionName, eventCount);
                }
            }
            for (int m = RestTrendingReport.EXCEPTIONS_START_COLUMN; m < RestTrendingReport.displayColumns.size(); ++m) {
                if (geotabExceptionEvents.get(RestTrendingReport.displayColumns.get(m)) != null) {
                    newReportRow.put(RestTrendingReport.displayColumns.get(m), geotabExceptionEvents.get(RestTrendingReport.displayColumns.get(m)).toString());
                }
                else if (newReportRow.get(RestTrendingReport.displayColumns.get(m)) == null) {
                    newReportRow.put(RestTrendingReport.displayColumns.get(m), "0");
                }
            }
            final String key = periodNumber + "|" + geotabVehicleName;
            combinedReport.put(geotabVehicleName, newReportRow);
        }
        return combinedReport;
    }
    
    public static Map<String, String> getNewReportRow(final String userName, final String db) {
        if (RestTrendingReport.displayColumns == null) {
            RestTrendingReport.displayColumns = loadReporColumntHeaders(userName, db);
        }
        final Map<String, String> reportRow = new HashMap<String, String>();
        for (final String column : RestTrendingReport.displayColumns) {
            reportRow.put(column, null);
        }
        return reportRow;
    }
    
    public static List<String> loadReporColumntHeaders(final String userName, final String db) {
        final List<String> reportColumnHeader = new ArrayList<String>();
        reportColumnHeader.add("Vehicle Name");
        reportColumnHeader.add("Group");
        reportColumnHeader.add("Distance");
        final GL_Report_DAO da = new GL_Report_DAO();
        ArrayList<String> gval = new ArrayList<String>();
        gval = (ArrayList<String>)da.getallbehave(userName, db);
        for (int j = 0; j < gval.size(); ++j) {
            reportColumnHeader.add(gval.get(j));
        }
        return reportColumnHeader;
    }
    
    public static List<String> loadTrendingReporColumntHeaders(final String userName, final String db) {
        final List<String> reportColumnHeader = new ArrayList<String>();
        reportColumnHeader.add("VehicleName");
        reportColumnHeader.add("Group");
        reportColumnHeader.add("Distance");
        reportColumnHeader.add("PeriodNumber");
        reportColumnHeader.add("StartDate");
        reportColumnHeader.add("EndDate");
        final GL_Report_DAO da = new GL_Report_DAO();
        ArrayList<String> gval = new ArrayList<String>();
        gval = (ArrayList<String>)da.getallbehave(userName, db);
        for (int j = 0; j < gval.size(); ++j) {
            reportColumnHeader.add(gval.get(j));
        }
        return reportColumnHeader;
    }
    
    public static Map<Integer, String> loadLytxBehaviors(final String lytxBehaviorsJson) {
        final Map<Integer, String> lBehaviors = new HashMap<Integer, String>();
        final JSONObject lytxBehaviorsJO = new JSONObject(lytxBehaviorsJson);
        final JSONArray lytxBehaviorsArray = lytxBehaviorsJO.getJSONArray("behaviors");
        for (int i = 0; i < lytxBehaviorsArray.length(); ++i) {
            final JSONObject behaviorJO = lytxBehaviorsArray.getJSONObject(i);
            final int lytxBehaviorId = behaviorJO.getInt("behaviorId");
            final String lytxDescription = "L-" + behaviorJO.getString("description");
            lBehaviors.put(lytxBehaviorId, lytxDescription);
        }
        return lBehaviors;
    }
    
    public static void printLytxBehaviors(final Map<Integer, String> lBehaviors) {
        for (final Map.Entry<Integer, String> lBehavior : lBehaviors.entrySet()) {
            System.out.println(lBehavior.getKey() + " | " + lBehavior.getValue());
        }
    }
    
    public static Map<Long, String> loadLytxVehicleIDNameMap(final String getVehicleResponseJson) {
        final Map<Long, String> lytxVehicleList = new HashMap<Long, String>();
        final JSONObject lytxVehiclesJO = new JSONObject(getVehicleResponseJson);
        final JSONArray lytxVehiclesArray = lytxVehiclesJO.getJSONArray("vehicles");
        for (int i = 0; i < lytxVehiclesArray.length(); ++i) {
            final String vehicleName = lytxVehiclesArray.getJSONObject(i).getString("name");
            final Long vehicleId = lytxVehiclesArray.getJSONObject(i).getLong("vehicleId");
            lytxVehicleList.put(vehicleId, vehicleName);
        }
        return lytxVehicleList;
    }
    
    public static String toStringValue(final Object object) {
        String value = "";
        if (object != null) {
            if (object instanceof String) {
                value = (String)object;
            }
            else {
                try {
                    value = "" + object.toString().trim();
                }
                catch (Exception ex) {}
            }
        }
        return value;
    }
    
    public static String convert(final GregorianCalendar gregorianCalendarDate) {
        final SimpleDateFormat formattedDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        final String dateFormatted = formattedDate.format(gregorianCalendarDate.getTime());
        return dateFormatted;
    }
    
    private static Date getDate(String dateStr) {
        dateStr = dateStr.substring(0, dateStr.indexOf(46));
        final TimeZone timeZone = TimeZone.getTimeZone("UTC");
        final Calendar calendar = Calendar.getInstance(timeZone);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateStr);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    
    private static Date getDateFromMilliSeconds(final String ms) {
        final Date stdate = new Date(Long.parseLong(ms.substring(33, ms.indexOf(44))));
        return stdate;
    }
    
    @RequestMapping(value = { "/createExcelTrendingReport" }, method = { RequestMethod.GET }, produces = { "application/json" })
    @ResponseBody
    private String createExcelReport(@RequestParam final String sdate, @RequestParam final String edate, @RequestParam final String geouname, @RequestParam final String geodatabase, @RequestParam final String url, @RequestParam final String filename, @RequestParam final String templect, @RequestParam final String entityType) throws IOException, FileNotFoundException {
        String responseJson = "";
        try {
            responseJson = this.ser.selectresponce(geouname, geodatabase);
        }
        catch (Exception ex) {}
        final List<String> displayColumns = loadTrendingReporColumntHeaders(geouname, geodatabase);
        final File source = new File("/home/atiadmin/GL_Driver_Safety_Report_Template_" + templect + ".xlsx");
        final File dest = new File("/usr/local/apache-tomcat-8.5.51/webapps/" + geodatabase + "/report/excel/as.xlsx");
        try {
            RestDriverSafetyReport.copyFileUsingStream(source, dest);
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        final Workbook workbook = WorkbookFactory.create(new File("/usr/local/apache-tomcat-8.5.51/webapps/" + geodatabase + "/report/excel/as.xlsx"));
        final Sheet sheet = workbook.getSheetAt(0);
        final DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        final Calendar calobj = Calendar.getInstance();
        for (int j3 = 0; j3 < 8; ++j3) {
            String name = "";
            String val = "";
            switch (j3) {
                case 0: {
                    name = "CompanyName";
                    val = geodatabase;
                    break;
                }
                case 1: {
                    name = "RunDate";
                    val = df.format(calobj.getTime());
                    break;
                }
                case 2: {
                    name = "FromDate";
                    val = sdate;
                    break;
                }
                case 3: {
                    name = "ToDate";
                    val = edate;
                    break;
                }
            }
            sheet.createRow(j3);
            final Row row = sheet.getRow(j3);
            Cell cell = row.getCell(0);
            if (cell == null) {
                cell = row.createCell(0);
            }
            cell.setCellValue(name);
            cell = row.createCell(1);
            cell.setCellValue(val);
            if (j3 == 0) {
                cell = row.createCell(3);
                cell.setCellValue("Trending Report");
                cell = row.createCell(4);
                if (entityType.equals("Device")) {
                    cell.setCellValue("Vehicle");
                }
                else {
                    cell.setCellValue("Driver");
                }
            }
        }
        sheet.createRow(4);
        final Row row2 = sheet.getRow(4);
        for (int h = 0; h < displayColumns.size(); ++h) {
            final Cell cell2 = row2.createCell(h);
            if (h == 0) {
                cell2.setCellValue("Weight");
            }
            else if (h == 1 || h == 2 || h == 3 || h == 4 || h == 5) {
                cell2.setCellValue("");
            }
            else {
                int D = 0;
                D = GL_Report_DAO.getwe(geouname, displayColumns.get(h).toString().trim(), geodatabase);
                cell2.setCellValue((double)D);
            }
        }
        sheet.createRow(5);
        final Row row3 = sheet.getRow(5);
        for (int j4 = 0; j4 < displayColumns.size(); ++j4) {
            Cell cell3 = row3.getCell(j4);
            if (cell3 == null) {
                cell3 = row3.createCell(j4);
            }
            cell3.setCellValue(displayColumns.get(j4).toString());
        }
        final JSONObject excelvd = new JSONObject(responseJson);
        JSONArray info;
        int s;
        Row row4;
        Cell cell4;
        JSONArray behave;
        int h2;
        for (info = excelvd.getJSONArray("information"), s = 0, s = 0; s < info.length(); ++s) {
            sheet.createRow(s + 6);
            row4 = sheet.getRow(s + 6);
            cell4 = row4.getCell(0);
            if (cell4 == null) {
                cell4 = row4.createCell(0);
            }
            cell4.setCellValue(info.getJSONObject(s).getString("VehicleName"));
            cell4 = row4.createCell(1);
            cell4.setCellValue(info.getJSONObject(s).getString("Group"));
            cell4 = row4.createCell(2);
            cell4.setCellValue((double)Integer.parseInt(info.getJSONObject(s).getString("Distance")));
            cell4 = row4.createCell(3);
            cell4.setCellValue((double)Integer.parseInt(info.getJSONObject(s).getString("PeriodNumber")));
            cell4 = row4.createCell(4);
            cell4.setCellValue(info.getJSONObject(s).getString("StartDate"));
            cell4 = row4.createCell(5);
            cell4.setCellValue(info.getJSONObject(s).getString("EndDate"));
            for (behave = info.getJSONObject(s).getJSONArray("Behave"), h2 = 0; h2 < behave.length(); ++h2) {
                cell4 = row4.createCell(h2 + 6);
                cell4.setCellValue((double)Integer.parseInt(behave.getJSONObject(h2).getString("Rule")));
            }
        }
        final Sheet report = workbook.getSheetAt(1);
        final Row rows = report.getRow(5);
        final Cell cells = rows.getCell(2);
        float min = 0.0f;
        try {
            min = GL_Report_DAO.getminmiles(geouname, geodatabase);
            System.out.println("min-value" + min);
        }
        catch (Exception ex2) {}
        System.out.println("min-value111" + min);
        cells.setCellValue((double)min);
        final int statrpoint = s + 7;
        final String[] formulas = { "Data!A@", "Data!B@", "Data!C@", "IF(C#>$C$6,TRUE,FALSE)", "DATE(LEFT(Data!E@,4),MID(Data!E@,6,2),MID(Data!E@,9,2))", "DATE(LEFT(Data!F@,4),MID(Data!F@,6,2),MID(Data!F@,9,2))", "Data!G@", "Data!H@", "Data!I@", "Data!J@", "Data!K@", "Data!L@", "Data!M@", "Data!N@", "Data!O@", "Data!P@", "IFERROR((G#*G$6)/($C#/100),0)", "IFERROR((H#*H$6)/($C#/100),0)", "IFERROR((I#*I$6)/($C#/100),0)", "IFERROR((J#*J$6)/($C#/100),0)", "IFERROR((K#*K$6)/($C#/100),0)", "IFERROR((L#*L$6)/($C#/100),0)", "IFERROR((M#*M$6)/($C#/100),0)", "IFERROR((N#*N$6)/($C#/100),0)", "IFERROR((O#*O$6)/($C#/100),0)", "IFERROR((P#*P$6)/($C#/100),0)", "AVERAGE(OFFSET($Q#,0,0,1,$AA$5))" };
        updateFormulaForReport(report, 7, s, -1, formulas);
        updateDateFormatFormulaForReport(workbook, report, 7, s, -1, formulas);
        final String newAllDataNamedRange = "Report!$A$7:$AA$" + statrpoint;
        final XSSFWorkbook glDSRWorkbook = (XSSFWorkbook)workbook;
        final XSSFSheet reportSheet = glDSRWorkbook.getSheet("Report");
        final XSSFName allDataNamedRange = glDSRWorkbook.getName("AllData");
        allDataNamedRange.setRefersToFormula(newAllDataNamedRange);
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final CellStyle dateCellStyle = workbook.createCellStyle();
        final short displayDateFormat = workbook.createDataFormat().getFormat("yyyy-MM-dd");
        dateCellStyle.setDataFormat(displayDateFormat);
        try (final FileOutputStream outputStream = new FileOutputStream("/usr/local/apache-tomcat-8.5.51/webapps/" + geodatabase + "/report/excel/" + filename + ".xlsx")) {
            XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
            workbook.write((OutputStream)outputStream);
        }
        workbook.close();
        return "{\"url\":\"" + url + geodatabase + "/report/excel/" + filename + ".xlsx\"}";
    }
    
    private static void updateDateFormatFormulaForReport(final Workbook workbook, final Sheet sheet, final int startRow, final int numberOfRows, final int rowOffset, final String[] formulaList) {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final CellStyle dateCellStyle = workbook.createCellStyle();
        final short displayDateFormat = workbook.createDataFormat().getFormat("yyyy-MM-dd");
        dateCellStyle.setDataFormat(displayDateFormat);
        for (int i = startRow; i < startRow + numberOfRows; ++i) {
            final Row curRow = sheet.createRow(i);
            for (int col = 0; col < formulaList.length; ++col) {
                final Cell newCell = curRow.createCell(col);
                String formula = formulaList[col];
                formula = formula.replace("@", Integer.toString(i));
                formula = formula.replace("#", Integer.toString(i + 1));
                newCell.setCellFormula(formula);
                if (col == 4 || col == 5) {
                    newCell.setCellStyle(dateCellStyle);
                }
            }
        }
    }
    
    private static void updateFormulaForReport(final Sheet sheet, final int startRow, final int numberOfRows, final int rowOffset, final String[] formulaList) {
        for (int i = startRow; i < startRow + numberOfRows; ++i) {
            final Row curRow = sheet.createRow(i);
            for (int col = 0; col < formulaList.length; ++col) {
                final Cell newCell = curRow.createCell(col);
                String formula = formulaList[col];
                formula = formula.replace("@", Integer.toString(i));
                formula = formula.replace("#", Integer.toString(i + 1));
                newCell.setCellFormula(formula);
            }
        }
    }
    
    public static Map<String, List<Trip>> loadVehicleTripsMap(final String entity, final String geouname, final String geodatabase, final String geosees, final String url, final String fromDate, final String toDate) throws MalformedURLException, IOException {
        final ArrayList<String> tripsData = (ArrayList<String>)Common_Geotab_DAO.getTrip(geouname, geodatabase, geosees, url, fromDate, toDate);
        for (final String tripData : tripsData) {
            final String[] tripVars = tripData.split("\\|");
            final String vehicleName = tripVars[0];
            final String[] driverName = tripVars[1].split(" ");
            final String driverFirstName = driverName[0];
            final String driverLastName = driverName[1];
            final LocalDateTime tripStartDate = convertToLocalDateTime(tripVars[2]);
            final LocalDateTime tripEndDate = convertToLocalDateTime(tripVars[3]);
            final Trip trip = new Trip(vehicleName, driverFirstName, driverLastName, tripStartDate, tripEndDate);
            List<Trip> trips = RestTrendingReport.vehicleTrips.get(vehicleName);
            if (trips == null) {
                trips = new ArrayList<Trip>();
                RestTrendingReport.vehicleTrips.put(vehicleName, trips);
            }
            trips.add(trip);
        }
        return RestTrendingReport.vehicleTrips;
    }
    
    public static LocalDateTime convertToLocalDateTime(final String strDate) {
        LocalDateTime ldt = null;
        if (strDate != null) {
            if (strDate.contains("java")) {
                final long dateInMilliSec = Long.parseLong(strDate.substring(33, strDate.indexOf(44)));
                ldt = LocalDateTime.ofInstant(Instant.ofEpochSecond(dateInMilliSec), TimeZone.getDefault().toZoneId());
            }
            else {
                ldt = LocalDateTime.parse(strDate.substring(0, strDate.indexOf(46)));
            }
        }
        return ldt;
    }
    
    public static String[] loadSampleTrips() {
        final String[] tripData = { "15 - BLUE Peterbilt|Ronald Harrower|2020-05-04T03:07:25.063Z|2020-05-04T03:09:00.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-12T11:45:11.063Z|2020-05-12T11:55:09.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-26T10:54:15.063Z|2020-05-26T10:55:07.000Z", "40 - WHITE Ford|Randall Arthur|2020-05-26T10:03:15.000Z|2020-05-26T10:56:12.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-11T11:26:33.063Z|2020-05-11T11:34:17.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-04T09:38:36.000Z|2020-05-04T10:53:41.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-26T09:14:07.063Z|2020-05-26T09:58:24.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-11T11:40:29.063Z|2020-05-11T11:50:05.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-04T10:34:13.063Z|2020-05-04T10:58:12.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-11T10:04:36.063Z|2020-05-11T11:30:16.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-11T08:45:31.127Z|2020-05-11T09:07:36.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T10:17:09.063Z|2020-05-18T10:42:09.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-18T09:14:57.063Z|2020-05-18T09:37:32.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-18T11:12:34.063Z|2020-05-18T11:15:30.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-18T09:38:01.000Z|2020-05-18T10:55:03.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-04T10:19:50.063Z|2020-05-04T10:43:31.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-26T10:58:02.063Z|2020-05-26T11:03:31.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-18T10:06:19.063Z|2020-05-18T10:33:33.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-04T10:23:01.000Z|2020-05-04T10:24:41.000Z", "29 - WHITE Peterbilt|Ronald Harrower|2020-05-18T09:44:24.063Z|2020-05-18T11:12:21.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-18T10:57:16.063Z|2020-05-18T11:03:15.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-04T10:34:54.063Z|2020-05-04T10:44:10.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-11T11:00:51.127Z|2020-05-11T11:36:35.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-18T10:40:23.063Z|2020-05-18T10:49:26.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-18T10:47:48.000Z|2020-05-18T11:08:57.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-11T11:35:02.063Z|2020-05-11T11:42:47.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-11T11:26:59.063Z|2020-05-11T11:27:43.000Z", "40 - WHITE Ford|Randall Arthur|2020-05-26T11:44:49.063Z|2020-05-26T11:55:35.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T10:46:45.063Z|2020-05-04T11:16:26.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-18T10:59:09.000Z|2020-05-18T11:15:13.000Z", "HOT SPARE - AIM Tractor 015039|Kenneth Moore|2020-05-11T10:30:47.063Z|2020-05-11T10:33:35.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-11T11:48:34.063Z|2020-05-11T12:11:50.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-18T10:51:10.000Z|2020-05-18T10:53:51.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-11T11:56:55.063Z|2020-05-11T12:05:45.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-11T11:30:45.063Z|2020-05-11T11:59:51.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-04T09:41:56.063Z|2020-05-04T10:55:54.000Z", "33 - RED Ford|Richard Fox|2020-05-11T11:37:24.000Z|2020-05-11T11:47:40.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-11T12:10:53.063Z|2020-05-11T12:12:59.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-18T11:13:29.063Z|2020-05-18T11:37:43.127Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T10:50:02.063Z|2020-05-18T11:22:02.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-11T11:18:54.063Z|2020-05-11T11:59:01.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-11T12:15:20.127Z|2020-05-11T12:16:10.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-04T07:29:32.000Z|2020-05-04T09:24:27.000Z", "16 - GRAY Peterbilt|Jay Hoagland|2020-05-04T11:20:15.063Z|2020-05-04T11:29:15.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-26T09:24:15.063Z|2020-05-26T09:27:37.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-11T11:54:29.063Z|2020-05-11T11:56:34.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-18T10:56:17.063Z|2020-05-18T11:11:33.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-18T07:19:38.000Z|2020-05-18T08:51:12.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-18T11:31:45.063Z|2020-05-18T11:32:02.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-11T12:03:32.063Z|2020-05-11T12:17:24.000Z", "33 - RED Ford|Richard Fox|2020-05-11T12:00:38.063Z|2020-05-11T12:07:17.000Z", "33 - RED Ford|Richard Fox|2020-05-11T12:17:51.127Z|2020-05-11T12:30:33.000Z", "39 - YELLOW Peterbilt|Kenneth Roberts|2020-05-26T10:28:09.000Z|2020-05-26T11:30:49.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-26T11:36:39.063Z|2020-05-26T11:37:20.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-04T11:02:37.063Z|2020-05-04T11:27:04.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-11T12:27:40.063Z|2020-05-11T12:29:53.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-26T11:08:47.063Z|2020-05-26T11:42:56.000Z", "16 - GRAY Peterbilt|Jay Hoagland|2020-05-04T11:38:06.063Z|2020-05-04T11:41:04.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-04T11:36:34.063Z|2020-05-04T11:54:27.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-18T09:02:47.000Z|2020-05-18T09:04:24.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-11T11:50:20.063Z|2020-05-11T12:19:56.000Z", "14 - BAFFIN Peterbilt|Mark Abraham|2020-05-02T17:51:14.063Z|2020-05-02T18:11:23.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-18T11:19:54.063Z|2020-05-18T11:40:08.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-11T12:19:38.063Z|2020-05-11T12:29:43.000Z", "16 - GRAY Peterbilt|Jay Hoagland|2020-05-16T15:31:03.000Z|2020-05-16T15:35:23.127Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-11T12:20:28.063Z|2020-05-11T12:48:20.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-11T07:24:22.063Z|2020-05-11T09:00:53.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T11:06:08.000Z|2020-05-26T12:08:29.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T11:24:49.063Z|2020-05-04T11:45:08.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-18T12:14:56.127Z|2020-05-18T12:16:53.063Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-18T11:17:21.073Z|2020-05-18T11:18:37.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-26T08:43:04.063Z|2020-05-26T09:15:17.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T11:28:46.063Z|2020-05-18T11:48:31.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-04T11:15:56.063Z|2020-05-04T11:29:13.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-18T08:43:40.063Z|2020-05-18T08:46:09.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T11:51:49.063Z|2020-05-04T12:00:07.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-26T11:09:06.063Z|2020-05-26T11:45:53.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-18T12:06:07.063Z|2020-05-18T12:10:51.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-26T10:56:13.063Z|2020-05-26T11:40:11.127Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T12:16:20.063Z|2020-05-26T12:30:39.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-11T12:37:23.063Z|2020-05-11T12:52:48.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-11T12:33:49.063Z|2020-05-11T12:49:12.000Z", "HOT SPARE - AIM Tractor 015039|Kenneth Moore|2020-05-11T12:09:30.127Z|2020-05-11T12:11:53.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-11T12:26:24.063Z|2020-05-11T13:00:29.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-11T13:37:08.063Z|2020-05-11T13:38:03.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-11T12:40:58.063Z|2020-05-11T12:58:54.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-11T13:00:29.063Z|2020-05-11T13:20:08.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-18T13:24:22.127Z|2020-05-18T13:24:39.000Z", "33 - RED Ford|Richard Fox|2020-05-11T12:36:17.063Z|2020-05-11T12:45:45.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-26T11:40:47.000Z|2020-05-26T12:06:17.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-04T13:42:52.127Z|2020-05-04T13:49:04.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-04T14:48:01.063Z|2020-05-04T14:49:10.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-11T15:10:46.063Z|2020-05-11T15:12:08.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-04T13:22:38.063Z|2020-05-04T13:50:12.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-04T14:04:53.063Z|2020-05-04T14:10:46.000Z", "16 - GRAY Peterbilt|Jay Hoagland|2020-05-04T14:08:59.063Z|2020-05-04T14:16:10.010Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-18T12:38:01.063Z|2020-05-18T13:03:41.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-04T14:05:33.063Z|2020-05-04T14:15:24.063Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T14:44:06.063Z|2020-05-04T14:50:26.000Z", "29 - WHITE Peterbilt|Mark Abraham|2020-05-04T13:07:08.063Z|2020-05-04T13:25:45.000Z", "33 - RED Ford|Richard Fox|2020-05-11T14:28:36.063Z|2020-05-11T14:40:33.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T12:55:19.063Z|2020-05-04T13:12:42.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-11T14:22:54.063Z|2020-05-11T14:54:54.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-11T14:48:04.063Z|2020-05-11T14:48:57.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T12:59:27.063Z|2020-05-18T13:03:53.000Z", "32 - BLUE Ford|Ethan Fellows|2020-05-08T20:33:52.063Z|2020-05-08T20:35:22.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-04T13:27:10.063Z|2020-05-04T13:53:44.000Z", "36 - BLUE International|Bob Boutelle|2020-05-18T15:28:10.127Z|2020-05-18T15:37:57.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T14:44:27.063Z|2020-05-18T15:17:30.000Z", "40 - WHITE Ford|Randall Arthur|2020-05-26T13:52:07.063Z|2020-05-26T14:03:23.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T14:46:22.063Z|2020-05-26T14:48:41.000Z", "36 - BLUE International|Bob Boutelle|2020-05-18T14:55:32.063Z|2020-05-18T15:04:03.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T11:19:07.063Z|2020-05-05T11:26:05.000Z", "33 - RED Ford|Richard Fox|2020-05-11T16:42:46.063Z|2020-05-11T17:13:55.000Z", "36 - BLUE International|Bob Boutelle|2020-05-18T16:06:18.063Z|2020-05-18T16:17:30.000Z", "33 - RED Ford|Richard Fox|2020-05-05T13:15:22.063Z|2020-05-05T13:28:34.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-12T11:27:27.127Z|2020-05-12T11:29:41.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T13:59:55.063Z|2020-05-26T14:16:16.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T13:29:10.063Z|2020-05-26T13:36:18.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T14:30:36.063Z|2020-05-26T14:42:58.000Z", "39 - YELLOW Peterbilt|Kenneth Roberts|2020-05-26T14:18:38.063Z|2020-05-26T14:33:16.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-18T15:14:51.063Z|2020-05-18T15:48:21.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-26T14:55:19.063Z|2020-05-26T15:10:27.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-18T14:44:15.063Z|2020-05-18T15:01:54.000Z", "HOT SPARE - AIM Tractor 015039|Kenneth Moore|2020-05-11T16:58:51.063Z|2020-05-11T19:08:04.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-18T14:34:28.063Z|2020-05-18T15:05:22.000Z", "34 - GOLD Ford|Ethan Fellows|2020-05-05T12:51:43.063Z|2020-05-05T13:08:52.127Z", "15 - BLUE Peterbilt|Michael Wilson|2020-05-05T12:31:45.063Z|2020-05-05T12:55:08.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T11:16:36.063Z|2020-05-12T11:20:01.000Z", "29 - WHITE Peterbilt|Jay Hoagland|2020-05-05T11:27:48.000Z|2020-05-05T12:31:39.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T13:07:50.063Z|2020-05-05T13:14:14.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-05T12:25:26.063Z|2020-05-05T12:55:04.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-12T09:05:49.000Z|2020-05-12T10:21:19.000Z", "29 - WHITE Peterbilt|Jay Hoagland|2020-05-05T13:15:12.063Z|2020-05-05T13:19:30.000Z", "38 - BLUE Peterbilt|Mark Abraham|2020-05-12T11:18:34.063Z|2020-05-12T11:35:01.000Z", "32 - BLUE Ford|Carlos Rivera|2020-05-11T20:43:03.063Z|2020-05-11T20:52:51.000Z", "33 - RED Ford|Richard Fox|2020-05-12T12:00:56.063Z|2020-05-12T12:33:44.000Z", "15 - BLUE Peterbilt|Michael Wilson|2020-05-05T14:10:39.063Z|2020-05-05T14:11:23.000Z", "34 - GOLD Ford|Ethan Fellows|2020-05-05T13:52:55.063Z|2020-05-05T13:56:07.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T13:46:45.063Z|2020-05-05T13:55:04.000Z", "33 - RED Ford|Richard Fox|2020-05-05T14:09:15.063Z|2020-05-05T14:18:34.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-12T11:26:46.063Z|2020-05-12T12:07:25.000Z", "34 - GOLD Ford|Ethan Fellows|2020-05-05T13:20:16.063Z|2020-05-05T13:22:41.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T13:18:23.127Z|2020-05-05T13:18:35.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-12T11:40:10.063Z|2020-05-12T11:54:36.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T14:04:46.063Z|2020-05-05T14:17:48.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-19T10:02:58.063Z|2020-05-19T10:20:19.000Z", "42 - RED Peterbilt|Bob Boutelle|2020-05-11T19:40:01.000Z|2020-05-11T19:58:12.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T12:48:47.063Z|2020-05-05T12:53:56.000Z", "HOT SPARE - AIM Tractor 015039|Kenneth Moore|2020-05-11T08:50:43.000Z|2020-05-11T08:52:23.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-04T08:50:50.063Z|2020-05-04T09:27:06.000Z", "33 - RED Ford|Richard Fox|2020-05-12T13:52:30.127Z|2020-05-12T13:57:25.063Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T17:49:12.063Z|2020-05-05T17:52:16.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-05T17:19:19.063Z|2020-05-05T17:28:41.000Z", "29 - WHITE Peterbilt|Jay Hoagland|2020-05-05T15:32:48.063Z|2020-05-05T15:42:24.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T15:55:17.063Z|2020-05-05T16:05:16.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-05T16:45:26.063Z|2020-05-05T17:06:32.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-05T15:39:36.063Z|2020-05-05T15:52:57.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-23T15:41:40.063Z|2020-05-23T15:42:12.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-18T10:23:40.063Z|2020-05-18T10:31:57.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-11T11:27:25.063Z|2020-05-11T11:27:58.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-11T09:43:05.000Z|2020-05-11T10:58:54.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-18T09:50:56.063Z|2020-05-18T09:54:58.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T13:54:10.063Z|2020-05-12T14:01:04.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-04T10:09:26.127Z|2020-05-04T10:10:24.063Z", "40 - WHITE Ford|Rick Schwenk|2020-05-11T10:09:25.063Z|2020-05-11T10:55:16.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-18T10:35:47.063Z|2020-05-18T10:39:24.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-11T11:16:16.063Z|2020-05-11T11:21:03.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-05T16:12:36.063Z|2020-05-05T16:13:59.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-11T11:02:34.063Z|2020-05-11T11:17:50.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-11T10:43:41.000Z|2020-05-11T11:03:15.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-11T10:36:51.063Z|2020-05-11T11:01:08.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-05T17:10:02.063Z|2020-05-05T18:53:42.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-18T10:42:24.063Z|2020-05-18T10:44:03.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T10:08:49.063Z|2020-05-04T10:35:24.000Z", "33 - RED Ford|Richard Fox|2020-05-12T13:26:01.063Z|2020-05-12T13:34:37.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T12:25:44.063Z|2020-05-18T12:32:22.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-05T18:07:17.063Z|2020-05-05T18:13:08.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-11T12:19:03.063Z|2020-05-11T12:56:08.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-05T07:11:32.063Z|2020-05-05T08:44:55.000Z", "33 - RED Ford|Richard Fox|2020-05-05T15:41:04.063Z|2020-05-05T15:54:22.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T16:35:42.063Z|2020-05-05T17:01:42.000Z", "36 - BLUE International|Bob Boutelle|2020-05-15T16:53:46.000Z|2020-05-15T17:02:04.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-18T05:41:55.000Z|2020-05-18T08:13:35.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-18T07:16:58.063Z|2020-05-18T08:59:17.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-16T15:56:53.127Z|2020-05-16T16:21:22.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-04T09:17:34.063Z|2020-05-04T09:32:52.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-09T15:36:27.063Z|2020-05-09T15:37:07.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T15:26:42.063Z|2020-05-05T15:33:24.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-12T12:07:13.063Z|2020-05-12T12:08:16.000Z", "33 - RED Ford|Richard Fox|2020-05-05T13:41:52.127Z|2020-05-05T14:00:59.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-05T12:31:02.063Z|2020-05-05T12:34:54.000Z", "15 - BLUE Peterbilt|Michael Wilson|2020-05-05T13:08:01.127Z|2020-05-05T13:17:00.063Z", "38 - BLUE Peterbilt|Mark Abraham|2020-05-12T10:06:54.063Z|2020-05-12T10:18:38.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-16T14:21:25.063Z|2020-05-16T14:23:34.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-04T05:00:52.063Z|2020-05-04T05:02:04.000Z", "HOT SPARE - AIM Tractor 015039|Kenneth Moore|2020-05-08T19:37:24.063Z|2020-05-08T19:40:12.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-18T05:32:56.000Z|2020-05-18T05:36:57.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-02T16:02:29.063Z|2020-05-02T16:04:42.000Z", "HOT SPARE - AIM Tractor 015039|Kenneth Moore|2020-05-11T05:27:14.000Z|2020-05-11T05:29:26.000Z", "38 - BLUE Peterbilt|Mark Abraham|2020-05-02T16:55:05.063Z|2020-05-02T16:57:33.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-04T05:02:27.000Z|2020-05-04T05:02:30.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-18T07:15:20.063Z|2020-05-18T07:16:20.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-16T09:59:14.063Z|2020-05-16T10:49:20.000Z", "40 - WHITE Ford|Ronald Harrower|2020-05-23T19:23:54.063Z|2020-05-23T19:24:45.000Z", "40 - WHITE Ford|Carlos Rivera|2020-05-25T13:47:21.127Z|2020-05-25T13:57:44.000Z", "HOT SPARE - AIM Tractor 015039|Kenneth Moore|2020-05-11T05:32:44.000Z|2020-05-11T07:08:18.000Z", "40 - WHITE Ford|Carlos Rivera|2020-05-25T14:04:07.063Z|2020-05-25T14:12:05.000Z", "24 - WHITE Transit|Luann Whitcomb|2020-05-25T17:17:12.063Z|2020-05-25T17:18:35.000Z", "24 - WHITE Transit|Luann Whitcomb|2020-05-25T17:01:59.063Z|2020-05-25T17:10:37.000Z", "28 - WHITE Ford|Lorrie Mabee|2020-05-25T16:56:05.063Z|2020-05-25T17:03:21.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-04T05:34:15.063Z|2020-05-04T08:14:16.000Z", "34 - GOLD Ford|Kevin Neidert|2020-05-24T19:13:02.127Z|2020-05-24T19:55:55.127Z", "28 - WHITE Ford|Lorrie Mabee|2020-05-25T18:30:08.063Z|2020-05-25T18:33:54.000Z", "34 - GOLD Ford|Mark Abraham|2020-05-25T18:17:09.063Z|2020-05-25T19:02:52.187Z", "28 - WHITE Ford|Lorrie Mabee|2020-05-25T17:58:35.063Z|2020-05-25T18:27:37.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-03T22:15:38.063Z|2020-05-03T22:16:29.000Z", "28 - WHITE Ford|Lorrie Mabee|2020-05-25T18:44:44.127Z|2020-05-25T18:57:43.000Z", "34 - GOLD Ford|Mark Abraham|2020-05-25T19:27:22.063Z|2020-05-25T19:31:38.127Z", "28 - WHITE Ford|Lorrie Mabee|2020-05-25T19:24:14.127Z|2020-05-25T19:52:49.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-04T08:24:52.063Z|2020-05-04T08:26:07.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-11T06:42:21.127Z|2020-05-11T08:06:31.000Z", "28 - WHITE Ford|Lorrie Mabee|2020-05-25T20:01:56.063Z|2020-05-25T20:06:57.000Z", "39 - YELLOW Peterbilt|Michael Wilson|2020-05-22T20:28:44.063Z|2020-05-22T20:30:07.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-24T17:30:16.063Z|2020-05-24T17:31:29.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-26T08:32:21.000Z|2020-05-26T08:49:55.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-04T07:33:05.063Z|2020-05-04T08:59:33.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-26T08:26:40.063Z|2020-05-26T08:27:34.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-11T08:10:18.063Z|2020-05-11T08:34:44.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-26T05:39:53.000Z|2020-05-26T08:15:35.000Z", "HOT SPARE - AIM Tractor 015039|Kenneth Moore|2020-05-11T07:20:24.063Z|2020-05-11T08:42:26.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-11T07:28:51.000Z|2020-05-11T08:50:38.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-26T06:44:42.000Z|2020-05-26T08:15:13.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-09T16:58:04.063Z|2020-05-09T17:16:46.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-04T09:32:48.063Z|2020-05-04T09:35:50.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-26T09:43:16.063Z|2020-05-26T10:06:24.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-11T09:37:24.000Z|2020-05-11T09:37:58.000Z", "42 - RED Peterbilt|Mark Abraham|2020-05-03T22:46:25.063Z|2020-05-03T23:31:47.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-04T08:30:41.063Z|2020-05-04T10:04:58.000Z", "29 - WHITE Peterbilt|Mark Abraham|2020-05-04T10:04:17.127Z|2020-05-04T10:05:00.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-04T09:51:00.063Z|2020-05-04T09:53:14.000Z", "29 - WHITE Peterbilt|Ronald Harrower|2020-05-18T07:29:45.063Z|2020-05-18T09:28:24.000Z", "HOT SPARE - AIM Tractor 015039|Kenneth Moore|2020-05-11T09:16:02.063Z|2020-05-11T09:52:34.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-11T09:03:03.063Z|2020-05-11T09:44:07.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-26T10:18:06.000Z|2020-05-26T10:26:38.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-26T10:33:41.063Z|2020-05-26T10:38:35.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-18T09:08:17.063Z|2020-05-18T09:45:16.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-18T09:12:25.063Z|2020-05-18T09:29:33.000Z", "HOT SPARE - AIM Tractor 015039|Kenneth Moore|2020-05-11T10:03:59.063Z|2020-05-11T10:08:08.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-04T09:46:14.063Z|2020-05-04T10:14:56.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-11T09:23:52.063Z|2020-05-11T09:46:53.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-18T09:54:30.063Z|2020-05-18T09:56:00.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-11T10:26:05.063Z|2020-05-11T10:39:13.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-26T09:23:49.000Z|2020-05-26T10:37:14.063Z", "32 - BLUE Ford|Carlos Rivera|2020-05-23T22:42:18.063Z|2020-05-23T22:43:02.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-18T17:30:08.000Z|2020-05-18T17:30:50.000Z", "33 - RED Ford|Richard Fox|2020-05-05T14:43:54.063Z|2020-05-05T14:55:52.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-11T13:28:02.063Z|2020-05-11T13:36:43.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-18T13:00:34.063Z|2020-05-18T13:03:56.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T12:41:59.063Z|2020-05-18T12:51:48.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-18T11:48:15.063Z|2020-05-18T12:15:08.000Z", "31 - WHITE Transit|Troy Giordano|2020-05-18T13:03:10.000Z|2020-05-18T13:14:41.000Z", "33 - RED Ford|Richard Fox|2020-05-11T13:06:01.127Z|2020-05-11T13:09:58.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-11T12:55:17.063Z|2020-05-11T13:16:54.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T12:28:49.063Z|2020-05-26T12:33:27.000Z", "29 - WHITE Peterbilt|Ronald Harrower|2020-05-18T11:19:54.063Z|2020-05-18T11:45:55.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-11T14:12:12.063Z|2020-05-11T14:14:17.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-18T11:47:27.063Z|2020-05-18T12:14:39.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T12:06:11.063Z|2020-05-04T12:14:41.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-11T13:01:16.063Z|2020-05-11T13:16:18.000Z", "29 - WHITE Peterbilt|Mark Abraham|2020-05-04T10:11:26.063Z|2020-05-04T12:01:25.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-18T11:26:34.063Z|2020-05-18T12:05:03.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-04T11:32:53.063Z|2020-05-04T12:31:39.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-18T15:22:39.063Z|2020-05-18T15:35:58.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T12:07:43.063Z|2020-05-18T12:18:15.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-11T13:16:15.063Z|2020-05-11T13:49:44.000Z", "HOT SPARE - AIM Tractor 015039|Kenneth Moore|2020-05-11T12:50:25.063Z|2020-05-11T12:50:50.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T12:16:16.063Z|2020-05-26T12:24:48.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-26T13:14:30.063Z|2020-05-26T13:32:36.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-18T14:46:33.063Z|2020-05-18T15:04:02.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-26T14:18:59.000Z|2020-05-26T14:22:10.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T12:56:13.127Z|2020-05-26T13:02:33.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-26T13:04:39.063Z|2020-05-26T13:13:15.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T13:53:23.127Z|2020-05-26T13:56:58.010Z", "36 - BLUE International|Bob Boutelle|2020-05-26T15:08:27.127Z|2020-05-26T15:15:56.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-18T15:05:56.063Z|2020-05-18T15:06:45.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-11T13:07:48.063Z|2020-05-11T13:57:06.000Z", "36 - BLUE International|Bob Boutelle|2020-05-11T17:51:32.063Z|2020-05-11T17:58:28.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-11T14:20:46.063Z|2020-05-11T14:29:38.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-26T13:26:12.063Z|2020-05-26T13:51:19.000Z", "31 - WHITE Transit|Bob Boutelle|2020-05-04T19:19:32.063Z|2020-05-04T19:20:41.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T14:30:24.063Z|2020-05-18T14:37:38.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-26T13:09:55.063Z|2020-05-26T13:40:08.000Z", "29 - WHITE Peterbilt|Ronald Harrower|2020-05-18T14:57:35.063Z|2020-05-18T15:10:40.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-26T14:30:16.063Z|2020-05-26T14:34:25.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T13:40:48.063Z|2020-05-26T13:48:00.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-11T13:59:35.063Z|2020-05-11T14:06:04.010Z", "29 - WHITE Peterbilt|Ronald Harrower|2020-05-18T14:13:40.063Z|2020-05-18T14:44:03.000Z", "31 - WHITE Transit|Bob Boutelle|2020-05-23T21:01:45.127Z|2020-05-23T21:02:05.000Z", "29 - WHITE Peterbilt|Ronald Harrower|2020-05-18T13:14:33.063Z|2020-05-18T13:25:18.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-18T13:00:40.063Z|2020-05-18T13:14:52.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T12:37:11.063Z|2020-05-26T12:47:40.000Z", "16 - GRAY Peterbilt|Jay Hoagland|2020-05-04T15:32:15.063Z|2020-05-04T15:43:55.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-04T13:34:55.063Z|2020-05-04T15:02:52.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-11T14:58:26.063Z|2020-05-11T15:31:24.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T15:26:14.063Z|2020-05-04T15:27:39.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-18T13:14:52.127Z|2020-05-18T13:28:35.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-04T14:39:54.063Z|2020-05-04T14:48:34.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-04T15:12:55.063Z|2020-05-04T15:14:17.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-18T12:40:00.063Z|2020-05-18T13:15:10.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-11T14:28:18.063Z|2020-05-11T15:56:49.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-12T16:04:45.063Z|2020-05-12T16:25:43.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-11T15:40:09.127Z|2020-05-11T15:45:03.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-04T15:50:27.063Z|2020-05-04T15:56:39.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-12T15:45:38.127Z|2020-05-12T15:53:02.000Z", "33 - RED Ford|Richard Fox|2020-05-11T15:59:33.063Z|2020-05-11T16:01:43.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-04T15:04:04.063Z|2020-05-04T15:14:51.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-11T16:20:14.063Z|2020-05-11T16:25:05.000Z", "36 - BLUE International|Bob Boutelle|2020-05-26T16:01:41.063Z|2020-05-26T16:08:54.000Z", "36 - BLUE International|Bob Boutelle|2020-05-12T15:42:51.000Z|2020-05-12T15:53:22.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T19:06:55.063Z|2020-05-05T19:17:58.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-04T15:11:36.063Z|2020-05-04T15:29:33.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-26T16:28:51.063Z|2020-05-26T17:25:11.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T15:32:18.063Z|2020-05-04T16:22:10.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-11T15:37:37.063Z|2020-05-11T15:46:45.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-04T15:10:46.063Z|2020-05-04T15:16:55.000Z", "29 - WHITE Peterbilt|Jay Hoagland|2020-05-05T15:48:25.063Z|2020-05-05T15:52:14.000Z", "33 - RED Ford|Richard Fox|2020-05-11T16:10:25.063Z|2020-05-11T16:11:16.000Z", "33 - RED Ford|Richard Fox|2020-05-12T14:47:22.063Z|2020-05-12T14:58:44.000Z", "36 - BLUE International|Bob Boutelle|2020-05-11T15:08:30.063Z|2020-05-11T15:20:11.127Z", "HOT SPARE - AIM Tractor 015039|Kenneth Moore|2020-05-11T14:24:57.063Z|2020-05-11T15:27:15.000Z", "36 - BLUE International|Bob Boutelle|2020-05-26T16:16:12.063Z|2020-05-26T16:27:47.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-04T14:44:52.063Z|2020-05-04T14:55:47.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T15:06:08.063Z|2020-05-12T15:09:24.000Z", "33 - RED Ford|Richard Fox|2020-05-05T17:14:41.063Z|2020-05-05T17:21:34.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T11:45:26.063Z|2020-05-19T11:59:59.000Z", "14 - BAFFIN Peterbilt|Kenneth Roberts|2020-05-19T13:02:39.063Z|2020-05-19T13:03:17.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T18:31:46.063Z|2020-05-05T18:43:20.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T16:32:17.063Z|2020-05-04T16:36:34.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-11T15:53:44.063Z|2020-05-11T15:55:30.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-18T13:45:18.063Z|2020-05-18T14:02:29.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-18T13:07:12.127Z|2020-05-18T13:17:13.000Z", "40 - WHITE Ford|Randall Arthur|2020-05-26T16:26:42.000Z|2020-05-26T17:17:15.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-19T13:14:04.063Z|2020-05-19T13:15:46.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-11T12:05:52.063Z|2020-05-11T12:20:25.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-28T12:49:48.063Z|2020-05-28T13:00:22.000Z", "36 - BLUE International|Bob Boutelle|2020-05-14T15:59:36.063Z|2020-05-14T16:11:31.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-28T13:12:47.127Z|2020-05-28T13:19:57.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-28T12:55:11.063Z|2020-05-28T13:14:31.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-07T16:51:16.063Z|2020-05-07T17:13:58.000Z", "31 - WHITE Transit|Kevin Neidert|2020-05-20T21:28:57.063Z|2020-05-20T21:37:55.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-28T11:58:59.063Z|2020-05-28T12:39:21.000Z", "37 - GOLD Peterbilt|Mark Abraham|2020-05-19T18:09:47.063Z|2020-05-19T18:18:13.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-28T12:52:04.063Z|2020-05-28T13:14:38.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-21T09:33:31.063Z|2020-05-21T09:55:44.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-08T14:36:02.063Z|2020-05-08T14:43:40.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-28T15:20:42.063Z|2020-05-28T15:27:16.000Z", "16 - GRAY Peterbilt|Michael Wilson|2020-05-08T14:47:08.063Z|2020-05-08T14:57:48.000Z", "38 - BLUE Peterbilt|Mark Abraham|2020-05-15T07:55:57.000Z|2020-05-15T08:03:35.000Z", "36 - BLUE International|Bob Boutelle|2020-05-08T15:02:54.063Z|2020-05-08T15:03:52.000Z", "36 - BLUE International|Bob Boutelle|2020-05-08T15:30:41.063Z|2020-05-08T15:33:57.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-01T12:17:22.063Z|2020-05-01T12:30:51.000Z", "38 - BLUE Peterbilt|Mark Abraham|2020-05-21T15:58:45.000Z|2020-05-21T17:16:40.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-20T13:35:34.063Z|2020-05-20T13:43:13.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-29T11:48:38.063Z|2020-05-29T11:55:14.000Z", "39 - YELLOW Peterbilt|Michael Wilson|2020-05-15T10:16:32.000Z|2020-05-15T11:10:16.000Z", "39 - YELLOW Peterbilt|Michael Wilson|2020-05-15T16:46:11.063Z|2020-05-15T16:48:34.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-02T10:10:27.063Z|2020-05-02T11:00:55.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-13T19:56:32.063Z|2020-05-13T19:59:00.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-08T15:11:01.127Z|2020-05-08T15:27:33.000Z", "33 - RED Ford|Michael Wilson|2020-05-21T18:35:03.063Z|2020-05-21T18:36:49.000Z", "14 - BAFFIN Peterbilt|Troy Giordano|2020-05-08T14:51:44.063Z|2020-05-08T15:06:21.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-16T11:59:05.063Z|2020-05-16T12:36:09.000Z", "38 - BLUE Peterbilt|Mark Abraham|2020-05-15T16:20:57.063Z|2020-05-15T17:14:43.000Z", "31 - WHITE Transit|Bob Boutelle|2020-05-15T17:35:59.063Z|2020-05-15T17:44:51.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-20T19:13:16.063Z|2020-05-20T20:07:29.000Z", "14 - BAFFIN Peterbilt|Mark Abraham|2020-05-29T11:29:34.063Z|2020-05-29T11:30:53.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-08T15:13:18.063Z|2020-05-08T15:29:31.127Z", "42 - RED Peterbilt|Richard Fox|2020-05-29T11:34:29.063Z|2020-05-29T11:48:30.000Z", "16 - GRAY Peterbilt|Jay Hoagland|2020-05-16T13:16:46.063Z|2020-05-16T13:29:30.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-29T11:13:48.127Z|2020-05-29T11:22:47.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-27T09:47:04.000Z|2020-05-27T11:01:55.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T18:40:13.063Z|2020-05-19T18:48:01.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-01T14:22:12.063Z|2020-05-01T14:34:19.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-05T14:48:37.063Z|2020-05-05T14:57:52.000Z", "39 - YELLOW Peterbilt|Kenneth Roberts|2020-05-26T16:28:06.063Z|2020-05-26T16:28:29.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-19T09:17:40.063Z|2020-05-19T10:42:11.000Z", "36 - BLUE International|Bob Boutelle|2020-05-12T14:25:38.063Z|2020-05-12T14:26:20.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T12:41:01.127Z|2020-05-19T12:45:00.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-19T11:06:48.063Z|2020-05-19T12:02:17.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T12:07:28.063Z|2020-05-19T12:14:28.000Z", "34 - GOLD Ford|Ethan Fellows|2020-05-05T15:21:44.063Z|2020-05-05T15:26:38.127Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T15:35:51.063Z|2020-05-05T15:43:54.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-05T15:36:45.063Z|2020-05-05T15:42:05.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-12T14:21:13.063Z|2020-05-12T14:28:51.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-19T12:25:38.063Z|2020-05-19T12:37:10.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-14T13:14:50.063Z|2020-05-14T13:34:37.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T16:24:13.063Z|2020-05-05T17:10:42.000Z", "29 - WHITE Peterbilt|Jay Hoagland|2020-05-05T16:12:45.063Z|2020-05-05T16:13:40.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-19T11:28:05.063Z|2020-05-19T11:28:45.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T15:53:14.063Z|2020-05-12T15:59:23.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-07T14:08:10.063Z|2020-05-07T14:13:42.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T17:28:44.063Z|2020-05-05T17:36:04.000Z", "15 - BLUE Peterbilt|Michael Wilson|2020-05-05T17:51:27.063Z|2020-05-05T18:29:33.000Z", "36 - BLUE International|Bob Boutelle|2020-05-26T15:44:47.127Z|2020-05-26T15:50:20.000Z", "36 - BLUE International|Bob Boutelle|2020-05-18T18:32:12.063Z|2020-05-18T18:32:47.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T15:28:16.063Z|2020-05-12T15:28:51.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-05T16:05:43.063Z|2020-05-05T16:19:19.000Z", "33 - RED Ford|Richard Fox|2020-05-12T15:16:07.063Z|2020-05-12T15:19:32.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-19T12:36:55.063Z|2020-05-19T12:38:04.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-12T16:03:54.063Z|2020-05-12T16:17:56.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T15:09:16.063Z|2020-05-05T15:17:28.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-12T15:41:34.063Z|2020-05-12T15:42:11.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-07T14:26:35.063Z|2020-05-07T14:35:42.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-07T15:03:08.063Z|2020-05-07T15:11:01.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-07T14:20:16.063Z|2020-05-07T14:55:19.000Z", "33 - RED Ford|Michael Wilson|2020-05-20T16:15:44.063Z|2020-05-20T16:28:58.000Z", "36 - BLUE International|Bob Boutelle|2020-05-14T13:16:19.063Z|2020-05-14T13:19:20.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-28T09:09:45.127Z|2020-05-28T10:42:57.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-07T15:55:36.063Z|2020-05-07T15:58:45.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-07T14:16:17.063Z|2020-05-07T14:44:34.000Z", "14 - BAFFIN Peterbilt|Kenneth Roberts|2020-05-20T18:23:05.063Z|2020-05-20T18:24:25.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-20T16:44:57.063Z|2020-05-20T16:46:28.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-01T16:49:30.063Z|2020-05-01T17:02:30.000Z", "14 - BAFFIN Peterbilt|Kenneth Roberts|2020-05-20T16:37:33.063Z|2020-05-20T18:12:59.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-01T16:44:33.063Z|2020-05-01T17:33:29.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-01T17:59:44.063Z|2020-05-01T18:24:04.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-28T11:38:52.000Z|2020-05-28T11:52:34.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-28T10:54:41.063Z|2020-05-28T11:38:04.063Z", "33 - RED Ford|Ethan Fellows|2020-05-01T17:01:18.063Z|2020-05-01T17:04:18.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-14T14:53:21.063Z|2020-05-14T15:04:50.000Z", "33 - RED Ford|Michael Wilson|2020-05-20T17:35:44.063Z|2020-05-20T17:49:06.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-07T15:46:01.063Z|2020-05-07T15:47:32.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-01T16:39:21.063Z|2020-05-01T16:40:52.000Z", "29 - WHITE Peterbilt|Ronald Harrower|2020-05-14T19:27:42.063Z|2020-05-14T19:41:22.000Z", "33 - RED Ford|Ethan Fellows|2020-05-01T17:23:06.063Z|2020-05-01T17:29:16.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-07T18:08:16.063Z|2020-05-07T18:34:04.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-21T08:59:38.063Z|2020-05-21T09:14:55.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-14T14:43:46.063Z|2020-05-14T14:48:59.000Z", "32 - BLUE Ford|Ethan Fellows|2020-05-14T15:45:28.063Z|2020-05-14T15:51:56.000Z", "36 - BLUE International|Bob Boutelle|2020-05-14T13:53:41.063Z|2020-05-14T14:00:01.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-07T16:01:50.063Z|2020-05-07T16:06:29.000Z", "33 - RED Ford|Ethan Fellows|2020-05-01T16:07:43.063Z|2020-05-01T16:09:55.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-01T15:41:25.063Z|2020-05-01T15:47:14.000Z", "36 - BLUE International|Bob Boutelle|2020-05-28T11:17:30.063Z|2020-05-28T11:32:54.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-01T17:12:07.063Z|2020-05-01T17:28:02.000Z", "33 - RED Ford|Ethan Fellows|2020-05-01T17:42:04.127Z|2020-05-01T17:55:12.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-13T18:43:46.063Z|2020-05-13T18:47:51.000Z", "36 - BLUE International|Bob Boutelle|2020-05-07T18:02:59.063Z|2020-05-07T18:48:41.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-14T12:44:48.063Z|2020-05-14T14:54:58.000Z", "32 - BLUE Ford|Ethan Fellows|2020-05-14T16:28:25.127Z|2020-05-14T16:38:43.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-01T17:58:27.063Z|2020-05-01T19:19:56.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-14T15:00:16.063Z|2020-05-14T15:01:38.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-06T17:07:19.127Z|2020-05-06T17:36:41.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-28T11:38:07.063Z|2020-05-28T11:44:07.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-06T17:33:18.127Z|2020-05-06T17:34:41.063Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-28T09:15:18.063Z|2020-05-28T09:35:31.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-27T09:40:51.063Z|2020-05-27T09:42:29.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-27T15:28:33.063Z|2020-05-27T15:30:33.000Z", "34 - GOLD Ford|Mark Abraham|2020-05-10T21:56:41.063Z|2020-05-10T22:45:36.187Z", "40 - WHITE Ford|Rick Schwenk|2020-05-21T10:51:21.063Z|2020-05-21T11:37:10.000Z", "36 - BLUE International|Bob Boutelle|2020-05-19T18:26:17.063Z|2020-05-19T18:33:39.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-07T18:30:21.063Z|2020-05-07T18:32:06.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-27T11:44:02.063Z|2020-05-27T11:58:40.000Z", "36 - BLUE International|Bob Boutelle|2020-05-14T15:37:20.063Z|2020-05-14T15:41:15.000Z", "36 - BLUE International|Bob Boutelle|2020-05-28T14:09:25.127Z|2020-05-28T14:09:49.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-13T09:53:57.063Z|2020-05-13T10:07:41.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-21T11:50:38.063Z|2020-05-21T12:00:04.000Z", "33 - RED Ford|Michael Wilson|2020-05-14T18:32:34.063Z|2020-05-14T18:32:47.063Z", "38 - BLUE Peterbilt|Mark Abraham|2020-05-21T12:20:47.063Z|2020-05-21T12:35:39.000Z", "36 - BLUE International|Bob Boutelle|2020-05-21T11:42:48.063Z|2020-05-21T11:51:10.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-27T11:00:28.073Z|2020-05-27T11:03:41.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-21T12:36:12.063Z|2020-05-21T12:39:54.000Z", "36 - BLUE International|Bob Boutelle|2020-05-07T16:17:00.063Z|2020-05-07T16:20:58.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-27T12:20:31.063Z|2020-05-27T12:25:30.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-28T10:53:59.063Z|2020-05-28T11:33:59.000Z", "29 - WHITE Peterbilt|Ronald Harrower|2020-05-14T15:07:10.063Z|2020-05-14T15:08:43.010Z", "40 - WHITE Ford|Rick Schwenk|2020-05-21T12:18:01.063Z|2020-05-21T12:27:08.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-12T16:15:52.063Z|2020-05-12T17:45:05.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-19T12:48:17.063Z|2020-05-19T13:06:16.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T18:00:23.127Z|2020-05-05T18:11:53.000Z", "36 - BLUE International|Bob Boutelle|2020-05-11T14:36:16.063Z|2020-05-11T14:37:19.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-12T16:29:03.063Z|2020-05-12T16:37:34.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-18T12:27:53.063Z|2020-05-18T12:50:40.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-04T12:55:13.063Z|2020-05-04T13:17:14.000Z", "28 - WHITE Ford|Ethan Fellows|2020-05-19T13:16:43.063Z|2020-05-19T13:24:06.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-04T12:40:52.063Z|2020-05-04T13:18:02.000Z", "33 - RED Ford|Richard Fox|2020-05-12T16:48:49.127Z|2020-05-12T16:59:58.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-19T13:36:20.063Z|2020-05-19T13:44:12.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-26T15:28:15.063Z|2020-05-26T16:49:01.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T17:15:09.063Z|2020-05-12T17:18:29.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-12T18:15:32.063Z|2020-05-12T18:15:47.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T12:33:06.063Z|2020-05-19T12:55:07.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T17:52:46.063Z|2020-05-26T18:44:00.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-19T12:55:36.000Z|2020-05-19T13:18:01.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-06T07:17:58.063Z|2020-05-06T07:22:15.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-05T17:38:31.127Z|2020-05-05T17:47:23.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-06T09:07:50.063Z|2020-05-06T10:22:48.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T16:09:33.063Z|2020-05-12T16:29:34.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-12T17:02:33.063Z|2020-05-12T17:20:43.000Z", "15 - BLUE Peterbilt|Michael Wilson|2020-05-05T17:31:12.063Z|2020-05-05T17:48:43.000Z", "36 - BLUE International|Bob Boutelle|2020-05-12T17:13:27.063Z|2020-05-12T17:17:28.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-19T14:03:45.127Z|2020-05-19T14:05:57.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T17:43:40.127Z|2020-05-12T17:44:13.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-19T14:14:37.063Z|2020-05-19T14:15:29.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-23T12:25:48.063Z|2020-05-23T12:33:14.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-19T12:44:51.063Z|2020-05-19T13:09:56.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-13T15:32:54.063Z|2020-05-13T15:36:51.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-20T09:41:05.063Z|2020-05-20T10:13:42.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-19T13:22:41.063Z|2020-05-19T13:24:56.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-12T17:23:38.063Z|2020-05-12T18:01:39.063Z", "32 - BLUE Ford|Michael Wilson|2020-05-13T16:00:50.063Z|2020-05-13T16:07:03.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-20T09:35:54.063Z|2020-05-20T09:36:17.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-13T15:16:12.063Z|2020-05-13T15:50:47.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-20T10:48:50.000Z|2020-05-20T11:10:48.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-27T14:54:32.063Z|2020-05-27T14:59:41.000Z", "14 - BAFFIN Peterbilt|Kenneth Roberts|2020-05-19T10:43:31.063Z|2020-05-19T11:29:28.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-01T11:36:03.063Z|2020-05-01T11:51:35.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T14:58:29.063Z|2020-05-12T15:02:30.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-19T12:14:53.063Z|2020-05-19T12:19:16.000Z", "28 - WHITE Ford|Ethan Fellows|2020-05-19T12:48:36.063Z|2020-05-19T12:58:43.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-19T10:38:49.000Z|2020-05-19T11:59:17.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-27T14:33:57.000Z|2020-05-27T14:46:12.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-23T13:13:40.063Z|2020-05-23T13:32:01.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-27T15:03:45.127Z|2020-05-27T15:05:14.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-01T11:21:35.063Z|2020-05-01T11:36:52.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-20T11:51:53.063Z|2020-05-20T11:56:21.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T11:30:13.000Z|2020-05-19T11:36:53.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-13T16:04:19.063Z|2020-05-13T16:34:16.000Z", "36 - BLUE International|Bob Boutelle|2020-05-12T15:57:19.063Z|2020-05-12T16:04:11.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T15:16:00.063Z|2020-05-12T15:33:24.000Z", "29 - WHITE Peterbilt|Jay Hoagland|2020-05-05T13:01:08.063Z|2020-05-05T13:03:23.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-12T12:36:00.063Z|2020-05-12T12:40:41.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-12T15:45:00.063Z|2020-05-12T16:49:06.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T12:44:55.063Z|2020-05-12T12:51:30.000Z", "34 - GOLD Ford|Ethan Fellows|2020-05-05T16:04:38.063Z|2020-05-05T16:07:21.127Z", "29 - WHITE Peterbilt|Jay Hoagland|2020-05-05T16:04:40.063Z|2020-05-05T16:05:06.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-05T13:06:30.063Z|2020-05-05T16:03:06.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T15:32:01.063Z|2020-05-26T15:55:09.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-20T11:51:26.063Z|2020-05-20T12:09:36.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T10:43:54.063Z|2020-05-19T11:23:47.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T14:57:48.063Z|2020-05-12T14:59:20.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T11:18:52.063Z|2020-05-05T11:47:59.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-12T14:37:20.063Z|2020-05-12T14:48:43.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-06T07:27:23.000Z|2020-05-06T08:57:30.000Z", "36 - BLUE International|Bob Boutelle|2020-05-12T15:19:48.063Z|2020-05-12T15:34:39.127Z", "39 - YELLOW Peterbilt|Kenneth Roberts|2020-05-26T18:15:42.063Z|2020-05-26T19:01:16.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T18:30:37.063Z|2020-05-12T18:34:29.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T17:10:19.063Z|2020-05-05T17:24:10.000Z", "36 - BLUE International|Bob Boutelle|2020-05-12T19:34:47.063Z|2020-05-12T19:41:49.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-19T07:58:21.063Z|2020-05-19T09:15:00.000Z", "29 - WHITE Peterbilt|Mark Abraham|2020-05-04T16:16:37.127Z|2020-05-04T18:06:52.000Z", "33 - RED Ford|Richard Fox|2020-05-11T17:35:33.063Z|2020-05-11T18:41:02.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T17:24:36.063Z|2020-05-04T17:29:39.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-26T12:19:37.063Z|2020-05-26T13:23:00.000Z", "34 - GOLD Ford|Ethan Fellows|2020-05-26T14:32:21.127Z|2020-05-26T14:41:16.127Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-18T16:46:23.063Z|2020-05-18T16:48:26.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T14:11:35.063Z|2020-05-26T14:22:10.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T13:07:10.063Z|2020-05-12T13:08:27.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-05T09:01:05.063Z|2020-05-05T09:19:54.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-05T09:45:40.063Z|2020-05-05T09:47:05.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-18T16:06:19.063Z|2020-05-18T16:20:59.000Z", "40 - WHITE Ford|Randall Arthur|2020-05-26T20:44:23.063Z|2020-05-26T20:53:47.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T14:58:11.063Z|2020-05-19T15:08:01.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-26T17:39:06.063Z|2020-05-26T17:57:01.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-04T16:47:24.127Z|2020-05-04T17:15:06.000Z", "38 - BLUE Peterbilt|Mark Abraham|2020-05-12T14:18:27.063Z|2020-05-12T15:48:18.000Z", "36 - BLUE International|Bob Boutelle|2020-05-18T17:52:07.063Z|2020-05-18T18:01:02.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-05T14:24:55.063Z|2020-05-05T14:32:49.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-11T09:04:14.063Z|2020-05-11T09:54:32.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-12T11:46:40.063Z|2020-05-12T11:54:40.000Z", "36 - BLUE International|Bob Boutelle|2020-05-26T18:04:30.063Z|2020-05-26T18:13:20.000Z", "36 - BLUE International|Bob Boutelle|2020-05-18T16:21:34.063Z|2020-05-18T16:29:32.000Z", "39 - YELLOW Peterbilt|Kenneth Roberts|2020-05-26T16:12:13.063Z|2020-05-26T16:14:06.127Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T13:16:28.063Z|2020-05-26T13:22:33.000Z", "14 - BAFFIN Peterbilt|Kenneth Roberts|2020-05-19T09:47:38.063Z|2020-05-19T09:49:13.000Z", "24 - WHITE Transit|Luann Whitcomb|2020-05-06T15:54:42.063Z|2020-05-06T16:00:15.000Z", "34 - GOLD Ford|Rick Schwenk|2020-05-13T13:21:44.127Z|2020-05-13T13:25:15.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T18:39:07.063Z|2020-05-12T19:48:01.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-23T14:32:52.063Z|2020-05-23T14:33:19.063Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T18:33:52.063Z|2020-05-19T18:40:38.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-20T09:34:55.063Z|2020-05-20T09:58:54.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-27T11:22:01.063Z|2020-05-27T11:39:56.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-01T13:13:46.063Z|2020-05-01T13:15:30.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-20T10:44:24.063Z|2020-05-20T10:45:30.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-23T14:33:04.063Z|2020-05-23T15:59:08.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-07T12:14:07.063Z|2020-05-07T12:14:43.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-20T06:38:43.063Z|2020-05-20T06:41:01.000Z", "14 - BAFFIN Peterbilt|Mark Abraham|2020-05-27T10:52:57.063Z|2020-05-27T11:17:31.000Z", "16 - GRAY Peterbilt|Troy Giordano|2020-05-27T12:22:10.063Z|2020-05-27T12:48:57.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-23T14:46:54.073Z|2020-05-23T15:02:00.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-13T16:13:46.063Z|2020-05-13T16:18:30.000Z", "32 - BLUE Ford|Carlos Rivera|2020-05-23T14:07:30.000Z|2020-05-23T14:22:38.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-23T14:23:32.063Z|2020-05-23T14:25:10.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-23T15:19:52.000Z|2020-05-23T15:25:12.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-20T11:17:40.063Z|2020-05-20T11:27:19.000Z", "36 - BLUE International|Bob Boutelle|2020-05-23T14:15:35.063Z|2020-05-23T14:19:45.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-01T11:58:33.063Z|2020-05-01T12:02:46.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-01T10:17:51.063Z|2020-05-01T10:31:52.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-13T14:35:58.063Z|2020-05-13T16:18:07.000Z", "32 - BLUE Ford|Carlos Rivera|2020-05-23T15:32:59.063Z|2020-05-23T15:33:48.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-27T14:12:37.127Z|2020-05-27T14:59:32.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-27T15:05:22.063Z|2020-05-27T15:18:47.000Z", "14 - BAFFIN Peterbilt|Kenneth Roberts|2020-05-19T12:55:12.063Z|2020-05-19T12:55:25.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T16:11:23.063Z|2020-05-12T16:13:42.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-12T15:05:35.063Z|2020-05-12T15:23:54.000Z", "36 - BLUE International|Bob Boutelle|2020-05-26T17:37:10.063Z|2020-05-26T17:48:19.000Z", "14 - BAFFIN Peterbilt|Kenneth Roberts|2020-05-19T13:25:39.063Z|2020-05-19T13:35:17.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T19:52:55.063Z|2020-05-05T20:26:54.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-01T12:08:17.063Z|2020-05-01T12:23:06.000Z", "39 - YELLOW Peterbilt|Kenneth Roberts|2020-05-26T16:44:50.000Z|2020-05-26T17:03:32.000Z", "31 - WHITE Transit|Troy Giordano|2020-05-18T13:17:41.063Z|2020-05-18T13:59:11.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T13:49:47.063Z|2020-05-18T13:59:46.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-12T15:35:29.063Z|2020-05-12T15:40:18.000Z", "31 - WHITE Transit|Bob Boutelle|2020-05-09T20:50:02.063Z|2020-05-09T21:05:55.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-23T14:06:22.063Z|2020-05-23T14:07:22.000Z", "40 - WHITE Ford|Randall Arthur|2020-05-26T12:51:30.063Z|2020-05-26T13:08:56.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T16:37:41.063Z|2020-05-26T16:52:42.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-04T17:50:51.063Z|2020-05-04T18:06:24.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T16:41:38.063Z|2020-05-04T16:44:18.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-01T12:52:07.063Z|2020-05-01T12:52:46.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-18T14:27:25.063Z|2020-05-18T14:27:41.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T14:13:23.063Z|2020-05-19T14:23:01.000Z", "39 - YELLOW Peterbilt|Kenneth Roberts|2020-05-26T21:06:23.063Z|2020-05-26T21:07:42.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-04T17:22:07.063Z|2020-05-04T17:32:48.000Z", "29 - WHITE Peterbilt|Jay Hoagland|2020-05-05T17:19:54.063Z|2020-05-05T17:37:22.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-18T14:19:35.063Z|2020-05-18T14:37:24.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-05T16:30:28.063Z|2020-05-05T16:37:53.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-01T11:47:31.063Z|2020-05-01T12:04:36.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T13:51:04.063Z|2020-05-19T14:05:01.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-26T16:23:14.063Z|2020-05-26T16:51:20.000Z", "34 - GOLD Ford|Ethan Fellows|2020-05-05T16:11:29.063Z|2020-05-05T16:26:56.187Z", "36 - BLUE International|Bob Boutelle|2020-05-26T16:39:28.063Z|2020-05-26T16:50:02.000Z", "43 - BLUE Freightliner|Mark Abraham|2020-05-27T15:57:29.063Z|2020-05-27T16:19:50.000Z", "33 - RED Ford|Richard Fox|2020-05-05T16:39:54.063Z|2020-05-05T16:46:06.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-26T12:59:42.063Z|2020-05-26T13:00:21.000Z", "HOT SPARE - AIM Tractor 015039|Kenneth Moore|2020-05-11T16:28:20.000Z|2020-05-11T16:31:56.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-19T17:46:27.063Z|2020-05-19T18:02:32.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-29T19:56:34.000Z|2020-05-29T19:57:59.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-19T19:36:10.063Z|2020-05-19T19:39:03.000Z", "36 - BLUE International|Bob Boutelle|2020-05-22T18:43:45.063Z|2020-05-22T18:44:05.000Z", "28 - WHITE Ford|Ethan Fellows|2020-05-19T13:41:07.127Z|2020-05-19T13:47:07.000Z", "14 - BAFFIN Peterbilt|Troy Giordano|2020-05-01T12:52:22.063Z|2020-05-01T13:30:11.000Z", "36 - BLUE International|Bob Boutelle|2020-05-11T17:13:38.063Z|2020-05-11T17:19:44.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T13:32:13.063Z|2020-05-26T13:46:55.000Z", "29 - WHITE Peterbilt|Jay Hoagland|2020-05-05T16:52:15.063Z|2020-05-05T17:10:32.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T10:24:13.000Z|2020-05-05T11:04:01.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-11T16:03:16.127Z|2020-05-11T16:04:28.000Z", "36 - BLUE International|Bob Boutelle|2020-05-19T14:32:55.063Z|2020-05-19T14:40:29.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T19:04:11.063Z|2020-05-26T19:09:25.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T15:11:20.063Z|2020-05-19T15:12:19.000Z", "14 - BAFFIN Peterbilt|Kenneth Roberts|2020-05-19T13:44:02.063Z|2020-05-19T14:24:39.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-12T16:33:54.063Z|2020-05-12T16:55:10.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T13:12:30.063Z|2020-05-05T13:13:01.000Z", "36 - BLUE International|Bob Boutelle|2020-05-18T17:33:00.063Z|2020-05-18T17:46:21.000Z", "38 - BLUE Peterbilt|Mark Abraham|2020-05-12T09:38:05.063Z|2020-05-12T09:38:38.000Z", "28 - WHITE Ford|Ethan Fellows|2020-05-19T16:48:39.063Z|2020-05-19T16:48:54.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-06T11:02:01.063Z|2020-05-06T11:28:32.000Z", "33 - RED Ford|Richard Fox|2020-05-05T12:29:36.063Z|2020-05-05T12:53:50.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-19T15:00:40.063Z|2020-05-19T16:58:57.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-06T13:15:46.063Z|2020-05-06T13:30:07.000Z", "40 - WHITE Ford|Troy Giordano|2020-05-06T14:24:58.063Z|2020-05-06T14:29:52.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-05T15:52:25.063Z|2020-05-05T15:56:19.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-05T13:06:23.063Z|2020-05-05T13:37:40.000Z", "36 - BLUE International|Bob Boutelle|2020-05-18T18:15:30.063Z|2020-05-18T18:27:42.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-27T10:05:35.063Z|2020-05-27T10:19:01.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-12T10:36:24.063Z|2020-05-12T11:03:38.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-19T17:14:29.063Z|2020-05-19T17:15:58.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-05T13:45:24.063Z|2020-05-05T14:12:38.000Z", "36 - BLUE International|Bob Boutelle|2020-05-19T17:22:27.063Z|2020-05-19T17:30:18.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-06T12:11:57.063Z|2020-05-06T12:41:32.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T16:09:19.063Z|2020-05-26T16:15:02.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T12:56:58.063Z|2020-05-12T13:08:06.000Z", "39 - YELLOW Peterbilt|Kenneth Roberts|2020-05-26T15:19:32.063Z|2020-05-26T15:47:04.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T13:26:44.063Z|2020-05-05T13:42:57.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-19T14:45:43.063Z|2020-05-19T14:50:43.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-26T15:00:09.063Z|2020-05-26T15:01:16.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T15:52:04.063Z|2020-05-05T15:56:57.000Z", "14 - BAFFIN Peterbilt|Kenneth Roberts|2020-05-19T14:53:36.000Z|2020-05-19T15:09:44.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T12:30:39.063Z|2020-05-05T12:38:18.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-19T18:09:31.063Z|2020-05-19T18:11:41.000Z", "39 - YELLOW Peterbilt|Bob Boutelle|2020-05-18T20:01:33.127Z|2020-05-18T20:04:13.000Z", "28 - WHITE Ford|Ethan Fellows|2020-05-19T15:52:38.063Z|2020-05-19T16:08:00.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T15:04:15.063Z|2020-05-26T15:10:24.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-27T07:16:28.063Z|2020-05-27T08:42:26.000Z", "33 - RED Ford|Richard Fox|2020-05-12T17:23:09.127Z|2020-05-12T17:38:19.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-06T15:22:35.063Z|2020-05-06T15:31:51.000Z", "36 - BLUE International|Bob Boutelle|2020-05-12T17:32:13.063Z|2020-05-12T17:42:41.000Z", "28 - WHITE Ford|Jay Hoagland|2020-05-26T18:56:45.063Z|2020-05-26T19:12:33.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T14:40:55.063Z|2020-05-19T14:42:39.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-13T16:37:37.000Z|2020-05-13T16:38:25.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-27T11:19:57.063Z|2020-05-27T11:34:27.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-13T08:23:54.000Z|2020-05-13T08:25:52.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-07T11:57:47.063Z|2020-05-07T11:58:41.000Z", "16 - GRAY Peterbilt|Troy Giordano|2020-05-27T15:41:49.063Z|2020-05-27T17:02:37.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-06T15:51:25.063Z|2020-05-06T15:56:23.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-13T15:19:13.063Z|2020-05-13T18:34:30.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-07T12:26:11.063Z|2020-05-07T12:39:52.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-20T10:25:01.063Z|2020-05-20T10:46:27.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-27T14:13:16.127Z|2020-05-27T14:24:00.000Z", "43 - BLUE Freightliner|Mark Abraham|2020-05-27T16:24:30.063Z|2020-05-27T16:28:06.000Z", "31 - WHITE Transit|Michael Wilson|2020-05-06T15:56:43.063Z|2020-05-06T16:06:03.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-01T13:18:46.063Z|2020-05-01T13:19:37.000Z", "14 - BAFFIN Peterbilt|Mark Abraham|2020-05-27T10:22:27.063Z|2020-05-27T10:36:31.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-12T13:34:37.063Z|2020-05-12T13:42:41.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-20T11:50:35.063Z|2020-05-20T11:52:24.127Z", "32 - BLUE Ford|Michael Wilson|2020-05-27T14:30:37.063Z|2020-05-27T14:39:32.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-07T11:57:16.063Z|2020-05-07T12:07:23.000Z", "31 - WHITE Transit|Michael Wilson|2020-05-06T15:42:41.063Z|2020-05-06T15:45:01.000Z", "24 - WHITE Transit|Luann Whitcomb|2020-05-06T15:31:37.063Z|2020-05-06T15:35:12.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-05T14:29:47.063Z|2020-05-05T14:48:51.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-18T18:03:14.063Z|2020-05-18T18:05:42.000Z", "14 - BAFFIN Peterbilt|Mark Abraham|2020-05-27T09:52:21.063Z|2020-05-27T09:53:07.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-13T16:41:26.063Z|2020-05-13T16:45:10.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-27T11:50:36.063Z|2020-05-27T12:03:35.000Z", "37 - GOLD Peterbilt|Mark Abraham|2020-05-19T18:26:21.063Z|2020-05-19T18:36:37.000Z", "36 - BLUE International|Bob Boutelle|2020-05-07T11:35:24.063Z|2020-05-07T11:37:16.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-01T09:11:58.063Z|2020-05-01T09:34:48.000Z", "34 - GOLD Ford|Rick Schwenk|2020-05-13T12:36:57.127Z|2020-05-13T12:45:40.127Z", "43 - BLUE Freightliner|Mark Abraham|2020-05-24T19:53:30.063Z|2020-05-24T19:58:49.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T13:11:44.063Z|2020-05-19T13:17:21.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-05T16:17:37.063Z|2020-05-05T16:36:12.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-13T11:12:45.063Z|2020-05-13T11:49:32.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-27T13:10:06.127Z|2020-05-27T13:13:31.000Z", "14 - BAFFIN Peterbilt|Kenneth Roberts|2020-05-19T11:39:36.063Z|2020-05-19T11:45:01.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T15:08:27.063Z|2020-05-12T15:09:31.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-12T15:24:16.063Z|2020-05-12T16:07:01.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T16:23:49.063Z|2020-05-26T16:25:17.000Z", "28 - WHITE Ford|Ethan Fellows|2020-05-19T13:01:23.000Z|2020-05-19T13:08:33.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T17:30:09.063Z|2020-05-05T17:45:09.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-19T13:19:02.063Z|2020-05-19T13:20:09.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-13T11:56:34.063Z|2020-05-13T12:26:19.000Z", "28 - WHITE Ford|Ethan Fellows|2020-05-19T13:29:08.063Z|2020-05-19T13:38:30.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-12T14:22:34.063Z|2020-05-12T14:41:12.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T14:27:41.063Z|2020-05-12T14:38:01.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T16:17:20.063Z|2020-05-05T16:28:48.000Z", "15 - BLUE Peterbilt|Michael Wilson|2020-05-05T16:25:23.063Z|2020-05-05T16:49:51.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-07T11:11:43.063Z|2020-05-07T11:34:01.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T11:06:28.063Z|2020-05-19T11:13:41.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-12T15:27:34.063Z|2020-05-12T15:28:22.000Z", "29 - WHITE Peterbilt|Jay Hoagland|2020-05-05T15:14:38.063Z|2020-05-05T15:20:42.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-27T11:58:40.063Z|2020-05-27T12:44:26.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-19T19:13:20.063Z|2020-05-19T19:39:28.000Z", "14 - BAFFIN Peterbilt|Mark Abraham|2020-05-27T12:41:09.063Z|2020-05-27T12:41:16.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-13T12:21:53.063Z|2020-05-13T12:37:36.000Z", "40 - WHITE Ford|Randall Arthur|2020-05-26T15:31:50.063Z|2020-05-26T15:59:56.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T14:43:59.063Z|2020-05-12T14:45:22.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-18T16:30:22.063Z|2020-05-18T16:39:52.000Z", "HOT SPARE - AIM Tractor 015039|Kenneth Moore|2020-05-11T12:54:10.000Z|2020-05-11T13:35:08.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T14:26:28.063Z|2020-05-05T14:42:41.000Z", "16 - GRAY Peterbilt|Jay Hoagland|2020-05-04T11:46:05.063Z|2020-05-04T12:06:43.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-04T13:12:52.127Z|2020-05-04T13:25:08.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T13:00:05.063Z|2020-05-12T13:00:47.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-04T12:41:23.063Z|2020-05-04T12:48:45.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-11T13:59:34.063Z|2020-05-11T14:31:37.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T12:40:29.063Z|2020-05-26T12:49:11.000Z", "34 - GOLD Ford|Ethan Fellows|2020-05-05T14:04:45.063Z|2020-05-05T14:10:46.063Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T13:30:56.063Z|2020-05-12T13:51:00.000Z", "40 - WHITE Ford|Randall Arthur|2020-05-26T13:16:39.000Z|2020-05-26T13:43:26.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-18T12:28:35.063Z|2020-05-18T12:34:02.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-11T13:46:07.127Z|2020-05-11T14:10:52.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T15:12:19.063Z|2020-05-05T15:20:14.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-18T16:30:13.063Z|2020-05-18T17:12:26.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T20:57:32.000Z|2020-05-05T21:53:59.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-12T12:55:56.127Z|2020-05-12T13:19:12.000Z", "16 - GRAY Peterbilt|Jay Hoagland|2020-05-04T12:33:14.127Z|2020-05-04T13:01:12.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-12T13:55:19.063Z|2020-05-12T14:12:05.000Z", "40 - WHITE Ford|Randall Arthur|2020-05-26T12:12:01.063Z|2020-05-26T12:27:15.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-26T11:52:08.063Z|2020-05-26T11:55:13.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T12:18:55.063Z|2020-05-12T12:53:13.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-19T13:33:14.063Z|2020-05-19T13:33:46.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T15:22:35.063Z|2020-05-05T15:28:40.000Z", "36 - BLUE International|Bob Boutelle|2020-05-23T20:10:32.063Z|2020-05-23T20:10:49.000Z", "36 - BLUE International|Bob Boutelle|2020-05-26T14:20:44.063Z|2020-05-26T14:33:26.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-26T11:55:43.063Z|2020-05-26T12:16:45.000Z", "29 - WHITE Peterbilt|Ronald Harrower|2020-05-22T14:12:32.063Z|2020-05-22T14:16:54.000Z", "29 - WHITE Peterbilt|Ronald Harrower|2020-05-15T16:50:16.063Z|2020-05-15T17:42:49.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-29T16:22:22.063Z|2020-05-29T16:27:48.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-02T14:31:50.063Z|2020-05-02T15:56:01.000Z", "29 - WHITE Peterbilt|Ronald Harrower|2020-05-29T11:06:13.000Z|2020-05-29T11:17:12.000Z", "41 - SILVER Ford|Lorrie Mabee|2020-05-21T20:38:10.063Z|2020-05-21T20:38:34.000Z", "38 - BLUE Peterbilt|Mark Abraham|2020-05-08T14:44:09.063Z|2020-05-08T15:00:44.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-29T13:58:44.063Z|2020-05-29T13:59:11.000Z", "36 - BLUE International|Bob Boutelle|2020-05-08T14:51:19.063Z|2020-05-08T14:58:39.000Z", "31 - WHITE Transit|Ethan Fellows|2020-05-07T15:57:12.063Z|2020-05-07T16:12:22.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-16T11:19:04.063Z|2020-05-16T11:32:32.000Z", "36 - BLUE International|Bob Boutelle|2020-05-08T17:59:44.063Z|2020-05-08T18:11:09.000Z", "31 - WHITE Transit|Bob Boutelle|2020-05-15T19:30:18.063Z|2020-05-15T19:32:24.000Z", "34 - GOLD Ford|Troy Giordano|2020-05-22T12:55:21.127Z|2020-05-22T13:00:21.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-29T13:41:50.063Z|2020-05-29T14:04:47.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-16T11:01:38.063Z|2020-05-16T11:48:04.000Z", "36 - BLUE International|Bob Boutelle|2020-05-29T15:40:57.063Z|2020-05-29T15:53:56.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-08T19:10:05.063Z|2020-05-08T19:11:05.000Z", "37 - GOLD Peterbilt|Mark Abraham|2020-05-22T13:01:51.063Z|2020-05-22T13:50:02.000Z", "36 - BLUE International|Bob Boutelle|2020-05-29T14:51:19.063Z|2020-05-29T14:59:27.000Z", "40 - WHITE Ford|Troy Giordano|2020-05-06T15:15:56.063Z|2020-05-06T15:16:44.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-06T14:06:36.063Z|2020-05-06T14:37:18.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-06T14:43:36.063Z|2020-05-06T14:48:19.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-06T14:34:21.063Z|2020-05-06T14:42:05.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T15:45:38.063Z|2020-05-19T15:56:37.000Z", "39 - YELLOW Peterbilt|Michael Wilson|2020-05-22T14:17:12.063Z|2020-05-22T14:28:17.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-22T14:09:42.063Z|2020-05-22T14:23:58.000Z", "14 - BAFFIN Peterbilt|Kenneth Roberts|2020-05-23T09:41:46.063Z|2020-05-23T09:42:31.000Z", "29 - WHITE Peterbilt|Ronald Harrower|2020-05-22T12:01:00.063Z|2020-05-22T12:06:33.000Z", "34 - GOLD Ford|Troy Giordano|2020-05-22T12:41:25.063Z|2020-05-22T12:45:04.127Z", "41 - SILVER Ford|Rick Schwenk|2020-05-23T12:59:38.063Z|2020-05-23T13:16:31.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-23T13:20:19.063Z|2020-05-23T13:20:26.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-23T11:39:09.063Z|2020-05-23T11:49:28.000Z", "34 - GOLD Ford|Kevin Neidert|2020-05-28T19:19:21.063Z|2020-05-28T19:36:08.127Z", "40 - WHITE Ford|Rick Schwenk|2020-05-23T09:37:16.063Z|2020-05-23T10:27:29.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-01T14:07:05.063Z|2020-05-01T14:13:57.000Z", "32 - BLUE Ford|Ethan Fellows|2020-05-08T18:53:15.063Z|2020-05-08T19:05:45.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-23T12:31:09.000Z|2020-05-23T12:31:10.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-01T12:40:11.000Z|2020-05-01T13:15:01.063Z", "36 - BLUE International|Bob Boutelle|2020-05-21T18:27:25.063Z|2020-05-21T18:28:30.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-01T13:26:30.063Z|2020-05-01T13:51:27.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-24T13:31:20.063Z|2020-05-24T13:32:07.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-14T07:25:21.000Z|2020-05-14T08:49:56.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-20T13:11:56.127Z|2020-05-20T13:21:30.063Z", "42 - RED Peterbilt|Richard Fox|2020-05-06T10:47:45.063Z|2020-05-06T10:52:51.010Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-20T13:33:25.000Z|2020-05-20T14:14:42.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-06T10:24:24.063Z|2020-05-06T10:36:42.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-18T11:46:22.063Z|2020-05-18T12:04:10.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-18T11:21:54.063Z|2020-05-18T11:49:07.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-11T13:15:10.063Z|2020-05-11T13:29:22.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T11:54:09.063Z|2020-05-18T12:01:44.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-18T12:19:03.063Z|2020-05-18T12:20:58.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-05T09:54:41.063Z|2020-05-05T10:18:19.127Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-18T12:26:07.063Z|2020-05-18T12:26:47.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-18T14:43:45.063Z|2020-05-18T15:09:05.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-27T18:19:23.063Z|2020-05-27T19:01:45.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-23T13:29:05.063Z|2020-05-23T13:33:35.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-20T14:13:19.063Z|2020-05-20T14:14:47.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-23T13:04:36.063Z|2020-05-23T13:13:53.000Z", "14 - BAFFIN Peterbilt|Troy Giordano|2020-05-01T12:15:46.063Z|2020-05-01T12:39:55.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-20T12:59:03.063Z|2020-05-20T13:17:27.000Z", "39 - YELLOW Peterbilt|Michael Wilson|2020-05-29T16:07:10.063Z|2020-05-29T16:10:35.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-24T12:10:04.063Z|2020-05-24T12:31:56.000Z", "32 - BLUE Ford|Carlos Rivera|2020-05-22T15:30:40.063Z|2020-05-22T15:37:47.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T22:01:59.063Z|2020-05-05T22:03:58.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-23T12:58:32.063Z|2020-05-23T13:00:01.063Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-01T12:34:17.063Z|2020-05-01T12:36:39.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-24T14:58:44.000Z|2020-05-24T14:58:45.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-23T11:50:10.063Z|2020-05-23T12:24:21.000Z", "43 - BLUE Freightliner|Mark Abraham|2020-05-24T13:09:01.063Z|2020-05-24T13:14:41.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-20T12:40:59.000Z|2020-05-20T12:45:18.000Z", "28 - WHITE Ford|Ethan Fellows|2020-05-19T18:03:07.063Z|2020-05-19T18:03:56.000Z", "34 - GOLD Ford|Ethan Fellows|2020-05-05T17:40:46.127Z|2020-05-05T18:02:00.127Z", "41 - SILVER Ford|Rick Schwenk|2020-05-23T10:43:49.063Z|2020-05-23T11:36:14.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-20T14:03:41.063Z|2020-05-20T14:37:39.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-07T12:47:06.063Z|2020-05-07T13:06:14.000Z", "33 - RED Ford|Michael Wilson|2020-05-20T14:12:19.063Z|2020-05-20T14:17:00.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T14:08:34.063Z|2020-05-19T14:16:44.000Z", "39 - YELLOW Peterbilt|Kenneth Roberts|2020-05-26T17:37:36.063Z|2020-05-26T17:49:26.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-20T14:45:42.063Z|2020-05-20T14:47:04.000Z", "42 - RED Peterbilt|Ethan Fellows|2020-05-16T11:21:26.063Z|2020-05-16T11:50:42.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-24T14:45:55.063Z|2020-05-24T14:46:13.000Z", "28 - WHITE Ford|Ethan Fellows|2020-05-19T14:52:31.063Z|2020-05-19T14:57:37.000Z", "35 - PURPLE Peterbilt|Kenneth Moore|2020-05-07T11:03:59.127Z|2020-05-07T12:59:06.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-19T14:35:27.063Z|2020-05-19T14:53:09.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-23T11:59:31.073Z|2020-05-23T12:00:18.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-06T10:51:33.063Z|2020-05-06T11:03:18.000Z", "35 - PURPLE Peterbilt|Kenneth Moore|2020-05-07T13:03:49.127Z|2020-05-07T13:04:56.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-19T13:12:28.127Z|2020-05-19T13:14:50.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-29T09:34:05.000Z|2020-05-29T10:49:03.000Z", "43 - BLUE Freightliner|Mark Abraham|2020-05-24T10:04:16.127Z|2020-05-24T10:06:21.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-07T12:42:18.127Z|2020-05-07T12:50:41.000Z", "43 - BLUE Freightliner|Mark Abraham|2020-05-24T14:27:02.000Z|2020-05-24T14:48:41.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-23T09:44:04.063Z|2020-05-23T10:57:38.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-01T13:38:25.063Z|2020-05-01T13:42:27.187Z", "33 - RED Ford|Michael Wilson|2020-05-20T14:37:12.063Z|2020-05-20T14:39:53.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-01T12:53:11.063Z|2020-05-01T13:01:07.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-01T13:25:47.063Z|2020-05-01T13:29:46.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-02T18:18:05.000Z|2020-05-02T18:35:40.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-26T16:16:15.063Z|2020-05-26T18:42:26.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-06T09:03:58.063Z|2020-05-06T09:11:16.000Z", "14 - BAFFIN Peterbilt|Mark Abraham|2020-05-27T09:07:26.063Z|2020-05-27T09:07:40.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-06T10:24:14.000Z|2020-05-06T10:24:18.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-26T18:49:46.063Z|2020-05-26T18:52:33.000Z", "40 - WHITE Ford|Randall Arthur|2020-05-26T18:23:27.063Z|2020-05-26T19:20:23.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T19:07:10.063Z|2020-05-12T19:16:37.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T13:29:55.127Z|2020-05-18T13:41:53.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-26T18:27:04.063Z|2020-05-26T19:06:27.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-06T09:15:40.063Z|2020-05-06T09:33:31.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T19:06:33.000Z|2020-05-26T19:08:23.000Z", "36 - BLUE International|Bob Boutelle|2020-05-19T15:54:32.063Z|2020-05-19T15:59:31.000Z", "36 - BLUE International|Bob Boutelle|2020-05-19T17:06:08.063Z|2020-05-19T17:13:45.000Z", "40 - WHITE Ford|Troy Giordano|2020-05-06T13:41:10.063Z|2020-05-06T14:06:25.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-06T12:26:03.063Z|2020-05-06T12:35:08.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T17:46:05.063Z|2020-05-04T18:09:10.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-06T09:12:15.063Z|2020-05-06T09:56:50.000Z", "24 - WHITE Transit|Luann Whitcomb|2020-05-25T17:24:37.063Z|2020-05-25T17:29:44.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-06T12:44:04.063Z|2020-05-06T12:52:06.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-06T12:43:45.063Z|2020-05-06T12:47:53.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-12T16:56:53.063Z|2020-05-12T16:58:06.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-06T13:29:56.063Z|2020-05-06T14:00:12.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-19T16:29:04.127Z|2020-05-19T16:37:39.000Z", "28 - WHITE Ford|Ethan Fellows|2020-05-19T15:32:24.063Z|2020-05-19T15:44:03.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T15:38:24.063Z|2020-05-19T15:43:29.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-06T11:39:34.063Z|2020-05-06T11:54:27.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-06T11:17:22.063Z|2020-05-06T11:18:31.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-04T15:31:08.063Z|2020-05-04T15:41:10.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-04T16:04:02.063Z|2020-05-04T16:16:29.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-11T15:50:35.063Z|2020-05-11T15:54:36.063Z", "29 - WHITE Peterbilt|Ronald Harrower|2020-05-18T13:38:32.127Z|2020-05-18T13:41:58.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-18T13:37:49.063Z|2020-05-18T13:42:51.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-11T15:16:56.063Z|2020-05-11T16:03:30.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-11T16:06:37.127Z|2020-05-11T16:15:40.000Z", "29 - WHITE Peterbilt|Mark Abraham|2020-05-04T15:34:55.063Z|2020-05-04T15:35:24.063Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-18T13:17:57.000Z|2020-05-18T13:28:13.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-04T16:20:53.063Z|2020-05-04T16:21:54.000Z", "33 - RED Ford|Richard Fox|2020-05-11T15:36:54.063Z|2020-05-11T15:50:26.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-04T15:47:34.063Z|2020-05-04T16:00:51.000Z", "36 - BLUE International|Bob Boutelle|2020-05-11T16:52:07.063Z|2020-05-11T17:00:01.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-26T12:33:18.063Z|2020-05-26T12:34:35.000Z", "HOT SPARE - AIM Tractor 015039|Kenneth Moore|2020-05-11T16:06:37.063Z|2020-05-11T16:07:08.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-27T12:01:07.063Z|2020-05-27T12:32:02.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-06T20:45:47.063Z|2020-05-06T21:41:18.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-06T16:41:21.063Z|2020-05-06T16:56:32.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T20:05:32.127Z|2020-05-19T21:05:15.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-13T11:17:41.063Z|2020-05-13T11:45:53.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-13T12:29:58.063Z|2020-05-13T12:37:23.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-13T12:32:26.063Z|2020-05-13T12:36:07.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-06T17:52:14.063Z|2020-05-06T17:54:25.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-27T12:43:47.063Z|2020-05-27T12:51:19.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-07T10:00:54.063Z|2020-05-07T10:44:38.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T19:01:31.063Z|2020-05-19T19:16:17.000Z", "33 - RED Ford|Richard Fox|2020-05-05T16:58:39.063Z|2020-05-05T17:07:42.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-13T12:41:55.063Z|2020-05-13T12:58:21.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-13T11:37:08.127Z|2020-05-13T12:48:00.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-13T13:08:48.063Z|2020-05-13T13:33:43.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-19T19:56:45.063Z|2020-05-19T20:46:09.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-19T18:18:59.063Z|2020-05-19T18:49:54.000Z", "31 - WHITE Transit|Bob Boutelle|2020-05-19T19:44:31.063Z|2020-05-19T19:44:46.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-27T11:16:18.063Z|2020-05-27T11:30:14.000Z", "31 - WHITE Transit|Bob Boutelle|2020-05-19T20:04:14.127Z|2020-05-19T20:32:34.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-27T13:29:59.063Z|2020-05-27T13:33:53.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-01T09:38:44.063Z|2020-05-01T10:01:33.000Z", "35 - PURPLE Peterbilt|Kenneth Moore|2020-05-07T09:33:39.127Z|2020-05-07T09:56:41.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-13T12:48:35.063Z|2020-05-13T13:09:41.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-19T18:45:00.063Z|2020-05-19T19:02:48.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-13T12:37:17.063Z|2020-05-13T12:38:19.127Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-27T12:50:03.063Z|2020-05-27T13:24:23.000Z", "16 - GRAY Peterbilt|Troy Giordano|2020-05-27T13:50:47.063Z|2020-05-27T14:04:20.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-27T12:17:56.063Z|2020-05-27T12:23:37.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-07T10:17:50.063Z|2020-05-07T10:52:40.000Z", "14 - BAFFIN Peterbilt|Mark Abraham|2020-05-27T11:47:05.063Z|2020-05-27T11:48:19.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-13T13:11:31.063Z|2020-05-13T13:11:47.000Z", "31 - WHITE Transit|Bob Boutelle|2020-05-11T19:05:51.063Z|2020-05-11T19:15:06.000Z", "40 - WHITE Ford|Randall Arthur|2020-05-26T14:21:24.063Z|2020-05-26T14:28:46.000Z", "34 - GOLD Ford|Mark Abraham|2020-05-25T19:37:20.063Z|2020-05-25T20:26:29.127Z", "31 - WHITE Transit|Bob Boutelle|2020-05-04T19:34:28.063Z|2020-05-04T19:40:53.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-18T09:09:02.063Z|2020-05-18T09:58:42.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-18T15:53:28.063Z|2020-05-18T15:56:01.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T10:13:33.127Z|2020-05-05T11:04:02.000Z", "36 - BLUE International|Bob Boutelle|2020-05-18T17:11:33.063Z|2020-05-18T17:21:34.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-11T16:26:32.063Z|2020-05-11T16:40:17.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-26T14:11:30.063Z|2020-05-26T14:38:27.000Z", "31 - WHITE Transit|Bob Boutelle|2020-05-04T19:31:11.063Z|2020-05-04T19:31:30.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T11:11:53.063Z|2020-05-05T11:14:41.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-01T17:04:02.063Z|2020-05-01T17:45:00.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-28T12:07:50.000Z|2020-05-28T12:27:32.000Z", "29 - WHITE Peterbilt|Ronald Harrower|2020-05-14T14:27:16.063Z|2020-05-14T14:34:55.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-14T13:22:33.063Z|2020-05-14T13:38:38.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-06T16:32:22.127Z|2020-05-06T16:36:06.000Z", "29 - WHITE Peterbilt|Ronald Harrower|2020-05-14T16:37:05.127Z|2020-05-14T16:49:38.063Z", "40 - WHITE Ford|Rick Schwenk|2020-05-27T10:28:10.063Z|2020-05-27T11:14:11.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-27T11:32:11.063Z|2020-05-27T11:36:12.000Z", "14 - BAFFIN Peterbilt|Mark Abraham|2020-05-27T10:02:04.063Z|2020-05-27T10:02:35.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-28T14:00:25.127Z|2020-05-28T14:17:13.000Z", "33 - RED Ford|Michael Wilson|2020-05-20T16:39:44.063Z|2020-05-20T16:42:48.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-01T17:49:02.063Z|2020-05-01T17:57:53.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-19T16:49:03.063Z|2020-05-19T17:28:22.000Z", "42 - RED Peterbilt|Jay Hoagland|2020-05-12T20:04:13.063Z|2020-05-12T20:19:49.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-28T14:12:26.063Z|2020-05-28T14:28:42.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-01T17:37:30.063Z|2020-05-01T17:39:34.000Z", "32 - BLUE Ford|Ethan Fellows|2020-05-14T15:03:25.063Z|2020-05-14T15:18:34.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-27T06:13:58.000Z|2020-05-27T08:34:32.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-13T05:33:58.000Z|2020-05-13T08:10:38.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-13T06:49:22.063Z|2020-05-13T08:20:37.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-27T09:00:51.000Z|2020-05-27T09:19:03.000Z", "14 - BAFFIN Peterbilt|Mark Abraham|2020-05-27T09:26:42.127Z|2020-05-27T09:48:50.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-13T08:46:00.063Z|2020-05-13T09:18:44.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-13T09:06:48.063Z|2020-05-13T09:08:22.127Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-27T08:51:25.063Z|2020-05-27T09:17:20.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-13T08:34:11.063Z|2020-05-13T09:02:37.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-27T15:59:18.063Z|2020-05-27T16:01:25.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-27T13:41:45.127Z|2020-05-27T14:00:14.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T18:06:07.063Z|2020-05-26T19:02:50.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-06T11:49:01.063Z|2020-05-06T12:00:16.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-26T18:53:45.063Z|2020-05-26T18:53:58.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-06T11:28:16.063Z|2020-05-06T11:28:49.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-06T12:21:45.063Z|2020-05-06T12:42:35.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-06T11:34:00.063Z|2020-05-06T12:05:46.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-06T12:12:35.127Z|2020-05-06T12:17:25.000Z", "31 - WHITE Transit|Bob Boutelle|2020-05-12T20:35:10.063Z|2020-05-12T20:45:34.000Z", "39 - YELLOW Peterbilt|Kenneth Roberts|2020-05-26T20:08:17.063Z|2020-05-26T20:15:27.000Z", "36 - BLUE International|Bob Boutelle|2020-05-19T17:37:08.063Z|2020-05-19T17:42:37.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T17:30:18.063Z|2020-05-12T18:27:13.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-06T12:59:22.063Z|2020-05-06T13:19:34.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-27T10:26:02.000Z|2020-05-27T10:48:52.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-26T19:11:53.063Z|2020-05-26T19:14:25.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T19:24:20.063Z|2020-05-12T20:01:19.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-06T14:17:08.063Z|2020-05-06T14:20:32.000Z", "40 - WHITE Ford|Troy Giordano|2020-05-06T14:46:54.063Z|2020-05-06T14:52:34.000Z", "28 - WHITE Ford|Ethan Fellows|2020-05-19T17:20:43.063Z|2020-05-19T17:42:23.000Z", "28 - WHITE Ford|Ethan Fellows|2020-05-19T16:20:17.063Z|2020-05-19T16:31:17.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-12T18:21:11.063Z|2020-05-12T18:21:43.000Z", "14 - BAFFIN Peterbilt|Mark Abraham|2020-05-27T12:03:15.063Z|2020-05-27T12:24:04.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-13T13:57:42.063Z|2020-05-13T14:26:13.000Z", "35 - PURPLE Peterbilt|Kenneth Moore|2020-05-07T10:31:45.063Z|2020-05-07T10:50:50.063Z", "16 - GRAY Peterbilt|Troy Giordano|2020-05-27T11:25:23.127Z|2020-05-27T12:00:54.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-13T13:35:41.063Z|2020-05-13T13:38:29.000Z", "36 - BLUE International|Bob Boutelle|2020-05-19T18:51:35.063Z|2020-05-19T19:00:30.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-27T13:18:02.063Z|2020-05-27T13:25:28.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-13T13:30:26.063Z|2020-05-13T13:43:16.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-12T18:05:28.063Z|2020-05-12T18:06:16.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-13T14:16:13.063Z|2020-05-13T14:24:01.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-01T12:30:36.063Z|2020-05-01T12:32:28.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-13T13:58:43.063Z|2020-05-13T14:03:23.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-01T11:07:43.063Z|2020-05-01T11:10:52.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-27T14:02:51.063Z|2020-05-27T14:10:50.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-19T17:23:27.063Z|2020-05-19T17:24:35.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-07T10:51:32.063Z|2020-05-07T11:01:11.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-27T13:48:06.063Z|2020-05-27T14:14:40.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-27T12:33:08.063Z|2020-05-27T12:39:05.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-07T08:12:35.063Z|2020-05-07T09:48:22.000Z", "31 - WHITE Transit|Bob Boutelle|2020-05-19T19:56:26.063Z|2020-05-19T19:56:49.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-13T14:20:35.063Z|2020-05-13T14:21:18.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-13T14:44:50.063Z|2020-05-13T14:59:39.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-20T07:01:20.063Z|2020-05-20T08:23:31.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-13T13:16:00.127Z|2020-05-13T13:46:53.000Z", "31 - WHITE Transit|Bob Boutelle|2020-05-19T19:39:57.063Z|2020-05-19T19:42:02.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-11T14:22:15.063Z|2020-05-11T14:22:38.000Z", "29 - WHITE Peterbilt|Mark Abraham|2020-05-04T12:23:44.063Z|2020-05-04T12:42:35.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-18T12:00:58.063Z|2020-05-18T12:47:55.000Z", "HOT SPARE - AIM Tractor 015039|Kenneth Moore|2020-05-11T13:38:02.063Z|2020-05-11T13:39:50.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-11T13:54:05.063Z|2020-05-11T14:06:48.063Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-04T11:11:49.000Z|2020-05-04T11:53:06.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-04T13:19:55.063Z|2020-05-04T13:20:36.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-04T11:42:14.063Z|2020-05-04T13:08:48.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-11T14:43:10.063Z|2020-05-11T15:03:23.000Z", "29 - WHITE Peterbilt|Mark Abraham|2020-05-04T13:00:34.063Z|2020-05-04T13:01:54.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-11T13:41:52.063Z|2020-05-11T14:07:19.000Z", "33 - RED Ford|Richard Fox|2020-05-11T13:24:54.063Z|2020-05-11T13:47:45.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-26T12:15:42.063Z|2020-05-26T12:19:26.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-04T12:35:09.063Z|2020-05-04T13:10:10.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-11T14:14:04.063Z|2020-05-11T14:34:08.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-19T13:32:08.063Z|2020-05-19T13:38:01.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T18:51:08.063Z|2020-05-12T18:51:49.000Z", "36 - BLUE International|Bob Boutelle|2020-05-12T16:25:09.063Z|2020-05-12T16:32:12.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T20:06:50.063Z|2020-05-12T20:07:34.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-13T12:51:40.063Z|2020-05-13T13:15:28.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T18:45:43.063Z|2020-05-19T19:50:00.000Z", "34 - GOLD Ford|Rick Schwenk|2020-05-13T12:53:34.063Z|2020-05-13T13:08:40.127Z", "40 - WHITE Ford|Rick Schwenk|2020-05-27T12:29:57.063Z|2020-05-27T12:38:34.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-20T09:21:23.063Z|2020-05-20T09:23:26.000Z", "33 - RED Ford|Richard Fox|2020-05-12T19:17:02.000Z|2020-05-12T19:17:30.000Z", "34 - GOLD Ford|Rick Schwenk|2020-05-13T12:02:55.063Z|2020-05-13T12:07:08.127Z", "42 - RED Peterbilt|Richard Fox|2020-05-27T13:14:44.063Z|2020-05-27T13:23:39.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-01T12:34:26.063Z|2020-05-01T12:41:35.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-07T11:02:36.063Z|2020-05-07T11:41:01.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-27T13:45:32.063Z|2020-05-27T13:51:24.000Z", "34 - GOLD Ford|Rick Schwenk|2020-05-13T14:15:48.063Z|2020-05-13T14:19:25.127Z", "14 - BAFFIN Peterbilt|Mark Abraham|2020-05-27T13:25:32.127Z|2020-05-27T14:25:47.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-07T11:30:05.063Z|2020-05-07T11:36:29.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-27T14:39:21.063Z|2020-05-27T14:46:25.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-27T14:44:46.063Z|2020-05-27T14:47:45.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-13T14:12:35.063Z|2020-05-13T14:57:05.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-27T12:59:38.063Z|2020-05-27T13:03:52.000Z", "16 - GRAY Peterbilt|Troy Giordano|2020-05-27T13:18:08.063Z|2020-05-27T13:24:07.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-01T05:33:25.063Z|2020-05-01T05:37:22.000Z", "34 - GOLD Ford|Rick Schwenk|2020-05-13T13:08:52.000Z|2020-05-13T13:16:40.127Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-01T11:33:12.063Z|2020-05-01T11:48:09.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-01T07:59:38.063Z|2020-05-01T08:00:39.000Z", "24 - WHITE Transit|Luann Whitcomb|2020-05-26T19:30:51.063Z|2020-05-26T19:33:14.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T13:24:34.063Z|2020-05-04T13:35:37.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-11T18:03:54.063Z|2020-05-11T18:06:02.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T10:15:58.000Z|2020-05-05T10:16:03.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T15:24:29.063Z|2020-05-18T15:45:02.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-04T16:49:58.063Z|2020-05-04T16:59:55.000Z", "32 - BLUE Ford|Carlos Rivera|2020-05-11T19:03:23.063Z|2020-05-11T19:14:02.000Z", "39 - YELLOW Peterbilt|Kenneth Roberts|2020-05-26T13:17:54.063Z|2020-05-26T13:18:16.000Z", "32 - BLUE Ford|Carlos Rivera|2020-05-11T18:53:37.000Z|2020-05-11T19:01:06.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-26T12:43:42.063Z|2020-05-26T12:54:57.000Z", "42 - RED Peterbilt|Mark Abraham|2020-05-10T19:42:58.063Z|2020-05-10T19:43:32.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T13:41:46.063Z|2020-05-04T13:59:31.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T17:14:36.063Z|2020-05-04T17:20:42.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-26T13:53:20.063Z|2020-05-26T14:15:37.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-26T14:19:44.063Z|2020-05-26T14:20:51.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-26T13:49:47.063Z|2020-05-26T13:59:12.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T17:46:08.063Z|2020-05-18T18:07:31.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-04T18:22:12.127Z|2020-05-04T19:26:11.000Z", "16 - GRAY Peterbilt|Jay Hoagland|2020-05-04T13:17:08.063Z|2020-05-04T13:40:44.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T12:53:35.000Z|2020-05-26T13:05:03.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-11T16:06:25.063Z|2020-05-11T17:50:41.000Z", "36 - BLUE International|Bob Boutelle|2020-05-18T15:50:45.063Z|2020-05-18T15:57:20.000Z", "16 - GRAY Peterbilt|Jay Hoagland|2020-05-04T17:00:31.063Z|2020-05-04T17:01:29.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-18T15:48:02.063Z|2020-05-18T15:50:17.000Z", "32 - BLUE Ford|Carlos Rivera|2020-05-11T18:48:01.063Z|2020-05-11T18:48:40.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T14:53:14.063Z|2020-05-26T14:55:05.000Z", "31 - WHITE Transit|Kevin Neidert|2020-05-18T16:10:02.063Z|2020-05-18T16:16:17.000Z", "35 - PURPLE Peterbilt|Kenneth Moore|2020-05-07T07:43:45.000Z|2020-05-07T07:46:55.000Z", "16 - GRAY Peterbilt|Ronald Harrower|2020-05-21T18:18:07.063Z|2020-05-21T18:18:48.000Z", "31 - WHITE Transit|Bob Boutelle|2020-05-19T19:46:55.063Z|2020-05-19T19:50:56.000Z", "31 - WHITE Transit|Bob Boutelle|2020-05-19T20:34:20.063Z|2020-05-19T20:57:43.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-20T06:46:06.063Z|2020-05-20T06:55:54.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-27T10:53:11.063Z|2020-05-27T10:53:38.063Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T18:09:48.063Z|2020-05-19T18:27:00.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-13T10:05:09.063Z|2020-05-13T10:56:47.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T19:32:29.063Z|2020-05-19T19:54:54.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-19T18:53:27.063Z|2020-05-19T19:31:39.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-06T15:22:15.063Z|2020-05-06T15:28:52.000Z", "34 - GOLD Ford|Carlos Rivera|2020-05-06T18:02:16.063Z|2020-05-06T18:08:48.127Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T21:11:33.063Z|2020-05-19T21:13:51.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-05T16:42:35.063Z|2020-05-05T16:51:51.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T13:29:21.063Z|2020-05-19T13:42:51.000Z", "36 - BLUE International|Bob Boutelle|2020-05-11T17:07:06.063Z|2020-05-11T17:07:38.000Z", "36 - BLUE International|Bob Boutelle|2020-05-12T16:54:07.063Z|2020-05-12T17:02:55.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T17:03:43.127Z|2020-05-26T17:22:31.000Z", "24 - WHITE Transit|Luann Whitcomb|2020-05-26T19:18:31.063Z|2020-05-26T19:21:24.000Z", "34 - GOLD Ford|Ethan Fellows|2020-05-05T15:48:58.063Z|2020-05-05T15:55:15.063Z", "14 - BAFFIN Peterbilt|Kenneth Roberts|2020-05-19T14:27:58.063Z|2020-05-19T14:28:23.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-12T17:05:43.063Z|2020-05-12T18:10:48.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-19T13:51:55.063Z|2020-05-19T14:06:01.000Z", "36 - BLUE International|Bob Boutelle|2020-05-12T17:20:58.063Z|2020-05-12T17:22:13.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-28T12:04:11.063Z|2020-05-28T12:04:32.000Z", "29 - WHITE Peterbilt|Jay Hoagland|2020-05-05T17:43:28.063Z|2020-05-05T17:46:38.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T17:50:22.063Z|2020-05-05T18:01:33.000Z", "36 - BLUE International|Bob Boutelle|2020-05-26T17:06:06.063Z|2020-05-26T17:23:24.000Z", "36 - BLUE International|Bob Boutelle|2020-05-19T14:48:41.063Z|2020-05-19T15:08:38.000Z", "28 - WHITE Ford|Lorrie Mabee|2020-05-25T20:07:38.063Z|2020-05-25T20:08:29.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-12T18:20:43.187Z|2020-05-12T18:24:20.000Z", "28 - WHITE Ford|Ethan Fellows|2020-05-19T13:54:23.063Z|2020-05-19T14:11:33.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-19T11:57:51.063Z|2020-05-19T12:53:08.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T17:12:38.187Z|2020-05-26T17:23:02.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-19T13:29:05.127Z|2020-05-19T13:30:55.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-28T11:32:05.000Z|2020-05-28T11:52:32.000Z", "33 - RED Ford|Richard Fox|2020-05-05T17:38:04.063Z|2020-05-05T17:53:12.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T19:34:26.000Z|2020-05-26T19:55:16.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-12T17:49:32.063Z|2020-05-12T18:18:39.000Z", "31 - WHITE Transit|Troy Giordano|2020-05-26T17:27:56.000Z|2020-05-26T17:37:18.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-19T13:45:57.063Z|2020-05-19T13:48:20.000Z", "15 - BLUE Peterbilt|Michael Wilson|2020-05-05T18:34:00.063Z|2020-05-05T18:35:04.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-19T15:14:43.063Z|2020-05-19T15:16:28.000Z", "14 - BAFFIN Peterbilt|Kenneth Roberts|2020-05-19T14:49:51.063Z|2020-05-19T14:51:00.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-19T14:12:32.063Z|2020-05-19T14:14:35.000Z", "14 - BAFFIN Peterbilt|Mark Abraham|2020-05-27T07:16:09.063Z|2020-05-27T07:25:59.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-19T16:33:04.063Z|2020-05-19T16:35:06.000Z", "28 - WHITE Ford|Lorrie Mabee|2020-05-26T18:16:32.063Z|2020-05-26T18:24:56.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-06T10:42:04.063Z|2020-05-06T11:20:29.000Z", "40 - WHITE Ford|Randall Arthur|2020-05-26T17:38:20.063Z|2020-05-26T18:10:52.000Z", "28 - WHITE Ford|Ethan Fellows|2020-05-19T17:50:52.063Z|2020-05-19T17:57:59.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-06T16:44:32.063Z|2020-05-06T17:14:55.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-13T11:08:57.063Z|2020-05-13T11:28:55.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-12T11:26:21.063Z|2020-05-12T11:33:46.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T20:27:52.063Z|2020-05-26T20:28:52.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-11T19:59:58.000Z|2020-05-11T20:18:13.000Z", "36 - BLUE International|Bob Boutelle|2020-05-18T16:38:49.127Z|2020-05-18T16:55:08.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-06T11:37:23.063Z|2020-05-06T11:45:36.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-06T13:56:45.063Z|2020-05-06T13:57:00.063Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-06T12:54:09.127Z|2020-05-06T12:55:19.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-13T10:55:04.000Z|2020-05-13T11:08:08.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-19T17:09:08.063Z|2020-05-19T17:10:37.000Z", "34 - GOLD Ford|Rick Schwenk|2020-05-13T10:54:30.063Z|2020-05-13T11:39:55.000Z", "24 - WHITE Transit|Luann Whitcomb|2020-05-06T15:09:13.063Z|2020-05-06T15:14:34.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T17:44:47.063Z|2020-05-19T17:51:42.000Z", "40 - WHITE Ford|Troy Giordano|2020-05-06T12:54:54.063Z|2020-05-06T13:22:54.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-12T11:35:40.063Z|2020-05-12T11:42:06.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-11T17:05:39.063Z|2020-05-11T17:10:02.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-19T17:26:11.063Z|2020-05-19T17:30:17.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-11T16:36:42.063Z|2020-05-11T16:48:25.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-06T15:22:09.063Z|2020-05-06T15:37:45.063Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-19T15:29:21.063Z|2020-05-19T17:05:49.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-18T17:21:34.000Z|2020-05-18T17:21:43.000Z", "31 - WHITE Transit|Bob Boutelle|2020-05-04T19:53:23.063Z|2020-05-04T19:59:55.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T10:12:55.063Z|2020-05-19T10:57:48.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-04T10:55:10.063Z|2020-05-04T11:51:56.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-20T10:09:15.063Z|2020-05-20T10:31:30.000Z", "16 - GRAY Peterbilt|Jay Hoagland|2020-05-04T13:49:25.063Z|2020-05-04T13:59:18.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-11T14:42:37.063Z|2020-05-11T14:53:52.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-12T12:17:07.127Z|2020-05-12T12:19:13.000Z", "16 - GRAY Peterbilt|Jay Hoagland|2020-05-04T14:28:55.063Z|2020-05-04T14:32:32.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T12:10:10.063Z|2020-05-12T12:13:29.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-12T12:13:22.063Z|2020-05-12T12:25:02.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-13T15:03:35.063Z|2020-05-13T15:08:52.000Z", "36 - BLUE International|Bob Boutelle|2020-05-19T16:28:30.063Z|2020-05-19T16:37:18.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-06T19:02:37.127Z|2020-05-06T19:46:44.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-06T16:07:42.063Z|2020-05-06T17:04:53.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T16:29:03.063Z|2020-05-19T16:45:53.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-27T11:41:30.063Z|2020-05-27T12:25:24.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-11T20:26:26.063Z|2020-05-11T20:27:42.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-19T14:59:57.063Z|2020-05-19T15:05:07.000Z", "34 - GOLD Ford|Carlos Rivera|2020-05-06T18:15:36.063Z|2020-05-06T18:30:38.127Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-06T15:15:23.063Z|2020-05-06T15:15:58.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-06T16:20:26.063Z|2020-05-06T16:26:15.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-04T14:24:04.127Z|2020-05-04T14:44:52.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-06T13:12:09.063Z|2020-05-06T13:23:48.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-06T14:51:05.000Z|2020-05-06T14:52:10.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-06T11:47:56.063Z|2020-05-06T12:01:09.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-06T14:17:49.063Z|2020-05-06T14:27:48.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-07T11:43:14.063Z|2020-05-07T11:45:55.000Z", "36 - BLUE International|Bob Boutelle|2020-05-12T19:16:31.063Z|2020-05-12T19:28:10.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-19T17:09:07.000Z|2020-05-19T17:09:44.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T17:56:03.063Z|2020-05-19T18:07:03.000Z", "40 - WHITE Ford|Troy Giordano|2020-05-06T12:39:54.063Z|2020-05-06T12:48:20.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-19T15:35:28.063Z|2020-05-19T15:39:41.000Z", "38 - BLUE Peterbilt|Mark Abraham|2020-05-12T10:48:15.063Z|2020-05-12T11:09:18.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-06T13:52:14.063Z|2020-05-06T13:53:06.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-06T14:59:35.063Z|2020-05-06T15:12:59.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-20T12:20:27.063Z|2020-05-20T12:26:16.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T15:22:33.000Z|2020-05-19T15:32:17.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-18T12:53:06.063Z|2020-05-18T12:54:19.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-06T14:27:26.063Z|2020-05-06T14:30:07.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T16:06:25.063Z|2020-05-19T16:11:23.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-06T14:52:45.063Z|2020-05-06T14:55:19.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-07T12:14:49.063Z|2020-05-07T12:16:25.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-27T15:37:14.063Z|2020-05-27T15:52:13.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-20T13:32:51.063Z|2020-05-20T13:33:27.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-19T15:50:41.063Z|2020-05-19T15:57:04.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-20T12:29:40.063Z|2020-05-20T13:01:06.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-20T12:25:38.063Z|2020-05-20T12:26:00.127Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-27T15:38:14.063Z|2020-05-27T18:26:58.000Z", "14 - BAFFIN Peterbilt|Troy Giordano|2020-05-01T10:17:17.000Z|2020-05-01T11:31:52.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-20T14:00:06.063Z|2020-05-20T14:09:09.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-06T12:25:41.063Z|2020-05-06T12:33:42.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-20T12:59:24.063Z|2020-05-20T13:10:01.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-13T18:15:26.063Z|2020-05-13T19:17:14.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-20T14:17:41.063Z|2020-05-20T14:26:55.000Z", "14 - BAFFIN Peterbilt|Troy Giordano|2020-05-01T11:55:59.063Z|2020-05-01T12:05:47.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-13T19:20:05.063Z|2020-05-13T19:52:48.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-20T13:05:46.063Z|2020-05-20T13:10:36.127Z", "42 - RED Peterbilt|Richard Fox|2020-05-20T13:46:22.127Z|2020-05-20T13:51:37.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-07T12:09:50.063Z|2020-05-07T12:33:21.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-13T16:50:48.063Z|2020-05-13T17:04:08.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-27T16:16:34.063Z|2020-05-27T16:21:25.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-20T10:59:19.063Z|2020-05-20T11:41:20.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-14T07:18:08.063Z|2020-05-14T07:21:51.000Z", "36 - BLUE International|Bob Boutelle|2020-05-19T16:44:10.063Z|2020-05-19T16:50:31.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-06T13:46:44.063Z|2020-05-06T13:48:55.000Z", "14 - BAFFIN Peterbilt|Kenneth Roberts|2020-05-20T09:42:54.000Z|2020-05-20T11:29:14.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T15:53:20.127Z|2020-05-19T15:56:13.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-18T13:46:35.063Z|2020-05-18T14:00:00.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-18T13:43:03.063Z|2020-05-18T13:45:34.000Z", "36 - BLUE International|Bob Boutelle|2020-05-11T17:03:05.063Z|2020-05-11T17:03:22.000Z", "HOT SPARE - AIM Tractor 015039|Kenneth Moore|2020-05-11T19:33:55.063Z|2020-05-11T19:48:29.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-26T12:27:09.063Z|2020-05-26T12:51:27.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T18:06:56.063Z|2020-05-05T18:10:21.063Z", "29 - WHITE Peterbilt|Jay Hoagland|2020-05-05T16:31:16.063Z|2020-05-05T16:35:15.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T19:20:04.063Z|2020-05-05T19:21:18.000Z", "29 - WHITE Peterbilt|Jay Hoagland|2020-05-05T14:24:12.063Z|2020-05-05T14:46:31.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T12:28:44.063Z|2020-05-19T12:30:45.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-11T14:10:50.127Z|2020-05-11T14:11:55.000Z", "29 - WHITE Peterbilt|Mark Abraham|2020-05-04T18:20:14.063Z|2020-05-04T18:21:58.000Z", "29 - WHITE Peterbilt|Jay Hoagland|2020-05-05T18:14:47.127Z|2020-05-05T18:15:19.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T17:47:45.063Z|2020-05-26T17:49:34.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-18T13:31:28.063Z|2020-05-18T13:33:06.127Z", "29 - WHITE Peterbilt|Jay Hoagland|2020-05-05T18:59:35.063Z|2020-05-05T20:09:40.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-07T09:25:35.000Z|2020-05-07T10:37:04.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-07T12:02:12.000Z|2020-05-07T12:14:18.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-18T13:37:44.063Z|2020-05-18T13:41:41.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-04T14:30:35.063Z|2020-05-04T14:32:59.000Z", "34 - GOLD Ford|Rick Schwenk|2020-05-13T13:29:11.063Z|2020-05-13T13:40:11.127Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-01T12:05:06.063Z|2020-05-01T12:31:08.000Z", "33 - RED Ford|Richard Fox|2020-05-11T15:01:03.063Z|2020-05-11T15:04:48.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-13T14:31:03.063Z|2020-05-13T14:35:26.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-01T05:40:58.000Z|2020-05-01T08:16:20.000Z", "36 - BLUE International|Bob Boutelle|2020-05-12T19:45:30.063Z|2020-05-12T19:45:43.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-27T15:11:56.063Z|2020-05-27T15:26:55.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-27T13:36:00.063Z|2020-05-27T13:40:37.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-01T07:51:51.063Z|2020-05-01T09:29:56.000Z", "34 - GOLD Ford|Rick Schwenk|2020-05-13T13:44:57.127Z|2020-05-13T13:53:22.127Z", "32 - BLUE Ford|Michael Wilson|2020-05-13T14:40:08.063Z|2020-05-13T14:42:18.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-01T09:49:13.063Z|2020-05-01T10:06:11.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-13T15:00:43.063Z|2020-05-13T15:03:05.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-07T11:47:07.063Z|2020-05-07T11:55:14.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-13T15:23:53.063Z|2020-05-13T15:27:14.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-27T15:01:36.063Z|2020-05-27T15:07:21.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-13T15:11:30.063Z|2020-05-13T15:16:20.000Z", "34 - GOLD Ford|Rick Schwenk|2020-05-13T14:25:36.063Z|2020-05-13T14:32:15.127Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-13T14:02:17.063Z|2020-05-13T14:19:43.000Z", "34 - GOLD Ford|Rick Schwenk|2020-05-13T14:46:30.063Z|2020-05-13T14:53:31.127Z", "32 - BLUE Ford|Michael Wilson|2020-05-27T15:38:04.063Z|2020-05-27T15:49:17.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-01T10:33:14.000Z|2020-05-01T11:26:36.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-27T16:34:02.063Z|2020-05-27T16:39:42.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-01T11:57:25.063Z|2020-05-01T11:58:37.063Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T11:36:35.063Z|2020-05-05T11:38:35.000Z", "31 - WHITE Transit|Bob Boutelle|2020-05-19T20:00:11.000Z|2020-05-19T20:00:49.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-12T12:04:53.063Z|2020-05-12T12:10:56.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-18T17:15:07.063Z|2020-05-18T17:17:06.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T13:09:58.063Z|2020-05-18T13:13:26.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-04T14:25:55.063Z|2020-05-04T14:29:39.000Z", "32 - BLUE Ford|Carlos Rivera|2020-05-11T20:26:22.063Z|2020-05-11T20:39:44.000Z", "33 - RED Ford|Richard Fox|2020-05-11T15:19:02.063Z|2020-05-11T15:23:42.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-19T16:08:38.063Z|2020-05-19T16:08:51.000Z", "16 - GRAY Peterbilt|Jay Hoagland|2020-05-04T14:45:45.063Z|2020-05-04T15:03:30.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-18T17:21:20.063Z|2020-05-18T17:23:01.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-04T13:19:07.063Z|2020-05-04T13:20:42.000Z", "16 - GRAY Peterbilt|Troy Giordano|2020-05-27T13:43:01.063Z|2020-05-27T13:43:43.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-11T15:19:52.063Z|2020-05-11T15:20:25.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-13T12:01:08.063Z|2020-05-13T12:13:39.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-13T12:47:19.063Z|2020-05-13T12:51:37.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-11T14:52:15.000Z|2020-05-11T15:29:14.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-04T14:46:18.063Z|2020-05-04T15:04:09.000Z", "29 - WHITE Peterbilt|Mark Abraham|2020-05-04T15:17:42.063Z|2020-05-04T15:20:04.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-20T08:36:11.063Z|2020-05-20T08:54:35.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-27T14:50:00.063Z|2020-05-27T14:59:17.000Z", "29 - WHITE Peterbilt|Ronald Harrower|2020-05-18T13:02:44.000Z|2020-05-18T13:02:46.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-12T10:10:18.127Z|2020-05-12T10:20:57.000Z", "29 - WHITE Peterbilt|Ronald Harrower|2020-05-18T15:16:22.127Z|2020-05-18T15:17:45.063Z", "39 - YELLOW Peterbilt|Kenneth Roberts|2020-05-26T11:46:33.063Z|2020-05-26T12:12:01.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-27T14:30:27.063Z|2020-05-27T14:35:30.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T18:37:19.063Z|2020-05-18T18:44:41.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-27T12:36:18.063Z|2020-05-27T13:25:47.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-26T11:48:35.063Z|2020-05-26T12:24:40.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-27T15:23:44.063Z|2020-05-27T15:28:29.000Z", "16 - GRAY Peterbilt|Troy Giordano|2020-05-27T14:08:15.063Z|2020-05-27T14:49:56.000Z", "34 - GOLD Ford|Rick Schwenk|2020-05-13T15:03:21.063Z|2020-05-13T15:18:01.127Z", "32 - BLUE Ford|Michael Wilson|2020-05-13T14:47:20.127Z|2020-05-13T14:55:56.000Z", "36 - BLUE International|Bob Boutelle|2020-05-23T15:06:38.127Z|2020-05-23T15:12:32.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-13T16:25:34.063Z|2020-05-13T16:31:41.000Z", "14 - BAFFIN Peterbilt|Kenneth Roberts|2020-05-23T12:44:30.063Z|2020-05-23T14:42:00.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-13T15:41:48.063Z|2020-05-13T15:48:43.063Z", "42 - RED Peterbilt|Richard Fox|2020-05-20T11:32:33.127Z|2020-05-20T11:56:22.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-01T12:43:53.063Z|2020-05-01T12:56:55.000Z", "14 - BAFFIN Peterbilt|Mark Abraham|2020-05-27T14:30:24.063Z|2020-05-27T14:57:53.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T14:08:07.063Z|2020-05-04T14:34:59.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-13T15:22:12.063Z|2020-05-13T15:52:03.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-13T14:59:42.063Z|2020-05-13T15:02:01.127Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-04T14:57:24.000Z|2020-05-04T15:49:55.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T18:48:42.063Z|2020-05-18T18:55:40.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-27T14:54:21.063Z|2020-05-27T14:58:48.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-07T12:12:53.063Z|2020-05-07T12:17:43.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-13T17:50:53.063Z|2020-05-13T17:54:05.000Z", "43 - BLUE Freightliner|Mark Abraham|2020-05-27T16:39:09.063Z|2020-05-27T16:39:38.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-27T16:08:56.063Z|2020-05-27T16:43:36.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-27T15:13:35.063Z|2020-05-27T15:14:49.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-27T13:57:26.127Z|2020-05-27T14:06:57.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-13T13:49:06.063Z|2020-05-13T14:09:12.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-01T12:03:22.063Z|2020-05-01T12:23:12.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-01T10:47:40.063Z|2020-05-01T10:50:30.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-11T15:27:07.000Z|2020-05-11T15:33:12.000Z", "36 - BLUE International|Bob Boutelle|2020-05-07T11:48:05.063Z|2020-05-07T12:03:40.063Z", "16 - GRAY Peterbilt|Jay Hoagland|2020-05-04T15:15:58.063Z|2020-05-04T15:19:12.000Z", "14 - BAFFIN Peterbilt|Mark Abraham|2020-05-27T12:43:39.063Z|2020-05-27T12:53:53.000Z", "15 - BLUE Peterbilt|Michael Wilson|2020-05-05T12:01:05.063Z|2020-05-05T12:14:20.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-01T08:04:00.000Z|2020-05-01T09:36:37.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-20T09:26:32.063Z|2020-05-20T09:28:06.063Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-07T11:38:41.063Z|2020-05-07T11:51:54.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-20T06:31:38.000Z|2020-05-20T09:09:15.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-05T11:15:46.063Z|2020-05-05T12:07:59.000Z", "32 - BLUE Ford|Carlos Rivera|2020-05-11T19:45:37.063Z|2020-05-11T20:20:07.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-27T12:55:21.063Z|2020-05-27T13:54:43.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-27T13:41:43.127Z|2020-05-27T13:47:10.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-04T15:27:04.063Z|2020-05-04T15:33:43.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-20T12:38:36.127Z|2020-05-20T12:54:49.000Z", "14 - BAFFIN Peterbilt|Kenneth Roberts|2020-05-20T11:44:41.063Z|2020-05-20T12:02:56.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-20T12:02:38.063Z|2020-05-20T12:10:09.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-01T13:26:23.063Z|2020-05-01T13:32:40.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T16:51:39.063Z|2020-05-04T16:57:34.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-18T13:26:23.063Z|2020-05-18T13:29:43.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T14:58:19.063Z|2020-05-04T15:14:25.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-01T11:59:37.000Z|2020-05-01T11:59:42.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-27T15:01:59.063Z|2020-05-27T15:12:39.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-27T16:56:45.063Z|2020-05-27T17:11:07.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-04T15:22:00.063Z|2020-05-04T15:25:13.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-11T15:27:52.063Z|2020-05-11T15:33:59.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-20T13:20:29.063Z|2020-05-20T13:29:23.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-18T13:24:37.000Z|2020-05-18T13:25:59.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T14:03:42.063Z|2020-05-18T14:23:41.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-26T12:38:22.063Z|2020-05-26T12:45:27.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T13:30:26.000Z|2020-05-12T13:30:27.000Z", "40 - WHITE Ford|Randall Arthur|2020-05-26T14:35:44.063Z|2020-05-26T14:38:22.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-04T16:12:50.063Z|2020-05-04T16:16:15.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T15:25:04.063Z|2020-05-26T15:33:14.000Z", "36 - BLUE International|Bob Boutelle|2020-05-11T17:28:20.063Z|2020-05-11T17:39:24.000Z", "HOT SPARE - AIM Tractor 015039|Kenneth Moore|2020-05-11T19:15:10.127Z|2020-05-11T19:17:14.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-04T17:27:01.063Z|2020-05-04T18:29:06.000Z", "40 - WHITE Ford|Randall Arthur|2020-05-26T12:03:14.063Z|2020-05-26T12:06:22.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-18T14:00:10.063Z|2020-05-18T14:15:53.000Z", "15 - BLUE Peterbilt|Michael Wilson|2020-05-05T16:58:47.063Z|2020-05-05T17:19:37.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-26T14:03:50.063Z|2020-05-26T14:05:45.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-18T14:19:47.063Z|2020-05-18T14:35:32.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T13:12:37.063Z|2020-05-12T13:18:20.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-26T13:05:01.063Z|2020-05-26T13:55:05.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-04T16:20:20.063Z|2020-05-04T17:05:33.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-12T13:30:29.063Z|2020-05-12T13:41:57.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-12T12:34:46.063Z|2020-05-12T12:49:11.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-04T18:12:11.063Z|2020-05-04T18:12:29.000Z", "36 - BLUE International|Bob Boutelle|2020-05-26T15:32:38.063Z|2020-05-26T15:40:00.187Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-26T17:05:28.063Z|2020-05-26T17:30:23.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-12T15:02:33.063Z|2020-05-12T15:03:15.000Z", "16 - GRAY Peterbilt|Jay Hoagland|2020-05-04T15:50:53.127Z|2020-05-04T16:54:05.000Z", "34 - GOLD Ford|Ethan Fellows|2020-05-05T13:29:56.063Z|2020-05-05T13:32:11.127Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T14:19:49.063Z|2020-05-12T14:26:12.000Z", "33 - RED Ford|Richard Fox|2020-05-05T18:57:10.063Z|2020-05-05T19:46:16.000Z", "15 - BLUE Peterbilt|Michael Wilson|2020-05-05T15:07:48.063Z|2020-05-05T15:40:02.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T18:16:08.063Z|2020-05-12T18:27:17.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T14:38:38.063Z|2020-05-19T14:50:30.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T15:19:46.063Z|2020-05-19T15:20:39.000Z", "34 - GOLD Ford|Ethan Fellows|2020-05-05T12:21:26.063Z|2020-05-05T12:27:54.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-06T10:30:15.127Z|2020-05-06T11:16:27.000Z", "39 - YELLOW Peterbilt|Kenneth Roberts|2020-05-26T19:44:58.063Z|2020-05-26T19:54:16.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-19T15:23:21.063Z|2020-05-19T15:28:11.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-19T13:23:57.063Z|2020-05-19T14:00:36.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-12T12:01:19.063Z|2020-05-12T12:11:33.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-19T15:02:53.063Z|2020-05-19T15:04:01.063Z", "40 - WHITE Ford|Randall Arthur|2020-05-26T16:11:51.063Z|2020-05-26T16:13:10.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-19T11:09:54.063Z|2020-05-19T11:40:23.000Z", "29 - WHITE Peterbilt|Jay Hoagland|2020-05-05T18:38:40.063Z|2020-05-05T18:49:56.000Z", "34 - GOLD Ford|Ethan Fellows|2020-05-26T14:08:24.063Z|2020-05-26T14:14:09.127Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T12:16:14.063Z|2020-05-05T12:21:26.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T15:53:58.000Z|2020-05-18T16:37:55.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-26T15:19:29.063Z|2020-05-26T16:18:25.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T12:10:17.063Z|2020-05-19T12:22:14.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T17:34:46.063Z|2020-05-18T17:41:04.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-18T16:27:08.063Z|2020-05-18T16:36:50.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-11T15:56:18.063Z|2020-05-11T15:56:55.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T19:25:32.063Z|2020-05-05T19:48:07.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-19T13:51:06.063Z|2020-05-19T13:53:48.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T18:49:39.063Z|2020-05-05T19:17:34.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-26T14:08:55.063Z|2020-05-26T14:10:45.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-19T14:21:41.063Z|2020-05-19T14:35:14.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T18:50:33.127Z|2020-05-26T19:00:18.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T14:56:28.063Z|2020-05-19T14:58:11.000Z", "36 - BLUE International|Bob Boutelle|2020-05-11T18:20:17.063Z|2020-05-11T18:29:50.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-12T09:41:31.063Z|2020-05-12T09:42:49.000Z", "36 - BLUE International|Bob Boutelle|2020-05-18T18:14:09.063Z|2020-05-18T18:14:23.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-11T16:05:28.063Z|2020-05-11T16:09:39.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-04T19:15:12.063Z|2020-05-04T21:31:24.000Z", "38 - BLUE Peterbilt|Mark Abraham|2020-05-12T09:00:49.063Z|2020-05-12T09:25:04.063Z", "32 - BLUE Ford|Bob Boutelle|2020-05-04T16:23:57.063Z|2020-05-04T16:30:11.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-08T20:33:50.063Z|2020-05-08T20:36:43.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-12T14:26:32.063Z|2020-05-12T14:55:22.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T14:34:55.063Z|2020-05-12T14:44:02.000Z", "40 - WHITE Ford|Randall Arthur|2020-05-26T14:41:48.127Z|2020-05-26T15:07:43.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T15:07:54.063Z|2020-05-26T15:16:45.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-26T10:16:20.063Z|2020-05-26T10:36:09.000Z", "38 - BLUE Peterbilt|Mark Abraham|2020-05-12T09:42:59.000Z|2020-05-12T09:59:15.063Z", "40 - WHITE Ford|Randall Arthur|2020-05-26T19:57:18.000Z|2020-05-26T20:14:22.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-12T15:06:13.063Z|2020-05-12T15:48:39.000Z", "33 - RED Ford|Richard Fox|2020-05-12T14:08:51.063Z|2020-05-12T14:12:27.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-12T13:44:35.127Z|2020-05-12T14:19:21.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T17:04:13.063Z|2020-05-04T17:10:46.000Z", "36 - BLUE International|Bob Boutelle|2020-05-12T18:29:56.127Z|2020-05-12T18:30:36.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-18T17:13:08.127Z|2020-05-18T17:16:03.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T13:51:01.127Z|2020-05-19T13:56:51.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-26T17:22:21.063Z|2020-05-26T17:49:51.000Z", "39 - YELLOW Peterbilt|Kenneth Roberts|2020-05-26T20:18:06.063Z|2020-05-26T20:59:06.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-19T09:29:24.063Z|2020-05-19T09:52:28.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-26T15:54:48.127Z|2020-05-26T15:55:03.063Z", "40 - WHITE Ford|Randall Arthur|2020-05-26T22:25:29.063Z|2020-05-26T22:28:39.000Z", "36 - BLUE International|Bob Boutelle|2020-05-12T18:35:59.063Z|2020-05-12T18:46:00.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-12T12:09:25.127Z|2020-05-12T12:10:17.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-12T14:58:09.127Z|2020-05-12T15:24:54.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-26T13:00:20.063Z|2020-05-26T13:00:31.000Z", "34 - GOLD Ford|Ethan Fellows|2020-05-05T16:56:28.063Z|2020-05-05T16:58:12.127Z", "28 - WHITE Ford|Ethan Fellows|2020-05-19T14:24:02.063Z|2020-05-19T14:27:25.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-04T18:36:27.063Z|2020-05-04T18:56:11.000Z", "28 - WHITE Ford|Ethan Fellows|2020-05-19T14:42:31.063Z|2020-05-19T14:48:26.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T16:55:26.063Z|2020-05-26T17:10:11.000Z", "34 - GOLD Ford|Ethan Fellows|2020-05-05T14:54:13.063Z|2020-05-05T15:07:04.127Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T19:25:22.063Z|2020-05-26T20:09:07.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T14:22:43.063Z|2020-05-19T14:22:58.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T17:32:06.063Z|2020-05-26T17:38:50.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-04T17:09:12.063Z|2020-05-04T17:10:43.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-05T10:34:53.063Z|2020-05-05T11:00:59.000Z", "36 - BLUE International|Bob Boutelle|2020-05-12T18:49:45.063Z|2020-05-12T18:54:21.000Z", "29 - WHITE Peterbilt|Ronald Harrower|2020-05-18T13:55:59.063Z|2020-05-18T14:07:38.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T12:00:34.000Z|2020-05-26T12:03:27.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T15:05:35.063Z|2020-05-05T15:06:27.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T13:11:25.063Z|2020-05-26T13:20:41.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-18T14:05:19.063Z|2020-05-18T14:59:05.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-19T14:59:21.063Z|2020-05-19T15:02:23.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-18T13:51:05.063Z|2020-05-18T14:23:13.000Z", "HOT SPARE - AIM Tractor 015039|Kenneth Moore|2020-05-11T19:29:43.063Z|2020-05-11T19:31:55.000Z", "40 - WHITE Ford|Randall Arthur|2020-05-26T20:28:21.063Z|2020-05-26T20:34:24.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-18T14:05:46.063Z|2020-05-18T14:09:08.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-26T14:34:26.063Z|2020-05-26T14:34:53.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-06T15:50:10.063Z|2020-05-06T15:58:23.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-05T15:23:13.063Z|2020-05-05T15:27:11.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T20:19:04.000Z|2020-05-26T20:20:12.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T17:35:52.063Z|2020-05-26T17:53:40.000Z", "24 - WHITE Transit|Luann Whitcomb|2020-05-06T15:01:17.063Z|2020-05-06T15:08:16.000Z", "39 - YELLOW Peterbilt|Kenneth Roberts|2020-05-26T19:12:31.063Z|2020-05-26T19:28:27.000Z", "39 - YELLOW Peterbilt|Kenneth Roberts|2020-05-26T12:46:04.063Z|2020-05-26T12:46:44.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-04T12:19:37.000Z|2020-05-04T12:20:59.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-18T14:01:06.127Z|2020-05-18T14:01:48.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-05T09:53:26.000Z|2020-05-05T10:29:36.000Z", "33 - RED Ford|Richard Fox|2020-05-05T19:52:15.000Z|2020-05-05T20:07:33.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-06T15:39:15.063Z|2020-05-06T15:44:41.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-06T14:23:26.000Z|2020-05-06T15:40:51.000Z", "40 - WHITE Ford|Troy Giordano|2020-05-06T14:57:40.063Z|2020-05-06T15:05:58.000Z", "40 - WHITE Ford|Randall Arthur|2020-05-26T21:00:52.063Z|2020-05-26T21:04:22.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-06T15:08:53.063Z|2020-05-06T15:15:15.127Z", "14 - BAFFIN Peterbilt|Mark Abraham|2020-05-27T07:30:58.063Z|2020-05-27T09:02:41.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-20T13:13:48.127Z|2020-05-20T13:35:24.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-01T10:56:51.063Z|2020-05-01T11:19:34.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-14T09:33:03.063Z|2020-05-14T09:54:15.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-06T15:25:04.063Z|2020-05-06T15:42:11.000Z", "36 - BLUE International|Bob Boutelle|2020-05-19T15:29:48.063Z|2020-05-19T15:42:12.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T16:46:22.127Z|2020-05-19T16:51:57.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-01T13:57:27.063Z|2020-05-01T15:32:50.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-06T14:58:59.063Z|2020-05-06T15:03:35.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-20T12:00:46.063Z|2020-05-20T12:19:06.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-06T14:08:13.063Z|2020-05-06T14:22:13.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-06T13:01:12.063Z|2020-05-06T13:04:51.000Z", "34 - GOLD Ford|Rick Schwenk|2020-05-13T15:31:39.063Z|2020-05-13T15:34:20.127Z", "14 - BAFFIN Peterbilt|Kenneth Roberts|2020-05-19T15:42:45.063Z|2020-05-19T16:40:46.000Z", "14 - BAFFIN Peterbilt|Kenneth Roberts|2020-05-20T12:24:47.063Z|2020-05-20T12:53:44.000Z", "36 - BLUE International|Bob Boutelle|2020-05-07T12:08:16.063Z|2020-05-07T12:16:56.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-19T16:44:37.063Z|2020-05-19T16:46:56.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-06T15:11:04.127Z|2020-05-06T15:16:11.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-06T13:34:11.063Z|2020-05-06T13:49:20.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-01T14:34:39.063Z|2020-05-01T14:43:00.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-01T14:51:56.127Z|2020-05-01T14:58:51.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-14T09:03:29.063Z|2020-05-14T09:19:20.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T20:02:51.063Z|2020-05-26T20:23:08.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-01T13:51:18.063Z|2020-05-01T14:01:21.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-13T20:33:06.063Z|2020-05-13T21:30:26.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-01T16:03:28.063Z|2020-05-01T16:06:40.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-07T13:07:56.063Z|2020-05-07T13:13:08.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-05T15:04:42.063Z|2020-05-05T15:15:25.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-18T19:13:37.063Z|2020-05-18T19:15:12.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-20T14:25:37.063Z|2020-05-20T14:26:23.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-14T09:41:32.000Z|2020-05-14T10:07:47.000Z", "33 - RED Ford|Michael Wilson|2020-05-20T14:21:43.127Z|2020-05-20T14:26:41.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T15:42:31.063Z|2020-05-26T15:54:13.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-01T15:27:37.063Z|2020-05-01T15:40:21.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-01T13:57:50.063Z|2020-05-01T13:59:20.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T16:27:10.127Z|2020-05-12T16:45:42.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-20T12:16:35.000Z|2020-05-20T13:10:05.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-19T11:39:32.127Z|2020-05-19T11:50:48.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-19T20:50:43.000Z|2020-05-19T20:51:18.000Z", "16 - GRAY Peterbilt|Troy Giordano|2020-05-27T12:59:08.063Z|2020-05-27T13:07:09.000Z", "14 - BAFFIN Peterbilt|Kenneth Roberts|2020-05-19T16:52:35.063Z|2020-05-19T16:53:51.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T14:40:57.063Z|2020-05-05T15:03:15.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-05T17:06:30.063Z|2020-05-05T17:09:51.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-12T14:47:21.063Z|2020-05-12T14:53:48.000Z", "36 - BLUE International|Bob Boutelle|2020-05-12T14:46:45.063Z|2020-05-12T15:13:13.000Z", "16 - GRAY Peterbilt|Troy Giordano|2020-05-27T11:11:49.000Z|2020-05-27T11:20:38.000Z", "14 - BAFFIN Peterbilt|Kenneth Roberts|2020-05-19T09:52:31.000Z|2020-05-19T10:29:23.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-27T12:56:03.063Z|2020-05-27T13:08:08.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T11:28:34.063Z|2020-05-12T11:34:53.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-12T12:36:53.063Z|2020-05-12T12:49:39.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T17:42:20.127Z|2020-05-05T17:43:59.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-12T11:17:41.063Z|2020-05-12T11:29:52.000Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-18T12:33:36.127Z|2020-05-18T13:16:55.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-13T11:47:10.063Z|2020-05-13T11:47:34.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-27T15:44:53.063Z|2020-05-27T17:14:22.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-01T10:21:26.063Z|2020-05-01T10:22:50.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-27T09:29:42.063Z|2020-05-27T10:19:13.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T14:09:18.063Z|2020-05-12T14:18:12.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-04T13:59:19.063Z|2020-05-04T14:09:26.000Z", "38 - BLUE Peterbilt|Mark Abraham|2020-05-12T11:45:17.063Z|2020-05-12T11:45:46.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-27T10:55:12.063Z|2020-05-27T10:56:43.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-06T12:12:21.063Z|2020-05-06T12:14:52.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T14:50:25.063Z|2020-05-26T14:59:58.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-26T14:31:40.063Z|2020-05-26T15:09:18.000Z", "29 - WHITE Peterbilt|Mark Abraham|2020-05-04T13:43:35.063Z|2020-05-04T14:03:05.000Z", "28 - WHITE Ford|Jay Hoagland|2020-05-26T18:33:22.063Z|2020-05-26T18:47:20.000Z", "34 - GOLD Ford|Rick Schwenk|2020-05-13T11:44:58.063Z|2020-05-13T11:55:12.127Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T12:50:13.000Z|2020-05-26T12:50:38.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T16:02:04.063Z|2020-05-26T16:05:01.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T14:56:59.063Z|2020-05-05T15:06:24.000Z", "33 - RED Ford|Richard Fox|2020-05-05T15:08:58.063Z|2020-05-05T15:12:34.000Z", "15 - BLUE Peterbilt|Michael Wilson|2020-05-05T15:50:27.063Z|2020-05-05T16:11:22.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-12T13:46:37.063Z|2020-05-12T13:51:39.000Z", "15 - BLUE Peterbilt|Michael Wilson|2020-05-05T14:24:56.063Z|2020-05-05T14:26:15.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-12T09:48:14.000Z|2020-05-12T10:41:29.000Z", "36 - BLUE International|Bob Boutelle|2020-05-12T18:59:29.063Z|2020-05-12T19:11:08.000Z", "34 - GOLD Ford|Ethan Fellows|2020-05-05T16:40:02.063Z|2020-05-05T16:50:42.127Z", "31 - WHITE Transit|Kevin Neidert|2020-05-18T16:17:57.063Z|2020-05-18T16:22:48.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T16:05:48.063Z|2020-05-05T16:14:12.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-04T11:54:32.063Z|2020-05-04T12:16:17.000Z", "36 - BLUE International|Bob Boutelle|2020-05-19T15:13:30.063Z|2020-05-19T15:19:36.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-01T08:30:35.063Z|2020-05-01T09:04:17.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-01T11:08:27.063Z|2020-05-01T11:15:28.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-20T11:39:17.127Z|2020-05-20T11:46:18.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-13T16:28:46.063Z|2020-05-13T16:31:01.000Z", "34 - GOLD Ford|Ethan Fellows|2020-05-26T14:41:39.000Z|2020-05-26T14:42:33.000Z", "16 - GRAY Peterbilt|Troy Giordano|2020-05-27T15:11:00.063Z|2020-05-27T15:14:49.000Z", "36 - BLUE International|Bob Boutelle|2020-05-19T19:04:57.063Z|2020-05-19T19:05:18.000Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-01T12:48:35.063Z|2020-05-01T13:16:32.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-05T15:00:06.063Z|2020-05-05T15:20:37.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-20T10:47:38.063Z|2020-05-20T11:32:22.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-18T16:39:53.063Z|2020-05-18T17:17:54.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-20T09:35:57.063Z|2020-05-20T10:50:04.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-13T16:53:25.063Z|2020-05-13T17:40:43.000Z", "33 - RED Ford|Richard Fox|2020-05-05T15:28:07.063Z|2020-05-05T15:31:41.000Z", "38 - BLUE Peterbilt|Mark Abraham|2020-05-12T12:51:06.063Z|2020-05-12T13:19:12.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T18:25:33.127Z|2020-05-05T18:33:23.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-07T11:01:37.063Z|2020-05-07T12:02:07.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-20T13:54:21.063Z|2020-05-20T13:56:27.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-04T15:52:27.063Z|2020-05-04T15:54:07.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-04T14:59:17.063Z|2020-05-04T16:15:32.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-11T15:41:25.063Z|2020-05-11T15:42:43.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T13:18:45.127Z|2020-05-18T13:26:51.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-04T16:02:23.063Z|2020-05-04T16:06:27.000Z", "36 - BLUE International|Bob Boutelle|2020-05-11T16:13:52.063Z|2020-05-11T16:23:39.063Z", "32 - BLUE Ford|Michael Wilson|2020-05-27T16:04:16.063Z|2020-05-27T16:07:56.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-01T14:28:10.063Z|2020-05-01T15:12:03.000Z", "39 - YELLOW Peterbilt|Kenneth Roberts|2020-05-26T17:59:56.063Z|2020-05-26T18:12:23.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-11T16:30:22.063Z|2020-05-11T16:32:44.000Z", "36 - BLUE International|Bob Boutelle|2020-05-11T15:35:15.063Z|2020-05-11T15:54:28.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-27T16:46:50.063Z|2020-05-27T16:50:52.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-04T17:09:17.063Z|2020-05-04T17:38:41.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-13T18:07:12.063Z|2020-05-13T18:07:52.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T17:33:56.063Z|2020-05-04T17:41:32.000Z", "29 - WHITE Peterbilt|Mark Abraham|2020-05-04T14:24:57.127Z|2020-05-04T15:14:46.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-19T14:48:53.063Z|2020-05-19T14:49:46.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-19T12:04:43.063Z|2020-05-19T12:08:27.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-19T12:21:24.063Z|2020-05-19T12:43:47.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-11T15:40:56.063Z|2020-05-11T16:58:14.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-18T13:25:01.063Z|2020-05-18T13:43:54.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-27T14:16:18.063Z|2020-05-27T15:00:22.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-01T14:01:58.063Z|2020-05-01T14:12:31.127Z", "42 - RED Peterbilt|Richard Fox|2020-05-20T12:10:13.063Z|2020-05-20T12:25:01.000Z", "28 - WHITE Ford|Ethan Fellows|2020-05-19T16:38:51.063Z|2020-05-19T16:40:25.000Z", "36 - BLUE International|Bob Boutelle|2020-05-19T16:04:52.063Z|2020-05-19T16:08:57.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-19T16:29:05.063Z|2020-05-19T16:29:40.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-06T14:36:46.063Z|2020-05-06T14:47:59.000Z", "34 - GOLD Ford|Ethan Fellows|2020-05-05T12:34:25.063Z|2020-05-05T12:43:41.127Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-26T14:02:54.063Z|2020-05-26T14:51:36.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T13:45:50.063Z|2020-05-12T13:45:50.063Z", "35 - PURPLE Peterbilt|Randall Arthur|2020-05-18T14:06:40.000Z|2020-05-18T16:23:26.000Z", "33 - RED Ford|Richard Fox|2020-05-12T12:54:41.063Z|2020-05-12T13:14:23.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-12T12:58:37.127Z|2020-05-12T13:01:53.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T14:19:02.063Z|2020-05-05T14:32:14.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-12T12:55:17.063Z|2020-05-12T12:59:37.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-26T15:14:02.063Z|2020-05-26T15:15:30.063Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-12T13:09:27.063Z|2020-05-12T13:09:54.063Z", "38 - BLUE Peterbilt|Mark Abraham|2020-05-12T11:47:12.063Z|2020-05-12T11:49:03.000Z", "40 - WHITE Ford|Randall Arthur|2020-05-26T21:13:21.063Z|2020-05-26T22:18:25.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T11:39:43.063Z|2020-05-12T12:38:16.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-06T16:05:43.063Z|2020-05-06T16:15:06.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-06T14:52:45.063Z|2020-05-06T15:01:38.000Z", "15 - BLUE Peterbilt|Michael Wilson|2020-05-05T13:42:23.063Z|2020-05-05T14:01:55.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-12T13:28:17.063Z|2020-05-12T13:28:55.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T13:56:35.063Z|2020-05-05T14:09:00.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-26T15:17:59.063Z|2020-05-26T16:14:25.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-23T17:12:07.063Z|2020-05-23T17:27:56.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T13:36:15.063Z|2020-05-12T13:40:46.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T16:52:48.063Z|2020-05-19T16:56:22.000Z", "34 - GOLD Ford|Carlos Rivera|2020-05-06T18:43:46.063Z|2020-05-06T18:57:14.127Z", "42 - RED Peterbilt|Richard Fox|2020-05-19T18:19:40.063Z|2020-05-19T18:28:40.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-12T13:35:37.063Z|2020-05-12T13:39:28.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-12T12:38:05.127Z|2020-05-12T13:00:09.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-27T12:07:12.063Z|2020-05-27T12:12:22.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-12T14:17:18.063Z|2020-05-12T14:30:47.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-19T16:57:54.063Z|2020-05-19T17:36:46.063Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T16:04:15.063Z|2020-05-19T16:22:32.063Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T17:01:01.063Z|2020-05-19T17:01:08.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-13T09:45:09.000Z|2020-05-13T10:57:45.000Z", "36 - BLUE International|Bob Boutelle|2020-05-19T17:51:19.063Z|2020-05-19T17:55:49.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T13:27:09.063Z|2020-05-12T13:28:50.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T14:09:23.063Z|2020-05-12T14:13:41.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-27T09:27:56.063Z|2020-05-27T09:49:16.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-18T15:53:36.063Z|2020-05-18T17:02:13.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-18T15:20:45.063Z|2020-05-18T17:55:06.063Z", "33 - RED Ford|Richard Fox|2020-05-12T14:23:53.063Z|2020-05-12T14:32:44.000Z", "34 - GOLD Ford|Carlos Rivera|2020-05-06T17:54:27.063Z|2020-05-06T17:55:47.127Z", "35 - PURPLE Peterbilt|Kenneth Moore|2020-05-07T07:50:13.000Z|2020-05-07T09:21:48.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-13T10:22:01.063Z|2020-05-13T10:57:38.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-27T10:36:39.063Z|2020-05-27T11:08:49.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-26T15:12:40.063Z|2020-05-26T15:13:07.000Z", "38 - BLUE Peterbilt|Randall Arthur|2020-05-06T15:47:14.063Z|2020-05-06T17:27:19.000Z", "38 - BLUE Peterbilt|Mark Abraham|2020-05-12T15:58:43.000Z|2020-05-12T16:03:06.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-19T17:46:56.063Z|2020-05-19T18:10:45.000Z", "15 - BLUE Peterbilt|Michael Wilson|2020-05-05T14:56:03.063Z|2020-05-05T14:59:53.000Z", "33 - RED Ford|Richard Fox|2020-05-12T15:27:19.127Z|2020-05-12T15:39:58.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T17:22:39.063Z|2020-05-19T17:23:03.000Z", "43 - BLUE Freightliner|Kenneth Moore|2020-05-13T09:30:47.063Z|2020-05-13T09:57:45.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T16:47:29.063Z|2020-05-18T17:09:49.000Z", "38 - BLUE Peterbilt|Mark Abraham|2020-05-12T13:43:58.063Z|2020-05-12T14:06:50.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T13:21:31.127Z|2020-05-12T13:24:29.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-19T12:00:19.063Z|2020-05-19T12:12:44.000Z", "29 - WHITE Peterbilt|Jay Hoagland|2020-05-05T12:40:32.063Z|2020-05-05T12:52:53.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-05T12:59:26.063Z|2020-05-05T13:03:38.063Z", "42 - RED Peterbilt|Bob Boutelle|2020-05-11T20:21:07.063Z|2020-05-11T20:22:18.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-18T17:15:59.063Z|2020-05-18T17:16:30.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-12T14:40:41.063Z|2020-05-12T14:44:23.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-19T10:39:46.063Z|2020-05-19T11:03:18.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-12T16:44:09.063Z|2020-05-12T16:52:56.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-06T19:49:52.063Z|2020-05-06T20:05:15.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-12T12:19:19.063Z|2020-05-12T12:31:04.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-18T15:22:36.127Z|2020-05-18T15:57:21.000Z", "29 - WHITE Peterbilt|Jay Hoagland|2020-05-05T13:26:58.063Z|2020-05-05T13:52:21.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-19T16:05:39.063Z|2020-05-19T16:18:58.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-13T09:15:32.063Z|2020-05-13T09:36:57.000Z", "35 - PURPLE Peterbilt|Kenneth Roberts|2020-05-12T14:27:58.063Z|2020-05-12T14:37:01.000Z", "36 - BLUE International|Bob Boutelle|2020-05-12T14:32:44.063Z|2020-05-12T14:38:32.000Z", "29 - WHITE Peterbilt|Jay Hoagland|2020-05-05T18:52:31.000Z|2020-05-05T18:56:30.000Z", "39 - YELLOW Peterbilt|Kenneth Roberts|2020-05-26T17:11:44.063Z|2020-05-26T17:26:30.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T12:52:57.063Z|2020-05-19T13:13:36.000Z", "36 - BLUE International|Bob Boutelle|2020-05-19T18:09:50.063Z|2020-05-19T18:14:59.063Z", "33 - RED Ford|Richard Fox|2020-05-05T18:08:30.063Z|2020-05-05T18:34:20.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-19T13:17:42.127Z|2020-05-19T13:34:24.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-04T09:05:50.063Z|2020-05-04T09:55:45.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-06T09:46:50.063Z|2020-05-06T10:09:51.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T17:14:25.063Z|2020-05-12T17:23:33.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T16:52:57.063Z|2020-05-12T16:59:49.000Z", "33 - RED Ford|Richard Fox|2020-05-12T17:49:25.063Z|2020-05-12T19:02:44.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T11:35:03.063Z|2020-05-19T12:04:41.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-26T11:53:57.063Z|2020-05-26T12:18:02.000Z", "33 - RED Ford|Richard Fox|2020-05-11T14:04:16.063Z|2020-05-11T14:09:56.000Z", "36 - BLUE International|Bob Boutelle|2020-05-26T14:42:53.063Z|2020-05-26T15:01:46.000Z", "31 - WHITE Transit|Troy Giordano|2020-05-18T14:12:04.063Z|2020-05-18T15:05:20.000Z", "29 - WHITE Peterbilt|Ronald Harrower|2020-05-18T12:30:03.063Z|2020-05-18T13:00:31.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-26T13:12:22.063Z|2020-05-26T13:24:01.000Z", "38 - BLUE Peterbilt|Kenneth Roberts|2020-05-18T15:12:40.063Z|2020-05-18T15:40:28.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-26T13:37:40.063Z|2020-05-26T13:43:04.000Z", "36 - BLUE International|Bob Boutelle|2020-05-11T14:42:39.063Z|2020-05-11T14:50:49.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-04T19:44:53.063Z|2020-05-04T19:45:42.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-04T12:24:12.127Z|2020-05-04T12:47:01.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-26T14:47:34.000Z|2020-05-26T14:51:15.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-04T12:27:42.127Z|2020-05-04T12:42:11.000Z", "16 - GRAY Peterbilt|Jay Hoagland|2020-05-04T12:21:54.127Z|2020-05-04T12:25:38.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-04T19:37:25.000Z|2020-05-04T19:38:13.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-11T13:22:13.063Z|2020-05-11T13:45:34.000Z", "32 - BLUE Ford|Carlos Rivera|2020-05-11T19:15:59.127Z|2020-05-11T19:37:54.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-04T12:03:01.063Z|2020-05-04T12:12:53.000Z", "40 - WHITE Ford|Rick Schwenk|2020-05-18T18:13:36.063Z|2020-05-18T18:32:57.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-26T14:23:28.127Z|2020-05-26T14:25:15.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-19T09:07:37.063Z|2020-05-19T09:09:31.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-05T11:36:02.063Z|2020-05-05T11:58:35.000Z", "38 - BLUE Peterbilt|Mark Abraham|2020-05-12T07:19:09.000Z|2020-05-12T08:47:56.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-26T15:08:14.063Z|2020-05-26T15:13:53.000Z", "39 - YELLOW Peterbilt|Kenneth Roberts|2020-05-26T14:38:31.063Z|2020-05-26T15:07:50.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-05T10:34:17.063Z|2020-05-05T11:12:07.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-26T13:44:57.063Z|2020-05-26T13:59:12.000Z", "33 - RED Ford|Richard Fox|2020-05-12T15:47:56.063Z|2020-05-12T16:06:56.000Z", "15 - BLUE Peterbilt|Michael Wilson|2020-05-05T11:41:12.000Z|2020-05-05T11:51:15.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-05T12:05:04.063Z|2020-05-05T12:05:52.000Z", "39 - YELLOW Peterbilt|Randall Arthur|2020-05-12T10:36:47.063Z|2020-05-12T11:19:41.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-26T14:35:10.063Z|2020-05-26T14:42:45.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-19T07:43:27.063Z|2020-05-19T07:52:13.000Z", "33 - RED Ford|Richard Fox|2020-05-11T18:46:00.000Z|2020-05-11T18:47:26.000Z", "37 - GOLD Peterbilt|Wayne Haney|2020-05-18T17:23:24.063Z|2020-05-18T19:09:11.000Z", "42 - RED Peterbilt|Ronald Harrower|2020-05-05T11:46:06.063Z|2020-05-05T12:09:03.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-11T18:08:46.063Z|2020-05-11T18:09:28.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T10:31:24.063Z|2020-05-12T11:10:31.000Z", "34 - GOLD Ford|Ethan Fellows|2020-05-05T12:11:12.063Z|2020-05-05T12:15:51.127Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-01T13:44:27.063Z|2020-05-01T13:52:20.000Z", "38 - BLUE Peterbilt|Kenneth Moore|2020-05-20T10:52:32.063Z|2020-05-20T11:14:49.000Z", "41 - SILVER Ford|Rick Schwenk|2020-05-20T12:29:30.063Z|2020-05-20T12:34:13.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-07T12:20:20.063Z|2020-05-07T12:32:50.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-01T11:40:36.127Z|2020-05-01T11:55:53.000Z", "37 - GOLD Peterbilt|Kenneth Roberts|2020-05-01T14:42:49.063Z|2020-05-01T15:08:54.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-12T13:23:28.063Z|2020-05-12T13:28:14.000Z", "16 - GRAY Peterbilt|Randall Arthur|2020-05-05T18:45:03.127Z|2020-05-05T18:54:24.063Z", "15 - BLUE Peterbilt|Ronald Harrower|2020-05-01T14:22:57.063Z|2020-05-01T14:29:38.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T15:44:10.073Z|2020-05-12T15:58:44.000Z", "42 - RED Peterbilt|Richard Fox|2020-05-27T18:23:58.063Z|2020-05-27T18:49:15.000Z", "14 - BAFFIN Peterbilt|Kenneth Roberts|2020-05-20T13:09:27.063Z|2020-05-20T13:30:36.000Z", "32 - BLUE Ford|Troy Giordano|2020-05-12T14:54:51.063Z|2020-05-12T15:38:42.000Z", "32 - BLUE Ford|Michael Wilson|2020-05-13T19:13:02.063Z|2020-05-13T20:09:52.000Z", "16 - GRAY Peterbilt|Ethan Fellows|2020-05-12T16:41:28.063Z|2020-05-12T16:46:38.000Z", "29 - WHITE Peterbilt|Jay Hoagland|2020-05-05T15:03:51.063Z|2020-05-05T15:09:46.000Z", "32 - BLUE Ford|Bob Boutelle|2020-05-05T18:22:12.063Z|2020-05-05T18:23:08.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-19T13:37:50.063Z|2020-05-19T13:42:49.000Z", "29 - WHITE Peterbilt|Jay Hoagland|2020-05-05T16:41:35.063Z|2020-05-05T16:44:14.000Z", "15 - BLUE Peterbilt|Jay Hoagland|2020-05-12T16:52:16.063Z|2020-05-12T17:09:54.000Z", "35 - PURPLE Peterbilt|Mark Abraham|2020-05-19T14:06:02.063Z|2020-05-19T14:24:08.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-19T12:52:13.063Z|2020-05-19T12:59:27.000Z", "14 - BAFFIN Peterbilt|Ronald Harrower|2020-05-26T16:01:22.063Z|2020-05-26T16:27:54.000Z", "29 - WHITE Peterbilt|Michael Wilson|2020-05-19T14:38:05.063Z|2020-05-19T14:46:29.000Z" };
        return tripData;
    }
    
    static {
        RestTrendingReport.lytxBehaviors = null;
        RestTrendingReport.periods = new LinkedHashMap<Integer, String[]>();
        RestTrendingReport.displayColumns = null;
        RestTrendingReport.lytxExceptionSummariesJson = null;
        RestTrendingReport.EXCEPTIONS_START_COLUMN = 3;
        RestTrendingReport.geotabVehicleExceptionSummariesJson = null;
        RestTrendingReport.dao = new GL_Report_DAO();
    }
}
