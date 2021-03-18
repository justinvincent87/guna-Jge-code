package com.vibaps.merged.safetyreport.dto.trailer;

import java.util.List;

import com.vibaps.merged.safetyreport.dto.gl.ReportParams;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TrailerParams extends ReportParams {

	private String			activeFrom;
	private String			activeTo;

	private String 			deviceId;
	private String 			trailerId;
	
	private String 			typeName;
	private String 			fromDate;
	private String 			toDate;
	
	private List<TrailerResponce> trailerParsedInput; 

}
