package com.vibaps.merged.safetyreport.config.aop;

import org.apache.commons.lang.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.vibaps.merged.safetyreport.dto.gl.AspectInfo;

import lombok.extern.log4j.Log4j2;

@Aspect
@Log4j2
@Component
public class PerformanceAspect extends AspectBase{
	
	@Pointcut("execution(public * com.vibaps.merged.safetyreport.api..*(..)) || "
			+ "execution(public * com.vibaps.merged.safetyreport.services..*(..)) || "
			+ "execution(public * com.vibaps.merged.safetyreport.dao..*(..))")
	public void appClassMethods() {}
	
	@Around("appClassMethods()")
	public Object performanceLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		final AspectInfo info = getAspectInfo(proceedingJoinPoint);
		final StopWatch stopWatch = new StopWatch();
		
		stopWatch.start();
		Object response = proceedingJoinPoint.proceed();
		stopWatch.stop();
		
		log.info("Execution time of {}.{} :: {} ms", info.getClassName(), info.getMethodName(), stopWatch.getTime());
		return response;
	}
}
