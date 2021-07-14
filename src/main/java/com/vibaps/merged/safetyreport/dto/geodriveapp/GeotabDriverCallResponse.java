package com.vibaps.merged.safetyreport.dto.geodriveapp;

import java.util.List;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeotabDriverCallResponse {
	@JsonInclude(Include.NON_NULL)
	private Integer eventCount;
	@JsonInclude(Include.NON_NULL)
	private String employeeNo;
	@JsonInclude(Include.NON_NULL)
	private String behavierId;
	@JsonInclude(Include.NON_NULL)
	private String firstName;
	@JsonInclude(Include.NON_NULL)
	private String lastName;
	@JsonInclude(Include.NON_NULL)
	private String startDate;
	@JsonInclude(Include.NON_NULL)
	private String endDate;
	@JsonInclude(Include.NON_NULL)
	private String range;
	@JsonInclude(Include.NON_NULL)
	private Integer distance;
	
	@JsonInclude(Include.NON_NULL)
	private Float weightData;
	
	@Transient
	private List<GeotabBehavierResponse> behavelist;
	
	

}

