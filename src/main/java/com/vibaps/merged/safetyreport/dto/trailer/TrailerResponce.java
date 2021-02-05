package com.vibaps.merged.safetyreport.dto.trailer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
private String trailerId;
private String deviceId;
private List<TrailerResponce> result;
@JsonInclude(Include.NON_NULL)
private Double latitude;
@JsonInclude(Include.NON_NULL)
private Double longitude;
private String formattedAddress;
private String attachedLocation;
private String detachedLocation;

public TrailerResponce(String activeFrom,String activeTo,String trailerId,String deviceId,String attachedLocation,String detachedLocation)
{
	this.activeFrom=activeFrom;
	this.activeTo=activeTo;
	this.trailerId=trailerId;
	this.deviceId=deviceId;
	
	this.attachedLocation=attachedLocation;
	this.detachedLocation=detachedLocation;

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

public TrailerResponce(Double latitude,Double longitude)
{
this.latitude=latitude;
this.longitude=longitude;
}

public TrailerResponce(String formattedAddress) {
	this.formattedAddress=formattedAddress;
	
}







}
