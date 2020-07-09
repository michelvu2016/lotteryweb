package com.mvu.lottery.aop;

import java.nio.file.Path;
import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import com.mvu.lottery.constant.LotteryConstants.LotteryType;
import com.mvu.lottery.stateholder.LastDrawnTicketStateHolder;


@Component
@Aspect
public class TracingAOP {
	private static Logger log = LoggerFactory.getLogger(TracingAOP.class);
	
	@Pointcut("execution(* com.mvu.lottery.service.*.*(..))")
	public void trackingService() {}
	
	@Pointcut("execution(* com.mvu.lottery.data.*.*(..))")
	public void trackingData() {}
	
	@Pointcut("execution(* com.mvu.lottery.constant.LotteryConfiguration.*(..))")
	public void trackingConfig() {}
	
	@Pointcut("execution(* com.mvu.lottery.customserializer.SerializeFieldConditionally.*(..))")
	public void trackingSerializeFieldConditionally() {}
	
	@Pointcut("execution(* com.mvu.lottery.security.*.*(..))")
	public void websec() {}
	
	@Pointcut("within(com.mvu.lottery.data.LastDrawnTicketData.*)")
	public void thisTicketData() {
		
	}
	
	@Pointcut("execution(* com.mvu.lottery.controller.*.*(..) )")
	public void analyzedLastDrawnNumberControllerTracer() {}
	
	@Pointcut("this(com.mvu.lottery.data.LastDrawnTicketData)")
	public void withinInsert() {
		
	}
	
	@Pointcut("execution(* com.mvu.lottery.util.*.*(..))")
	public void processUtilTracking() {}
	
	
	@Pointcut("execution(* com.mvu.lottery.configuration.MySqlDBConfig.*.*(..))")
	public void mySqlDBConfig() {
		
	}
	
	
	@Before(value="trackingConfig() && args(com.mvu.lottery.constant.LotteryConstants.LotteryType)")
	public void beforeTrackingTheConfigWithArg(JoinPoint jp) {
		log.info(String.format("Class: %s method: %s", jp.getThis(), jp.getSignature().getName()));
		
		Arrays.stream(jp.getArgs()).forEach(item -> log.info(item.toString()));
		
		
	}
	
	@Around("trackingService() || trackingData() || trackingConfig() "
			+ "|| withinInsert() || thisTicketData() || trackingSerializeFieldConditionally() || processUtilTracking()"
			+ "|| analyzedLastDrawnNumberControllerTracer() || mySqlDBConfig()")
	public Object traceDataRetrieveal(ProceedingJoinPoint pjp) throws Throwable {
		
		Object retObj = null;
		
		try {
			
			retObj = pjp.proceed();
			
		} catch (Throwable e) {
			log.info(String.format("Class: %s method: %s throws exception: %s", pjp.getThis(), pjp.getSignature().getName(), e.toString()));
			throw e;
		}
		
		log.info(String.format("Class: %s method: %s result: %s", pjp.getThis(), pjp.getSignature().getName(), retObj));
		
		return retObj;
	}
	
	@AfterReturning(pointcut="execution(* com.mvu.lottery.data.LastDrawnTicketData.readXLinesFromFilePath(..))", returning="retObj")
	public void tracingDataAfterReturning(JoinPoint jp, Object retObj) {
		log.info(String.format("Class: %s method: %s result: %s", jp.getThis(), jp.getSignature().getName(), retObj));
	}
	
	@After("websec()")
	public void afterSec(JoinPoint jp) {
		log.info(">>>>After configuring the web security");
	}
	
	
	@AfterThrowing(value="trackingConfig()", throwing="exc")
	public void afterThrowingConfig(JoinPoint jp, Throwable exc) {
		
		
	}

	
	
}
