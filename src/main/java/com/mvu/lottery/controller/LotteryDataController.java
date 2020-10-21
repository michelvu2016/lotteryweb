package com.mvu.lottery.controller;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mvu.lottery.constant.LotteryConfiguration;
import com.mvu.lottery.constant.LotteryConstants;
import com.mvu.lottery.customserializer.SerializeFieldConditionally;
import com.mvu.lottery.data.model.SelectedTicketData;
import com.mvu.lottery.service.LastDrawnTicketService;
import com.mvu.lottery.stateholder.LastDrawnNumberOnfileInfo;
import com.mvu.lottery.stateholder.LastDrawnTicketStateHolder;
import com.mvu.lottery.stateholder.ResultResponse;
import com.mvu.lottery.util.SessionDataBySessionIdAndDataKey;
import com.mvu.lottery.util.StringUtils;

@RestController
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LotteryDataController implements LotteryConstants {
	
	private static Logger log = LoggerFactory.getLogger(LotteryDataController.class);
	
	@Inject
	private LotteryConfiguration lotteryConfig;
	@Inject
	private LastDrawnTicketService lastDrawnTicketService;
	
	@Autowired
	private SerializeFieldConditionally serializer;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private SessionDataBySessionIdAndDataKey<LastDrawnNumberOnfileInfo, Future<LastDrawnNumberOnfileInfo>> asynchStorage;
	
	/**
	 * 
	 * @param selectedTicketData
	 * @return
	 */
	@PostMapping("/api/selectedTicket/save")
	public Object postSelectedTicket(@RequestBody SelectedTicketData selectedTicketData) {
		
		System.out.println(">>>postSelectedTicket:"+selectedTicketData);
		return "";
	}
	
	/**
	 * 
	 * @param resp
	 * @param numLines
	 * @param id
	 * @return
	 */
	@GetMapping("api/lastDrawnNumbers/{id}")
	@CrossOrigin
	public Object getLastDrawnNumbersAsynch(HttpServletResponse resp,
			HttpServletRequest req,
			@PathParam("numLines") int numLines,
			@PathVariable("id") String id,
			@PathParam("baton") String baton) {
		
		
		Object data = null;
		ResultResponse resultRes = null;
		try
		{
			if(null != baton && baton.length() > 8) {
				if(this.asynchStorage.isDataAvailable(baton)) {
					data = this.asynchStorage.retrieveData(baton);
					
					List<LastDrawnTicketStateHolder> listOfTickets = ((LastDrawnNumberOnfileInfo) data).getListOfTicketHolders();
					int numOfLastDrawnNeeded = ((LastDrawnNumberOnfileInfo) data).getNumOfDrawnsNeeded();
					
					resultRes = ResultResponse.success();
					
					resultRes.setData(data);
					return this.serializer.serializePojo(resultRes, "filteronmega");
				} else {
					resultRes = ResultResponse.failed(">>>There is no data available.");
					return resultRes;
				}
					
				
			}
			
			data = this.lastDrawnTicketService.retrieveLastDrawnTicketsAsynchFor(numLines, getLotteryTypeFromName(id),
					req.getSession(true).getId()
					);
			resultRes = ResultResponse.pending();
			resultRes.setBaton((String) data); 
			
			
			///Dump the content of the asynch task holder
			this.asynchStorage.dump((k, v) -> {
				try {
					System.out.println(String.format("%s - %s", k.toString(), v.get().toString()));
				} catch (Exception e) {
					System.out.println(">>>>Exception in dumping out the result");
					e.printStackTrace();
				}
			});
			
			return resultRes;
			
			 //return this.serializer.serializePojo(data, "filteronmega");
			//return this.serializer.serializePojo(data, "filteronmega");
		
		} catch (Exception e) {
			log.error(">>>Exception:"+e);
			e.printStackTrace();
			return null; ///Need more work
		}
		
		
	}
	
    
	
	//----------------------- Using lottery type param -----------------------///
	/**
	 * Add a single drawn ticket to the data bank
	 * @param resp
	 * @param ticketHolder
	 * @param id
	 * @return
	 */
	@CrossOrigin
	@PutMapping("api/lastDrawnTicket/{id}")
	public ResultResponse addLastDrawnTicketFor(HttpServletResponse resp, @RequestBody LastDrawnTicketStateHolder ticketHolder,
			@PathVariable("id") String id) {
		
		
		log.info(">>received data:"+ticketHolder.toString());
		try {
			this.lastDrawnTicketService.insertNewLastDrawnTicketFor(ticketHolder, getLotteryTypeFromName(id));
			
			URI location = ServletUriComponentsBuilder.fromCurrentRequest()
								.path("/{id}")
								.buildAndExpand(id)
								.toUri();
			
			ResponseEntity entity = ResponseEntity.created(location).build();
			
			return ResultResponse.success();
		} catch (Exception e) {
			return ResultResponse.failed("Operation failed:"+e);
		}
	}
	
	/**
	 * 
	 * @param resp
	 * @param ticketHolder
	 * @param id
	 * @return
	 */
	@CrossOrigin
	@PutMapping("api/lastDrawnTickets/json/{id}")
	public ResultResponse addLastDrawnTicketAsJsonFor(HttpServletResponse resp, @RequestBody String lastDrawnNumberJsonString,
			@PathVariable("id") String id) {
		
		
		log.info(">>received data:"+lastDrawnNumberJsonString.toString());
		try {
			this.lastDrawnTicketService.insertNewLastDrawnTicketFor(
					StringUtils.filterTopCommentFromJsonString(lastDrawnNumberJsonString), 
					getLotteryTypeFromName(id));
			return ResultResponse.success();
		} catch (Exception e) {
			return ResultResponse.failed("Operation failed:"+e);
		}
	}
	
	
	/**
	 * Add a collection of last drawn ticket to the databank
	 * @param resp
	 * @param ticketHolders
	 * @param id
	 * @return
	 */
	@CrossOrigin
	@PutMapping("api/lastDrawnTickets/{id}")
	public ResultResponse addLastDrawnTicketFor(HttpServletResponse resp, @RequestBody LastDrawnTicketStateHolder[] ticketHolders,
			@PathVariable("id") String id) {
		
		
		log.info(">>received data:"+ticketHolders.toString());
		try {
			this.lastDrawnTicketService.insertNewLastDrawnTicketFor(
					Arrays.asList(ticketHolders),
					getLotteryTypeFromName(id)
					);
			return ResultResponse.success();
		} catch (Exception e) {
			return ResultResponse.failed("Operation failed:"+e);
		}
	}
	
	
	
	
}
