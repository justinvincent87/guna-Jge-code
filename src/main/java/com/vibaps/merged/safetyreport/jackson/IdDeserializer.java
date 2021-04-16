package com.vibaps.merged.safetyreport.jackson;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.vibaps.merged.safetyreport.services.trailer.TrailerService;

/**
 * <p>
 *  Deserialzer for pull id attribute from child to parent
 * </p>
 * @author Justin Vincent
 * @version 1.0
 * @since Mar 17, 2021
 */
public class IdDeserializer extends JsonDeserializer<String> {

	@Autowired
	private TrailerService trailerService;
	

	private static final DateFormat PATTERN = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		
		 DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
		    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy hh.mm a");

		    
		     ZonedDateTime parsed = ZonedDateTime.parse(p.getValueAsString(), formatter.withZone(ZoneId.of("Asia/Kolkata")));
		     if(parsed.getYear()>Calendar.getInstance().get(Calendar.YEAR))
		     {
		    	 return "-";
		     }
		     return  parsed.format(formatter1);
     		}
}
