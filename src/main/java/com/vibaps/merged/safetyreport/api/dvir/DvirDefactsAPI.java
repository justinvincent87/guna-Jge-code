package com.vibaps.merged.safetyreport.api.dvir;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibaps.merged.safetyreport.dto.dvir.DvirDefactsResponse;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerParams;
import com.vibaps.merged.safetyreport.entity.gl.ComDatabase;
import com.vibaps.merged.safetyreport.services.dvir.DvirDefactsServices;

@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping({ "/dvirDefacts" })
public class DvirDefactsAPI {

	@Autowired
	private DvirDefactsServices dvirDefactsServices;
	
	@PostMapping(value = "/show-dvir-defacts")
	public DvirDefactsResponse insertDevice(@RequestBody TrailerParams reportParams) throws SQLException{
		
		return dvirDefactsServices.showDvirDefacts(reportParams);
	}
}
