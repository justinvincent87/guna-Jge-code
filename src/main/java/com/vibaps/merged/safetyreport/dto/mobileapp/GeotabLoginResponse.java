package com.vibaps.merged.safetyreport.dto.mobileapp;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeotabLoginResponse {
	private String database;
    private String sessionId;
    private String userName;
    
    
}
