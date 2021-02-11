package com.vibaps.merged.safetyreport.dao.truckdown;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
		
		
		
		 truckdoun=repairBookingService.findById(1L);
		 List<String> serviceResponse=new ArrayList<String>();
		 String componyResponce="";
		 String url="";
		 
		if(truckdoun.isPresent())
		 {
			String[] serviceArray = serviceType.split(",");
			
			for(int i=0;i<serviceArray.length;i++)
			{
				serviceType=serviceArray[i];
			 url=truckdoun.get().getBaseUrl()+"search?key="+truckdoun.get().getKey()+"&service="+serviceType+"&lng="+lng+"&lat="+lat;
			
				System.out.println(url);
				
				serviceResponse.add(getTruckdownResponce(url));
			}
	     }
	
			
		for(int t=0;t<serviceResponse.size();t++)
		{
		 JSONObject serviceObject = new JSONObject(serviceResponse.get(t));
	        JSONArray serviceArray = serviceObject.getJSONArray("ls");
	        
	        for (int i = 0; i < serviceArray.length(); i++) 
	        {
	        	

	    		
	    		JSONArray indujualCompany=new JSONArray();
	        	
	        	
	            JSONObject servicecJO = serviceArray.getJSONObject(i);
	            Long lid = servicecJO.getLong("lid");
	            String ids = servicecJO.getString("_id");
	          
	            
	            
	            url=truckdoun.get().getBaseUrl()+"location?key="+truckdoun.get().getKey()+"&id="+lid;
	        	
	            
	            String locationResponce=null;
	           
	            locationResponce=getTruckdownResponce(url);
	            
	            componyResponce="{\"result\":["+locationResponce+"]}";
	            
	            
	            url=truckdoun.get().getBaseUrl()+"phone?key="+truckdoun.get().getKey()+"&id="+ids;
	        	
	            
	            String phoneResponce=null;
	           
	            	phoneResponce="{\"result\":["+getTruckdownResponce(url)+"]}";
	            
	            
	            
	            
	         
	            	
	            JSONObject companyObject = new JSONObject(componyResponce);
		        JSONArray companyArray = companyObject.getJSONArray("result");
		        
		        for (int j = 0; j < companyArray.length(); j++) 
		        {
		        	
		        	JSONObject companyJO = companyArray.getJSONObject(j);
		        	JSONObject uiResponce = new JSONObject();
		        	uiResponce.put("company_name",servicecJO.getString("n"));
		        	uiResponce.put("website",servicecJO.getString("wa"));
		        	uiResponce.put("company_address",servicecJO.getJSONObject("a"));
		        	
		        	JSONObject companyphoneObject = new JSONObject(phoneResponce);
		        	uiResponce.put("company_phone",companyphoneObject.getJSONArray("result").get(0));
		        
		        	 holeCompany.put(uiResponce);
		        	 
		        }
	           
	           
	            //catch on array
	           
	        }
	        holeResponce.put("company_details",holeCompany);   
		System.out.println("{\"company_details\":"+holeCompany+"}");
		}

		return "{\"company_details\":"+holeCompany+"}";
	}
	private boolean duplicateCheck(List<String> delarArray,String ids)
	{
		boolean status=false;
		
		for (String idCheck : delarArray)
		  {
		   if (idCheck==ids)
		   {
		    status=true;
		   }
		  }
			/*
			 * System.out.println(ids+"--"+delarArray.size());
			 * System.out.println(status+"----");
			 */
		return status;
	}
	public Object getTruckdowndealer(Double lat, Double lng,String serviceType) throws MalformedURLException, IOException
	{
		JSONObject holeResponce = new JSONObject();
		JSONArray holeCompany=new JSONArray();
		
		List<String> duplicateRemove =new ArrayList<String>();
		
		 truckdoun=repairBookingService.findById(Long.valueOf(1));
		 List<String> serviceResponse=new ArrayList<String>();
		 String componyResponce="";
		 String url="";
		if(truckdoun.isPresent())
		 {
String[] serviceArray = serviceType.split(",");
			
			for(int i=0;i<serviceArray.length;i++)
			{
				serviceType=serviceArray[i];
			 url=truckdoun.get().getBaseUrl()+"search?key="+truckdoun.get().getKey()+"&service="+serviceType+"&lng="+lng+"&lat="+lat;
			 serviceResponse.add(getTruckdownResponce(url));
			}
		 }
		
		
			
			
		for(int t=0;t<serviceResponse.size();t++)	
		{
		JSONObject serviceObject = new JSONObject(serviceResponse.get(t));
        JSONArray serviceArray = serviceObject.getJSONArray("ls");
      
        for (int i = 0; i < serviceArray.length(); i++) 
        {
        	

    		
        	
        	
            JSONObject servicecJO = serviceArray.getJSONObject(i);
            Long lid = servicecJO.getLong("lid");
            String ids = servicecJO.getString("_id");
            
         
    		
    		if(duplicateRemove.isEmpty() || !duplicateCheck(duplicateRemove,ids))
    		{
    			JSONObject uiResponce = new JSONObject();
            uiResponce.put("company_name",servicecJO.getString("n"));
        	uiResponce.put("website",servicecJO.getString("wa"));
        	uiResponce.put("company_address",servicecJO.getJSONObject("a"));
        	uiResponce.put("lid",lid);
        	uiResponce.put("id",ids);
        	duplicateRemove.add(ids);
        	holeCompany.put(uiResponce);

    		}
        }
        holeResponce.put("company_details",holeCompany);  
		}
		
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
         
         locationResponce=getTruckdownResponce(url);
         
         componyResponce="{\"result\":["+locationResponce+"]}";
         
        
		return componyResponce.toString();
	}
	
	public Object getTruckdownservices() throws MalformedURLException, IOException {
		String url="";
		
		 truckdoun=repairBookingService.findById(Long.valueOf(1));
		 
		 url=truckdoun.get().getBaseUrl()+"service?key="+truckdoun.get().getKey();
    	
        
        String serviceResponce=null;
        
        	serviceResponce="{\"dropval\":["+getTruckdownResponce(url)+"]}";
        
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
        
         	phoneResponce="{\"result\":["+getTruckdownResponce(url)+"]}";
         
         JSONObject companyphoneObject = new JSONObject(phoneResponce);
         
        
		return companyphoneObject.getJSONArray("result").get(0).toString();
	}
	

	public String getTruckdownResponce(String urls) throws MalformedURLException, IOException
	{
		StringBuilder response = new StringBuilder();
		
		
		
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
	
		
		return response.toString();
		
	}

	
	

}
