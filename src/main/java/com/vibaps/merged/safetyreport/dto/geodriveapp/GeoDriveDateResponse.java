package com.vibaps.merged.safetyreport.dto.geodriveapp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GeoDriveDateResponse {

	private String startDate;
	private String endDate;
	private String range;
}
