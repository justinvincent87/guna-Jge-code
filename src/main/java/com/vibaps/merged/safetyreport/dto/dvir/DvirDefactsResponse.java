package com.vibaps.merged.safetyreport.dto.dvir;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.vibaps.merged.safetyreport.entity.gl.GenDefects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DvirDefactsResponse {

	private String deviceOrTrailerName;
	private String dateTime;
	private String logType;
	private String driverName;
	private List<GenDefects> remark;
	private String assetType;
	List<DvirDefactsResponse> result;
	
	public DvirDefactsResponse(String deviceOrTrailerName, String dateTime, String logType, String driverName,List<GenDefects> remark,String assetType) {
		this.deviceOrTrailerName = deviceOrTrailerName;
		this.dateTime = dateTime;
		this.logType = logType;
		this.driverName = driverName;
		this.remark=remark;
		this.assetType=assetType;
	}

	public DvirDefactsResponse(List<DvirDefactsResponse> result) {
		super();
		this.result = result;
	}
	
	
}
