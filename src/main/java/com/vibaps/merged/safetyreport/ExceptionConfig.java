package com.vibaps.merged.safetyreport;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vibaps.merged.safetyreport.common.AppMsg;
import com.vibaps.merged.safetyreport.dto.gl.GeoTabReponse;
import com.vibaps.merged.safetyreport.exception.GeoTabException;

@ControllerAdvice
public class ExceptionConfig extends ResponseEntityExceptionHandler {

	/**
	 * Handle any geo tab application exceptions
	 * 
	 * @param ex
	 * @return
	 */
@ExceptionHandler(GeoTabException.class)
	public ResponseEntity<GeoTabReponse> handleGeoTabException(GeoTabException ex) {
		
		AppMsg appMsg = ex.getAppMsg();
		GeoTabReponse responseBody = GeoTabReponse.builder()
				.isError(true)
				.isSuccess(false)
				.errorMsg(appMsg.getHttpStatus().getReasonPhrase())
				.errorCode(appMsg.getCode())
				.description(appMsg.getMessage())
				.build();
		return new ResponseEntity<GeoTabReponse>(responseBody, appMsg.getHttpStatus());
	}
}
