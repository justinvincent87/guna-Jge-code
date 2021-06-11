package com.vibaps.merged.safetyreport.dto.mobileapp;

import java.util.List;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeotabDeviceResponse 
{	
private String id;
private String name;

@Transient
private GeotabDeviceResponse[] result;

public GeotabDeviceResponse(GeotabDeviceResponse[] result) {
	super();
	this.result = result;
}




}
