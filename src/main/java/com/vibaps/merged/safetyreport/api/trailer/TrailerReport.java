package com.vibaps.merged.safetyreport.api.trailer;
/**
 * @author Gunalan A
 * @version 1.0
 * @since Jan 26, 2021
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibaps.merged.safetyreport.ExeptionHandler;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerResponce;
import com.vibaps.merged.safetyreport.services.trailer.TrailerService;

@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping({"/trailerReport"})
public class TrailerReport {
	
	@Autowired
	private TrailerService trailerService;

@PostMapping(value = "/show-report",produces = MediaType.APPLICATION_JSON_VALUE)
public TrailerResponce showReport(@RequestBody TrailerParams trailerParams) 
{
	return trailerService.showReport(trailerParams);

}

@PostMapping(value = "/getDevice",produces = MediaType.APPLICATION_JSON_VALUE)
public TrailerResponce getDevice(@RequestBody TrailerParams trailerParams)
{
	
	return trailerService.getDevice(trailerParams);

}

@PostMapping(value = "/getTrailer",produces = MediaType.APPLICATION_JSON_VALUE)
public TrailerResponce getTrailer(@RequestBody TrailerParams trailerParams)
{
	return trailerService.getTrailer(trailerParams);

}


	
}
