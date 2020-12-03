package com.vibaps.merged.safetyreport.dao.gl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api_GetEvents_V5.GetEventsResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.json.JSONArray;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lytx.dto.ExistingSessionRequest;
import com.lytx.dto.GetBehaviorsResponse;
import com.lytx.dto.GetEventsByLastUpdateDateRequest;
import com.lytx.dto.GetVehiclesRequest;
import com.lytx.dto.GetVehiclesResponse;
import com.lytx.services.ISubmissionServiceV5Proxy;
import com.vibaps.merged.safetyreport.HibernateUtil;
import com.vibaps.merged.safetyreport.api.trending.RestTrendingReport;
import com.vibaps.merged.safetyreport.entity.gl.Gen_Device;
import com.vibaps.merged.safetyreport.entity.gl.Gen_Driver;
import com.vibaps.merged.safetyreport.entity.gl.Gl_RulelistEntity;
import com.vibaps.merged.safetyreport.entity.gl.ReportRow;
import com.vibaps.merged.safetyreport.entity.gl.Score;
import com.vibaps.merged.safetyreport.entity.gl.Trip;
import com.vibaps.merged.safetyreport.services.gl.GL_Report_SER;
import org.json.JSONArray;
import org.json.JSONObject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

public class GL_Report_DAO {
	static Gl_RulelistEntity enty = new Gl_RulelistEntity();
	static  GL_Report_SER ser = new GL_Report_SER();

	String title;
	LocalDateTime startDate;
	LocalDateTime endDate;
	String reportBy;  //Vehicle or Driver
	static int minimumDistance=50;
//	List<String> selectedRuleNames;
	static Map<String, Integer> selectedRules;
	 static List<ReportRow> reportRows;
	static Map<String, Map<String, Integer>> lytxVehicleEventsRecord;
	final Integer EXCEPTIONS_START_COLUMN  = 3;
	static List<String> displayReportColumnHeaders;
	static Map<String,List<Trip>> vehicleTrips;
    static Map<Long, String> lytxVehicleList; 
    static Map<Integer, String> lytxBehaviors;  
    static List<Score> topNRecords;
    static List<Score> bottomNRecords;
    static Integer N_TOP_BOTTOM_RECORDS = 10;
    static List<Map.Entry<String, Integer>> loadSelectedRuleNames;
	public static Object view(String geouserid,String db) {
		Map<String, Object> result = new HashMap<>();
		List<Gl_RulelistEntity> obj = null;
		List<Gl_RulelistEntity> obj1 = null;

		BigInteger countuser = null;
		
		try {
			Session session3 = HibernateUtil.getsession();
			Transaction	transaction3 = session3.beginTransaction();
			countuser = (BigInteger) session3.createSQLQuery("SELECT count(*) FROM gen_user where companyid=:userid and db=:db").setParameter("userid", geouserid).setParameter("db", db).uniqueResult();
			transaction3.commit();
		
		} catch (Exception exception) 
		{
			System.out.println(exception+"exo-------");
		}	


		if(countuser.intValue() ==0)
		{
			try {
				
				Session session = HibernateUtil.getsession();
				Transaction	transaction = session.beginTransaction();
				BigInteger newUserId = (BigInteger) session.createSQLQuery("call insertuserrecord(:userid,:db)").setParameter("userid", geouserid).setParameter("db", db).uniqueResult();
				System.out.println("newUserId"+newUserId);
				transaction.commit();
				
			
				
				System.out.println("newUserId....."+newUserId);

					
					if(newUserId.intValue() != 0)
					{
						
						try {
							Session session1 = HibernateUtil.getsession();
							Transaction transaction1 = session1.beginTransaction();
							obj1 = session1.createSQLQuery("SELECT gen_rulelist_id,status,weight FROM gl_selectedvalues where gen_user_id=1 order by gen_rulelist_id").list();
							transaction1.commit();
							
							Iterator it = obj1.iterator();
							Session session2 = HibernateUtil.getsession();
							Transaction transaction2 = session2.beginTransaction();
							while(it.hasNext()){
							     Object[] line = (Object[]) it.next();
							     int ob = session2.createSQLQuery("insert into gl_selectedvalues(gen_user_id,gen_rulelist_id,status,weight) values (:userid,:rule,:status,:weight)").setParameter("weight",Integer.parseInt(line[2].toString())).setParameter("status", Integer.parseInt(line[1].toString())).setParameter("rule", Integer.parseInt(line[0].toString())).setParameter("userid",newUserId).executeUpdate();
							}
							  transaction2.commit();					

							
						} catch (Exception exception) {}
						
						
					}
					
					
					
			} catch (Exception exception) 
			{
			System.out.println(exception);	
			}	
			
			
		}
		
		
		try {
			Session session = HibernateUtil.getsession();
			Transaction transaction = session.beginTransaction();
			obj = session.createSQLQuery("select * from (select a.id,CONCAT(a.rulecompany,'-',a.rulename) as value,rulevalue,a.rulecompany,b.status,b.weight,d.minmiles from gl_rulelist a,gl_selectedvalues b,gen_user c,gl_minmiles d where c.companyid=:userid and c.db=:db and b.gen_user_id=c.id and d.gen_user_id=c.id and a.id=b.gen_rulelist_id order by value) as value order by value.status desc").setParameter("userid", geouserid).setParameter("db", db).setResultTransformer((ResultTransformer)Transformers.ALIAS_TO_ENTITY_MAP).list();
			transaction.commit();
		} catch (Exception exception) {}
		return obj;
	}
	
