package com.vibaps.merged.safetyreport.dto.truckdown;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TruckDownResponce {

	private String company_name;
	private String website;
	private String company_address;
	private Long lid;
	private String ids;
	private List<TruckDownResponce> company_details;
	

	
	
	public TruckDownResponce(String company_name, String website, String company_address, Long lid, String ids) {
		this.company_name = company_name;
		this.website = website;
		this.company_address = company_address;
		this.lid = lid;
		this.ids = ids;
	}
	public TruckDownResponce(List<TruckDownResponce> company_details) {
		this.company_details = company_details;
	}

	
	
	
	
	
	
	

}
