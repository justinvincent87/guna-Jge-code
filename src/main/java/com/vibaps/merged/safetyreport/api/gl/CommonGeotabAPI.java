package com.vibaps.merged.safetyreport.api.gl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
import com.vibaps.merged.safetyreport.services.gl.CommonGeotabService;

@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping({ "/geotab_common_call" })
public class CommonGeotabAPI {
@Autowired
private CommonGeotabService commonGeotabService;


@PostMapping(value ="/insertDriver")
public Object view(@RequestBody ReportParams reportParams) throws IOException {
	
	return commonGeotabService.insertDriver(reportParams);
}

@PostMapping(value = "/insertDevice")
public Object insertDevice(@RequestBody ReportParams reportParams) throws IOException {
	
	return commonGeotabService.insertDevice(reportParams);
}

@RequestMapping(value ="/getTripRecords")
public Object getTripRecords(@RequestBody ReportParams reportParams) throws IOException {
	
	return commonGeotabService.getTripRecords(reportParams);
}

}
