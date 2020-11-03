package com.vibaps.merged.safetyreport.api.gl;


import com.vibaps.merged.safetyreport.services.gl.GL_Report_SER;
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
@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping({ "/glreport" })
public class RestDriverSafetyReport {
	
	@Autowired
	private GL_Report_SER glReportService;
	
	@PostMapping(value="/insert")
	public Object insert(@RequestBody ReportParams reportParams) {
		return glReportService.insert(reportParams);
	}

	@PostMapping(value ="/getbehave" )
	public Object view(@RequestBody ReportParams reportParams) {
		return glReportService.view(reportParams);
	}

	@PostMapping(value ="/getbehaveui")
	public Object viewui(@RequestParam String geouserid,@RequestParam String db) {
		return glReportService.viewui(geouserid,db);
	}

	@PostMapping(value ="/getbehaveadd")
	public Object viewadd(@RequestParam String geouserid,@RequestParam String db) {
		return glReportService.viewadd(geouserid,db);
	}

	@PostMapping(value="/getLybehave")
	public Object getLybehave(@RequestParam String geouserid,@RequestParam String db) {
		return glReportService.getLybehave(geouserid,db);
	}
	@PostMapping(value ="/getReport")
	public Object getReport(@RequestParam String sees, @RequestParam String sdate, @RequestParam String edate,
			@RequestParam String groupid, @RequestParam String geosees, @RequestParam ArrayList<String> geotabgroups,
			@RequestParam String geouname, @RequestParam String geodatabase, @RequestParam String url,
			@RequestParam String filename, @RequestParam String templect, @RequestParam String enttype,@RequestParam String endpoint)
			throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {

		return glReportService.process(sees, sdate, edate, groupid, geosees, geotabgroups, geouname, geodatabase, url, filename,
				templect, enttype,endpoint);

	}
	@PostMapping(value="/getReportGeo")
	public Object getReportGeo(@RequestParam String sdate, @RequestParam String edate, @RequestParam String geosees,
			@RequestParam ArrayList<String> geotabgroups, @RequestParam String userName,
			@RequestParam String geodatabase, @RequestParam String url, @RequestParam String filename,
			@RequestParam String templect, @RequestParam String enttype)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		return glReportService.getReportGeo(sdate,edate,geosees,geotabgroups,userName,geodatabase,url,filename,templect,enttype);
	}

	@PostMapping(value ="/createExcelReport",produces = { "application/string" })
	private String createExcelReport(@RequestParam String sdate, @RequestParam String edate,
			@RequestParam String geouname, @RequestParam String geodatabase, @RequestParam String url,
			@RequestParam String filename, @RequestParam String templect) throws IOException, FileNotFoundException {
	
		return glReportService.createExcelReport(sdate,edate,geouname,geodatabase,url,filename,templect);

	}




}