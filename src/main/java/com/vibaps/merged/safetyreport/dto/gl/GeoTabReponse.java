package com.vibaps.merged.safetyreport.dto.gl;

import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.formula.functions.T;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerResponce;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class GeoTabReponse 
{
	private Boolean isSuccess;
	private Boolean isError;
	@JsonInclude(Include.NON_NULL)
	private String errorCode;
	@JsonInclude(Include.NON_NULL)
	private String description;
	@JsonInclude(Include.NON_NULL)
	private String errorMsg;
	@JsonInclude(Include.NON_NULL)
	private Object data;
	@JsonInclude(Include.NON_NULL)
	private List<TrailerResponce> trailerResponce;
	
}
