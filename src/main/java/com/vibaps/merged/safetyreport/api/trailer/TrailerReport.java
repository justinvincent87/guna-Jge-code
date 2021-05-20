package com.vibaps.merged.safetyreport.api.trailer;
import java.io.IOException;
import java.util.List;

/**
 * @author Gunalan A
 * @version 1.0
 * @since Jan 26, 2021
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vibaps.merged.safetyreport.ExceptionConfig;
import com.vibaps.merged.safetyreport.ExeptionHandler;
import com.vibaps.merged.safetyreport.common.AppMsg;
import com.vibaps.merged.safetyreport.dto.gl.GeoTabReponse;
import com.vibaps.merged.safetyreport.dto.trailer.DeviceResponse;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerAttachementResponce;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerListResponse;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerResponse;
import com.vibaps.merged.safetyreport.exception.GeoTabException;
import com.vibaps.merged.safetyreport.services.trailer.TrailerService;

import lombok.extern.log4j.Log4j2;

@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping({"/trailerReport"})
@Log4j2
public class TrailerReport {
	
	@Autowired
	private TrailerService trailerService;
	
	private GeoTabReponse responseBody;
	private AppMsg appMsg;
	

	


@PostMapping(value = "/show-report",produces = MediaType.APPLICATION_JSON_VALUE)
public TrailerResponse showReport(@RequestBody TrailerParams trailerParams) throws JsonMappingException, JsonProcessingException
{
	return trailerService.showReport(trailerParams);

}

@PostMapping(value = "/show-report-count",produces = MediaType.APPLICATION_JSON_VALUE)
public String showReportcount(@RequestBody TrailerParams trailerParams) throws JsonMappingException, JsonProcessingException
{
	return trailerService.showReportCount(trailerParams);

}

@PostMapping(value = "/show-device",produces = MediaType.APPLICATION_JSON_VALUE)
public String showDevice(@RequestBody TrailerParams trailerParams) throws JsonMappingException, JsonProcessingException
{
	return trailerService.showDevice(trailerParams);

}

@PostMapping(value = "/show-trailer",produces = MediaType.APPLICATION_JSON_VALUE)
public String showTrailer(@RequestBody TrailerParams trailerParams) throws JsonMappingException, JsonProcessingException
{
	return trailerService.showTrailer(trailerParams);

}





@PostMapping(value = "/get-Time-Zone",produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<GeoTabReponse> getUserTimeZone(@RequestBody TrailerParams trailerParams) throws JsonMappingException, JsonProcessingException
{
	

	 responseBody = GeoTabReponse.builder()
	.isSuccess(true)
	.isError(false)
	.data(trailerService.getUserTimeZone(trailerParams))
	.build();
	
		return new ResponseEntity<GeoTabReponse>(responseBody,HttpStatus.OK);

}

@PostMapping(value = "/getDevice",produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<GeoTabReponse> getDevice(@RequestBody TrailerParams trailerParams)
{
		
				
					 responseBody = GeoTabReponse.builder()
					.isSuccess(true)
					.isError(false)
					.trailerResponce( trailerService.getDevice(trailerParams).getResult())
					.build();
			
		
		return new ResponseEntity<GeoTabReponse>(responseBody, HttpStatus.OK);
}

@PostMapping(value = "/getTrailer",produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<GeoTabReponse> getTrailer(@RequestBody TrailerParams trailerParams)
{
			
							
							 responseBody = GeoTabReponse.builder()
							.isSuccess(true)
							.isError(false)
							.trailerResponce(trailerService.getTrailer(trailerParams).getResult())
							.build();
					
	return new ResponseEntity<GeoTabReponse>(responseBody, HttpStatus.OK);


}



@PostMapping(value = "/show-report-page",produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<GeoTabReponse> showReportPageWise(@RequestBody TrailerParams trailerParams)
{

			
			 responseBody = GeoTabReponse.builder()
			.isSuccess(true)
			.isError(false)
			.trailerResponce(trailerService.convertParsedReponseShow(trailerParams).getResult())
			.build();
	
	return new ResponseEntity<GeoTabReponse>(responseBody,HttpStatus.OK);

}


@PostMapping(value = "/get-trailer-attachement",produces = MediaType.APPLICATION_JSON_VALUE)
public  List<TrailerResponse> getTrailerAttachementData(@RequestBody TrailerParams trailerParams)
{
return  trailerService.getTrailerAttachementData(trailerParams).getResult();
}

	
}
