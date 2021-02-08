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
	
	@Autowired
	Optional<TdUser> truckdoun;
	
	
	public Object view(Double lat, Double lng,String serviceType,String day) throws MalformedURLException, IOException {
		
		
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
		System.out.println(url);
		
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
<<<<<<< HEAD
	           
	            locationResponce=getTruckdownResponce(url);
	            
=======
	            try
	            {
	            locationResponce=getTruckdownResponce(url);
	            }catch (Exception e) {
					// TODO: handle exception
				}
>>>>>>> feature/truckdown
	            componyResponce="{\"result\":["+locationResponce+"]}";
	            
	            
	            url=truckdoun.get().getBaseUrl()+"phone?key="+truckdoun.get().getKey()+"&id="+ids;
	        	
	            
	            String phoneResponce=null;
<<<<<<< HEAD
	           
	            	phoneResponce="{\"result\":["+getTruckdownResponce(url)+"]}";
	            
	            
	            
	            
	         
=======
	            try
	            {
	            	phoneResponce="{\"result\":["+getTruckdownResponce(url)+"]}";
	            }catch (Exception e) {
					// TODO: handle exception
				}
	            
	            
	            
	            try
	            {
>>>>>>> feature/truckdown
	            	
	            JSONObject companyObject = new JSONObject(componyResponce);
		        JSONArray companyArray = companyObject.getJSONArray("result");
		        
		        for (int j = 0; j < companyArray.length(); j++) 
		        {
		        	JSONObject companyJO = companyArray.getJSONObject(j);
		        	JSONObject timing = companyJO.getJSONObject("hours");
		        	JSONObject entries= timing.getJSONObject("entries");
		        	JSONObject dayentries= entries.getJSONObject(day);
		        	uiResponce.put("company_name",servicecJO.getString("n"));
		        	uiResponce.put("website",servicecJO.getString("wa"));
		        	uiResponce.put("company_address",servicecJO.getJSONObject("a"));
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
<<<<<<< HEAD
	           
=======
	            }catch (Exception e) {
					// TODO: handle exception
	            	System.out.println(e);
				}
>>>>>>> feature/truckdown
	           
	            //catch on array
	           
	        }
	        holeResponce.put("company_details",holeCompany);   
		System.out.println("{\"company_details\":"+holeCompany+"}");
		

		return "{\"company_details\":"+holeCompany+"}";
	}
	
	public Object getTruckdowndealer(Double lat, Double lng,String serviceType) throws MalformedURLException, IOException
	{
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
		
<<<<<<< HEAD
		
=======
		try
	      {	
>>>>>>> feature/truckdown
			serviceResponse=getTruckdownResponce(url);
			
			
			
		JSONObject serviceObject = new JSONObject(serviceResponse);
        JSONArray serviceArray = serviceObject.getJSONArray("ls");
      
        for (int i = 0; i < serviceArray.length(); i++) 
        {
        	JSONObject uiResponce = new JSONObject();

    		
        	
        	
            JSONObject servicecJO = serviceArray.getJSONObject(i);
            Long lid = servicecJO.getLong("lid");
            String ids = servicecJO.getString("_id");
            
            uiResponce.put("company_name",servicecJO.getString("n"));
        	uiResponce.put("website",servicecJO.getString("wa"));
        	uiResponce.put("company_address",servicecJO.getJSONObject("a"));
        	uiResponce.put("lid",lid);
        	uiResponce.put("id",ids);
        	
        	holeCompany.put(uiResponce);
        }
        holeResponce.put("company_details",holeCompany);  
<<<<<<< HEAD
      
=======
      }catch (Exception e) {
		// TODO: handle exception
    	  System.out.println("-----"+e);
	} 
>>>>>>> feature/truckdown
		
		return holeResponce.toString();
	}
	
	public Object getTruckTiming(Long lid) throws MalformedURLException, IOException
	{
		JSONObject holeResponce = new JSONObject();
		JSONArray holeCompany=new JSONArray();
		
		
		 truckdoun=repairBookingService.findById(Long.valueOf(1));
		 String serviceResponse="";
		 String componyResponce="";
		 String url="";
		if(truckdoun.isPresent())
		 {
			url=truckdoun.get().getBaseUrl()+"location?key="+truckdoun.get().getKey()+"&id="+lid;
		 }
		 String locationResponce=null;
<<<<<<< HEAD
         
         locationResponce=getTruckdownResponce(url);
         
=======
         try
         {
         locationResponce=getTruckdownResponce(url);
         }catch (Exception e) {
				// TODO: handle exception
			}
>>>>>>> feature/truckdown
         componyResponce="{\"result\":["+locationResponce+"]}";
         
        
		return componyResponce.toString();
	}
	
<<<<<<< HEAD
	public Object getTruckdownservices() throws MalformedURLException, IOException {
=======
	public Object getTruckdownservices() {
>>>>>>> feature/truckdown
		String url="";
		
		 truckdoun=repairBookingService.findById(Long.valueOf(1));
		 
		 url=truckdoun.get().getBaseUrl()+"service?key="+truckdoun.get().getKey();
    	
        
        String serviceResponce=null;
<<<<<<< HEAD
        
        	serviceResponce="{\"dropval\":["+getTruckdownResponce(url)+"]}";
        
=======
        try
        {
        	serviceResponce="{\"dropval\":["+getTruckdownResponce(url)+"]}";
        }catch (Exception e) {
				// TODO: handle exception
			}
>>>>>>> feature/truckdown
        JSONObject companyphoneObject = new JSONObject(serviceResponce);
        
       
		return companyphoneObject.toString();
	}
	
	
	public Object getTruckPhone(String ids) throws MalformedURLException, IOException
	{
		JSONObject holeResponce = new JSONObject();
		JSONArray holeCompany=new JSONArray();
		
		
		 truckdoun=repairBookingService.findById(Long.valueOf(1));
		 String serviceResponse="";
		 String componyResponce="";
		 String url="";
		 
		 url=truckdoun.get().getBaseUrl()+"phone?key="+truckdoun.get().getKey()+"&id="+ids;
     	
         
         String phoneResponce=null;
<<<<<<< HEAD
        
         	phoneResponce="{\"result\":["+getTruckdownResponce(url)+"]}";
         
=======
         try
         {
         	phoneResponce="{\"result\":["+getTruckdownResponce(url)+"]}";
         }catch (Exception e) {
				// TODO: handle exception
			}
>>>>>>> feature/truckdown
         JSONObject companyphoneObject = new JSONObject(phoneResponce);
         
        
		return companyphoneObject.getJSONArray("result").get(0).toString();
	}
	

	public String getTruckdownResponce(String urls) throws MalformedURLException, IOException
	{
		StringBuilder response = new StringBuilder();
		
		
<<<<<<< HEAD
		
=======
		try {
>>>>>>> feature/truckdown
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
<<<<<<< HEAD
	
=======
		} catch (MalformedURLException e) {
		e.printStackTrace();
		} catch (IOException e) {
		e.printStackTrace();
		}
>>>>>>> feature/truckdown
		
		return response.toString();
		
	}

	
	

}
