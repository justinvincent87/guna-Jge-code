package com.vibaps.merged.safetyreport.dto.geodriveapp;

import java.util.List;

import javax.persistence.Transient;

import org.apache.poi.ss.formula.functions.T;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LytxGroupResponse{
	@JsonInclude(Include.NON_NULL)
	private List<T> groups;
}

