package com.vibaps.merged.safetyreport.dto.trailer;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrailerListResponse {
	private String id;
	private String name;
	private List groups;
	private TrailerListResponse[] trailerResponce;
	private String database;
	private String serverUrl;
	
	
	public TrailerListResponse(TrailerListResponse[] trailerResponce) {
		this.trailerResponce = trailerResponce;
	}

}
