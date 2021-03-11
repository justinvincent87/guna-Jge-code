package com.vibaps.merged.safetyreport.dto.truckdown;

import java.util.List;

import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerResponce;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TruckDownResponce {

	private String company_name;
	private String website;
	private JSONObject company_address;
	private Long lid;
	private String ids;
	private List<TruckDownResponce> company_details;
	private List<JsonObject> result;
	
	private String status;
	
	public TruckDownResponce(String company_name, String website, JSONObject company_address, Long lid, String ids) {
		this.company_name = company_name;
		this.website = website;
		this.company_address = company_address;
		this.lid = lid;
		this.ids = ids;
	}
	public TruckDownResponce(List<TruckDownResponce> company_details) {
		this.company_details = company_details;
	}
	public TruckDownResponce(List<JsonObject> result, String status) {
		this.result = result;
		this.status = status;
	}
	
	
	
	
	

}
