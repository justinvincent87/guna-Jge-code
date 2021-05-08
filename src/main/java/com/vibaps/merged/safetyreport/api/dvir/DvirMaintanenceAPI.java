package com.vibaps.merged.safetyreport.api.dvir;

import java.sql.SQLException;

import org.apache.poi.sl.usermodel.ObjectMetaData.Application;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vibaps.merged.safetyreport.dto.dvir.DvirDefactsResponse;
import com.vibaps.merged.safetyreport.dto.dvir.IdNameSerialization;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerParams;
import com.vibaps.merged.safetyreport.entity.gl.ComDatabase;
import com.vibaps.merged.safetyreport.services.dvir.DvirMaintanenceServices;

@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping({ "/dvirDefacts" })
public class DvirMaintanenceAPI {

	@Autowired
	private DvirMaintanenceServices dvirMaintanenceServices;
	
	@PostMapping(value = "/show-dvir-maintanence",produces=MediaType.APPLICATION_JSON_VALUE)
	public Object insertDevice(@RequestBody TrailerParams reportParams) throws SQLException{
		
		return dvirMaintanenceServices.showDvirDefacts(reportParams);
	}
	
	/*
	 * @PostMapping(value = "test-basic-data") public IdNameSerialization[]
	 * getStaticData(@RequestBody TrailerParams reportParams) {
	 * switch(reportParams.getSize()) { case 1 : return
	 * dvirMaintanenceServices.getController(reportParams); case 2 : return
	 * dvirMaintanenceServices.getFailureMode(reportParams); case 3 : return
	 * dvirMaintanenceServices.getDiagnostic(reportParams); case 4 : return
	 * dvirMaintanenceServices.getEventRule(reportParams); } return null; }
	 */
}
