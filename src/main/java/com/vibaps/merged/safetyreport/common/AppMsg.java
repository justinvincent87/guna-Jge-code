package com.vibaps.merged.safetyreport.common;

import org.springframework.http.HttpStatus;

public enum AppMsg {

	//Common validation messages
	CV001("CV001", "Invalid arguments", HttpStatus.BAD_REQUEST),
	
	ER001("ER001", "Empty response from geo tab service", HttpStatus.NO_CONTENT),
	ER002("ER002", "Problem with external service", HttpStatus.INTERNAL_SERVER_ERROR),
	ER003("ER003", "Error while parse response", HttpStatus.INTERNAL_SERVER_ERROR),
	SUCCESS("SUCCESS","Success Responce",HttpStatus.OK),
	CV002("CV002", "Invalid parameters", HttpStatus.OK),
	;

	private String		code;
	private String		message;
	private HttpStatus	httpStatus;
	
	

	private AppMsg(String code, String message) {
		this.code		= code;
		this.message	= message;
	}

	private AppMsg(String code, String message, HttpStatus httpStatus) {
		this.code		= code;
		this.message	= message;
		this.httpStatus	= httpStatus;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
