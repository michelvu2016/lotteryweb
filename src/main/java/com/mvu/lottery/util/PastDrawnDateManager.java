package com.mvu.lottery.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mvu.lottery.constant.LotteryConfiguration;
import com.mvu.lottery.constant.LotteryConstants;

public class PastDrawnDateManager implements LotteryConstants {
	
	@Autowired
	private LotteryConfiguration lotterConfig;
	

	public PastDrawnDateManager() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * Return the list of drawn dates for the lottery type
	 * @param numberOfDrawnDatesBack
	 * @param fromDate
	 * @param lotteryType
	 * @return
	 * @throws Exception
	 */
	public List<LocalDate> lastNDrawnDates(int numberOfDrawnDatesBack, final LocalDate fromDate, LotteryType lotteryType) throws Exception {
		
		List<LocalDate> retDates = new LinkedList<>();

		LocalDate useFromDate = fromDate;

		if (useFromDate == null) {
			useFromDate = LocalDate.now();
		}

		for (int index = 1; index <= numberOfDrawnDatesBack; index++) {
			useFromDate = this.dayOfWeekTester(useFromDate, lotterConfig.getDrawnDates(lotteryType));

			retDates.add(useFromDate);

		}

		return retDates;
	}
	
	/**
	 * Calculate and return the closest drawn date for the lottery type
	 * @param dateIn
	 * @param daysOfWeek
	 * @return
	 */
	private LocalDate dayOfWeekTester(LocalDate dateIn, DayOfWeek[] daysOfWeek) {
		DayOfWeek currDayOfWeek = dateIn.getDayOfWeek();
		
		int offset = 0;
		
		for (int index = 0; index < daysOfWeek.length; index++) {
			if (currDayOfWeek.getValue() <= daysOfWeek[index].getValue()) {
				int lastDrawnDateFromTodayIndex = index - 1;
				if (lastDrawnDateFromTodayIndex < 0) {
					lastDrawnDateFromTodayIndex = daysOfWeek.length - 1;
				}
				DayOfWeek lastDrawnDayOfWeekFromToday = daysOfWeek[lastDrawnDateFromTodayIndex];
				
				int currDayOfWeekOrdValue = currDayOfWeek.getValue();
				if (currDayOfWeekOrdValue < lastDrawnDayOfWeekFromToday.getValue() ) {
					currDayOfWeekOrdValue += 7;
				}
				
				offset = currDayOfWeekOrdValue - lastDrawnDayOfWeekFromToday.getValue();
				break;

			}
		}
		
		LocalDate pastDrawmDate = dateIn.minusDays(offset);
		
		return pastDrawmDate;
	}
	
}
