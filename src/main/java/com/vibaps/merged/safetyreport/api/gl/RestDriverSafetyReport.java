package com.vibaps.merged.safetyreport.api.gl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
import com.vibaps.merged.safetyreport.services.gl.RestDriverSafetyReportService;

@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping({ "/glreport" })
public class RestDriverSafetyReport {

	@Autowired
	private RestDriverSafetyReportService glReportService;

	@PostMapping(value = "/insert")
	public Object insert(@RequestBody ReportParams reportParams) {
		return glReportService.insert(reportParams);
	}

	@PostMapping(value = "/getbehave")
	public Object view(@RequestBody ReportParams reportParams) {
		return glReportService.view(reportParams);
	}

	@PostMapping(value = "/getbehaveui")
	public Object viewui(@RequestBody ReportParams reportParams) {
		return glReportService.viewui(reportParams);
	}

	@PostMapping(value = "/getbehaveadd")
	public Object viewadd(@RequestBody ReportParams reportParams) {
		return glReportService.viewadd(reportParams);
	}

	@PostMapping(value = "/getLybehave")
	public Object getLybehave(@RequestBody ReportParams reportParams) {
		return glReportService.getLybehave(reportParams);
	}

	@PostMapping(value = "/getReport", produces = MediaType.APPLICATION_JSON_VALUE)
	public Object getReport(@RequestBody ReportParams reportParams)
	        throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {

		return glReportService.process(reportParams);

	}

	@PostMapping(value = "/getReportGeo")
	public Object getReportGeo(@RequestBody ReportParams reportParams)
	        throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {
		return glReportService.getReportGeo(reportParams);
	}

	@PostMapping(value = "/createExcelReport", produces = { "application/string" })
	private String createExcelReport(@RequestBody ReportParams reportParams) throws IOException, FileNotFoundException {

		return glReportService.createExcelReport(reportParams);

	}

}