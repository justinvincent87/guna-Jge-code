package com.vibaps.merged.safetyreport.util;

import java.util.Objects;

import com.vibaps.merged.safetyreport.common.AppMsg;
import com.vibaps.merged.safetyreport.exception.GeoTabException;

public final class Assert {

	private Assert() throws IllegalAccessException {
		throw new IllegalAccessException("Illegal access to Assert class");
	}
	
	/**
	 * Throw {@code GeoTabException} if object is null
	 * 
	 * @param object
	 * @param appMsg
	 */
	public static void isNull(Object object, AppMsg appMsg) {
		if(Objects.isNull(object)) {
			throw new GeoTabException(appMsg);
		}
	}
	
	/**
	 * Throw {@code GeoTabException} if condition is false
	 * 
	 * @param condition
	 * @param appMsg
	 */
	public static void valid(Boolean condition, AppMsg appMsg) {
		if(!condition) {
			throw new GeoTabException(appMsg);
		}
	}
}
