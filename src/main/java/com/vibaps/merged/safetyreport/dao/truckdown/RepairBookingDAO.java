package com.vibaps.merged.safetyreport.dao.truckdown;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import javax.transaction.Transactional;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vibaps.merged.safetyreport.entity.truckdown.TdUser;
import com.vibaps.merged.safetyreport.services.truckdown.RepairBookingService;
import com.vibaps.merged.safetyreport.services.truckdown.TdUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;



@Component
public class RepairBookingDAO{

	@Autowired
	private TdUserService repairBookingService;
	
	
	public Object view(Double lat, Double lng,String serviceType,String day) throws MalformedURLException, IOException {
		Optional<TdUser> truckdoun=null;
		
		JSONObject holeResponce = new JSONObject();
		JSONArray holeCompany=new JSONArray();
		
		
		
		 truckdoun=repairBookingService.findById(Long.valueOf(1));
		 String serviceResponse="";
		 String componyResponce="";
		 String url="";
		if(truckdoun.isPresent())
		 {
			 url=truckdoun.get().getBaseUrl()+"search?key="+truckdoun.get().getKey()+"&service="+serviceType+"&lng="+lng+"&lat="+lat;
			
		 }
		
		
		serviceResponse=getTruckdownResponce(url);
			
		
		 JSONObject serviceObject = new JSONObject(serviceResponse);
	        JSONArray serviceArray = serviceObject.getJSONArray("ls");
	        
	        for (int i = 0; i < serviceArray.length(); i++) 
	        {
	        	JSONObject uiResponce = new JSONObject();

	    		
	    		JSONArray indujualCompany=new JSONArray();
	        	
	        	
	            JSONObject servicecJO = serviceArray.getJSONObject(i);
	            Long lid = servicecJO.getLong("lid");
	            String ids = servicecJO.getString("_id");
	            
	            
	            url=truckdoun.get().getBaseUrl()+"location?key="+truckdoun.get().getKey()+"&id="+lid;
	        	
	            
	            String locationResponce=null;
	            try
	            {
	            locationResponce=getTruckdownResponce(url);
	            }catch (Exception e) {
					// TODO: handle exception
				}
	            componyResponce="{\"result\":["+locationResponce+"]}";
	            
	            
	            url=truckdoun.get().getBaseUrl()+"phone?key="+truckdoun.get().getKey()+"&id="+ids;
	        	
	            
	            String phoneResponce=null;
	            try
	            {
	            	phoneResponce="{\"result\":["+getTruckdownResponce(url)+"]}";
	            }catch (Exception e) {
					// TODO: handle exception
				}
	            
	            
	            
	            try
	            {
	            	
	            JSONObject companyObject = new JSONObject(componyResponce);
		        JSONArray companyArray = companyObject.getJSONArray("result");
		        
		        for (int j = 0; j < companyArray.length(); j++) 
		        {
		        	JSONObject companyJO = companyArray.getJSONObject(j);
		        	JSONObject timing = companyJO.getJSONObject("hours");
		        	JSONObject entries= timing.getJSONObject("entries");
		        	JSONObject dayentries= entries.getJSONObject(day);
		        	uiResponce.put("company_name",servicecJO.getString("n"));
		        	uiResponce.put("company_timing",dayentries);
		        	JSONObject companyphoneObject = new JSONObject(phoneResponce);
		        	uiResponce.put("company_phone",companyphoneObject.getJSONArray("result").get(0));
		        	
					/*
					 * JSONArray dayentriesvalue= dayentries.getJSONArray("entries");
					 * 
					 * for(int k=0;k<dayentriesvalue.length();k++) { JSONObject dayJO =
					 * dayentriesvalue.getJSONObject(k); String startTime = dayJO.getString("st");
					 * String endTime = dayJO.getString("et"); String openstatus =
					 * dayJO.getString("t"); System.out.println(startTime +"----"+ endTime); }
					 */
		        	 holeCompany.put(uiResponce);
		        	 
		        }
	            }catch (Exception e) {
					// TODO: handle exception
	            	System.out.println(e);
				}
	           
	            //catch on array
	           
	        }
	        holeResponce.put("company_details",holeCompany);   
		System.out.println("{\"company_details\":"+holeCompany+"}");
		

		return "{\"company_details\":"+holeCompany+"}";
	}
	
	

	private String getTruckdownResponce(String urls) throws MalformedURLException, IOException
	{
		StringBuilder response = new StringBuilder();
		
		
		try {
		       URL url = new URL(urls);
		       HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		if (conn.getResponseCode() != 200) {
		       throw new RuntimeException("Failed : HTTP error code : "
		                     + conn.getResponseCode());
		}
		 
		BufferedReader br = new BufferedReader(new InputStreamReader(
		       (conn.getInputStream())));
		String output;
		while ((output = br.readLine()) != null) {
		      
		       response.append(output);
		        response.append('\r');
		}
		conn.disconnect();
		} catch (MalformedURLException e) {
		e.printStackTrace();
		} catch (IOException e) {
		e.printStackTrace();
		}
		
		return response.toString();
		
	}
	

}
