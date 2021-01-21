package com.vibaps.merged.safetyreport.config.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;

import com.vibaps.merged.safetyreport.dto.gl.AspectInfo;

public abstract class AspectBase {

	/**
	 * Extract Apect basic information from JoinPoint
	 * @param joinPoint
	 * @return Aspect information
	 */
	protected AspectInfo getAspectInfo(JoinPoint joinPoint) {
		
		Signature signature = joinPoint.getSignature();
		
		return AspectInfo.builder()
				.className(signature.getDeclaringType().getSimpleName())
				.methodName(signature.getName())
				.build();
	}
}
