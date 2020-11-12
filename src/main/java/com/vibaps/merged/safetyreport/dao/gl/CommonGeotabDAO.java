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
import com.vibaps.merged.safetyreport.api.gl.RestDriverSafetyReport;
import com.vibaps.merged.safetyreport.entity.gl.GenDevice;
import com.vibaps.merged.safetyreport.entity.gl.GenDriver;
import com.vibaps.merged.safetyreport.entity.gl.GlRulelistEntity;
import com.vibaps.merged.safetyreport.repo.gl.CommonGeotabRepository;
import com.vibaps.merged.safetyreport.repo.gl.UserReportFilterRepository;

@Repository
public class CommonGeotabDAO  {
	@Autowired
	private GlReportDAO glReportDAO;
	@Autowired
	private CommonGeotabRepository commonGeotabRepository;
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

		  
		  
		  for(int i=0;i<geotabDeiverJOArray.length();i++)
		  {
			  try
			  {
				  commonGeotabRepository.insertDevice(obj, geotabDeiverJOArray.getJSONObject(i).getString("id"), geotabDeiverJOArray.getJSONObject(i).getString("name"));
			
				
			  }catch (Exception e) {
				// TODO: handle exception
			}
		  }
  
		}
		
		return "Saved";
	}
	
private int  getCompanyId(String geotabUserId,String db) {
		
		int obj = 0;
		
		try {
			
			
			obj=commonGeotabRepository.getCompanyId(geotabUserId, db);
			
			
		} catch (Exception exception) {
			
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

				
				  
				  for(int i=0;i<geotabDeiverJOArray.length();i++)
				  {
					  try
					  {
						  
						  commonGeotabRepository.insertDriver(obj, geotabDeiverJOArray.getJSONObject(i).getString("id"), geotabDeiverJOArray.getJSONObject(i).getString("firstName")+" "+geotabDeiverJOArray.getJSONObject(i).getString("lastName"));
					  }catch (Exception e) {
						// TODO: handle exception
					}
				  }
  
				}
				
				return null;
	}
	
	public List<String> getTripRecords(String geouserid, String databaseName, String geosess, String url,String sdate,String edate) throws MalformedURLException, IOException
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
	
	public ArrayList<String> getTrip(String geouserid, String databaseName, String geosess, String url,String sdate,String edate) throws MalformedURLException, IOException
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
