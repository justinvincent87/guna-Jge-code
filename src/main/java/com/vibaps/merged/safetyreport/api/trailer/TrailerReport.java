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

@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping({"/trailerReport"})
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

@PostMapping(value = "/show-device",produces = MediaType.APPLICATION_JSON_VALUE)
public DeviceResponse showDevice(@RequestBody TrailerParams trailerParams) throws JsonMappingException, JsonProcessingException
{
	
	return trailerService.showDevice(trailerParams);

}

@PostMapping(value = "/show-trailer",produces = MediaType.APPLICATION_JSON_VALUE)
public TrailerListResponse showTrailer(@RequestBody TrailerParams trailerParams) throws JsonMappingException, JsonProcessingException
{
	
	return trailerService.showTrailer(trailerParams);

}





@PostMapping(value = "/getDevice",produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<GeoTabReponse> getDevice(@RequestBody TrailerParams trailerParams)
{
		if(trailerParams.getGeotabDatabase().isEmpty() ||
			   trailerParams.getGeotabSessionId().isEmpty() ||
			   trailerParams.getGeotabUserName().isEmpty())
			{
				
				 GeoTabException ex=new GeoTabException(AppMsg.CV002);
				 appMsg = ex.getAppMsg();
				
				 responseBody = GeoTabReponse.builder()
						.isError(true)
						.errorMsg(appMsg.getHttpStatus().getReasonPhrase())
						.errorCode(appMsg.getCode())
						.description(appMsg.getMessage())
						.build();
			}
			else
			{
					GeoTabException ex=new GeoTabException(AppMsg.SUCCESS);
					 appMsg = ex.getAppMsg();
					 responseBody = GeoTabReponse.builder()
					.isSuccess(true)
					.isError(false)
					.trailerResponce( trailerService.getDevice(trailerParams).getResult())
					.build();
			}
		
		return new ResponseEntity<GeoTabReponse>(responseBody, appMsg.getHttpStatus());
}

@PostMapping(value = "/getTrailer",produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<GeoTabReponse> getTrailer(@RequestBody TrailerParams trailerParams)
{
			if(trailerParams.getGeotabDatabase().isEmpty() ||
					   trailerParams.getGeotabSessionId().isEmpty() ||
					   trailerParams.getGeotabUserName().isEmpty())
					{
						
						 GeoTabException ex=new GeoTabException(AppMsg.CV002);
						 appMsg = ex.getAppMsg();
						
						 responseBody = GeoTabReponse.builder()
								.isError(true)
								.errorMsg(appMsg.getHttpStatus().getReasonPhrase())
								.errorCode(appMsg.getCode())
								.description(appMsg.getMessage())
								.build();
					}
					else
					{
							GeoTabException ex=new GeoTabException(AppMsg.SUCCESS);
							 appMsg = ex.getAppMsg();
							 responseBody = GeoTabReponse.builder()
							.isSuccess(true)
							.isError(false)
							.trailerResponce(trailerService.getTrailer(trailerParams).getResult())
							.build();
					}
	return new ResponseEntity<GeoTabReponse>(responseBody, appMsg.getHttpStatus());


}

@PostMapping(value = "/show-report-page",produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<GeoTabReponse> showReportPageWise(@RequestBody TrailerParams trailerParams)
{

			GeoTabException ex=new GeoTabException(AppMsg.SUCCESS);
			 appMsg = ex.getAppMsg();
			 responseBody = GeoTabReponse.builder()
			.isSuccess(true)
			.isError(false)
			.trailerResponce(trailerService.convertParsedReponseShow(trailerParams).getResult())
			.build();
	
	return new ResponseEntity<GeoTabReponse>(responseBody, appMsg.getHttpStatus());

}


@PostMapping(value = "/get-trailer-attachement",produces = MediaType.APPLICATION_JSON_VALUE)
public  List<TrailerResponse> getTrailerAttachementData(@RequestBody TrailerParams trailerParams)
{
return  trailerService.getTrailerAttachementData(trailerParams).getResult();
}

	
}
