package com.vibaps.merged.safetyreport.dto.trailer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;

import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomDateSerializer {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh.mm a");

  public CustomDateSerializer() {
      this(null);
  }

  public CustomDateSerializer(Class t) {
      super();
  }
  
  @SuppressWarnings("deprecation")
public void serialize (Date value, JsonGenerator gen, SerializerProvider arg2)
    throws IOException, JsonProcessingException {
	  if(value.getYear()>Calendar.getInstance().get(Calendar.YEAR))
	     {
	      gen.writeString("-");
	     }
      gen.writeString(value.toLocaleString());
  }
}

