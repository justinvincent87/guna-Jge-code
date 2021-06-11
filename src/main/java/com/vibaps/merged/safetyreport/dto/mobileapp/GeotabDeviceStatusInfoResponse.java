package com.vibaps.merged.safetyreport.dto.mobileapp;

import java.util.List;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vibaps.merged.safetyreport.jackson.IdDeserializer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeotabDeviceStatusInfoResponse {
private Double latitude;
private Double longitude;
private String driver;
private String deviceId;

@Transient
private List<GeotabDeviceStatusInfoResponse> result;

public GeotabDeviceStatusInfoResponse(List<GeotabDeviceStatusInfoResponse> result) {
	super();
	this.result = result;
}

@JsonSetter("device")
@JsonDeserialize(using = IdDeserializer.class)
public void setDevice(String deviceId) {
	this.deviceId = deviceId;
}

}
