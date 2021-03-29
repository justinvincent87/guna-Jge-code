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
public class DeviceResponse {
	private String id;
	private String name;
	
	private Date activeFrom;
	private Date activeTo;
	
	private List groups;
	private DeviceResponse[] deviceResponce;
	private String database;
	private String serverUrl;
	
	
	public DeviceResponse(DeviceResponse[] deviceResponce) {
		this.deviceResponce = deviceResponce;
	}

}
