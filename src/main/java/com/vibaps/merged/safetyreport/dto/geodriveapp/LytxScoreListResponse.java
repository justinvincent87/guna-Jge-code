package com.vibaps.merged.safetyreport.dto.geodriveapp;

import java.util.List;

import javax.persistence.Transient;

import org.apache.poi.ss.formula.functions.T;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class LytxScoreListResponse{
	
	@JsonInclude(Include.NON_NULL)
	private String employeeNum;
	
	private Float totalScore;
	private String firstName;
	private String lastName;
	
	@Transient 
	private Integer rank;
	@Transient 
	private String range;
	@Transient 
	private String stareDate;
	@Transient 
	private String endDate;
	@Transient
	private List<LytxScoreListResponse> result;
	@Transient
	private Boolean status;
	@Transient 
	private List<List<LytxScoreListResponse>> data;
	@Transient
	private List<GeoDriveDateResponse> dateRange;
	
	
	public LytxScoreListResponse(List<LytxScoreListResponse> result) {
		super();
		this.result = result;
	}

	public LytxScoreListResponse(String range, List<LytxScoreListResponse> result) {
		super();
		this.range = range;
		this.result = result;
	}

	public LytxScoreListResponse(List<List<LytxScoreListResponse>> data, Boolean status,List<GeoDriveDateResponse> dateRange) {
		super();
		this.data = data;
		this.status = status;
		this.dateRange=dateRange;
	}
	

	
}

