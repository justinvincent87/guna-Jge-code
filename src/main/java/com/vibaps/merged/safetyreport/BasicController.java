package com.vibaps.merged.safetyreport;
import org.apache.logging.log4j.core.tools.picocli.CommandLine.Parameters;
import org.apache.tomcat.util.json.JSONParser;
import org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api_GetEvents_V5.GetEventsResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lytx.dto.Behavior;
import com.lytx.dto.ExistingSessionRequest;
import com.lytx.dto.GetBehaviorsResponse;
import com.lytx.dto.GetEventsByLastUpdateDateRequest;
import com.lytx.dto.GetGroupsByIdRequest;
import com.lytx.dto.GetGroupsResponse;
import com.lytx.dto.GetUsersRequest;
import com.lytx.dto.GetUsersResponse;
import com.lytx.dto.GetVehiclesRequest;
import com.lytx.dto.GetVehiclesResponse;
import com.lytx.dto.LoginResponse;
import com.lytx.services.ISubmissionServiceV5Proxy;
import com.vibaps.merged.safetyreport.entity.gl.Gen_Driver;
import com.vibaps.merged.safetyreport.services.gl.GL_Report_SER;

import lombok.experimental.var;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import springfox.documentation.spring.web.json.Json;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin(origins = "*",allowedHeaders = "*")
@RestController
@RequestMapping("Lytx/Api")
public class BasicController {
	@RequestMapping(value="/getLytxSessionId",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE) 
	  public java.lang.String getLytxSessionId(HttpServletRequest request) throws RemoteException 
	  { 
        return Cookies.getcook(request,"SesId");
	  }
	

	
	  @RequestMapping(value="/GetVehicle",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE) 
	  public @ResponseBody Object GetVehicle(@RequestParam String sees,@RequestParam String endpoint) throws RemoteException 
	  { 
	  ISubmissionServiceV5Proxy er=new ISubmissionServiceV5Proxy(endpoint);
		  GetVehiclesResponse vr=new GetVehiclesResponse();
		  GetVehiclesRequest getVehiclesRequest=new GetVehiclesRequest();
		  getVehiclesRequest.setIncludeSubgroups(true);
		  getVehiclesRequest.setSessionId(sees);
		  vr=er.getVehicles(getVehiclesRequest);
		  return vr;
	  }
	  
	  
	  @RequestMapping(value="/GetUser",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE) 
	  public @ResponseBody Object GetUser(@RequestParam String sees,@RequestParam String endpoint) throws RemoteException 
	  { 
	  ISubmissionServiceV5Proxy er=new ISubmissionServiceV5Proxy(endpoint);
	  GetUsersResponse vr=new GetUsersResponse();
	  GetUsersRequest getusersrequest=new GetUsersRequest();
	  getusersrequest.setSessionId(sees);
		  vr=er.getUsers(getusersrequest);
		  return vr;
	  }
	  
	  @RequestMapping(value="/GetGroupbyId",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE) 
	  public @ResponseBody Object GetGroupbyId(@RequestParam String sees,@RequestParam String endpoint) throws RemoteException 
	  { 
		  ISubmissionServiceV5Proxy er=new ISubmissionServiceV5Proxy(endpoint);
      GetGroupsResponse vr=new GetGroupsResponse();
      GetGroupsByIdRequest getgroup=new GetGroupsByIdRequest();
      getgroup.setSessionId(sees);
      getgroup.setIncludeSubgroups(true);
      
      vr=er.getGroupsById(getgroup);
      return vr;
	  }
	  
	  @RequestMapping(value="/GetEventsByLastUpdateDate",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE) 
	  public @ResponseBody Object GetEventsByLastUpdateDate(@RequestParam String sees,@RequestParam String sdate,@RequestParam String edate,@RequestParam String groupid,@RequestParam String endpoint) throws RemoteException, ParseException 
	  {      
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    String sDate=sdate;
		    String eDate=edate;
		    Date ssdate=sdf.parse(sDate);  
		    Date eedate=sdf.parse(eDate);  
		  ISubmissionServiceV5Proxy er=new ISubmissionServiceV5Proxy(endpoint);
		  GetEventsResponse vr=new GetEventsResponse();
		  GetEventsByLastUpdateDateRequest geteventbyid=new
		  GetEventsByLastUpdateDateRequest(); 
		  geteventbyid.setSessionId(sees);
		  geteventbyid.setStartDate(ssdate);
		  geteventbyid.setEndDate(eedate);
		  if(!groupid.equalsIgnoreCase("null"))
		  {
		  geteventbyid.setGroupId(Long.parseLong(groupid));
		  }
		  vr=er.getEventsByLastUpdateDate(geteventbyid);
		  
		  
		 
      return vr;
	  }
	  
