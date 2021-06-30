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
	
	private String employeeNum;
	private Float totalScore;
	private Float totalScoreTrend;
	private String firstName;
	private String lastName;
	
	@Transient
	private List<LytxScoreListResponse> result;

	public LytxScoreListResponse(List<LytxScoreListResponse> result) {
		super();
		this.result = result;
	}
	
	
}

