package com.vibaps.merged.safetyreport.dto.trailer;

import java.util.Date;

import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	
}