	  @RequestMapping(value="/GetBehave",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE) 
	  public @ResponseBody Object GetBehave(@RequestParam String sees,@RequestParam String endpoint) throws RemoteException 
	  {
		  ISubmissionServiceV5Proxy er=new ISubmissionServiceV5Proxy(endpoint);
		ExistingSessionRequest re=new ExistingSessionRequest();
		  re.setSessionId(sees);

		  GetBehaviorsResponse getb=new GetBehaviorsResponse();
		  getb=er.getBehaviors(re);

		  
		return getb; 

	  }
	  
	  
	  
	  @RequestMapping(value="/Login",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE) 
	  public @ResponseBody Object Login(@RequestParam String dbname,String endpoint) throws RemoteException 
	  { 
		  String lytxUsername="";
		  String lytxPassword="";
		  
		  List<Object[]> list=null;
			Session session = null;

			
			Transaction transaction = null;
			try {
				session = HibernateUtil.getsession();
				transaction = session.beginTransaction();
				list =session.createSQLQuery("SELECT ly_username,ly_password FROM ly_user where dbname=:db").setParameter("db", dbname).list();
				transaction.commit();
			} catch (Exception exception) {
				System.out.println(exception);
			}
			session.close();
			
			Iterator it = list.iterator();
			while(it.hasNext()){
			     Object[] line = (Object[]) it.next();
			      lytxUsername=line[0].toString();
				  lytxPassword= line[1].toString();
			   
			     
			}
 
		  
		  
		  ISubmissionServiceV5Proxy er=new ISubmissionServiceV5Proxy(endpoint);
		  LoginResponse value=er.login(lytxUsername,lytxPassword);
		  return value;
	  }
	  

	  @RequestMapping(value="/LoginAsCompany",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE) 
	  public @ResponseBody Object LoginasCompany(@RequestParam String userid,@RequestParam String password,@RequestParam Long companyId,@RequestParam String endpoint) throws RemoteException 
	  { 
		  ISubmissionServiceV5Proxy er=new ISubmissionServiceV5Proxy(endpoint);
		  //LoginResponse value=er.login("BrigiottaEventsAPI","cQc!4FXxR8");
		  LoginResponse value=er.loginCompany(userid, password, companyId);
		  return value;
	  }
	  
	  
	  @RequestMapping(value="/GeotabCall",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE) 
	  public @ResponseBody String GeotabCall(@RequestParam String fdate,String tdate,@RequestParam String geouserid) throws IOException, ParseException 
	  { 
		  String gvalue="";
		  Object getgeodropdown=GetGeotabBehaveDropDown(geouserid);
		  
		  ArrayList getl=(ArrayList) getgeodropdown;
		  
		  for(int j=0;j<getl.size();j++)
		  {
			  if(j != getl.size()-1)
			  {
			  gvalue=gvalue+"{"+"\"id\":\""+getl.get(j)+"\"},";
			  }
			  else
			  {
				  gvalue=gvalue+"{"+"\"id\":\""+getl.get(j)+"\"}";

			  }
		  }
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		    String sDate=fdate;
		    String eDate=tdate;
		    Date ssdate=sdf.parse(sDate);  
		    Date eedate=sdf.parse(eDate);  
		
			  String uri="https://my52.geotab.com/apiv1";
		
			
			  String urlParameters  = "{\"method\":\"ExecuteMultiCall\",\"params\":{\"calls\":[{\"method\":\"GetReportData\",\"params\":{\"argument\":{\"runGroupLevel\":-1,\"isNoDrivingActivityHidden\":true,\"fromUtc\":\""+fdate+"T01:00:00.000Z\",\"toUtc\":\""+tdate+"T03:59:59.000Z\",\"entityType\":\"Device\",\"reportArgumentType\":\"RiskManagement\",\"groups\":[{\"id\":\"GroupCompanyId\"}],\"reportSubGroup\":\"None\",\"rules\":["+gvalue+"]}}},{\"method\":\"Get\",\"params\":{\"typeName\":\"SystemSettings\"}}],\"credentials\":{\"database\":\"brigiottas_farmland\",\"sessionId\":\"pzHYW10KLIECJEKA2S1cSw\",\"userName\":\"atiadmin@assuredtelematics.com\"}}}";
			  
			  String serverurl = uri;
			  
			  System.out.println(uri+"?"+urlParameters);
		        HttpURLConnection con = (HttpURLConnection) new URL(serverurl).openConnection();
		        con.setRequestMethod("POST");
		        con.setRequestProperty("Content-Type",
		           " application/json; charset=utf-8");
		        con.setRequestProperty("Content-Language", "en-US");
		        con.setDoOutput(true);
		        con.setUseCaches (false);
		        con.setDoInput(true);
		        DataOutputStream wr = new DataOutputStream (
		                  con.getOutputStream ());
		        wr.writeBytes (urlParameters);
		        wr.flush ();
		        wr.close ();

		        //Get Response
		        InputStream is =con.getInputStream();
		        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		        String line;
		        StringBuilder response = new StringBuilder();
		        while((line = rd.readLine()) != null) {
		            response.append(line);
		            response.append('\r');
		        }
		        rd.close();
		        JsonParser parser = new JsonParser();
		        JsonObject o = parser.parse(response.toString()).getAsJsonObject();
		        //JsonObject s = parser.parse(o.getAsJsonArray("result").get(0).toString()).getAsJsonObject();
				
		        return "{\"results\":"+o.getAsJsonArray("result").get(0).toString()+"}";
			 
	  }
	  @RequestMapping(value="/GetGeotabBehaveDropDown",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE) 
	  public @ResponseBody Object GetGeotabBehaveDropDown(@RequestParam String geouserid) throws RemoteException 
	  { 
		  GL_Report_SER rep=new GL_Report_SER();
		  return rep.getgeodropdown(geouserid);
	  }
	  
