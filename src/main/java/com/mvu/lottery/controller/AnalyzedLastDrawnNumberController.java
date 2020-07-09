package com.mvu.lottery.controller;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mvu.lottery.constant.LotteryConstants;
import com.mvu.lottery.service.AnalyzedDrawnNumberService;
import com.mvu.lottery.stateholder.AsynchDataStateHolder;
import com.mvu.lottery.stateholder.ResultResponse;

@RestController
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AnalyzedLastDrawnNumberController implements LotteryConstants {

	private AnalyzedDrawnNumberService service;
	private static Logger log = LoggerFactory.getLogger(AnalyzedLastDrawnNumberController.class);
	
	@Autowired
	public AnalyzedLastDrawnNumberController(AnalyzedDrawnNumberService service) {
		this.service = service;
	}
	
	/**
	 * Return the data string containing the anyalyzed past drawn numbers
	 * @param id
	 * @return
	 */
	@GetMapping("api/getAnalyzedDrawnNumbers/{id}")
	public Object getAnalyzedDrawnNumbersFor(
			@PathVariable("id") String id,
			@PathParam("baton") String baton, HttpServletRequest req) {
		
		try {
			log.info(">>>>baton:"+baton);
			AsynchDataStateHolder stateHolder = this.service.retrieveAnalyzedDataAsynch(getLotteryTypeFromName(id), baton);
			ResultResponse resultResp = ResultResponse.fromStateHolder(stateHolder);
			return resultResp;
			
		} catch (Exception e) {
			return ResultResponse.failed(e.toString());
		}
		
		
		
	}

}
