package com.vibaps.merged.safetyreport.api.trending;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
import com.vibaps.merged.safetyreport.services.trending.RestTrendingReportService;

@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping({ "/trendingreport" })
public class RestTrendingReport {
	@Autowired
	private RestTrendingReportService restTrendingReportService;

	@PostMapping(value ="/getTrendingReport")
	public Object getReportGeo(@RequestBody ReportParams reportParams)
			throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {
		return restTrendingReportService.getReportGeo(reportParams);

		
	}

	/*
	 * @PostMapping(value ="/getTrendingReportNonLytx") public Object
	 * getReportGeoLytx(@RequestBody ReportParams reportParams) throws
	 * EncryptedDocumentException, InvalidFormatException, IOException { return
	 * restTrendingReportService.getReportGeoLytx(reportParams); }
	 */
	
	@PostMapping(value ="/createExcelTrendingReport")
	private String createExcelReport(@RequestBody ReportParams reportParams)
			throws IOException, FileNotFoundException {
		return restTrendingReportService.createExcelReport(reportParams);

	}


}