	  /*  @RequestMapping(value="/GetEventsByLastUpdateDateforReport",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE) 
	
	 * public @ResponseBody Object GetEventsByLastUpdateDateforReport(@RequestParam
	 * String sees,@RequestParam String sdate,@RequestParam String
	 * edate,@RequestParam String groupid,@RequestParam String endpoint) throws
	 * RemoteException, ParseException { SimpleDateFormat sdf = new
	 * SimpleDateFormat("yyyy-MM-dd"); String sDate=sdate; String eDate=edate; Date
	 * ssdate=sdf.parse(sDate); Date eedate=sdf.parse(eDate);
	 * ISubmissionServiceV5Proxy er=new ISubmissionServiceV5Proxy(endpoint);
	 * GetEventsResponse vr=new GetEventsResponse();
	 * GetEventsByLastUpdateDateRequest geteventbyid=new
	 * GetEventsByLastUpdateDateRequest(); geteventbyid.setSessionId(sees);
	 * geteventbyid.setStartDate(ssdate); geteventbyid.setEndDate(eedate);
	 * if(!groupid.equalsIgnoreCase("null")) {
	 * geteventbyid.setGroupId(Long.parseLong(groupid)); }
	 * 
	 * 
	 * 
	 * vr=er.getEventsByLastUpdateDate(geteventbyid); Object
	 * gv=GetVehicle(sees,endpoint);
	 * 
	 * return TypeUtils.getlyval(vr,gv); }
	 */
	  
	  
	  
    @GetMapping("/ExcelDownload")
    public ModelAndView showLoginPage(Model model) 
    {
       return new ModelAndView("exceldownload");
    }
    
    public static Object GetEventsByLastUpdateDates(@RequestParam String sees,@RequestParam String sdate,@RequestParam String edate,@RequestParam String groupid,@RequestParam String endpoint) throws RemoteException, ParseException 
	  {      
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    String sDate=sdate;
		    String eDate=edate;
		    Date ssdate=sdf.parse(sDate);  
		    Date eedate=sdf.parse(eDate);  
		  ISubmissionServiceV5Proxy er=new ISubmissionServiceV5Proxy(endpoint);
		  GetEventsResponse vr=new GetEventsResponse();
		  GetEventsByLastUpdateDateRequest geteventbyid=new
		  GetEventsByLastUpdateDateRequest(); 
		  geteventbyid.setSessionId(sees);
		  geteventbyid.setStartDate(ssdate);
		  geteventbyid.setEndDate(eedate);
		  if(!groupid.equalsIgnoreCase("null"))
		  {
		  geteventbyid.setGroupId(Long.parseLong(groupid));
		  }
		  vr=er.getEventsByLastUpdateDate(geteventbyid);
		  
		  
		 
    return vr;
	  }
    
    
    
}


