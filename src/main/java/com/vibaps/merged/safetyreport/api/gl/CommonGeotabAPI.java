package com.vibaps.merged.safetyreport.api.gl;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerParams;
import com.vibaps.merged.safetyreport.entity.gl.ComDatabase;
import com.vibaps.merged.safetyreport.services.gl.CommonGeotabService;

@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping({ "/geotab_common_call" })
public class CommonGeotabAPI {

@Autowired
private CommonGeotabService commonGeotabService;




@PostMapping(value = "/insertDevice")
public ComDatabase insertDevice(@RequestBody TrailerParams reportParams) throws SQLException{
	
	return commonGeotabService.insertDevice(reportParams);
}
@PostMapping(value = "/insertTrailer")
public ComDatabase insertTrailer(@RequestBody TrailerParams reportParams) throws SQLException{
	
	return commonGeotabService.insertTrailer(reportParams);
}




}
