package com.vibaps.merged.safetyreport.dto.trailer;

import java.util.Date;

import javax.persistence.Id;

import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vibaps.merged.safetyreport.jackson.IdDeserializer;

import lombok.Data;

@Data
public class TrailerAttachementResponce {
	@Id
	private String id;
	private String version;
	@Transient
	private String activeFrom;
	@Transient
	private String activeTo;
	private String trailerId;
	private String deviceId;
	private String database;
	private String serverUrl;
	private String trailerName;
	
	private String deviceName;
	private String attachedLocation;
	
	private String detachedLocation;
	

	
	
	@JsonSetter("activeFrom")
	@JsonDeserialize(using = IdDeserializer.class)
	public void setActiveFrom(String activeFrom) {
		this.activeFrom = activeFrom;
	}
	
	@JsonSetter("activeTo")
	@JsonDeserialize(using = IdDeserializer.class)
	public void setActiveTo(String activeTo) {
		this.activeTo = activeTo;
	}
	
	
	
}
