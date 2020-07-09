package com.mvu.lottery.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ExceptiontracingAOP {
	
	private static Logger log = LoggerFactory.getLogger(ExceptiontracingAOP.class);
	
	@Pointcut("execution(* com.mvu.lottery.util.TicketUtils.*(..))")
	public void executionOfTicketUtils() {}
	
	
	@Pointcut("execution(* com.mvu.lottery.controller.*.*(..) )")
	public void analyzedLastDrawnNumberControllerTracer() {}
	
	
	@AfterThrowing(pointcut="executionOfTicketUtils() || analyzedLastDrawnNumberControllerTracer()", throwing="exception")
	public void afterThrowingException(JoinPoint jp, Exception exception) {
		log.info(String.format("Class: %s method: %s throws: %s", jp.getThis(), jp.getSignature().getName(), exception));
		
	}
}
