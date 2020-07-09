package com.mvu.lottery.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.text.DateFormatter;

import com.mvu.lottery.constant.LotteryConstants;
import com.mvu.lottery.exceptions.TicketDataInvalidFormatException;
import com.mvu.lottery.stateholder.LastDrawnTicketStateHolder;


public class LoteryDataConverter implements LotteryConstants {
	
	
	public LastDrawnTicketStateHolder toStateHolder(String data) throws Exception {
		String[] parts = data.split("\t");
		
		if (parts.length < 2)
			throw new TicketDataInvalidFormatException(">>>Line format invalid");
		
		String[] dateSeq = parts[0].split(" - ");
		String numbers = parts[1];
		String mega = null;
		if (parts.length > 2)
			mega = parts[2];
		
		String dateString = null;
		String seq = null;
		if(dateSeq.length >= 2)
		{
			dateString = dateSeq[0];
			seq = dateSeq[1];
		}
		
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(DATE_TICKET_DRAWN_FORMAT_PATTERN);
		
		
		
		LocalDate date = LocalDate.parse(dateString, dateFormat);
		
		
		
		
		
		return new LastDrawnTicketStateHolder(date, seq, stringToStringArray(numbers), mega);
	}
	
	
	private static String[] stringToStringArray(String numbers) throws Exception {
		
		List<String> numList = new LinkedList<>();
		
		for(int beginIndex = 0, endIndex = beginIndex + 2; 
				beginIndex < numbers.length() - 1; 
				beginIndex = endIndex, endIndex = beginIndex + 2) {
		
			numList.add(numbers.substring(beginIndex, endIndex));
		}
	
		
		return numList.toArray(new String[numList.size()]);
	}

	
	
}
