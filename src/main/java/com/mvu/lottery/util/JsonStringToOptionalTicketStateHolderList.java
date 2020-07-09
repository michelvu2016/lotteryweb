package com.mvu.lottery.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.mvu.lottery.stateholder.LastDrawnTicketStateHolder;

import net.minidev.json.JSONArray;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JsonStringToOptionalTicketStateHolderList implements Converter<String, Optional<List<LastDrawnTicketStateHolder>>> {

	private static Logger logger = LoggerFactory.getLogger(JsonStringToOptionalTicketStateHolderList.class);
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
	
	private static String zeroPadding = "000000000000000000000000";
	
	public JsonStringToOptionalTicketStateHolderList() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @return
	 */
	public int numberOfLastDrawnNeedToBePulled() {
		
		
		return 0;
	}
	
	/**
	 * 
	 * @param fromDataFile
	 * @return
	 */
	public Optional<List<LastDrawnTicketStateHolder>> ticketStateHolderFromJsonString(String jsonString) {
		
		List<LastDrawnTicketStateHolder> lastTicketList = new LinkedList<LastDrawnTicketStateHolder>();
		try {

			logger.info(">>>>Enter ticketStateHolderFromJsonString(String jsonString)");

			DocumentContext jsonContext = JsonPath.parse(jsonString);

			String previousNumbers = "$['PreviousDraws']";

			Object result = jsonContext.read(previousNumbers);

			logger.info(">>>>Result of jsonContext.read(previousNumbers): "+result);
			
			if (result instanceof JSONArray) {
				JSONArray prevNumbers = (JSONArray) result;
				prevNumbers.forEach(numbers -> {
					/*
					 * System.out.println(numbers);
					 * System.out.println(numbers.getClass().getName());
					 */
					LastDrawnTicketStateHolder ticketHolder = new LastDrawnTicketStateHolder();
					
					Map<String, ?> prevNumberMap = (Map<String, ?>) numbers;

					Integer drawSeqNum = (Integer) prevNumberMap.get("DrawNumber");
					String date = (String) prevNumberMap.get("DrawDate");

					Map<String, ?> winningNums = (Map<String, ?>) prevNumberMap.get("WinningNumbers");

					ticketHolder.setDate(localDateFromString(date));
					ticketHolder.setSeqNum(String.valueOf(drawSeqNum));
					
					final String[] mega = new String[] {null};
					List<String> numberList = new LinkedList<>();
					winningNums.forEach((key, value) -> {
						
						Map<String, ?> numberMap = (Map<String, ?>) value;
						String num = padLeadingZero((String) numberMap.get("Number"),2);
						Boolean isMega = (Boolean) numberMap.get("IsSpecial");
						if (isMega) {
							mega[0] = num;
						} else {
							numberList.add(num);
						}
						
						
					});
					
					if(mega[0] != null) {
						ticketHolder.setMega(mega[0]);
					}
					
					ticketHolder.setNumbers(numberList.toArray(new String[0]));
					lastTicketList.add(ticketHolder);

				});
				
				
			}

			// System.out.println(result);
			// System.out.println(result.getClass().getName());

		} catch (Exception e) {
			System.out.println(">>>>Exception:" + e);
			return Optional.of(null);
		}
		
		logger.info(">>>>List of tickets: "+lastTicketList);
		return Optional.of(lastTicketList);
	}

	/**
	 * 
	 * @param dateAsString
	 * @return
	 */
	public static LocalDate localDateFromString(String dateAsString) {
		
		String dateString = dateAsString.replace('T', ' ');
		
		LocalDate date = LocalDate.parse(dateString, formatter);
		
		return date;
	}
	
	/**
	 * 
	 * @param instring
	 * @param numberOfPos
	 * @return
	 */
	public static String padLeadingZero(String instring, int numberOfPos) {
		
		String field = zeroPadding + instring;
		
		String retField = field.substring(field.length()-numberOfPos);
		
		return retField;
	}

	@Override
	public Optional<List<LastDrawnTicketStateHolder>> convert(String source) {
		// TODO Auto-generated method stub
		return ticketStateHolderFromJsonString(source);
	}
	 
}
