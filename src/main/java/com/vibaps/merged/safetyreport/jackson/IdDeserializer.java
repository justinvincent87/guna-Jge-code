package com.vibaps.merged.safetyreport.jackson;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;


public class IdDeserializer extends JsonDeserializer<String> {

	private static final String ATTR_ID = "id";

	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		
		ObjectCodec codec = p.getCodec();
		JsonNode node = codec.readTree(p);
		
		
		if(node.asText().equals("NoFailureModeId") || node.isNull())
		{
		return "-";
		}
		
		return node.get(ATTR_ID).asText();
		
	}
}