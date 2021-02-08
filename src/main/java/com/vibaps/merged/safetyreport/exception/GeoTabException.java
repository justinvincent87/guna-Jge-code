package com.vibaps.merged.safetyreport.exception;

import com.vibaps.merged.safetyreport.common.AppMsg;

/**
 * <p>
 * Global exception for GeoTabApi
 * </p>
 * 
 * @author Justin Vincent
 * @version 1.0
 * @since Jan 16, 2021
 */
public class GeoTabException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private AppMsg		appMsg;
	private String		message;
	private Throwable	exception;

	/**
	 * Constructor with app msg enum
	 * 
	 * @param appMsg
	 */
	public GeoTabException(AppMsg appMsg) {
		this(appMsg, null, null);
	}

	/**
	 * Constructor with custom message
	 * 
	 * @param message
	 */
	public GeoTabException(String message) {
		this(null, message, null);
	}

	/**
	 * Constructor with exception object
	 * 
	 * @param object
	 */
	public GeoTabException(Throwable object) {
		this(null, null, object);
	}

	/**
	 * Constructor with app msg enum and custom message
	 * 
	 * @param appMsg
	 * @param message
	 */
	public GeoTabException(AppMsg appMsg, String message) {
		this(appMsg, message, null);
	}

	/**
	 * Constructor with custom message and exception object
	 * 
	 * @param message
	 * @param object
	 */
	public GeoTabException(String message, Throwable object) {
		this(null, message, object);
	}

	/**
	 * Constructor with enum and exception object
	 * 
	 * @param appMsg
	 * @param object
	 */
	public GeoTabException(AppMsg appMsg, Throwable object) {
		this(appMsg, null, object);
	}

	/**
	 * Constructor with enum, custom message and exception object
	 * 
	 * @param appMsg
	 * @param message
	 * @param exception
	 */
	public GeoTabException(AppMsg appMsg, String message, Throwable exception) {
		this.appMsg		= appMsg;
		this.message	= message;
		this.exception	= exception;
	}

	/**
	 * @return enum object
	 */
	public AppMsg getAppMsg() {
		return appMsg;
	}

	/**
	 * @return custom message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return exception object
	 */
	public Throwable getException() {
		return exception;
	}
}
