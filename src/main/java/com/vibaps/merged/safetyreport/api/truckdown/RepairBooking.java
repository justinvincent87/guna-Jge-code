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
	

	
	@PostMapping(value="/viewtruckdownresponse")
	public ResponseEntity<String> viewtruckdownresponse(@RequestParam String url) throws IOException {
		
		return repairBookingService.viewtruckdownresponse(url);
	}
	
	@PostMapping(value="/getTruckTiming")
	public TruckDownResponce getTruckTiming(@RequestBody TrackDownEntity entity) throws IOException {
		
		return repairBookingService.getTruckTiming(entity);
	}
	
	@PostMapping(value="/getTruckDownDealerPhone")
	public TruckDownResponce getTruckPhone(@RequestBody TrackDownEntity entity) throws IOException {
		
		return repairBookingService.getTruckPhone(entity);
	}
	
	@PostMapping(value="/getTruckDownDealer")
	public TruckDownResponce getTruckdowndealer(@RequestBody TrackDownEntity entity) throws IOException {
		
		return repairBookingService.getTruckdowndealer(entity);
	}
	
	@PostMapping(value="/getTruckDownOauth")
	public int getTruckdownOath() throws IOException {
		
		return repairBookingService.getoath();
	}
	
	@PostMapping(value="/getTruckDownService")
	public TruckDownResponce getTruckdownservices() throws IOException {
		
		return repairBookingService.getTruckdownservices();
	}

}
