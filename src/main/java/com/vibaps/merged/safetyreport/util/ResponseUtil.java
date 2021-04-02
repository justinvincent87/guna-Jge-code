package com.vibaps.merged.safetyreport.util;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.vibaps.merged.safetyreport.common.AppMsg;
import com.vibaps.merged.safetyreport.dto.truckdown.TruckDownResponce;

/**
 * <p>
 * This class used to parse the response which received from GeoTabApi
 * </p>
 * 
 * @author Justin Vincent
 * @version 1.0
 * @since Jan 16, 2021
 */
public final class ResponseUtil {

	private ResponseUtil() throws IllegalAccessException {
		throw new IllegalAccessException("Illegal access to ResponseUtil class");
	}

	/**
	 * Parse response string as json object if startus code is 200
	 * 
	 * @param response
	 * @return
	 */
	public static JsonObject parseResponse(ResponseEntity<String> response) {

		//Validate response
		Assert.isNull(response, AppMsg.ER001);
		Assert.valid(StringUtils.isNotBlank(response.getBody()), AppMsg.ER001);
		Assert.valid(HttpStatus.OK.equals(response.getStatusCode()), AppMsg.ER002);
		
		//Parse response object
		JsonElement parsedResponse = JsonParser.parseString(response.getBody());
		Assert.isNull(parsedResponse, AppMsg.ER003);
		return parsedResponse.getAsJsonObject();
	}
	
	public static String parseResponseArray(ResponseEntity<String> response) {

		//Validate response
		Assert.isNull(response, AppMsg.ER001);
		Assert.valid(StringUtils.isNotBlank(response.getBody()), AppMsg.ER001);
		Assert.valid(HttpStatus.OK.equals(response.getStatusCode()), AppMsg.ER002);
		
		JSONArray jsonArray = new JSONArray(response.getBody());

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", jsonArray);
		
		return jsonObj.toString();
		}
	public static String parseResponseObject(ResponseEntity<String> response) 
	{

		//Validate response
		Assert.isNull(response, AppMsg.ER001);
		Assert.valid(StringUtils.isNotBlank(response.getBody()), AppMsg.ER001);
		Assert.valid(HttpStatus.OK.equals(response.getStatusCode()), AppMsg.ER002);
		
		return response.getBody();
	}
	
		
}
