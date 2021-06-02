package com.vibaps.merged.safetyreport.dto.dvir;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
@NoArgsConstructor
public class FaultData {

	private String dateTime;
	private String deviceId;
	
	private String controller;
	private String diagnostic;
	private String failureMode;
	
	private String faultState;
	
	@Transient
	private String deviceName;
	
	@Transient
	private Map<String,List<FaultData>> result;
	
	@Transient
	private String startDate;
	
	@Transient
	private String endDate;
	
	@Transient
	private Integer count;
	
	
	
	
	
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

	public FaultData(Map<String, List<FaultData>> result) {
		super();
		this.result = result;
	}

	public FaultData(String deviceId, String diagnostic, String deviceName, String startDate, String endDate,
			Integer count,String controller) {
		this.deviceId = deviceId;
		this.diagnostic = diagnostic;
		this.deviceName = deviceName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.count = count;
		this.controller=controller;
	}

	
	
	
}
