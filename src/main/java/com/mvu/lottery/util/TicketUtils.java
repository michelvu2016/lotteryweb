package com.mvu.lottery.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import com.mvu.lottery.constant.LotteryConstants;
import com.mvu.lottery.exceptions.TicketDataInvalidFormatException;
import com.mvu.lottery.stateholder.LastDrawnTicketStateHolder;

public class TicketUtils implements LotteryConstants {
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TICKET_DRAWN_FORMAT_PATTERN);
	
	/**
	 * Return the LocalDate object from the ticket string
	 * @param ticketString
	 * @return
	 * @throws Exception
	 */
	public static LocalDate extractLocalDateTimeFromTicketString(final String ticketString) throws Exception {
		LocalDate date = null;
		String[] parts = ticketString.split(" - ");
		if (parts.length < 2) {
			throw new TicketDataInvalidFormatException("The ticket data format is invalid. No - found.");
		} else {
				date = LocalDate.parse(parts[0].trim(), formatter);
			}
		
		return date;
	}
	
	
	
	
	/**
	 * Return the date as String
	 * @param date
	 * @return String
	 * @throws Exception
	 */
	public static String localDateToString(final LocalDate date) throws Exception {
		return date.format(formatter);
	}
	
	/**
	 * Return String record from state holder
	 * @param stateHolder
	 * @return
	 * @throws Exception
	 */
	public static String stringFromStateHolder(final LastDrawnTicketStateHolder stateHolder) throws Exception {
	
		String res = null;
		
		if(stateHolder.getMega() != null) {
			res = String.format(RECORD_FORMAT_WITH_MEGA, 
				localDateToString(
								stateHolder.getDate()
							),
				stateHolder.getSeqNum(),
				
				reduce(
				stateHolder.getNumbers()
				),
				stateHolder.getMega()
				);
			} else {
				res = String.format(RECORD_FORMAT_WITHOUT_MEGA, 
						localDateToString(
										stateHolder.getDate()
									),
						stateHolder.getSeqNum(),
						
						reduce(
						stateHolder.getNumbers()
						));
			}
		
		return res;
	}
	
	/**
	 * Combine the String array into a single 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	private static String reduce(String[] input) throws Exception {
		Optional<String> retString = Arrays.stream(input).reduce((s, i) -> s.concat(i));

		return retString.orElse(null);

	}
	/**
	 * 
	 * @param fromDate
	 * @param toDate
	 * @param drawnDate
	 * @return
	 */
	public static int numberOfDrawnDatesFrom(LocalDate fromDate, LocalDate toDate, String[] drawnDate) {
		
		LocalDate startFromDate = LocalDate.of(fromDate.getYear(), fromDate.getMonth(), fromDate.getDayOfMonth());
		
		int count = 0;
		boolean daily = false;
		
		if (drawnDate == null) {
			daily = true;
		}
		

		
	
		
		while(fromDate.isEqual(toDate) || fromDate.isBefore(toDate)) {
			String dayOfWeek = fromDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US);
			
			if (daily) {
				count++;
			} else {
				
				if (Stream.of(drawnDate).anyMatch(day -> {
					
					return day.equals(dayOfWeek);
				})) {
					count++;
				}
				
			}
				
			
			fromDate = fromDate.plusDays(1);
			
			
		}
		
		return count;
		
		
	}

}
