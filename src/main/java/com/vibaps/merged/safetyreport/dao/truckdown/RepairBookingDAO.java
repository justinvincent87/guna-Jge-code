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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.mail.handlers.text_plain;
import com.vibaps.merged.safetyreport.dto.truckdown.TruckDownResponce;
import com.vibaps.merged.safetyreport.entity.truckdown.TdUser;
import com.vibaps.merged.safetyreport.repo.truckdown.TdUserRepo;
import com.vibaps.merged.safetyreport.services.truckdown.RepairBookingService;
import com.vibaps.merged.safetyreport.services.truckdown.TdUserService;
import com.vibaps.merged.safetyreport.util.ResponseUtil;

import javassist.expr.NewArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;



@Component
public class RepairBookingDAO{

	@Autowired
	private TdUserService repairBookingService;
	
	@Autowired
	Optional<TdUser> truckdoun;
	
	@Autowired
	TdUserRepo tdUserRepo;
	
	
	
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

		return status;
	}
	public TruckDownResponce getTruckdowndealer(Double lat, Double lng,String serviceType) throws MalformedURLException, IOException
	{
		JSONObject holeResponce = new JSONObject();
		List<TruckDownResponce> holeCompany=new ArrayList<TruckDownResponce>();
		
		List<String> duplicateRemove =new ArrayList<String>();
		
		 truckdoun=repairBookingService.findById(Long.valueOf(1));
		 List<JsonObject> serviceResponse=new ArrayList<JsonObject>();
		 String componyResponce="";
		 String url="";
		if(truckdoun.isPresent())
		 {
String[] serviceArray = serviceType.split(",");
			
			for(int i=0;i<serviceArray.length;i++)
			{
				serviceType=serviceArray[i];
			 url=truckdoun.get().getBaseUrl()+"search?key="+truckdoun.get().getKey()+"&service="+serviceType+"&lng="+lng+"&lat="+lat;
			 serviceResponse.add(ResponseUtil.parseResponse(getTruckdownResponce(url)));
	      

			}
		 }
		
		for(int t=0;t<serviceResponse.size();t++)	
		{
		JsonObject serviceObject = serviceResponse.get(t);
        JsonArray serviceArray = serviceObject.getAsJsonArray("ls");
      
        for (int i = 0; i < serviceArray.size(); i++) 
        {
        	JsonObject servicecJO = serviceArray.get(i).getAsJsonObject();
            Long lid = servicecJO.get("lid").getAsLong();
            String ids = servicecJO.get("_id").getAsString();
            
         
    		
    		if(duplicateRemove.isEmpty() || !duplicateCheck(duplicateRemove,ids))
    		{
    		
    		TruckDownResponce uiResponce = new TruckDownResponce(servicecJO.get("n").getAsString(), servicecJO.get("wa").getAsString(), servicecJO.get("a").getAsJsonObject().toString(), lid, ids);
            duplicateRemove.add(ids);
        	holeCompany.add(uiResponce);

    		}
        }
		}

		return new TruckDownResponce(holeCompany);
	}


	
	
	public String getTruckTiming(Long lid) throws MalformedURLException, IOException
	{
		
		
		 truckdoun=repairBookingService.findById(Long.valueOf(1));
		 String serviceResponse="";
		 String url="";
		if(truckdoun.isPresent())
		 {
			url=truckdoun.get().getBaseUrl()+"location?key="+truckdoun.get().getKey()+"&id="+lid;
		 }
		return ResponseUtil.parseResponseArray(getTruckdownResponce(url));
	}
	
	public String getTruckdownservices() throws MalformedURLException, IOException {
		String url="";
		
		
		 truckdoun=repairBookingService.findById(Long.valueOf(1));
		 
		 url=truckdoun.get().getBaseUrl()+"service?key="+truckdoun.get().getKey();
		 
		 return ResponseUtil.parseResponseArray(getTruckdownResponce(url));
		 }
	

	public String getTruckPhone(String ids) throws MalformedURLException, IOException
	{

		 truckdoun=repairBookingService.findById(Long.valueOf(1));
		 String url="";
		 url=truckdoun.get().getBaseUrl()+"phone?key="+truckdoun.get().getKey()+"&id="+ids;
		 return ResponseUtil.parseResponseArray(getTruckdownResponce(url));

	}
	
	
	public ResponseEntity<String> getTruckdownResponce(String urls) throws MalformedURLException, IOException
	{
		Optional<TdUser> truckdoun=repairBookingService.findById(1L);
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();   
	    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	 	headers.setBearerAuth(truckdoun.get().getAccessToken());
	    HttpEntity request = new HttpEntity(headers);
	 	ResponseEntity<String> response = restTemplate.exchange(urls,HttpMethod.GET,request, String.class);    	    
		return response;
		
	}

	
	@Transactional
	@Scheduled(cron = "0 0/45 * * * ?")
	public int getTruckdownOauthResponce() throws MalformedURLException, IOException
	{
				Optional<TdUser> truckdoun=repairBookingService.findById(1L);
				String serverurl = truckdoun.get().getBaseUrl()+"apiv2/oauth2/token?key="+truckdoun.get().getKey();
				String urlParameters=getTokenParameter();
				RestTemplate restTemplate = new RestTemplate();
			    HttpHeaders headers = new HttpHeaders();   
			    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			 	headers.setBearerAuth(truckdoun.get().getAccessToken());
			    HttpEntity request = new HttpEntity(headers);
			 	ResponseEntity<String> response = restTemplate.exchange(serverurl,HttpMethod.POST, request, String.class,urlParameters);
				JSONObject res=new JSONObject(response.toString());
		        return updatetoken(res);
	}
	
	private String getTokenParameter()
	{
		Optional<TdUser> truckdoun=repairBookingService.findById(1L);
		
		 String urlParameters="{\n"
		      		+ "\"api_key\": \""+truckdoun.get().getKey()+"\",\n"
		      		+ "\"api_secret\":\""+truckdoun.get().getSecretKey()+"\",\n"
		      		+ "\"token\": \""+truckdoun.get().getToken()+"\",\n"
		      		+ "\"grant_type\": \""+truckdoun.get().getGrantType()+"\"\n"
		      		+ "}";
		 
		 return urlParameters;
	}
	
	@Transactional
	public int updatetoken(JSONObject oauthresponce)
	{
		return tdUserRepo.update(oauthresponce.getString("refresh_token").toString(),"refresh_token",oauthresponce.getString("access_token").toString(),1L);
	}
	
	
	

}
