package com.vibaps.merged.safetyreport.dto.gl;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReportFilter {

	private String companyId;
	private String minMiles;
	private String viewReportBy;
	private Map<String, Integer> exceptions;
}
