package com.vibaps.merged.safetyreport.dto.mobileapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeotabUserResponce {

	private String id;
	private String firstName;
	private String lastName;
	
}
