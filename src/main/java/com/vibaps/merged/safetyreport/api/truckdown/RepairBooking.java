package com.vibaps.merged.safetyreport.api.truckdown;

import java.io.IOException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibaps.merged.safetyreport.entity.truckdown.TrackDownEntity;
import com.vibaps.merged.safetyreport.services.truckdown.RepairBookingService;




@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping({ "/truckdown"})
public class RepairBooking {
	
	@Autowired
	private RepairBookingService repairBookingService;
	
	@PostMapping(value="/viewrepaircenter")
	public Object view(@RequestBody TrackDownEntity entity) throws IOException {
		
		return repairBookingService.view(entity);
	}

}
