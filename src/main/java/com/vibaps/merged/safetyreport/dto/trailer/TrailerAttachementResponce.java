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
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date activeFrom;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date activeTo;
	private String trailerId;
	private String deviceId;
	private String database;
	private String serverUrl;
	private String trailerName;
	
	private String deviceName;
	private String attachedLocation;
	
	private String detachedLocation;
	
	@Transient
	private String activeFromString;
	
	@Transient
	private String activeToString;
	

	
	
	/*
	 * @JsonSetter("activeFromString")
	 * 
	 * @JsonDeserialize(using = IdDeserializer.class) public void
	 * setActiveFromString(Date activeFrom) { this.activeFromString =
	 * activeFromString; }
	 * 
	 * @JsonSetter("activeToString")
	 * 
	 * @JsonDeserialize(using = IdDeserializer.class) public void
	 * setActiveToString(Date activeTo) { this.activeToString = activeToString; }
	 */
	
	
	
}
