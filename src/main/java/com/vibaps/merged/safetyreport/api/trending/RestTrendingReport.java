package com.vibaps.merged.safetyreport.api.trending;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.vibaps.merged.safetyreport.services.trending.restTrendingReportService;

@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping({ "/trendingreport" })
public class RestTrendingReport {
	@Autowired
	private restTrendingReportService restTrendingReportService;

	@PostMapping(value ="/getTrendingReport")
	public Object getReportGeo(@RequestParam String groupid, @RequestParam String sdate, @RequestParam String edate,
			@RequestParam String sees, @RequestParam String geosees, @RequestParam ArrayList<String> geotabgroups,
			@RequestParam String userName, @RequestParam String geodatabase, @RequestParam String url,
			@RequestParam String enttype, @RequestParam String period, @RequestParam String endpoint)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		return restTrendingReportService.getReportGeo(groupid,sdate,edate,sees,geosees,geotabgroups,userName,geodatabase,url,enttype,period,endpoint);

		
	}

	@PostMapping(value ="/getTrendingReportNonLytx")
	public Object getReportGeoLytx(@RequestParam String sdate, @RequestParam String edate, @RequestParam String geosees,
			@RequestParam ArrayList<String> geotabgroups, @RequestParam String userName,
			@RequestParam String geodatabase, @RequestParam String url, @RequestParam String enttype, String period)
			throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
		return restTrendingReportService.getReportGeoLytx(sdate,edate,geosees,geotabgroups,userName,
				geodatabase,url,enttype,period);	
	}
	
	@PostMapping(value ="/createExcelTrendingReport")
	private String createExcelReport(@RequestParam String sdate, @RequestParam String edate,
			@RequestParam String geouname, @RequestParam String geodatabase, @RequestParam String url,
			@RequestParam String filename, @RequestParam String templect, @RequestParam String entityType)
			throws IOException, FileNotFoundException {
		return restTrendingReportService.createExcelReport(sdate,edate,geouname,geodatabase,url,filename,templect,entityType);

	}


}