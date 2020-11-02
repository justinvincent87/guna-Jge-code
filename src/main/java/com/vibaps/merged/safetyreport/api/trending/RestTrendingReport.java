package com.vibaps.merged.safetyreport.api.trending;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api_GetEvents_V5.GetEventsResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lytx.dto.ExistingSessionRequest;
import com.lytx.dto.GetBehaviorsResponse;
import com.lytx.dto.GetEventsByLastUpdateDateRequest;
import com.lytx.dto.GetVehiclesRequest;
import com.lytx.dto.GetVehiclesResponse;
import com.lytx.services.ISubmissionServiceV5Proxy;
import com.vibaps.merged.safetyreport.api.gl.RestDriverSafetyReport;
import com.vibaps.merged.safetyreport.dao.gl.Common_Geotab_DAO;
import com.vibaps.merged.safetyreport.dao.gl.GL_Report_DAO;
import com.vibaps.merged.safetyreport.entity.gl.Score;
import com.vibaps.merged.safetyreport.entity.gl.Trip;
import com.vibaps.merged.safetyreport.services.gl.GL_Report_SER;
import com.vibaps.merged.safetyreport.services.trending.restTrendingReportService;

@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping({ "/trendingreport" })
public class RestTrendingReport {
	@Autowired
	private restTrendingReportService restTrendingReportService;

	@GetMapping(value ="/getTrendingReport")
	public Object getReportGeo(@RequestParam String groupid, @RequestParam String sdate, @RequestParam String edate,
			@RequestParam String sees, @RequestParam String geosees, @RequestParam ArrayList<String> geotabgroups,
			@RequestParam String userName, @RequestParam String geodatabase, @RequestParam String url,
			@RequestParam String enttype, @RequestParam String period, @RequestParam String endpoint)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		return restTrendingReportService.getReportGeo(groupid,sdate,edate,sees,geosees,geotabgroups,userName,geodatabase,url,enttype,period,endpoint);

		
	}

	@GetMapping(value ="/getTrendingReportNonLytx")
	public Object getReportGeoLytx(@RequestParam String sdate, @RequestParam String edate, @RequestParam String geosees,
			@RequestParam ArrayList<String> geotabgroups, @RequestParam String userName,
			@RequestParam String geodatabase, @RequestParam String url, @RequestParam String enttype, String period)
			throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
		return restTrendingReportService.getReportGeoLytx(sdate,edate,geosees,geotabgroups,userName,
				geodatabase,url,enttype,period);	
	}
	
	@GetMapping(value ="/createExcelTrendingReport")
	private String createExcelReport(@RequestParam String sdate, @RequestParam String edate,
			@RequestParam String geouname, @RequestParam String geodatabase, @RequestParam String url,
			@RequestParam String filename, @RequestParam String templect, @RequestParam String entityType)
			throws IOException, FileNotFoundException {
		return restTrendingReportService.createExcelReport(sdate,edate,geouname,geodatabase,url,filename,templect,entityType);

	}


}