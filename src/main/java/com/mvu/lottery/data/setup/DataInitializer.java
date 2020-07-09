package com.mvu.lottery.data.setup;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.mvu.lottery.stateholder.LastDrawnTicketStateHolder;

import net.minidev.json.JSONArray;

public class DataInitializer {

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
	
	private static String zeroPadding = "000000000000000000000000";
	
	public DataInitializer() {
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
	public Optional<List<LastDrawnTicketStateHolder>>	initData(String fromDataFile) {
		
		List<LastDrawnTicketStateHolder> lastTicketList = new LinkedList<LastDrawnTicketStateHolder>();
		try {
			byte[] content = Files.readAllBytes(Paths.get(fromDataFile));

			String jsonString = new String(content);

			DocumentContext jsonContext = JsonPath.parse(jsonString);

			String previousNumbers = "$['PreviousDraws']";

			Object result = jsonContext.read(previousNumbers);

			
			
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
	 
}
