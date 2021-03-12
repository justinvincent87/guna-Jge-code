package com.vibaps.merged.safetyreport.api.truckdown;

import java.io.IOException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.vibaps.merged.safetyreport.dto.truckdown.TruckDownResponce;
import com.vibaps.merged.safetyreport.entity.truckdown.TdUser;
import com.vibaps.merged.safetyreport.entity.truckdown.TrackDownEntity;
import com.vibaps.merged.safetyreport.services.truckdown.RepairBookingService;




@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping({ "/truckdown"})
public class RepairBooking {
	
	@Autowired
	private RepairBookingService repairBookingService;
	

	
	@PostMapping(value="/viewtruckdownresponse",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> viewtruckdownresponse(@RequestParam String url) throws IOException {
		
		return repairBookingService.viewtruckdownresponse(url);
	}
	
	@PostMapping(value="/getTruckTiming",produces = MediaType.APPLICATION_JSON_VALUE)
	public String getTruckTiming(@RequestBody TrackDownEntity entity) throws IOException {
		
		return repairBookingService.getTruckTiming(entity);
	}
	
	@PostMapping(value="/getTruckDownDealerPhone",produces = MediaType.APPLICATION_JSON_VALUE)
	public String getTruckPhone(@RequestBody TrackDownEntity entity) throws IOException {
		
		return repairBookingService.getTruckPhone(entity);
	}
	
	@PostMapping(value="/getTruckDownDealer",produces = MediaType.APPLICATION_JSON_VALUE)
	public TruckDownResponce getTruckdowndealer(@RequestBody TrackDownEntity entity) throws IOException {
		
		return repairBookingService.getTruckdowndealer(entity);
	}
	
	@PostMapping(value="/getTruckDownOauth",produces = MediaType.APPLICATION_JSON_VALUE)
	public int getTruckdownOath() throws IOException {
		
		return repairBookingService.getoath();
	}
	
	@PostMapping(value="/getTruckDownService",produces = MediaType.APPLICATION_JSON_VALUE)
	public String getTruckdownservices() throws IOException {
		
		return repairBookingService.getTruckdownservices();
	}

}
