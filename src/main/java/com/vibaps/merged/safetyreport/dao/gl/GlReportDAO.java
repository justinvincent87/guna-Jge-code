package com.vibaps.merged.safetyreport.dao.gl;

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
import java.time.Instant;
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
import java.util.Map.Entry;
import java.util.TimeZone;

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
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lytx.dto.ExistingSessionRequest;
import com.lytx.dto.GetBehaviorsResponse;
import com.lytx.dto.GetEventsByLastUpdateDateRequest;
import com.lytx.dto.GetVehiclesRequest;
import com.lytx.dto.GetVehiclesResponse;
import com.lytx.services.ISubmissionServiceV5Proxy;
import com.vibaps.merged.safetyreport.entity.gl.GenDevice;
import com.vibaps.merged.safetyreport.entity.gl.GenDriver;
import com.vibaps.merged.safetyreport.entity.gl.GlRulelistEntity;
import com.vibaps.merged.safetyreport.entity.gl.ReportRow;
import com.vibaps.merged.safetyreport.entity.gl.Score;
import com.vibaps.merged.safetyreport.entity.gl.Trip;
import com.vibaps.merged.safetyreport.repo.gl.UserReportFilterRepository;

import lombok.extern.log4j.Log4j2;
@Log4j2
@Repository
public class GlReportDAO {
	
@Autowired	
private UserReportFilterRepository userReportFilterRepository;

@Autowired 
private CommonGeotabDAO commonGeotabDAO;

	
	private int ROW_OFFSET = -1;
	private int FORMULA_START_ROW = 7;

	String title;
	LocalDateTime startDate;
	LocalDateTime endDate;
	String reportBy;  //Vehicle or Driver
	 int minimumDistance=50;
//	List<String> selectedRuleNames;
	 Map<String, Integer> selectedRules;
	 List<ReportRow> reportRows;
	 Map<String, Map<String, Integer>> lytxVehicleEventsRecord;
	 Integer EXCEPTIONS_START_COLUMN  = 3;
	 List<String> displayReportColumnHeaders;
	 Map<String,List<Trip>> vehicleTrips;
     Map<Long, String> lytxVehicleList; 
     Map<Integer, String> lytxBehaviors;  
     List<Score> topNRecords;
     List<Score> bottomNRecords;
     Integer N_TOP_BOTTOM_RECORDS = 10;
     List<Map.Entry<String, Integer>> loadSelectedRuleNames;
    
	public Object view(String companyid,String db) {
		Map<String, Object> result = new HashMap<>();
		List<GlRulelistEntity> obj = null;
		List<GlRulelistEntity> obj1 = null;

		Integer countuser = null;
		
		try {

			countuser=userReportFilterRepository.userCount(companyid, db);
		
		} catch (Exception e) 
		{
			log.error("error occured : userReportFilterRepository->userCount",e);
		}	


		
		if(countuser.intValue() ==0) { 
			try {

		 BigInteger newUserId = userReportFilterRepository.userCreation(companyid, db);

		  
		  
		  if(newUserId.intValue() != 0) {
		  
		  try { 
			  
			  obj1=userReportFilterRepository.getRuleList();
		  
		  Iterator it = obj1.iterator(); 
		  while(it.hasNext())
		  {
		  Object[] line = (Object[]) it.next(); 
		  userReportFilterRepository.insertUserRuleList(newUserId, Integer.parseInt(line[0].toString()), Integer.parseInt(line[1].toString()), Integer.parseInt(line[2].toString()));

		  }		  
		  } catch (Exception e) {
				log.error("error occured : userReportFilterRepository->insertUserRuleList",e);

		  }

		  
		  }
		  
		  
		  
		  } catch (Exception e) { 
			  
				log.error("error occured :userReportFilterRepository->userCreation",e);

			  
		  }
		  
		 
		  }
		
		 
		
		
		/*
		 * try { Session session = HibernateUtil.getsession(); Transaction transaction =
		 * session.beginTransaction(); obj = session.
		 * createSQLQuery("select * from (select a.id,CONCAT(a.rulecompany,'-',a.rulename) as value,rulevalue,a.rulecompany,b.status,b.weight,d.minmiles from gl_rulelist a,gl_selectedvalues b,gen_user c,gl_minmiles d where c.companyid=:userid and c.db=:db and b.gen_user_id=c.id and d.gen_user_id=c.id and a.id=b.gen_rulelist_id order by value) as value order by value.status desc"
		 * ).setParameter("userid", geouserid).setParameter("db",
		 * db).setResultTransformer((ResultTransformer)Transformers.ALIAS_TO_ENTITY_MAP)
		 * .list(); transaction.commit(); } catch (Exception exception) {}
		 */
		return countuser;
	}
	
	public  Object viewadd(String geotabUserid,String db) {
	
		List<GlRulelistEntity> obj = null;

		try {
			obj = userReportFilterRepository.viewadd(geotabUserid, db);
		} catch (Exception exception) {}
		return obj;
	}
	
	public Object getgeodropdown(String geotabUserid,String db) {

		List<GlRulelistEntity> obj = null;

		try {

			obj = userReportFilterRepository.getgeodropdown(geotabUserid,db);
		} catch (Exception exception) {}
		return obj;
	}
	
	public Object getLybehave(String geouserid,String db) {
		
		List<GlRulelistEntity> obj = null;
		
		try {
		obj = userReportFilterRepository.getLybehave(geouserid, db);
		} catch (Exception exception) {}
		return obj;
	}
	
	public List getallbehave(String geotabUserid,String db) {
	
		List obj = new ArrayList();
		
		try {
		
			obj = userReportFilterRepository.getallbehave(geotabUserid, db);
			
		} catch (Exception exception) {}
		return obj;
	}
	
	public List getallbehaveui(String geotabUserid,String db) {
		
		List obj = new ArrayList();
		
		try {
			obj = userReportFilterRepository.getallbehave(geotabUserid, db);
		} catch (Exception exception) {}
		return obj;
	}
	
	//For Report
	public  List<Object[]> getallBehaveFromDB(String geotabUserid,String db) {
		List<Object[]> obj = null;
		
		try {
			
			obj = userReportFilterRepository.getallBehaveFromDB(geotabUserid, db);
		} catch (Exception exception) {}
		return obj;
	}
	
	
	public int geoCount(String geotabUserid,String db) {
		int obj = 0;
		try {
			obj = userReportFilterRepository.geoCount(geotabUserid, db);
			}
		catch (Exception exception) {}
		return obj;
	}
	
	public int lyCount(String geotabUserid,String db) {
		int obj = 0;
		try {
			obj = userReportFilterRepository.lyCount(geotabUserid, db);

		} catch (Exception exception) {}
		return obj;
	}
	
	public  int getwe(String geotabUserid, String rule,String db) {
		int obj = 0;
		try {
			obj = userReportFilterRepository.getWeight(geotabUserid, db, rule);
		} catch (Exception exception) {}
		return obj;
	}
	
	public float getminmiles(String geotabUserid,String db) {
		float obj = 0.0F;
		try {
			obj=userReportFilterRepository.getminmiles(geotabUserid, db);
		} catch (Exception exception) {}
		return obj;
	}
	public Object updateresponce(String geotabUserId, String responseJson,String db) {
	
		try {
			userReportFilterRepository.updateresponce(geotabUserId, db, responseJson);
			
		} catch (Exception exception) {}
		return "Data Inserted";
	}
	public String selectresponce(String geotabuUserId,String db) {
		// TODO Auto-generated method stub
		String i = "";
		try {
			i = userReportFilterRepository.selectresponce(geotabuUserId, db);
		} catch (Exception exception) {}
		return i;
	}
	
	public Object insert(ArrayList<GlRulelistEntity> v, String companyid, String minmiles,String db) {
		Map<String, String> result = new HashMap<>();
		
		int i = 0;
		try {
			i=userReportFilterRepository.getRuleListInsert(companyid, db, Float.valueOf(Float.parseFloat(minmiles)));
			
		} catch (Exception exception) {}
		int j = 0;
		if (i > 0)
			try {
				for (int d = 0; d < v.size(); d++) {
				
					userReportFilterRepository.updateRuleListValue(companyid, db, Integer.valueOf(((GlRulelistEntity)v.get(d)).getWeight()), ((GlRulelistEntity)v.get(d)).getRulevalue());
					result.put("result", "Rules list saved");
				} 
			} catch (Exception exception) {} 
		return result;
	}
	
	public List<GenDevice> deviceName(String geotabUserId,String db) {
		List<GenDevice> deviceNameList=new ArrayList<GenDevice>();
		
		List<GenDevice> list=null;
		
		Session session = null;
		Map<String,String> obj = new LinkedHashMap<String, String>();;
		Transaction transaction = null;
		try {
			
			list =userReportFilterRepository.deviceInfo(geotabUserId, db);
			} catch (Exception exception) {}
		

Iterator it = list.iterator();
while(it.hasNext()){
     Object[] line = (Object[]) it.next();
     GenDevice eq = new GenDevice();
     eq.setDeviceId(line[0].toString());
     eq.setDeviceName(line[1].toString());

     deviceNameList.add(eq);
}

		return deviceNameList;
	}
	
