package com.vibaps.merged.safetyreport.dto.dvir;

import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.poi.ss.formula.functions.T;
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
	List fullresult;
	private boolean status;
	
	@Transient
	private Map<String,List<?>> data;
	
	@Transient
	private String deviceName;
	
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

	public DvirDefactsResponse(List fullresult, boolean status) {
		super();
		this.fullresult = fullresult;
		this.status = status;
	}

	public DvirDefactsResponse(String deviceOrTrailerName, String dateTime, String logType, String driverName,
			List<GenDefects> remark, String assetType, String deviceName) {
		this.deviceOrTrailerName = deviceOrTrailerName;
		this.dateTime = dateTime;
		this.logType = logType;
		this.driverName = driverName;
		this.remark = remark;
		this.assetType = assetType;
		this.deviceName = deviceName;
	}

	public DvirDefactsResponse(Map<String, List<?>> data) {
		super();
		this.data = data;
	}

	
	
	
}
