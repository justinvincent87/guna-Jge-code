package com.vibaps.merged.safetyreport.dto.geodriveapp;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GeoDriveAppResponse {
	
	private String name;
	private Long score;
	private Integer rank;
	private Integer persentage;
	private Character status;

	private List<GeoDriveAppResponse> result;

	public GeoDriveAppResponse(List<GeoDriveAppResponse> result) {
		super();
		this.result = result;
	}

	
}
