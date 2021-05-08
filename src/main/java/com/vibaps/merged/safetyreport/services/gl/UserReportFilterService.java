package com.vibaps.merged.safetyreport.services.gl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibaps.merged.safetyreport.common.AppConstants;
import com.vibaps.merged.safetyreport.dto.gl.Behave;
import com.vibaps.merged.safetyreport.dto.gl.ReportFilter;
import com.vibaps.merged.safetyreport.entity.gl.GlRulelistEntity;
import com.vibaps.merged.safetyreport.repo.gl.UserReportFilterRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserReportFilterService {
	
	@Autowired
	private UserReportFilterRepository filterRepo;
	
	/**
	 * Load selected rule names from DB
	 * 
	 * @param username
	 * @param database
	 * @return
	 */
	public List<Behave> getSelectedRuleNames(String username, String database) {
		
		List<Object[]> data = filterRepo.getallBehaveFromDB(username, database);
		
		if(CollectionUtils.isEmpty(data)) {
			if(log.isDebugEnabled()) {
				log.debug("Behave doesn't exist for username: {} and database:{}", username, database);
			}
			return Collections.emptyList();
		}
		
		log.info("Behave matched count: {}", data.size());
		
		return data.stream()
			.map(record -> new Behave((String) record[0], (Integer) record[1],(String) record[2]))
			.collect(Collectors.toList());
	}
	
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
	
	public List<GlRulelistEntity> getGeoDropDown(String username, String database){
		return filterRepo.getgeodropdown(username, database);
	}
	
	/**
	 * Get combine default and db column headers
	 * 
	 * @param userName
	 * @param db
	 * @return
	 */
	public List<String> loadReporColumntHeaders(String userName, String db) {
		List<String> reportColumnHeader = new ArrayList<String>();
		reportColumnHeader.addAll(Arrays.asList(AppConstants.DEFAULT_HEADERS));
		reportColumnHeader.addAll(getSelectedRuleNames(userName, db).stream()
				.map(Behave::getRulevalue).collect(Collectors.toList()));
		return reportColumnHeader;
	}
}