	public static Object viewadd(String geouserid,String db) {
		Map<String, Object> result = new HashMap<>();
		Session session = null;
		List<Gl_RulelistEntity> obj = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getsession();
			transaction = session.beginTransaction();
			obj = session.createSQLQuery("select * from (select a.id,CONCAT(a.rulecompany,'-',a.rulename) as value,rulevalue,a.rulecompany,b.status,b.weight,d.minmiles from gl_rulelist a,gl_selectedvalues b,gen_user c,gl_minmiles d where c.companyid=:userid and c.db=:db and b.gen_user_id=c.id and d.gen_user_id=c.id and a.id=b.gen_rulelist_id order by value) as value order by value.value").setParameter("userid", geouserid).setParameter("db", db).setResultTransformer((ResultTransformer)Transformers.ALIAS_TO_ENTITY_MAP).list();
			transaction.commit();
		} catch (Exception exception) {}
		return obj;
	}
	
	public Object getgeodropdown(String geouserid) {
		Map<String, Object> result = new HashMap<>();
		Session session = null;
		List<Gl_RulelistEntity> obj = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getsession();
			transaction = session.beginTransaction();
			obj = session.createSQLQuery("select a.rulevalue from gl_rulelist a,gl_selectedvalues b,gen_user c where c.companyid=:userid and b.gen_user_id=c.id and a.id=b.gen_rulelist_id and b.status=1 and a.rulecompany='G'").setParameter("userid", geouserid).list();
			transaction.commit();
		} catch (Exception exception) {}
		return obj;
	}
	
	public Object getLybehave(String geouserid,String db) {
		Map<String, Object> result = new HashMap<>();
		Session session = null;
		List<Gl_RulelistEntity> obj = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getsession();
			transaction = session.beginTransaction();
			obj = session.createSQLQuery("select a.rulevalue from gl_rulelist a,gl_selectedvalues b,gen_user c where c.companyid=:userid and c.db=:db and b.gen_user_id=c.id and a.id=b.gen_rulelist_id and a.rulecompany='L'").setParameter("userid", geouserid).setParameter("db", db).list();
			transaction.commit();
		} catch (Exception exception) {}
		return obj;
	}
	
	public ArrayList getallbehave(String geouserid,String db) {
		Session session = null;
		ArrayList obj = new ArrayList();
		Transaction transaction = null;
		try {
			session = HibernateUtil.getsession();
			transaction = session.beginTransaction();
			obj = (ArrayList)session.createSQLQuery("select CONCAT(a.rulecompany,'-',a.rulename) as value from gl_rulelist a,gl_selectedvalues b,gen_user c where c.companyid=:userid and c.db=:db and b.gen_user_id=c.id and a.id=b.gen_rulelist_id and b.status=1 order by value").setParameter("db", db).setParameter("userid", geouserid).list();
			transaction.commit();
		} catch (Exception exception) {}
		return obj;
	}
	
	public ArrayList getallbehaveui(String geouserid,String db) {
		Session session = null;
		ArrayList obj = new ArrayList();
		Transaction transaction = null;
		try {
			session = HibernateUtil.getsession();
			transaction = session.beginTransaction();
			obj = (ArrayList)session.createSQLQuery("select CONCAT(a.rulecompany,'-',a.rulename) as value,b.weight from gl_rulelist a,gl_selectedvalues b,gen_user c where c.companyid=:userid and c.db=:db and b.gen_user_id=c.id and a.id=b.gen_rulelist_id and b.status=1 order by value").setParameter("userid", geouserid).setParameter("db", db).setResultTransformer((ResultTransformer)Transformers.ALIAS_TO_ENTITY_MAP).list();
			transaction.commit();
		} catch (Exception exception) {}
		return obj;
	}
	
	//For Report
	@SuppressWarnings("unchecked")
	public static List<Object[]> getallBehaveFromDB(String geouserid,String db) {
		Session session = null;
		List<Object[]> obj = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getsession();
			transaction = session.beginTransaction();
			obj = session.createSQLQuery("select concat(b.rulecompany,'-',b.rulename) as val,a.weight from gl_selectedvalues a,gl_rulelist b,gen_user c where c.companyid=:userid and c.db=:db and a.status= 1 and a.gen_user_id=c.id and a.gen_rulelist_id=b.id order by val").setParameter("userid", geouserid).setParameter("db", db).list();
			transaction.commit();
		} catch (Exception exception) {}
		
	
		
		
		return obj;
	}
	
	
	public static int geoCount(String geouserid,String db) {
		Session session = null;
		BigInteger obj = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getsession();
			transaction = session.beginTransaction();
			obj = ((BigInteger) session.createSQLQuery("select count(b.id) from gl_selectedvalues a,gl_rulelist b,gen_user c where c.companyid=:userid and c.db=:db and a.status= 1 and a.gen_user_id=c.id and b.rulecompany='G' and a.gen_rulelist_id=b.id").setParameter("userid", geouserid).setParameter("db", db).uniqueResult());
			transaction.commit();
		} catch (Exception exception) {
			System.out.println(exception+"exe");
		}
		return obj.intValue();
	}
	
	public static int lyCount(String geouserid,String db) {
		Session session = null;
		BigInteger obj = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getsession();
			transaction = session.beginTransaction();
			obj = ((BigInteger) session.createSQLQuery("select count(b.id) from gl_selectedvalues a,gl_rulelist b,gen_user c where c.companyid=:userid and c.db=:db and a.status= 1 and a.gen_user_id=c.id and b.rulecompany='L' and a.gen_rulelist_id=b.id").setParameter("userid", geouserid).setParameter("db", db).uniqueResult());
			transaction.commit();
		} catch (Exception exception) {}
		return obj.intValue();
	}
	
	public static int getwe(String geouserid, String rule,String db) {
		Session session = null;
		int obj = 0;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getsession();
			transaction = session.beginTransaction();
			obj = ((Integer)session.createSQLQuery("select a.weight from gl_selectedvalues a,gen_user b,gl_rulelist c where a.gen_user_id=b.id and b.companyid=:userid and b.db=:db and a.gen_rulelist_id=c.id and concat(c.rulecompany,'-',c.rulename)=:rule").setParameter("userid", geouserid).setParameter("db", db).setParameter("rule", rule).uniqueResult()).intValue();
			transaction.commit();
		} catch (Exception exception) {}
		return obj;
	}
	
	public static float getminmiles(String geouserid,String db) {
		Session session = null;
		float obj = 0.0F;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getsession();
			transaction = session.beginTransaction();
			obj = ((Float)session.createSQLQuery("select a.minmiles from gl_minmiles a,gen_user b where b.companyid=:userid and b.db=:db and a.gen_user_id=b.id").setParameter("userid", geouserid).setParameter("db", db).uniqueResult()).floatValue();
			transaction.commit();
		} catch (Exception exception) {}
		return obj;
	}
	public Object updateresponce(String geouname, String responseJson,String db) {
		// TODO Auto-generated method stub
		System.out.println(responseJson.length());
		Session session = null;
		int i = 0;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getsession();
			transaction = session.beginTransaction();
			i = session.createSQLQuery("update gl_responce a,gen_user b set a.responce_json=:responce where b.companyid=:userid and b.db=:db and b.id=a.gen_user_id").setParameter("responce",responseJson).setParameter("db",db).setParameter("userid",geouname).executeUpdate();
			transaction.commit();
		} catch (Exception exception) {}
		return i;
	}
	public String selectresponce(String geouname,String db) {
		// TODO Auto-generated method stub
		
		
		Session session = null;
		String i = "";
		Transaction transaction = null;
		try {
			session = HibernateUtil.getsession();
			transaction = session.beginTransaction();
			i = (String) session.createSQLQuery("select a.responce_json from gl_responce a,gen_user b where b.companyid=:userid and b.db=:db and b.id=a.gen_user_id").setParameter("userid",geouname).setParameter("db",db).uniqueResult();
			transaction.commit();
		} catch (Exception exception) {}
		return i;
	}
	
	public Object insert(ArrayList<Gl_RulelistEntity> v, String companyid, String minmiles,String db) {
		Map<String, String> result = new HashMap<>();
		Session session = null;
		int i = 0;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getsession();
			transaction = session.beginTransaction();
			i = session.createSQLQuery("update gl_rulelist a,gl_selectedvalues b,gen_user c,gl_minmiles d set b.status=0,d.minmiles=:minmiles  where  c.companyid=:companyid and c.db=:db  and b.gen_user_id=c.id and a.id=b.gen_rulelist_id and b.status=1 and d.gen_user_id=c.id").setParameter("companyid", companyid).setParameter("db", db).setParameter("minmiles", Float.valueOf(Float.parseFloat(minmiles))).executeUpdate();
			transaction.commit();
		} catch (Exception exception) {}
		int j = 0;
			try {
				for (int d = 0; d < v.size(); d++) {
					session = HibernateUtil.getsession();
					transaction = session.beginTransaction();
					j = session.createSQLQuery("update gl_rulelist a,gl_selectedvalues b,gen_user c set b.status=1,b.weight=:we where  c.companyid=:companyid and c.db=:db and b.gen_user_id=c.id and a.id=b.gen_rulelist_id and a.rulevalue=:rval").setParameter("we", Integer.valueOf(((Gl_RulelistEntity)v.get(d)).getWeight())).setParameter("companyid", companyid).setParameter("db", db).setParameter("rval", ((Gl_RulelistEntity)v.get(d)).getRulevalue()).executeUpdate();
					transaction.commit();
					result.put("result", "Rules list saved");
				} 
			} catch (Exception exception) {} 
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Gen_Device> deviceName(String geouserid,String db) {
		List<Gen_Device> deviceNameList=new ArrayList<Gen_Device>();
		
		List<Object[]> list=null;
		
		Session session = null;
		Map<String,String> obj = new LinkedHashMap<String, String>();;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getsession();
			transaction = session.beginTransaction();
			list =session.createSQLQuery("SELECT a.device_id,a.device_name FROM gen_device a,gen_user b where a.ref_gen_user_id=b.id and b.companyid=:userid and b.db=:db").setParameter("userid", geouserid).setParameter("db", db).list();
			transaction.commit();
		} catch (Exception exception) {}
		session.close();
		

