package com.vibaps.merged.safetyreport.dto.gl;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AspectInfo {
	private String className;
	private String methodName;
}
