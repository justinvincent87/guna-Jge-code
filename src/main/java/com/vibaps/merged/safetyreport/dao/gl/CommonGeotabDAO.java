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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vibaps.merged.safetyreport.HibernateUtil;
import com.vibaps.merged.safetyreport.api.gl.RestDriverSafetyReport;
import com.vibaps.merged.safetyreport.entity.gl.GenDevice;
import com.vibaps.merged.safetyreport.entity.gl.GenDriver;
import com.vibaps.merged.safetyreport.entity.gl.GlRulelistEntity;
import com.vibaps.merged.safetyreport.repo.gl.UserReportFilterRepository;

@Repository
public class CommonGeotabDAO  {
	@Autowired
	private static GlReportDAO glReportDAO;
@Transactional	
public Object insertDevice(String geouserid,String databaseName,String geosess,String url) throws IOException {
		// TODO Auto-generated method stub

int obj=getCompanyId(geouserid,databaseName);
int result=0;
		System.out.println(obj+"--fg--"+geouserid);
		if(obj > 0)
		{
   		 String uri = "https://"+url+"/apiv1";
	      String urlParameters ="{\"method\":\"Get\",\"params\":{\"typeName\":\"Device\",\"credentials\":{\"database\":\""+databaseName+"\",\"sessionId\":\""+geosess+"\",\"userName\":\""+geouserid+"\"}}}";
	      
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
	      String GeotabDriverResponse = o.toString(); //Responce
	      
	    // System.out.println(GeotabDriverResponse); 
	      JSONObject geotabDriverJO = new JSONObject(GeotabDriverResponse);
		  JSONArray geotabDeiverJOArray = geotabDriverJO.getJSONArray("result");

		  
		  Session session = HibernateUtil.getsession();
			Transaction transaction = session.beginTransaction();
		  
		  for(int i=0;i<geotabDeiverJOArray.length();i++)
		  {
			  try
			  {
				  
				  
					result =session.createSQLQuery("insert into gen_device(ref_gen_user_id,device_id,device_name) values (:refid,:deviceid,:devicename)").setParameter("refid",obj).setParameter("deviceid",geotabDeiverJOArray.getJSONObject(i).getString("id")).setParameter("devicename",geotabDeiverJOArray.getJSONObject(i).getString("name")).executeUpdate();
					
				
			  }catch (Exception e) {
				// TODO: handle exception
			}
		  }
		  transaction.commit();
				
		  
	

		  
		}
		
		return null;
	}
	
	private int  getCompanyId(String geouserid,String db) {
		
		int obj = 0;
		Session session=null;
		Transaction transaction = null;
		try {
			 session = HibernateUtil.getsession();
			transaction = session.beginTransaction();
			obj = ((BigInteger) session.createSQLQuery("select id from gen_user where companyid=:userid and db=:db").setParameter("userid", geouserid).setParameter("db", db).uniqueResult()).intValue();
			
			System.out.println(obj+"----"+geouserid);

			
			transaction.commit();
		} catch (Exception exception) {
			System.out.println(exception);
		}
		
		return obj;
	}

	public Object insertDriver(String geouserid, String databaseName, String geosess, String url) throws IOException {
		// TODO Auto-generated method stub
		int obj=getCompanyId(geouserid,databaseName);
		int result=0;
				System.out.println(obj+"--fg--"+geouserid);
				if(obj > 0)
				{
		   		 String uri = "https://"+url+"/apiv1";
			      String urlParameters ="{\"method\":\"Get\",\"params\":{\"typeName\":\"User\",\"credentials\":{\"database\":\""+databaseName+"\",\"sessionId\":\""+geosess+"\",\"userName\":\""+geouserid+"\"}}}";
			      
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
			      String GeotabDriverResponse = o.toString(); //Responce
			      
			    // System.out.println(GeotabDriverResponse); 
			      JSONObject geotabDriverJO = new JSONObject(GeotabDriverResponse);
				  JSONArray geotabDeiverJOArray = geotabDriverJO.getJSONArray("result");

				  
				  Session session = HibernateUtil.getsession();
					Transaction transaction = session.beginTransaction();
				  
				  for(int i=0;i<geotabDeiverJOArray.length();i++)
				  {
					  try
					  {
						  
						  
							result =session.createSQLQuery("insert into gen_driver(ref_gen_user_id,driver_id,driver_name) values (:refid,:deviceid,:devicename)").setParameter("refid",obj).setParameter("deviceid",geotabDeiverJOArray.getJSONObject(i).getString("id")).setParameter("devicename",geotabDeiverJOArray.getJSONObject(i).getString("firstName")+" "+geotabDeiverJOArray.getJSONObject(i).getString("lastName")).executeUpdate();
							
						
					  }catch (Exception e) {
						// TODO: handle exception
					}
				  }
				  transaction.commit();
						
				  
			

				  
				}
				
				return null;
	}
	
