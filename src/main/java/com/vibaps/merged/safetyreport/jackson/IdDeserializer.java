package com.vibaps.merged.safetyreport.jackson;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * <p>
 *  Deserialzer for pull id attribute from child to parent
 * </p>
 * @author Justin Vincent
 * @version 1.0
 * @since Mar 17, 2021
 */
public class IdDeserializer extends JsonDeserializer<String> {

	private static final DateFormat PATTERN = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		
	DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");


	Date date =new Date();

	try {
	    date=inputFormat.parse(p.getValueAsString());

	} catch(Exception e) {
	} 

	return 	PATTERN.format(date);
     		}
}
