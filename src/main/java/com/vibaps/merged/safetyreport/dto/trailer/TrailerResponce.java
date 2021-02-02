package com.vibaps.merged.safetyreport.dto.trailer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrailerResponce {
	
private String id;
private String name;
private String activeFrom;
private String activeTo;
private String trailer_id;
private String device_id;
private List<TrailerResponce> result;

public TrailerResponce(String activeFrom,String activeTo,String trailer_id,String device_id)
{
	this.activeFrom=activeFrom;
	this.activeTo=activeTo;
	this.trailer_id=trailer_id;
	this.device_id=device_id;

}


public TrailerResponce(List<TrailerResponce> result)
{
this.result=result;	
}

public TrailerResponce(String id,String name)
{
this.id=id;
this.name=name;
}







}
