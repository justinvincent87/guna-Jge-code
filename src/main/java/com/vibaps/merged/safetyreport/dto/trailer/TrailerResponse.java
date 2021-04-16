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
public class TrailerResponse {
	
private String id;
private String name;
private String activeFrom;
private String activeTo;
private String trailerId;
private String trailerName;
private String deviceName;
private String deviceId;
private List<TrailerResponse> result;
@JsonInclude(Include.NON_NULL)
private List<TrailerResponse> parsetrailercall;
private List<TrailerResponse> parseaddressresponce;
private Double latitude;
@JsonInclude(Include.NON_NULL)
private Double longitude;
private String formattedAddress;
private String attachedLocation;
private String detachedLocation;
private String trailerattachedId;

private String defactId;
private String defactName;
private boolean defactStatus;

private TrailerAttachementResponce[] trailerResponce;




public TrailerResponse(String activeFrom,String activeTo,String trailerId,String deviceId,String attachedLocation,String detachedLocation)
{
	this.activeFrom=activeFrom;
	this.activeTo=activeTo;
	this.trailerId=trailerId;
	this.deviceId=deviceId;
	this.attachedLocation=attachedLocation;
	this.detachedLocation=detachedLocation;

}

/*
 * public TrailerResponse(String activeFrom,String activeTo,String
 * trailerId,String deviceId,String attachedLocation,String
 * detachedLocation,String trailerattachedId) { this.activeFrom=activeFrom;
 * this.activeTo=activeTo; this.trailerId=trailerId; this.deviceId=deviceId;
 * 
 * this.attachedLocation=attachedLocation;
 * this.detachedLocation=detachedLocation; this.trailerattachedId =
 * trailerattachedId;
 * 
 * }
 */

public TrailerResponse(String activeFrom,String activeTo,String trailerId,String deviceId)
{
	this.activeFrom=activeFrom;
	this.activeTo=activeTo;
	this.trailerId=trailerId;
	this.deviceId=deviceId;
}

public TrailerResponse(String activeFrom, String activeTo, String trailerId, String deviceId,
		String trailerattachedId) {
	this.activeFrom = activeFrom;
	this.activeTo = activeTo;
	this.trailerId = trailerId;
	this.deviceId = deviceId;
	this.trailerattachedId = trailerattachedId;
}



public TrailerResponse(List<TrailerResponse> result)
{
this.result=result;	
}

public TrailerResponse(String id,String name)
{
this.id=id;
this.name=name;
}

public TrailerResponse(Double latitude,Double longitude)
{
this.latitude=latitude;
this.longitude=longitude;
}

public TrailerResponse(String formattedAddress) {
	this.formattedAddress=formattedAddress;
	
}

public TrailerResponse(String id, String name, String deviceId) {
	this.id = id;
	this.name = name;
	this.deviceId = deviceId;
}

public TrailerResponse(String id, String name, String defactId, String defactName,boolean defactStatus) {
	super();
	this.id = id;
	this.name = name;
	this.defactId = defactId;
	this.defactName = defactName;
}

public TrailerResponse(TrailerAttachementResponce[] trailerResponce) {
	this.trailerResponce = trailerResponce;
}

public TrailerResponse(String id, String activeFrom, String activeTo, String trailerName, String deviceName,
		String attachedLocation, String detachedLocation) {
	super();
	this.id = id;
	this.activeFrom = activeFrom;
	this.activeTo = activeTo;
	this.trailerName = trailerName;
	this.deviceName = deviceName;
	this.attachedLocation = attachedLocation;
	this.detachedLocation = detachedLocation;
}













}
