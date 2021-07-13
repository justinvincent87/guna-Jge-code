package com.vibaps.merged.safetyreport.dto.mobileapp;

import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vibaps.merged.safetyreport.jackson.DriverIdDeserializer;
import com.vibaps.merged.safetyreport.jackson.IdDeserializer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeotabDeviceStatusInfoResponse {
private Double latitude;
private Double longitude;
private String deviceId;
private String driver;
private Integer speed;
private String dateTime;

@Transient
private Integer enginHours;

@Transient
private Double odaMeter;

@Transient
private String deviceAddress;
@Transient
private String driverName;


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

@JsonSetter("driver")
@JsonDeserialize(using = DriverIdDeserializer.class)
public void setDriver(String driver) {
	this.driver = driver;
}

}
