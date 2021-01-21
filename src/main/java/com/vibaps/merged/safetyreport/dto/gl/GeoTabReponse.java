package com.vibaps.merged.safetyreport.dto.gl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class GeoTabReponse {
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
}