	public Object getReportGeo(String startDate,String endDate,String geosees,
			ArrayList<String> geotabgroups,String userName,
			String geodatabase,String url,String filename,
			String templect,String enttype)
	{
		String responseJson="";
		Object getgeodropdown = getgeodropdown(userName,geodatabase);
		ArrayList<String> getl = (ArrayList<String>) getgeodropdown;
		String value = "";
		Map<String, Map<String, String>> combinedReport = new HashMap<>();
		List<String> displayColumns = null;
		Map<Integer, String> lytxBehaviors = null;
		try {
		
			String geotabDriverExceptionSummariesJson=geotabVechileDriverResponce(startDate, endDate, getl, url, geotabgroups, enttype, userName, geodatabase, geosees);
			try {

				// load the header for report data (from the database based on the userName in
				// actual application)
				displayColumns = loadReporColumntHeaders(userName,geodatabase);

				// Load Lytx vehicle map with vehicleId and names

				// Map<Long, String> lytxVehicleList = loadLytxVehicleIDNameMap();
				// lytxBehaviors= loadLytxBehaviors();
				// String[] lytxBehaviorsArray = new String[lytxBehaviors.size()];

				int bCount = 0;

//	        	//create report object with Geotab VEHICLE data:
//	    		Map<String, Map<String, String>> combinedReport = extractGeotabVehicleData(geotabVehicleExceptionSummariesJson);

				// create report object with Geotab DRIVER data:
				if (enttype.equals("Driver")) {
					combinedReport = extractGeotabDriverData(geotabDriverExceptionSummariesJson, userName,geodatabase);
				} else {
					
					combinedReport = extractGeotabVehicleData(geotabDriverExceptionSummariesJson, userName,geodatabase);

					//System.out.println(combinedReport+"-fdf---");
				}

				responseJson=createJsonForGeotabResponce(displayColumns, combinedReport);
				
//System.out.println(responseJson);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("error occured",e);
			}
		} catch (Exception exception) {
		}

		try {
			updateresponce(userName, responseJson,geodatabase);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return responseJson;
		
	}
	
	public String createJsonForGeotabResponce(List<String> displayColumns,Map<String, Map<String, String>> combinedReport)
	{
		String responseJson = "";
		List<Integer> totals = new ArrayList<>();


		StringBuffer combinedReportResponseJson = new StringBuffer();

		// create a json response
		totals = new ArrayList<Integer>();
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
	
	public String geotabVechileDriverResponce(String startDate,String endDate,ArrayList<String> getl,String url,ArrayList<String> geotabgroups,String enttype,String userName,
			String geodatabase,String geosees) throws MalformedURLException, IOException, ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sDate = startDate;
		String eDate = endDate;
		Date ssdate = sdf.parse(sDate);
		Date eedate = sdf.parse(eDate);
		String gvalue = "";
		for (int j = 0; j < getl.size(); j++) {
			if (j != getl.size() - 1) {
				gvalue = gvalue + "{\"id\":\"" + (String) getl.get(j) + "\"},";
			} else {
				gvalue = gvalue + "{\"id\":\"" + (String) getl.get(j) + "\"}";
			}
		}
		String groupvalue = "";
		for (int i = 0; i < geotabgroups.size(); i++) {
			if (i != geotabgroups.size() - 1) {
				groupvalue = groupvalue + "{\"id\":\"" + (String) geotabgroups.get(i) + "\"},";
			} else {
				groupvalue = groupvalue + "{\"id\":\"" + (String) geotabgroups.get(i) + "\"}";
			}
		}
		String uri = "https://" + url + "/apiv1";
		String urlParameters = "{\"method\":\"ExecuteMultiCall\",\"params\":{\"calls\":[{\"method\":\"GetReportData\",\"params\":{\"argument\":{\"runGroupLevel\":-1,\"isNoDrivingActivityHidden\":true,\"fromUtc\":\""
				+ sDate + "T01:00:00.000Z\",\"toUtc\":\"" + eDate + "T03:59:59.000Z\",\"entityType\":\"" + enttype
				+ "\",\"reportArgumentType\":\"RiskManagement\",\"groups\":[" + groupvalue
				+ "],\"reportSubGroup\":\"None\",\"rules\":[" + gvalue
				+ "]}}},{\"method\":\"Get\",\"params\":{\"typeName\":\"SystemSettings\"}}],\"credentials\":{\"database\":\""
				+ geodatabase + "\",\"sessionId\":\"" + geosees + "\",\"userName\":\"" + userName + "\"}}}";

		String serverurl = uri;
		
	//	System.out.println(uri+urlParameters);
		
		
		
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

		String geotabDriverExceptionSummariesJson = "{\"result\":" + o.getAsJsonArray("result").get(0).toString()
				+ "}";
		
		return geotabDriverExceptionSummariesJson;
	}

	@SuppressWarnings("unchecked")
	public List<GenDriver> driverName(String geotabUserId,String db) {
		
		Session session = null;
		Map<String,String> obj = new LinkedHashMap<String, String>();
		List<GenDriver> driverNameList=new ArrayList<GenDriver>();
		List<GenDriver> list=new ArrayList();

		
		Transaction transaction = null;
		try {
			list=userReportFilterRepository.getDriverInfo(geotabUserId, db);
						
		} catch (Exception exception) {
		}
		
		if(list.size() > 0)
		{
			for(int i=0;i<list.size();i++)
			{
				GenDriver eq = new GenDriver();
			     eq.setDriverId(list.get(i).getDriverId());
			     eq.setDriverName(list.get(i).getDriverName());
			     driverNameList.add(eq);
			}
		}

		
		return driverNameList;
	}
	//Report Data
	
	
	
	public String processforNonLytx(String sdate, String edate, String geosees, ArrayList<String> geotabgroups, String geouname, String geodatabase, String url, String filename, String templect, String enttype) throws ParseException, MalformedURLException, IOException {
		
		reportBy=enttype;

		 displayReportColumnHeaders=loadReporColumntHeaders(geouname,geodatabase);


title="";
//Vehicle or Driver
//List<String> selectedRuleNames;
Map<String, Integer> selectedRules = new LinkedHashMap<String, Integer>();
reportRows = new ArrayList<ReportRow>();

List<String> displayReportColumnHeaders=new ArrayList<String>();
vehicleTrips=new LinkedHashMap<String, List<Trip>>();
lytxVehicleList=new LinkedHashMap<Long, String>(); 
lytxBehaviors=new LinkedHashMap<Integer, String>();  
topNRecords=new ArrayList<Score>();
bottomNRecords=new ArrayList<Score>();


		if(reportBy.equalsIgnoreCase("Driver")) {
			//Load Trips data to get driver data corresponding to Vehicles;
			vehicleTrips = loadVehicleTripsMap(geouname,geodatabase,geosees,url,sdate,edate);
		}
		
		//process GEOTAB exceptions response
		if(reportBy.equalsIgnoreCase("Driver")) {
			System.out.println("COMBINED REPORT - DRIVER");
			extractGeotabDriverData(getGeotabDriverExceptionSummariesResponseJson(sdate,edate,geouname,geotabgroups,geodatabase,geosees,url,enttype),geouname);
		} else {
			System.out.println("COMBINED REPORT - VEHICLE");
			extractGeotabVehicleData(getGeotabVehicleExceptionSummariesResponseJson(sdate,edate,geouname,geotabgroups,geodatabase,geosees,url,enttype),geouname);
		}
		
		//process LYTX exceptions response
		//Create a Map of lytx vehicleIds to exception map
    //   lytxVehicleEventsRecord = extractExceptionDataFromLytxResponse(getLytxExceptionSummariesResponseJson(sdate,edate,sees,groupid),sees);
        
       //System.out.println(lytxVehicleEventsRecord+"----");
        
        //combine Lytx exceptions data with the geotab exception report
        //updateCombinedReportWithLytxExceptions(lytxVehicleEventsRecord,geouname);

	/*
	 * calculateTopBottomNRecords(); //Print the top scores for(int i = 0; i <
	 * topNRecords.size(); i++) { System.out.println("Top " + (i+1)+" - " +
	 * topNRecords.get(i).getName() + ", " +
	 * topNRecords.get(i).getMeanWeightedAvgScores()); } //Print the bottom scores
	 * for(int i = 0; i < bottomNRecords.size(); i++) { System.out.println("Bottom "
	 * + (i+1)+" - " + bottomNRecords.get(i) + ", " +
	 * bottomNRecords.get(i).getMeanWeightedAvgScores()); }
	 */
    	
    	
    	
		String reportResponseJson = createReportReponseJson(geouname);
		 try
			{
			 updateresponce(geouname,reportResponseJson,geodatabase);
			}catch (Exception e) {
				// TODO: handle exception
			}
		 System.out.println(reportResponseJson);
		return reportResponseJson;
	}
	

	
		
		public String process(String sees, String sdate, String edate, String groupid, String geosees, ArrayList<String> geotabgroups, String geouname, String geodatabase, String url, String filename, String templect, String enttype,String endpoint) throws ParseException, MalformedURLException, IOException {
			
			reportBy=enttype;

			 displayReportColumnHeaders=loadReporColumntHeaders(geouname,geodatabase);


 title="";
  //Vehicle or Driver
//List<String> selectedRuleNames;
Map<String, Integer> selectedRules = new LinkedHashMap<String, Integer>();
reportRows = new ArrayList<ReportRow>();

 List<String> displayReportColumnHeaders=new ArrayList<String>();
  vehicleTrips=new LinkedHashMap<String, List<Trip>>();
 lytxVehicleList=new LinkedHashMap<Long, String>(); 
 lytxBehaviors=new LinkedHashMap<Integer, String>();  
 topNRecords=new ArrayList<Score>();
 bottomNRecords=new ArrayList<Score>();
 

			if(reportBy.equalsIgnoreCase("Driver")) {
				//Load Trips data to get driver data corresponding to Vehicles;
				vehicleTrips = loadVehicleTripsMap(geouname,geodatabase,geosees,url,sdate,edate);
			}
			
			//process GEOTAB exceptions response
			
			
			if(reportBy.equalsIgnoreCase("Driver")) {
				System.out.println("COMBINED REPORT - DRIVER");
				extractGeotabDriverData(getGeotabDriverExceptionSummariesResponseJson(sdate,edate,geouname,geotabgroups,geodatabase,geosees,url,enttype),geouname);
			} else {
				System.out.println("COMBINED REPORT - VEHICLE");
				extractGeotabVehicleData(getGeotabVehicleExceptionSummariesResponseJson(sdate,edate,geouname,geotabgroups,geodatabase,geosees,url,enttype),geouname);
			}
			
			//process LYTX exceptions response
			//Create a Map of lytx vehicleIds to exception map
	       lytxVehicleEventsRecord = extractExceptionDataFromLytxResponse(getLytxExceptionSummariesResponseJson(sdate,edate,sees,groupid,endpoint),sees,endpoint);
	        
	     //  System.out.println(lytxVehicleEventsRecord+"----");
	        
	        //combine Lytx exceptions data with the geotab exception report
	        updateCombinedReportWithLytxExceptions(lytxVehicleEventsRecord,geouname);

		/*
		 * calculateTopBottomNRecords(); //Print the top scores for(int i = 0; i <
		 * topNRecords.size(); i++) { System.out.println("Top " + (i+1)+" - " +
		 * topNRecords.get(i).getName() + ", " +
		 * topNRecords.get(i).getMeanWeightedAvgScores()); } //Print the bottom scores
		 * for(int i = 0; i < bottomNRecords.size(); i++) { System.out.println("Bottom "
		 * + (i+1)+" - " + bottomNRecords.get(i) + ", " +
		 * bottomNRecords.get(i).getMeanWeightedAvgScores()); }
		 */
	    	
	    	
	    	
			String reportResponseJson = createReportReponseJson(geouname);
			 try
				{
				 updateresponce(geouname,reportResponseJson,geodatabase);
				}catch (Exception e) {
					// TODO: handle exception
				}
			 System.out.println(reportResponseJson);
			return reportResponseJson;
		}

		/**
		 * @param geotabDriverExceptionSummariesResponseJson
		 */
		private void extractGeotabDriverData(String geotabDriverExceptionSummariesResponseJson,String userName) {

			try
			{
			
		//	List<String> displayReportColumnHeaders = loadReporColumntHeaders(userName);
			JSONObject geotabEventsJO = new JSONObject(geotabDriverExceptionSummariesResponseJson);
			JSONArray geotabEventsJOArray = geotabEventsJO.getJSONArray("result");
		
			
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
					
					String geotabExceptionName = "G-" + geotabExceptionRuleJO.getString("name");
					
					//System.out.println(geotabExceptionName+"-"+eventCount);
					geotabExceptionEvents.put(geotabExceptionName, geotabExceptionEvents.get(geotabExceptionName)==null?eventCount:geotabExceptionEvents.get(geotabExceptionName)+eventCount);
					}
				}
				for(int m=EXCEPTIONS_START_COLUMN; m < displayReportColumnHeaders.size(); m++) {
					if(geotabExceptionEvents.get(displayReportColumnHeaders.get(m)) != null) {
						reportRow.getSelectedRules().put(displayReportColumnHeaders.get(m), (geotabExceptionEvents.get(displayReportColumnHeaders.get(m))));
					} else {
						if(reportRow.getSelectedRules().get(displayReportColumnHeaders.get(m)) == null){
							reportRow.getSelectedRules().put(displayReportColumnHeaders.get(m), 0);
						}	
					}
				}
				reportRows.add(reportRow);
			}
			}catch (Exception e) {
				// TODO: handle exception
			}
		}

		/**
		 * @param geotabVehicleExceptionSummariesResponseJson
		 */
		private void extractGeotabVehicleData(String geotabVehicleExceptionSummariesResponseJson,String userName) {
			//GEOTAB Events processing

			//System.out.println(geotabVehicleExceptionSummariesResponseJson);
			try
			{
				// displayReportColumnHeaders=loadReporColumntHeaders(userName);
			
			//System.out.println(displayReportColumnHeaders.toString());
			//System.out.println("res : "+geotabVehicleExceptionSummariesResponseJson);
			JSONObject geotabEventsJO = new JSONObject(geotabVehicleExceptionSummariesResponseJson);
			JSONArray geotabEventsJOArray = geotabEventsJO.getJSONArray("result");
			
		
			for (int i = 0; i < geotabEventsJOArray.length(); i++) {

				ReportRow reportRow = new ReportRow();
			    JSONObject resultsChild = geotabEventsJOArray.getJSONObject(i);
			    JSONObject itemJO = resultsChild.getJSONObject("item");
			    //vehicleName
			    String geotabVehicleName = itemJO.getString("name");
			    reportRow.setName(geotabVehicleName);
				//group
				JSONArray geotabVehicleGroups = itemJO.getJSONArray("groups");
				String group = null;
				for(int j = 0; j < geotabVehicleGroups.length(); j++) {
					if(group == null) {
						group = geotabVehicleGroups.getJSONObject(j).getString("name");
					} else {
						String newGroup = geotabVehicleGroups.getJSONObject(j).getString("name");
						if("Prohibit Idling".equalsIgnoreCase(newGroup)) {
							group = newGroup + ", " + group;
						} else {
							group = group + ", " + newGroup;
						}
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

				try {
				for(int k = 0; k < geotabExceptionSummariesJA.length(); k++) {
					if(!geotabExceptionSummariesJA.isNull(k))
					{
					int eventCount = geotabExceptionSummariesJA.getJSONObject(k).getInt("eventCount");

					JSONObject geotabExceptionRuleJO = geotabExceptionSummariesJA.getJSONObject(k).getJSONObject("exceptionRule");

					String geotabExceptionName = "G-" + geotabExceptionRuleJO.getString("name");

					geotabExceptionEvents.put(geotabExceptionName, geotabExceptionEvents.get(geotabExceptionName)==null?eventCount:geotabExceptionEvents.get(geotabExceptionName)+eventCount);
					}

				}
				}catch (Exception e) {
					// TODO: handle exception
					System.out.println(e+"000");
				}

				for(int m=3; m < displayReportColumnHeaders.size(); m++) {
					if(geotabExceptionEvents.get(displayReportColumnHeaders.get(m)) != null) {
						reportRow.getSelectedRules().put(displayReportColumnHeaders.get(m), (geotabExceptionEvents.get(displayReportColumnHeaders.get(m))));
					} else {
						if(reportRow.getSelectedRules().get(displayReportColumnHeaders.get(m)) == null){
							reportRow.getSelectedRules().put(displayReportColumnHeaders.get(m), 0);
						}	
					}
				}
				reportRows.add(reportRow);
		
			}	}catch (Exception e) {
				// TODO: handle exception
				
				System.out.println("json"+e);
			}
			
		}
	    
	    //cleaned - in use
		/**
		 * @param getLytxExceptionSummariesResponseJson
		 * @throws RemoteException 
		 */
		private Map<String, Map<String, Integer>> extractExceptionDataFromLytxResponse(String getLytxExceptionSummariesResponseJson,String lytxSess,String endpoint) throws RemoteException {
			//Load Lytx vehicle map with vehicleId and names
			
			lytxBehaviors=null;
		 lytxVehicleList=null;
			if(lytxVehicleList == null) {
				 lytxVehicleList=new LinkedHashMap<Long, String>();
		        lytxVehicleList = loadLytxVehicleID_NameMap(getLytxVehicleID_NameResponseJson(lytxSess,endpoint));
			}
	 		//Load Lytx Behaviours map
			if(lytxBehaviors == null) {
				lytxBehaviors=new LinkedHashMap<Integer, String>();
				lytxBehaviors = loadLytxBehaviors(getLytxBehaviorsResponseJson(lytxSess,endpoint));  

			}
			//Process lytxExceptionSummariesJson
			lytxVehicleEventsRecord = new HashMap<String, Map<String, Integer>>();
		
			
			JSONObject lytxEventsJO = new JSONObject(getLytxExceptionSummariesResponseJson);
			JSONArray lytxEventsArray = lytxEventsJO.getJSONArray("events");
			for (int i = 0; i < lytxEventsArray.length(); i++) {
			    Long eventsVehicleId = lytxEventsArray.getJSONObject(i).getLong("vehicleId");
			    String vehicleName = lytxVehicleList.get(eventsVehicleId);
			    
			    
			    Map<String, Integer> lytxExceptionEvents = lytxVehicleEventsRecord.get(vehicleName);
			    if(lytxExceptionEvents == null) {
			    	lytxExceptionEvents = new HashMap<String, Integer>();
			    	
				
			    	lytxVehicleEventsRecord.put(vehicleName, lytxExceptionEvents);
			    	

			    }
				JSONArray lytxBehavioursArray = lytxEventsArray.getJSONObject(i).getJSONArray("behaviors");
				for(int j = 0; j < lytxBehavioursArray.length(); j++) {
					int behavior = lytxBehavioursArray.getJSONObject(j).getInt("behavior");
					String exceptionName = lytxBehaviors.get(behavior);
					Integer behaviorCount = lytxExceptionEvents.get(exceptionName); 
					if(behaviorCount == null) {
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
		 */
		private void updateCombinedReportWithLytxExceptions(Map<String, Map<String, Integer>> lytxVehicleEventsRecord,String userName) {
			//for every vehicle in lytxVehicleEventsRecord
			// displayReportColumnHeaders=loadReporColumntHeaders(userName);
			
			for (Map.Entry<String, Map<String, Integer>> lytxVehiclesEventsMapEntry : lytxVehicleEventsRecord.entrySet()) {
				//Get the report row corresponding to that vehicle.
				String lytxVehicleName = lytxVehiclesEventsMapEntry.getKey();
				
				//possible performance issue here.  Better to use Maps.
				ReportRow reportRow = null;
				for(ReportRow row : reportRows) {
					if(row.getName().equalsIgnoreCase(lytxVehicleName)) {
						reportRow = row;
						break;
					}
				}
				//if the lytxVehicle is not in the Geotab's vehicle list, then skip.
				if(reportRow == null){
					continue;
				} 
				Map<String, Integer> lytxVehExceptions = lytxVehiclesEventsMapEntry.getValue();
				for(int m=EXCEPTIONS_START_COLUMN; m < displayReportColumnHeaders.size(); m++) {
					if(lytxVehExceptions.get(displayReportColumnHeaders.get(m)) != null) {
						//System.out.println(lytxVehicleName+"-"+displayReportColumnHeaders.get(m)+"--"+lytxVehExceptions.get(displayReportColumnHeaders.get(m)));
						
						reportRow.getSelectedRules().put(displayReportColumnHeaders.get(m),lytxVehExceptions.get(displayReportColumnHeaders.get(m)));
					}
				}
			}
		}

		public String createReportReponseJson(String userName) {
	        //create a json response
			
			// displayReportColumnHeaders=loadReporColumntHeaders(userName,db);
			String responseJson = null;
	        StringBuffer combinedReportResponseJson = new StringBuffer();
	    	try {
	        	List<Integer> totals = new ArrayList<Integer>();
	        	for(int q=0; q<displayReportColumnHeaders.size(); q++) {
	        		totals.add(0);
	        	}
	            combinedReportResponseJson.append("\"information\": [");
	            boolean firstRow = true;
	            int rulesRecords = displayReportColumnHeaders.size()-EXCEPTIONS_START_COLUMN;
//	          for (Map.Entry<String, Map<String, String>> combinedReportRows : combinedReport.entrySet()) {
	           	for (ReportRow row : reportRows ) {	
	            	if(!firstRow){
	            		combinedReportResponseJson.append(",");
	            	} else {
	            		firstRow = false;
	            	}
	            	combinedReportResponseJson.append("{");
	                boolean rulesHeadedAdded = false;
	            	int headerCount = 0;
	            	int rowItemCount = 0;
	       			rulesHeadedAdded = false;
	           		combinedReportResponseJson.append("\"" + "VehicleName" + "\": \"" + row.getName() + "\"");
	    			combinedReportResponseJson.append(",");
	           		combinedReportResponseJson.append("\"" + "Group" + "\": \"" + row.getGroup() + "\"");
	    			combinedReportResponseJson.append(",");
	           		combinedReportResponseJson.append("\"" + "Distance" + "\": \"" + row.getDistance() + "\"");
	    			combinedReportResponseJson.append(",");
	    			rowItemCount = 4;
	    			
	    			int counter=2;
	    			
	            	for(Map.Entry<String,Integer> data : row.getSelectedRules().entrySet()) {
	            		counter++;
	            		if(headerCount++ > 0 && headerCount<displayReportColumnHeaders.size()+1){
	            			combinedReportResponseJson.append(",");
	            		}
	            			if(!rulesHeadedAdded) {
	                			combinedReportResponseJson.append("\"Behave\": [");
	                			rulesHeadedAdded = true;
	            			}
	                    	combinedReportResponseJson.append("{");
	                		combinedReportResponseJson.append("\"Rule\": \"" + data.getValue() + "\"}");
	                		//System.out.println(row.getName()+displayReportColumnHeaders.get(counter)+"--"+data.getValue());
	                		
	                		totals.set(rowItemCount-1, (totals.get(rowItemCount-1) + data.getValue()));
	                    	if(rowItemCount++ == displayReportColumnHeaders.size()) {
	                    		combinedReportResponseJson.append("]");
	                    	}
	            		
	            	}
	             	combinedReportResponseJson.append("}");
	            }
	            combinedReportResponseJson.append("]}");
	    		
	            StringBuffer totalsJson = new StringBuffer();
	            totalsJson.append("{\"totals\": [");
	            int ruleCounter = 0;
	            for(int totalVal : totals) {
	            	totalsJson.append("{ \"Rule\": \"" + totalVal + "\" }");
	            	ruleCounter++;
	            	if(ruleCounter != displayReportColumnHeaders.size()) {
	            		totalsJson.append(",");
	            	}
	            }
	            totalsJson.append("],");
	            
	            responseJson = totalsJson.toString() + combinedReportResponseJson.toString();
	            System.out.println(responseJson);
	            
	            // you will get OUTPUT like this below
	            //for vehicle:   {"totals": [{ "Rule": "0" },{ "Rule": "0" },{ "Rule": "0" },{ "Rule": "39" },{ "Rule": "0" },{ "Rule": "35" },{ "Rule": "0" },{ "Rule": "0" },{ "Rule": "0" },{ "Rule": "34" },{ "Rule": "23" },{ "Rule": "6" }],"information": [{"Vehicle Name": "15 - BLUE Peterbilt","Group": "Prohibit Idling, 10,001 to 26,000 GVWR","Distance": "606","Behave": [{"Rule": "2"},{"Rule": "0"},{"Rule": "2"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "12"},{"Rule": "0"},{"Rule": "1"}]},{"Vehicle Name": "14 - BAFFIN Peterbilt","Group": "Prohibit Idling, 2 Axle CDL","Distance": "359","Behave": [{"Rule": "1"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "4"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "16 - GRAY Peterbilt","Group": "Prohibit Idling, 10,001 to 26,000 GVWR","Distance": "13","Behave": [{"Rule": "0"},{"Rule": "0"},{"Rule": "2"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "2"},{"Rule": "0"}]},{"Vehicle Name": "31 - WHITE Transit","Group": "Prohibit Idling, Under 10,000 GVWR","Distance": "51","Behave": [{"Rule": "13"},{"Rule": "0"},{"Rule": "5"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "32 - BLUE Ford","Group": "Prohibit Idling, 10,001 to 26,000 GVWR","Distance": "25","Behave": [{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "2"},{"Rule": "0"},{"Rule": "1"}]},{"Vehicle Name": "33 - RED Ford","Group": "Prohibit Idling, 10,001 to 26,000 GVWR","Distance": "265","Behave": [{"Rule": "2"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "35 - PURPLE Peterbilt","Group": "Prohibit Idling, 3 Axle CDL","Distance": "767","Behave": [{"Rule": "4"},{"Rule": "0"},{"Rule": "5"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "6"},{"Rule": "2"},{"Rule": "0"}]},{"Vehicle Name": "36 - BLUE International","Group": "Prohibit Idling, 10,001 to 26,000 GVWR","Distance": "254","Behave": [{"Rule": "1"},{"Rule": "0"},{"Rule": "3"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "37 - GOLD Peterbilt","Group": "Prohibit Idling, 10,001 to 26,000 GVWR","Distance": "717","Behave": [{"Rule": "4"},{"Rule": "0"},{"Rule": "7"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "40 - WHITE Ford","Group": "Prohibit Idling, 10,001 to 26,000 GVWR","Distance": "69","Behave": [{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "41 - SILVER Ford","Group": "Prohibit Idling, VEHICLE: CMV (Non-CDL)","Distance": "147","Behave": [{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "2"}]},{"Vehicle Name": "42 - RED Peterbilt","Group": "Prohibit Idling, 10,001 to 26,000 GVWR","Distance": "501","Behave": [{"Rule": "1"},{"Rule": "0"},{"Rule": "1"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "4"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "43 - BLUE Freightliner","Group": "Prohibit Idling, VEHICLE: CDL Tractor","Distance": "1103","Behave": [{"Rule": "11"},{"Rule": "0"},{"Rule": "10"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "38 - BLUE Peterbilt","Group": "0","Distance": "0","Behave": [{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "4"},{"Rule": "4"},{"Rule": "0"}]},{"Vehicle Name": "34 - GOLD Ford","Group": "0","Distance": "0","Behave": [{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "37 - GOLDPeterbilt","Group": "0","Distance": "0","Behave": [{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "15"},{"Rule": "0"}]},{"Vehicle Name": "40 - WHITEFord","Group": "0","Distance": "0","Behave": [{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "2"}]},{"Vehicle Name": "29 - WHITE Peterbilt","Group": "0","Distance": "0","Behave": [{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "2"},{"Rule": "0"},{"Rule": "0"}]}]}
	            //for driver :	 {"totals": [{ "Rule": "0" },{ "Rule": "0" },{ "Rule": "0" },{ "Rule": "68" },{ "Rule": "6" },{ "Rule": "61" },{ "Rule": "22" },{ "Rule": "0" },{ "Rule": "0" },{ "Rule": "0" },{ "Rule": "0" },{ "Rule": "0" }],"information": [{"Vehicle Name": "Rick Schwenk","Group": "CMV Driver (Non-CDL), VEHICLE: CMV (Non-CDL)","Distance": "821","Behave": [{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "Troy Giordano","Group": "VEHICLE: CDL Truck, LICENSE: CDL B, VEHICLE: CMV (Non-CDL)","Distance": "894","Behave": [{"Rule": "3"},{"Rule": "1"},{"Rule": "3"},{"Rule": "3"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "Jay Hoagland","Group": "LICENSE: CDL A, VEHICLE: CDL Tractor, VEHICLE: CDL Truck, VEHICLE: CMV (Non-CDL)","Distance": "668","Behave": [{"Rule": "0"},{"Rule": "0"},{"Rule": "1"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "Kenneth Moore","Group": "VEHICLE: CDL Truck, VEHICLE: CMV (Non-CDL), VEHICLE: CDL Tractor, LICENSE: CDL A","Distance": "1931","Behave": [{"Rule": "7"},{"Rule": "4"},{"Rule": "23"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "Richard Fox","Group": "CMV Driver (Non-CDL), VEHICLE: CMV (Non-CDL)","Distance": "596","Behave": [{"Rule": "2"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "Michael Merwick","Group": "CMV Driver (Non-CDL), VEHICLE: CMV (Non-CDL)","Distance": "231","Behave": [{"Rule": "2"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "Carlos Rivera","Group": "CMV Driver (Non-CDL), VEHICLE: CMV (Non-CDL)","Distance": "874","Behave": [{"Rule": "6"},{"Rule": "0"},{"Rule": "0"},{"Rule": "6"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "Kevin Neidert","Group": "CMV Driver (Non-CDL), VEHICLE: CMV (Non-CDL)","Distance": "4","Behave": [{"Rule": "5"},{"Rule": "0"},{"Rule": "2"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "Christopher Rounds","Group": "VEHICLE: CMV (Non-CDL), CMV Driver (Non-CDL)","Distance": "69","Behave": [{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "Luann Whitcomb","Group": "CMV Driver (Non-CDL), VEHICLE: CMV (Non-CDL)","Distance": "166","Behave": [{"Rule": "7"},{"Rule": "0"},{"Rule": "1"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "Lorrie Mabee","Group": "VEHICLE: CMV (Non-CDL), CMV Driver (Non-CDL)","Distance": "584","Behave": [{"Rule": "8"},{"Rule": "0"},{"Rule": "1"},{"Rule": "6"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "Bob Boutelle","Group": "CMV Driver (Non-CDL), VEHICLE: CMV (Non-CDL)","Distance": "417","Behave": [{"Rule": "2"},{"Rule": "0"},{"Rule": "10"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "Randall Arthur","Group": "VEHICLE: CDL Tractor, VEHICLE: CMV (Non-CDL), LICENSE: CDL A, VEHICLE: CDL Truck","Distance": "781","Behave": [{"Rule": "7"},{"Rule": "0"},{"Rule": "1"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "Ethan Fellows","Group": "CMV Driver (Non-CDL), VEHICLE: CMV (Non-CDL)","Distance": "621","Behave": [{"Rule": "3"},{"Rule": "0"},{"Rule": "3"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "Wayne Haney","Group": "VEHICLE: CMV (Non-CDL), CMV Driver (Non-CDL)","Distance": "1444","Behave": [{"Rule": "0"},{"Rule": "0"},{"Rule": "3"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "Michael Wilson","Group": "VEHICLE: CMV (Non-CDL), VEHICLE: CDL Truck, VEHICLE: CDL Tractor, LICENSE: CDL A","Distance": "0","Behave": [{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "1"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "Kenneth Roberts","Group": "LICENSE: CDL B, VEHICLE: CDL Truck, VEHICLE: CMV (Non-CDL)","Distance": "999","Behave": [{"Rule": "3"},{"Rule": "1"},{"Rule": "2"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "Ronald Harrower","Group": "VEHICLE: CDL Tractor, VEHICLE: CDL Truck, VEHICLE: CMV (Non-CDL), CMV Driver (Non-CDL), LICENSE: CDL A","Distance": "688","Behave": [{"Rule": "4"},{"Rule": "0"},{"Rule": "3"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "Mary Beth Moss","Group": "CMV Driver (Non-CDL), VEHICLE: CMV (Non-CDL)","Distance": "79","Behave": [{"Rule": "1"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]},{"Vehicle Name": "Non-CDL Driver","Group": "Assets, Compliance, Mechanics, Office, Operations","Distance": "735","Behave": [{"Rule": "8"},{"Rule": "0"},{"Rule": "8"},{"Rule": "6"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"},{"Rule": "0"}]}]}


	    	} catch(Exception e) {
	    		e.printStackTrace();
	    	}
	    	
			return responseJson;
		}
		/*
		 * returns the list of top n number of vehicles or drivers whose score is the least.
		 * param: top number of records needed.
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

	    public Map<String, List<Trip>> loadVehicleTripsMap(String geouserid, String databaseName, String geosess, String url,String sdate,String edate) throws MalformedURLException, IOException {
	    	//Run the api calls to get the trips, users etc data from Geotab and Store the values in the VehicleDriverMap object.
	/*    	api.call("Get", {
	    		"typeName": "Trip",
	    		search: {
	    			fromDate: '2020-05-01T00:00:00.000Z', 		//this.getStartDate()
	    			toDate: "2020-05-30T00:00:00.000Z",   		//this.getEndDate()
	    		}
	    	}, function(result) {
	    		for (var i = 0; i < result.length; i++)
	    		{
	    		   if(result[i].driver!="UnknownDriverId")
	    		    {
	    			   //console.log(result[i]);
	    			   getDeviceData(result[i]);
	    		    }
	    		}
	    		}, function(e) {
	    		console.error("Failed:", e);
	    		});

	    		function getDeviceData(deviceId)
	    		{
	    			var a="";    
	    			api.call("Get", {
	    				"typeName": "Device",
	    				"search": 
	    				{
	    					id: deviceId.device.id
	    				}
	    			}, function(results) {
	    		        api.call("Get", {
	    		        	"typeName": "User",
	    		        	search: {
	    		        	id: deviceId.driver.id
	    		        }
	    		       }, function(resultss) {
	    		    	   console.log(results[0].name+"---"+resultss[0].firstName+" "+resultss[0].lastName+"-"+deviceId.start+"-"+deviceId.stop);
	    		       }, function(e) {
	    		    	   console.error("Failed:", e);
	    		       });  
	    		        //console.log (results[0].name);
	    			}, function(e) {
	    				//console.error("Failed:", e);
	    			});
	    		}
	    		*/
	    	//THE FOLLOWING METHOD CALL 'loadSampleTrips()' SHOULD BE REPLACED WITH ACTUAL CALL AND VALUE RETURNED AS STRING ARRAY.
	    		ArrayList<String> tripsData = commonGeotabDAO.getTrip(geouserid, databaseName, geosess, url, sdate, edate);
	    	// END METHOD CALL 'loadSampleTrips()'
	    		
	    	Map<String, List<Trip>> vehicleTrips = new HashMap<String, List<Trip>>();
	    	for(String tripData: tripsData) {
	    		String[] tripVars = tripData.split("\\|");
	    		String vehicleName = tripVars[0];
	    		String driverName[] = tripVars[1].split(" ");  //ASSUMPTION: NO SPACES IN FIRSTNAME AND LASTNAMES.  THE DRIVER NAME WILL HAVE ONLY ONE SPACE BETWEEN FIRST AND LAST NAMES.
	    		String driverFirstName = driverName[0];
	    		String driverLastName = "";
	    		try {
	    		driverLastName=driverName[1];
	    		}catch (Exception e) {
					// TODO: handle exception
	    			System.out.println(e);
				}
	    		LocalDateTime tripStartDate = convertToLocalDateTime(tripVars[2]);
	    		LocalDateTime tripEndDate = convertToLocalDateTime(tripVars[3]);
	    		Trip trip = new Trip(vehicleName, driverFirstName, driverLastName, tripStartDate, tripEndDate);
	    		List<Trip> trips = vehicleTrips.get(vehicleName);
	    		if(trips == null) {
	    			trips = new ArrayList<Trip>();
	    			vehicleTrips.put(vehicleName, trips);
	    		} 
	   			trips.add(trip);
	    	}
	    	return vehicleTrips;
	    }
	    
	    //Load lytx behavior to map - Key=lytxBehaviorId, Value=BehaviorName
	    public Map<Integer, String> loadLytxBehaviors(String lytxBehaviorsResponseJson) {
	    	Map<Integer, String> lBehaviors = new HashMap<Integer, String>();
	        JSONObject lytxBehaviorsJO = new JSONObject(lytxBehaviorsResponseJson);
	        JSONArray lytxBehaviorsArray = lytxBehaviorsJO.getJSONArray("behaviors");
	        for (int i = 0; i < lytxBehaviorsArray.length(); i++) {
	            JSONObject behaviorJO = lytxBehaviorsArray.getJSONObject(i);
	            int lytxBehaviorId = behaviorJO.getInt("behaviorId");
	            String lytxDescription = "L-" + behaviorJO.getString("description");
	            lBehaviors.put(lytxBehaviorId, lytxDescription);
	        }   
//	        printLytxBehaviors(lBehaviors);
	        return lBehaviors;
	    }

	    public Map<Long, String> loadLytxVehicleID_NameMap(String getVehicleResponseJson){
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
	    
	    
	    public void printLytxBehaviors(Map<Integer, String> lBehaviors) {

	    	for (Map.Entry<Integer, String> lBehavior : lBehaviors.entrySet()) {
	    		System.out.println(lBehavior.getKey() + " | " + lBehavior.getValue());
	    	}

	    }

	    
	    public List<String> loadReporColumntHeaders(String userName,String db) {
	    	List<String> reportColumnHeader = new ArrayList<String>();
	    	reportColumnHeader.add("Vehicle Name");
	    	reportColumnHeader.add("Group");
	    	reportColumnHeader.add("Distance");
	    	List<Map.Entry<String, Integer>> selectedRuleNames = loadSelectedRuleNames(userName,db);
	    	for(Entry<String, Integer> header : selectedRuleNames) {
	    		reportColumnHeader.add(header.getKey());
	    	}
	    	return reportColumnHeader;
	    }
	    
		/**
		 * @param reportColumnHeader
		 */
		private  List<Map.Entry<String, Integer>> loadSelectedRuleNames(String userName,String db) {
			
			selectedRules = new LinkedHashMap<String, Integer>();
	    	
			List<Object[]> obj=getallBehaveFromDB(userName,db);
			for(Object[] objs : obj){
	    		selectedRules.put(String.valueOf(objs[0]), Integer.parseInt(String.valueOf(objs[1])));
			}
			
	    
	    	
			return new ArrayList<Map.Entry<String, Integer>>(selectedRules.entrySet());
		}
	    
		public LocalDateTime convertToLocalDateTime(String strDate) {
			LocalDateTime ldt = null;
			if(strDate != null) {
				if(strDate.contains("java")) {
					long dateInMilliSec = Long.parseLong(strDate.substring(33, strDate.indexOf(',')));
					ldt = LocalDateTime.ofInstant(Instant.ofEpochSecond(dateInMilliSec), TimeZone.getDefault().toZoneId());  
				} else {
					ldt = LocalDateTime.parse(strDate.substring(0, strDate.indexOf('.')));
				}
			}
			return ldt;
		}

		public LocalDateTime[] getPreviousWeek(LocalDateTime date) {
		    final int dayOfWeek = date.getDayOfWeek().getValue();
		    final LocalDateTime from = date.minusDays(dayOfWeek + 6); // (dayOfWeek - 1) + 7
		    final LocalDateTime to = date.minusDays(dayOfWeek);

		    return new LocalDateTime[]{from, to};
		}

		public LocalDateTime[] getPreviousMonth(LocalDateTime date) {
		    final LocalDateTime from = date.minusDays(date.getDayOfMonth() - 1).minusMonths(1);
		    final LocalDateTime to = from.plusMonths(1).minusDays(1);

		    return new LocalDateTime[]{from, to};
		}
			

		

		//FOR TESTING ONLY:  This method should make the actual call to Geotab and get the exceptionSummariesJson
		//Guna todo: copy the request here (commented) to get the response below;
		public String getGeotabDriverExceptionSummariesResponseJson(String sdate,String edate,String geouname,ArrayList<String> geotabgroups,String geodatabase,String geosees,String url,String enttype) throws ParseException, MalformedURLException, IOException {
		
			  Object getgeodropdown = getgeodropdown(geouname,geodatabase);
			    ArrayList<String> getl = (ArrayList<String>)getgeodropdown;
			  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		      String sDate = sdate;
		      String eDate = edate;
		      Date ssdate = sdf.parse(sDate);
		      Date eedate = sdf.parse(eDate);
		      String gvalue = "";
		      for (int j = 0; j < getl.size(); j++) {
		        if (j != getl.size() - 1) {
		          gvalue = gvalue + "{\"id\":\"" + (String)getl.get(j) + "\"},";
		        } else {
		          gvalue = gvalue + "{\"id\":\"" + (String)getl.get(j) + "\"}";
		        } 
		      } 
		      String groupvalue = "";
		      for (int i = 0; i < geotabgroups.size(); i++) {
		        if (i != geotabgroups.size() - 1) {
		          groupvalue = groupvalue + "{\"id\":\"" + (String)geotabgroups.get(i) + "\"},";
		        } else {
		          groupvalue = groupvalue + "{\"id\":\"" + (String)geotabgroups.get(i) + "\"}";
		        } 
		      } 
		      String uri = "https://" + url + "/apiv1";
		      String urlParameters = "{\"method\":\"ExecuteMultiCall\",\"params\":{\"calls\":[{\"method\":\"GetReportData\",\"params\":{\"argument\":{\"runGroupLevel\":-1,\"isNoDrivingActivityHidden\":true,\"fromUtc\":\"" + sdate + "T01:00:00.000Z\",\"toUtc\":\"" + edate + "T03:59:59.000Z\",\"entityType\":\""+enttype+"\",\"reportArgumentType\":\"RiskManagement\",\"groups\":[" + groupvalue + "],\"reportSubGroup\":\"None\",\"rules\":[" + gvalue + "]}}},{\"method\":\"Get\",\"params\":{\"typeName\":\"SystemSettings\"}}],\"credentials\":{\"database\":\"" + geodatabase + "\",\"sessionId\":\"" + geosees + "\",\"userName\":\"" + geouname + "\"}}}";
		      
		     // System.out.println(uri+"-"+urlParameters);
		      
		      String serverurl = uri;
		      HttpURLConnection con = (HttpURLConnection)(new URL(serverurl)).openConnection();
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
		      
		      String geotabDriverExceptionSummariesResponseJson = "{\"result\":" + o.getAsJsonArray("result").get(0).toString() + "}";
		
		      
			
			//System.out.println(geotabDriverExceptionSummariesResponseJson);
			
			return geotabDriverExceptionSummariesResponseJson;
		}	

		//FOR TESTING ONLY:  This method should make the actual call to Geotab and get the exceptionSummariesJson
		//Guna todo: copy the request here (commented) to get the response below;
		public String getGeotabVehicleExceptionSummariesResponseJson(String sdate,String edate,String geouname,ArrayList<String> geotabgroups,String geodatabase,String geosees,String url,String enttype) throws ParseException, MalformedURLException, IOException {
			Object getgeodropdown = getgeodropdown(geouname,geodatabase);
		    ArrayList<String> getl = (ArrayList<String>)getgeodropdown;
			String gvalue = "";
			for (int j = 0; j < getl.size(); j++) {
				if (j != getl.size() - 1) {
					gvalue = gvalue + "{\"id\":\"" + getl.get(j) + "\"},";
				} else {
					gvalue = gvalue + "{\"id\":\"" + getl.get(j) + "\"}";
				}
			}
			String groupvalue = "";
			for (int i = 0; i < geotabgroups.size(); i++) {
				if (i != geotabgroups.size() - 1) {
					groupvalue = groupvalue + "{\"id\":\"" + (String) geotabgroups.get(i) + "\"},";
				} else {
					groupvalue = groupvalue + "{\"id\":\"" + (String) geotabgroups.get(i) + "\"}";
				}
			}
			String uri = "https://" + url + "/apiv1";
			String urlParameters = "{\"method\":\"ExecuteMultiCall\",\"params\":{\"calls\":[{\"method\":\"GetReportData\",\"params\":{\"argument\":{\"runGroupLevel\":-1,\"isNoDrivingActivityHidden\":true,\"fromUtc\":\""
					+ sdate + "T01:00:00.000Z\",\"toUtc\":\"" + edate
					+ "T03:59:59.000Z\",\"entityType\":\"Device\",\"reportArgumentType\":\"RiskManagement\",\"groups\":["
					+ groupvalue + "],\"reportSubGroup\":\"None\",\"rules\":[" + gvalue
					+ "]}}},{\"method\":\"Get\",\"params\":{\"typeName\":\"SystemSettings\"}}],\"credentials\":{\"database\":\""
					+ geodatabase + "\",\"sessionId\":\"" + geosees + "\",\"userName\":\"" + geouname + "\"}}}";
			String serverurl = uri;
			
			//System.out.println(url+"-"+urlParameters);
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
		      
		      String geotabDriverExceptionSummariesJson = "{\"result\":" + o.getAsJsonArray("result").get(0).toString() + "}";
		
		      
			 //String geotabDriverExceptionSummariesJson = geotabVehicleExceptionSummariesResponseJson.toString();
			// String  geotabDriverExceptionSummariesJson = "{\"results\":" + geotabVehicleExceptionSummariesResponseJson.getAsJsonArray("result").get(0).toString() + "}";
		
			//System.out.println(geotabDriverExceptionSummariesJson);
			return geotabDriverExceptionSummariesJson;
		}	

		
		//FOR TESTING ONLY:  This method should make the actual call to Lytx and get the exceptionSummariesJson with startDate and EndDate as search parameters.
		//Guna todo: copy the request here (commented) to get the response below;
		public String getLytxExceptionSummariesResponseJson(String sdate,String edate,String lytxSess,String groupid,String endpoint) throws ParseException {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String sDate = sdate;
			String eDate = edate;
			Date ssdate = sdf.parse(sDate);
			Date eedate = sdf.parse(eDate);
			ISubmissionServiceV5Proxy dr = new ISubmissionServiceV5Proxy(endpoint);
			GetEventsResponse qr = new GetEventsResponse();
			GetEventsByLastUpdateDateRequest geteventbyid = new GetEventsByLastUpdateDateRequest();
			geteventbyid.setSessionId(lytxSess);
			geteventbyid.setStartDate(ssdate);
			geteventbyid.setEndDate(eedate);
			if (!groupid.equalsIgnoreCase("null"))
			{
				geteventbyid.setGroupId(Long.valueOf(Long.parseLong(groupid)));
			}
				try {
				qr = dr.getEventsByLastUpdateDate(geteventbyid);
			} catch (Exception e) {
				System.out.println(e);
			}
			JSONObject jsonObject3 = new JSONObject(qr);
			String lytxExceptionSummariesResponseJson = toStringValue(jsonObject3);
			

			return lytxExceptionSummariesResponseJson;
		}
		
		//FOR TESTING ONLY:  This method should make the actual call to (Lytx OR Geotab ?) and get the vehicles and the vehicleIDs.  Request search parameters are .....
		//Guna todo: copy the request here (commented) to get the response below;
		public String getLytxVehicleID_NameResponseJson(String lytxSess,String endpoint) throws RemoteException {
			
			ISubmissionServiceV5Proxy pr = new ISubmissionServiceV5Proxy(endpoint);
			GetVehiclesResponse lr = new GetVehiclesResponse();
			GetVehiclesRequest getVehiclesRequest = new GetVehiclesRequest();
			getVehiclesRequest.setIncludeSubgroups(Boolean.valueOf(true));
			getVehiclesRequest.setSessionId(lytxSess);
			lr = pr.getVehicles(getVehiclesRequest);

			JSONObject jsonObject2 = new JSONObject(lr);
			
			
			String lytxVehicleID_NameResponseJson = toStringValue(jsonObject2);
		  	return lytxVehicleID_NameResponseJson;
		}

		//FOR TESTING ONLY:  This method should make the actual call to Lytx and get the Lytx behavious and their Ids.
		//Guna todo: copy the request here (commented) to get the response below;
		public String getLytxBehaviorsResponseJson(String lytxSess,String endpoint) throws RemoteException {
			ISubmissionServiceV5Proxy er = new ISubmissionServiceV5Proxy(endpoint);
			ExistingSessionRequest re = new ExistingSessionRequest();
			re.setSessionId(lytxSess);
			GetBehaviorsResponse getb = new GetBehaviorsResponse();
			getb = er.getBehaviors(re);

			JSONObject jsonObject = new JSONObject(getb);
			String lytxBehaviorsResponseJson = toStringValue(jsonObject);
			
			return lytxBehaviorsResponseJson.toString();
		}


		public String toStringValue(Object object) {
			String value = "";
			if (object != null)
				if (object instanceof String) {
					value = (String) object;
				} else {
					try {
						value = "" + object.toString().trim();
					} catch (Exception exception) {
					}
				}
			return value;
		}
		
	    /*
		 * returns the list of top n number of vehicles or drivers whose score is the least.
		 * param: top number of records needed.
		 */
		public  void calculateTopBottomNRecords(String userName,String responce,Workbook workbook,String db) {
			reportRows = new ArrayList<ReportRow>();

			loadReportRowsFromJson(responce,userName,db);
			Map<String, Score> scores = new HashMap<String, Score>();
			List<Score> zeroScores = new ArrayList<Score>();
			List<Score> topScores = new ArrayList<Score>();
	   		int countOfRules = loadSelectedRuleNames(userName,db).size();
	   		List<Map.Entry<String, Integer>> selectedRuleNames=loadSelectedRuleNames(userName,db);
	       	for (ReportRow row : reportRows ) {
	       		Score score = new Score();
//	       		row.setScore(score);
	       		score.setName(row.getName());
	       		Map<String, Double> weightedAvgScore = new LinkedHashMap<String, Double>();
	       		score.setWeightedAvgScores(weightedAvgScore);
	       		double totalWeightedAvgScores = 0d;
	        	for(Map.Entry<String, Integer> selectedRulesEntry : selectedRuleNames) {
	        		String ruleName = selectedRulesEntry.getKey();
	        		Integer weight = selectedRules.get(ruleName);
	           		Integer ruleVal = row.getSelectedRules().get(ruleName);
	           		if(ruleVal == null) {
	           			continue;
	           		}
	           		if(ruleVal > 0) {
	           			double weightedAvg = (double)(ruleVal * weight)/(minimumDistance * 100);
	           			score.getWeightedAvgScores().put(ruleName, weightedAvg);
	           			totalWeightedAvgScores += weightedAvg;
	           			score.setMeanWeightedAvgScores((double)(totalWeightedAvgScores/countOfRules));
	           		} else {
	           			score.getWeightedAvgScores().put(ruleName, 0d);
	           		}
	        	}
	        	if(score.getMeanWeightedAvgScores() == null) {
	        		score.setMeanWeightedAvgScores(0d);
	        	}
	        	
	        	if(score.getMeanWeightedAvgScores() == 0d) {
	       			score.setMeanWeightedAvgScores((double)row.getDistance());
	        		zeroScores.add(score);
	        	} else {
	       			topScores.add(score);
	        	}
	       	}
	       	
	       	int topScoresCount = topScores.size();
	       	int zeroScoresCount = zeroScores.size(); 

	       	System.out.println("zeroscores size = " + zeroScores.size());
	       	System.out.println("topscores size = " + topScores.size());

	       	topNRecords = new ArrayList<Score>();
	       	bottomNRecords = new ArrayList<Score>();
	       	if(zeroScoresCount > 0) {
	       		zeroScores.sort(Comparator.comparing(Score::getMeanWeightedAvgScores).reversed());
	       		for(int i = 0; i < zeroScoresCount && i < N_TOP_BOTTOM_RECORDS;  i++) {
	       			topNRecords.add(zeroScores.get(i));
	       		}
	       	}
	       	
	       	topScores.sort(Comparator.comparing(Score::getMeanWeightedAvgScores));
	       	for(int i = 0; (i < topScoresCount) && (i < (N_TOP_BOTTOM_RECORDS - zeroScoresCount)); i++) {
	       		topNRecords.add(topScores.get(i));
	       	}
	       	//BOTTOM N RECORDS
	       	topScores.sort(Comparator.comparing(Score::getMeanWeightedAvgScores).reversed());
	       	for(int i = 0; (i < topScoresCount) && (i < (N_TOP_BOTTOM_RECORDS)); i++) {
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
	    	
	    	//Wrire To Excel	

			
			  Sheet reportNthRecord = workbook.getSheetAt(4);
			  
			  int startingNthrecord=5; reportNthRecord.createRow(4);
			  
			  Row rowsNthRecord = reportNthRecord.getRow(4); rowsNthRecord.createCell(0);
			  Cell cellsNthRecord = rowsNthRecord.getCell(0);
			  cellsNthRecord.setCellValue("Top Ten Records"); rowsNthRecord.createCell(1);
			  
			  cellsNthRecord = rowsNthRecord.getCell(1);
			  cellsNthRecord.setCellValue("Value"); rowsNthRecord.createCell(5);
			  
			  cellsNthRecord = rowsNthRecord.getCell(5);
			  cellsNthRecord.setCellValue("Botton Ten Records");
			  rowsNthRecord.createCell(6);
			  
			  cellsNthRecord = rowsNthRecord.getCell(6);
			  cellsNthRecord.setCellValue("Value");
			 
			  for(int i = 0; i < topNRecords.size(); i++) 
			  {
			  reportNthRecord.createRow(i+startingNthrecord);
			  
			  rowsNthRecord = reportNthRecord.getRow(i+startingNthrecord);
			  rowsNthRecord.createCell(0);
			  
			  cellsNthRecord = rowsNthRecord.getCell(0);
			  
			  cellsNthRecord.setCellValue(topNRecords.get(i).getName());
			  rowsNthRecord.createCell(1);
			  
			  cellsNthRecord = rowsNthRecord.getCell(1);
			  
			  cellsNthRecord.setCellValue(topNRecords.get(i).getMeanWeightedAvgScores());
			  
			
			  
			  }
			  
			  //Print the bottom scores
			  for(int i = 0; i < bottomNRecords.size(); i++) {
			 
			  rowsNthRecord = reportNthRecord.getRow(i+startingNthrecord);
			  rowsNthRecord.createCell(5);
			  
			  cellsNthRecord = rowsNthRecord.getCell(5);
			  
			  cellsNthRecord.setCellValue(bottomNRecords.get(i).getName());
			  
			  rowsNthRecord.createCell(6);
			  
			  cellsNthRecord = rowsNthRecord.getCell(6);
			  
			 cellsNthRecord.setCellValue(bottomNRecords.get(i).getMeanWeightedAvgScores());
			  
		}
	    	
	    	
	    	
	}

	
	    public  List<ReportRow> loadReportRowsFromJson(String reportDataJson,String userName,String db) {
	    	List<Map.Entry<String, Integer>> ruleList = new ArrayList<Map.Entry<String, Integer>>(loadSelectedRuleNames(userName,db));
	        JSONObject reportDataJO = new JSONObject(reportDataJson);
	        JSONArray reportDataArray = reportDataJO.getJSONArray("information");
	        for (int i = 0; i < reportDataArray.length(); i++) {
	            JSONObject reportRowJO = reportDataArray.getJSONObject(i);
	            ReportRow reportRow = new ReportRow();
	            reportRow.setSelectedRules(new LinkedHashMap<String, Integer>());
	            reportRow.setName(reportRowJO.getString("VehicleName"));
	            reportRow.setGroup(reportRowJO.getString("Group"));
	            reportRow.setDistance(Long.parseLong(reportRowJO.getString("Distance")));
	            JSONArray behaviorArray = reportRowJO.getJSONArray("Behave");
	            for(int j = 0; j < behaviorArray.length(); j++ ) {
	            	JSONObject behaveItem = behaviorArray.getJSONObject(j);
	            	reportRow.getSelectedRules().put(ruleList.get(j).getKey(), behaveItem.getInt("Rule"));
	            }
	            reportRows.add(reportRow);
	        }
			return reportRows;   
	    }
	    
		public List<String> loadReporColumntHeadersTrending(String userName,String db) {
			List<String> reportColumnHeader = new ArrayList<>();
			reportColumnHeader.add("VehicleName");
			reportColumnHeader.add("Group");
			reportColumnHeader.add("Distance");
			GlReportDAO da = new GlReportDAO();
			List<String> gval = new ArrayList();
			gval = da.getallbehave(userName,db);
			for (int j = 0; j < gval.size(); j++) {
				// System.out.println(j + "-----" + gval.get(j));
				reportColumnHeader.add(gval.get(j));
			}
			//System.out.println(reportColumnHeader.size() + "-----");
			return reportColumnHeader;
		}
	    
		private Map<String, Map<String, String>> extractGeotabDriverData(String geotabDriverExceptionSummariesJson,
				String userName,String db) {

			List<String> displayColumns = loadReporColumntHeadersTrending(userName,db);

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
					try
					{
					int eventCount = geotabExceptionSummariesJA.getJSONObject(k).getInt("eventCount");
					JSONObject geotabExceptionRuleJO = geotabExceptionSummariesJA.getJSONObject(k)
							.getJSONObject("exceptionRule");
					String geotabExceptionName = "G-" + geotabExceptionRuleJO.getString("name");
					geotabExceptionEvents.put(geotabExceptionName, geotabExceptionEvents.get(geotabExceptionName)==null?eventCount:geotabExceptionEvents.get(geotabExceptionName)+eventCount);
					}catch (Exception e) {
						// TODO: handle exception
					}
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
				String userName,String db) {
			Map<String, Map<String, String>> combinedReport = new LinkedHashMap<String, Map<String, String>>();

			try {

				List<String> displayColumns = loadReporColumntHeadersTrending(userName,db);

				// create report object:
				// GEOTAB Events processing
				JSONObject geotabEventsJO = new JSONObject(geotabVehicleExceptionSummariesJson);
				JSONArray geotabEventsJOArray = geotabEventsJO.getJSONArray("result");
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
							try {
								group = geotabVehicleGroups.getJSONObject(j).getString("name");
							} catch (Exception e) {
								// TODO: handle exception
							}
						} else {

							try {
								String newGroup = geotabVehicleGroups.getJSONObject(j).getString("name");
								if ("Prohibit Idling".equalsIgnoreCase(newGroup)) {
									group = newGroup + ", " + group;
								} else {
									group = group + ", " + newGroup;
								}
							} catch (Exception e) {
								// TODO: handle exception
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
						
						try {
						int eventCount = geotabExceptionSummariesJA.getJSONObject(k).getInt("eventCount");
						JSONObject geotabExceptionRuleJO = geotabExceptionSummariesJA.getJSONObject(k)
								.getJSONObject("exceptionRule");
						String geotabExceptionName = "G-" + geotabExceptionRuleJO.getString("name");
					
					//	System.out.println("-----guna"+geotabExceptionName);

						geotabExceptionEvents.put(geotabExceptionName, geotabExceptionEvents.get(geotabExceptionName)==null?eventCount:geotabExceptionEvents.get(geotabExceptionName)+eventCount);
						}catch (Exception e) {
							// TODO: handle exception
						}
					}
					for (int m = 3; m < displayColumns.size(); m++) {
						if (geotabExceptionEvents.get(displayColumns.get(m)) != null) {
							System.out.println(displayColumns.get(m)+"----notnull");
							
							newReportRow.put(displayColumns.get(m),
									(geotabExceptionEvents.get(displayColumns.get(m))).toString());
							System.out.println(displayColumns.get(m)+"----not111111null");

						} else {

							if (newReportRow.get(displayColumns.get(m)) == null) {
								
								newReportRow.put(displayColumns.get(m), "0");
							}
						}
					}
					
					System.out.println(geotabVehicleName+"----"+newReportRow);
					
					combinedReport.put(geotabVehicleName, newReportRow);
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			return combinedReport;
		}
		
		public String createExcelReport(String sdate,String edate,String geouname,String geodatabase,String url,
				String filename,String templect) throws EncryptedDocumentException, IOException
		{
			String responseJson = "";
			try {
				responseJson = selectresponce(geouname,geodatabase);
			} catch (Exception e) {
				// TODO: handle exception
			}

			List<String> displayColumns = loadReporColumntHeaders(geouname,geodatabase);

			File source = new File(
					"/usr/local/apache-tomcat-8.5.51/webapps/GL_Driver_Safety_Report_Template_" + templect + ".xlsx");
			File dest = new File("/usr/local/apache-tomcat-8.5.51/webapps/" + geodatabase + "/report/excel/as.xlsx");
			try {
				copyFileUsingStream(source, dest);
			} catch (IOException e3) {
				e3.printStackTrace();
			}
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
					D = getwe(geouname, ((String) displayColumns.get(h)).toString().trim(),geodatabase);
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
				JSONArray behave = info.getJSONObject(s).getJSONArray("Behave");
				for (int h2 = 0; h2 < behave.length(); h2++) {
					cell4 = row4.createCell(h2 + 3);
					cell4.setCellValue(Integer.parseInt(behave.getJSONObject(h2).getString("Rule")));
				}
			}
			Sheet report = workbook.getSheetAt(1);
			Row rows = report.getRow(5);
			Cell cells = rows.getCell(2);
			float min = 0.0F;
			try {
				min = getminmiles(geouname,geodatabase);
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

			String newAllDataNamedRange = "Report!$A$7:$Y$" + statrpoint;
			XSSFWorkbook glDSRWorkbook = (XSSFWorkbook) workbook;
			XSSFSheet reportSheet = glDSRWorkbook.getSheet("Report");
			XSSFName allDataNamedRange = glDSRWorkbook.getName("AllData");
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
			InputStream is = null;
			OutputStream os = null;
			try {
				is = new FileInputStream(source);
				os = new FileOutputStream(dest);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = is.read(buffer)) > 0)
					os.write(buffer, 0, length);
			} finally {
				is.close();
				os.close();
			}
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
		

}