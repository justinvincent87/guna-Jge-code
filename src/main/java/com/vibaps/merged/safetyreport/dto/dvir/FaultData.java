package com.vibaps.merged.safetyreport.dto.dvir;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vibaps.merged.safetyreport.jackson.IdDeserializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class FaultData {

	private String dateTime;
	private String deviceId;
	
	private String controller;
	private String diagnostic;
	private String failureMode;
	
	private String faultState;
	
	
	@JsonSetter("controller")
	@JsonDeserialize(using = IdDeserializer.class)
	public void setController(String controller) {
		this.controller = controller;
	}
	
	@JsonSetter("diagnostic")
	@JsonDeserialize(using = IdDeserializer.class)
	public void setDiagnostic(String diagnostic) {
		this.diagnostic = diagnostic;
	}
	
	
	@JsonSetter("failureMode")
	@JsonDeserialize(using = IdDeserializer.class)
	public void setFailureMode(String failureMode) {
		this.failureMode = failureMode;
	}
	
	@JsonSetter("device")
	@JsonDeserialize(using = IdDeserializer.class)
	public void setDevice(String deviceId) {
		this.deviceId = deviceId;
	}
}