Iterator it = list.iterator();
while(it.hasNext()){
     Object[] line = (Object[]) it.next();
     Gen_Device eq = new Gen_Device();
     eq.setDevice_id(line[0].toString());
     eq.setDevice_name(line[1].toString());

     deviceNameList.add(eq);
}

		return deviceNameList;
	}

	@SuppressWarnings("unchecked")
	public static List<Gen_Driver> driverName(String geouserid,String db) {
		
		Session session = null;
		Map<String,String> obj = new LinkedHashMap<String, String>();
		List<Gen_Driver> driverNameList=new ArrayList<Gen_Driver>();
		List<Object[]> list=null;

		
		Transaction transaction = null;
		try {
			session = HibernateUtil.getsession();
			transaction = session.beginTransaction();
			list =session.createSQLQuery("SELECT a.driver_id,a.driver_name FROM gen_driver a,gen_user b where a.ref_gen_user_id=b.id and b.companyid=:geouserid and b.db=:db").setParameter("geouserid", geouserid).setParameter("db", db).list();
			transaction.commit();
		} catch (Exception exception) {
			System.out.println(exception);
		}
		session.close();
		
		Iterator it = list.iterator();
		while(it.hasNext()){
		     Object[] line = (Object[]) it.next();
		     Gen_Driver eq = new Gen_Driver();
		     eq.setDriver_id(line[0].toString());
		     eq.setDriver_name(line[1].toString());
		     driverNameList.add(eq);
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
				ser.updateresponce(geouname,reportResponseJson,geodatabase);
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
	
			int geoRuleCount=0;
			geoRuleCount=geoCount(geouname, geodatabase);
			
			System.out.println("Geoci---"+geoRuleCount);
			
			if(geoRuleCount > 0)
			{
			if(reportBy.equalsIgnoreCase("Driver")) {
				System.out.println("COMBINED REPORT - DRIVER");
				extractGeotabDriverData(getGeotabDriverExceptionSummariesResponseJson(sdate,edate,geouname,geotabgroups,geodatabase,geosees,url,enttype),geouname);
			} else {
				System.out.println("COMBINED REPORT - VEHICLE");
				extractGeotabVehicleData(getGeotabVehicleExceptionSummariesResponseJson(sdate,edate,geouname,geotabgroups,geodatabase,geosees,url,enttype),geouname);
			}
			}
			
			//process LYTX exceptions response
			//Create a Map of lytx vehicleIds to exception map
	       lytxVehicleEventsRecord = extractExceptionDataFromLytxResponse(getLytxExceptionSummariesResponseJson(sdate,edate,sees,groupid,endpoint),sees,endpoint);
	        
	     //  System.out.println(lytxVehicleEventsRecord+"----");
	        
	        //combine Lytx exceptions data with the geotab exception report
	       if(geoRuleCount > 0)
			{
	        updateCombinedReportWithLytxExceptions(lytxVehicleEventsRecord,geouname);
			}
	       else
	       {
		        createLytxExceptionsReport(lytxVehicleEventsRecord,geouname);
 
	       }
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
					ser.updateresponce(geouname,reportResponseJson,geodatabase);
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
		
		private void createLytxExceptionsReport(Map<String, Map<String, Integer>> lytxVehicleEventsRecord,String userName) {
			//for every vehicle in lytxVehicleEventsRecord
			// displayReportColumnHeaders=loadReporColumntHeaders(userName);
			
			for (Map.Entry<String, Map<String, Integer>> lytxVehiclesEventsMapEntry : lytxVehicleEventsRecord.entrySet()) {
				//Get the report row corresponding to that vehicle.
				String lytxVehicleName = lytxVehiclesEventsMapEntry.getKey();
				
				//possible performance issue here.  Better to use Maps.
				ReportRow reportRow = new ReportRow();
				reportRow.setDistance(0);
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
	    		ArrayList<String> tripsData = Common_Geotab_DAO.getTrip(geouserid, databaseName, geosess, url, sdate, edate);
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
		private static List<Map.Entry<String, Integer>> loadSelectedRuleNames(String userName,String db) {
			//String[] selectedRuleNmesArray= {"G-Hard Acceleration","G-Harsh Braking", "G-Harsh Cornering", "G-Seatbelt", "G-[01 - WARNING] Speeding 10 MPH Over Posted Speed Limit", "G-[02 - VIOLATION] Speeding 20 MPH Over Posted Speed Limit", "L-Food or Drink", "L-Driver Smoking", "L-Handheld Device"};
	    	
	    	//String[][] selectedRulesData= {{"G-Hard Acceleration", "2"},{"G-Harsh Braking", "3"}, {"G-Harsh Cornering", "3"}, {"G-Seatbelt", "3"}, {"G-[01 - WARNING] Speeding 10 MPH Over Posted Speed Limit", "3"}, {"G-[02 - VIOLATION] Speeding 20 MPH Over Posted Speed Limit", "3"}, {"L-Food or Drink", "3"}, {"L-Driver Smoking", "3"}, {"L-Handheld Device", "3"}};
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
		
			  Object getgeodropdown = this.ser.getgeodropdown(geouname);
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
			Object getgeodropdown = this.ser.getgeodropdown(geouname);
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


		public static String toStringValue(Object object) {
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
		public static void calculateTopBottomNRecords(String userName,String responce,Workbook workbook,String db) {
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

	
	    public static List<ReportRow> loadReportRowsFromJson(String reportDataJson,String userName,String db) {
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
}