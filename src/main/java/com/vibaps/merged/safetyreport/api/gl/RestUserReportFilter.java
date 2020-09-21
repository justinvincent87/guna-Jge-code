package com.vibaps.merged.safetyreport.api.gl;

import static com.vibaps.merged.safetyreport.common.AppConstants.RESPONSE_SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibaps.merged.safetyreport.dto.gl.ReportFilter;
import com.vibaps.merged.safetyreport.services.gl.UserReportFilterService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping("/user-report-filter")
public class RestUserReportFilter {
	
	@Autowired
	private UserReportFilterService reportFilterService;

	@PostMapping(value = "/persist", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String insert(@RequestBody ReportFilter reportFilter) {
		
		if(log.isDebugEnabled()) {
			log.debug("User report filter payload: {}", reportFilter);
		}
		reportFilterService.persistReportFilter(reportFilter);
		return RESPONSE_SUCCESS;
	}
}