package com.vibaps.merged.safetyreport.services.gl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibaps.merged.safetyreport.dto.gl.ReportFilter;
import com.vibaps.merged.safetyreport.repo.gl.UserReportFilterRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserReportFilterService {
	
	@Autowired
	private UserReportFilterRepository filterRepo;
	
	/**
	 * Update user report filter options which they selected from GUI
	 * 
	 * @param reportFilter
	 */
	public void persistReportFilter(final ReportFilter reportFilter) {
		
		validateReportFilter(reportFilter);
		
		filterRepo.updateStatus(reportFilter.getCompanyId(), reportFilter.getMinMiles());
		if(log.isDebugEnabled()) {
			log.debug("Status updated for companyId: {} and minMiles: {}", reportFilter.getCompanyId(), reportFilter.getMinMiles());
		}
		
		reportFilter.getExceptions()
					.entrySet()
					.parallelStream()
					.forEach( e -> filterRepo.updateRule(reportFilter.getCompanyId(), e.getValue(), e.getKey()));
		log.info("User report filter persist success for companyId: {} and minMiles: {}", reportFilter.getCompanyId(), reportFilter.getMinMiles());
	}

	private void validateReportFilter(ReportFilter reportFilter) {
		
		if(Objects.isNull(reportFilter)) {
			throw new IllegalArgumentException("User report filter should not be null");
		}
		
		if(Objects.isNull(reportFilter.getExceptions()) || reportFilter.getExceptions().isEmpty()) {
			String errorMsg = "Exceptions should not be empty";
			log.warn(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}
	}
}