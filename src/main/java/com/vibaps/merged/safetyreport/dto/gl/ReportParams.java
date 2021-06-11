package com.vibaps.merged.safetyreport.dto.gl;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReportParams{

	private List<String>	rules;
	private List<Integer>	weight;
	private String			companyId;
	private String			minmiles;
	private String			lytexSessionid;
	private String			startDate;
	private String			endDate;
	private String			groupId;
	private String			geotabSessionId;
	private String			geotabGroups;
	private String			geotabUserName;
	private String			geotabDatabase;
	private String			geotabPassword;
	private String			url;
	private String			filename;
	private String			templect;
	private String			entityType;
	private String			endPoint;
	private String			period;
	private List<Integer>	ruleId;
	private String 			filePath;
	private MultipartFile  	file;
	private String database;
	private String groups;
	private String fromDate;
	private String lytxuserName;
	private String lytxpassword;
	
}