	public static List<String> getTripRecords(String geouserid, String databaseName, String geosess, String url,String sdate,String edate) throws MalformedURLException, IOException
	{
		

		
		List<String> compaindRecord=new ArrayList<String>();
		String fromDate=sdate+"T00:00:00.000Z";
		String toDate=edate+"T10:00:00.000Z";
		 String uri = "https://"+url+"/apiv1";
	      String urlParameters ="{\"method\":\"Get\",\"params\":{\"typeName\":\"Trip\",\"search\":{\"fromDate\":\""+fromDate+"\",\"toDate\":\""+toDate+"\"},\"credentials\":{\"database\":\""+databaseName+"\",\"sessionId\":\""+geosess+"\",\"userName\":\""+geouserid+"\"}}}";
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
	      String GeotabDriverResponse = o.toString(); //Responce
	      
	      JSONObject geotabEventsJO = new JSONObject(GeotabDriverResponse);
			JSONArray geotabEventsJOArray = geotabEventsJO.getJSONArray("result");
			
			
		
			
			Map<String,String> deviceName=new LinkedHashMap<String,String>();
			Map<String,String> driverName=new LinkedHashMap<String,String>();
			
			List<GenDevice> deviceNameList=new ArrayList<GenDevice>();
			List<GenDriver> driverNameList=new ArrayList<GenDriver> ();
			
			try
			{
				driverNameList=glReportDAO.driverName(geouserid,databaseName);
				
				for(int i=0;i<driverNameList.size();i++)
				{
					driverName.put(driverNameList.get(i).getDriverId(),driverNameList.get(i).getDriverName());
				}
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
			}
			
			try
			{
				deviceNameList=glReportDAO.deviceName(geouserid,databaseName);
				for(int i=0;i<deviceNameList.size();i++)
				{
					deviceName.put(deviceNameList.get(i).getDeviceId(),deviceNameList.get(i).getDeviceName());
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
			
			for (int i = 0; i < geotabEventsJOArray.length(); i++) 
			{
				
				try {
				  JSONObject resultsChild = geotabEventsJOArray.getJSONObject(i);
				    JSONObject driverIdJO = resultsChild.getJSONObject("driver");
				    JSONObject deviceIdJO = resultsChild.getJSONObject("device");
				    //vehicleName
				    long dataCallsStart=System.currentTimeMillis();

				    //String driverName = GL_Report_DAO.driverName(geouserid, driverIdJO.getString("id").toString());
				    //String deviceName = GL_Report_DAO.deviceName(geouserid, deviceIdJO.getString("id").toString()); 
				    
				 
				    

				    
				    String start=resultsChild.getString("start");
				    String stop=resultsChild.getString("stop");
				    compaindRecord.add(deviceName.get(deviceIdJO.getString("id").toString())+"|"+driverName.get(driverIdJO.getString("id").toString())+"|"+start+"|"+stop);
		
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
			
			
			

	      
	   	return compaindRecord;

	}
	
	public static ArrayList<String> getTrip(String geouserid, String databaseName, String geosess, String url,String sdate,String edate) throws MalformedURLException, IOException
	{
		
		ArrayList<String> compaindRecord = new ArrayList<String>();
		
		
		String fromDate=sdate+"T00:00:00.000Z";
		String toDate=edate+"T10:00:00.000Z";
		 String uri = "https://"+url+"/apiv1";
	      String urlParameters ="{\"method\":\"Get\",\"params\":{\"typeName\":\"Trip\",\"search\":{\"fromDate\":\""+fromDate+"\",\"toDate\":\""+toDate+"\"},\"credentials\":{\"database\":\""+databaseName+"\",\"sessionId\":\""+geosess+"\",\"userName\":\""+geouserid+"\"}}}";
	      String serverurl = uri;
	      
	      System.out.println(urlParameters);
	      
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
	      String GeotabDriverResponse = o.toString(); //Responce

	      JSONObject geotabEventsJO = new JSONObject(GeotabDriverResponse);
			JSONArray geotabEventsJOArray = geotabEventsJO.getJSONArray("result");
			
			
		
			
			Map<String,String> deviceName=new LinkedHashMap<String,String>();
			Map<String,String> driverName=new LinkedHashMap<String,String>();
			
			List<GenDevice> deviceNameList=new ArrayList<GenDevice>();
			List<GenDriver> driverNameList=new ArrayList<GenDriver> ();
			
			try
			{
				driverNameList=glReportDAO.driverName(geouserid,databaseName);
				
				for(int i=0;i<driverNameList.size();i++)
				{
					driverName.put(driverNameList.get(i).getDriverId(),driverNameList.get(i).getDriverName());
				}
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
			}
			
			try
			{
				deviceNameList=glReportDAO.deviceName(geouserid,databaseName);
				for(int i=0;i<deviceNameList.size();i++)
				{
					deviceName.put(deviceNameList.get(i).getDeviceId(),deviceNameList.get(i).getDeviceName());
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
			
			for (int i = 0; i < geotabEventsJOArray.length(); i++) 
			{
				
				try {
				  JSONObject resultsChild = geotabEventsJOArray.getJSONObject(i);
				    JSONObject driverIdJO = resultsChild.getJSONObject("driver");
				    JSONObject deviceIdJO = resultsChild.getJSONObject("device");
				    //vehicleName
				    long dataCallsStart=System.currentTimeMillis();

				    //String driverName = GL_Report_DAO.driverName(geouserid, driverIdJO.getString("id").toString());
				    //String deviceName = GL_Report_DAO.deviceName(geouserid, deviceIdJO.getString("id").toString()); 
				    
				 
				    

				    
				    String start=resultsChild.getString("start");
				    String stop=resultsChild.getString("stop");
				    compaindRecord.add(deviceName.get(deviceIdJO.getString("id").toString())+"|"+driverName.get(driverIdJO.getString("id").toString())+"|"+start+"|"+stop);
		
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
			
			
	      
	   	return compaindRecord;

	}
	

}
