package com.vibaps.merged.safetyreport.dto.gl;

import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReportParams {

	private ArrayList<String>	rules;
	private ArrayList<Integer>	weight;
	private String				companyId;
	private String				minmiles;
	private String				lytexSessionid;
	private String				startDate;
	private String				endDate;
	private String				groupId;
	private String				geotabSessionId;
	private ArrayList<String>	geotabGroups;
	private String				geotabUserName;
	private String				geotabDatabase;
	private String				url;
	private String				filename;
	private String				templect;
	private String				entityType;
	private String				endPoint;

}
