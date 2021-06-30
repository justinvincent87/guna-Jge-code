package com.vibaps.merged.safetyreport.dto.geodriveapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LytxTokenResponse {
	
	private String accessToken;
	private String action;

}
