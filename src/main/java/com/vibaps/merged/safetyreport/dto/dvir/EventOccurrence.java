package com.vibaps.merged.safetyreport.dto.dvir;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vibaps.merged.safetyreport.jackson.IdDeserializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventOccurrence {

	private String eventDate;
	private String eventRule;
	private String deviceId;
	
	@Transient
	private String deviceName;
	
	
	@JsonSetter("eventRule")
	@JsonDeserialize(using = IdDeserializer.class)
	public void setEventRule(String eventRule) {
		this.eventRule = eventRule;
	}
	
	@JsonSetter("device")
	@JsonDeserialize(using = IdDeserializer.class)
	public void setDevice(String deviceId) {
		this.deviceId = deviceId;
	}
}
