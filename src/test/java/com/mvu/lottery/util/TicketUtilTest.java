package com.mvu.lottery.util;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mvu.lottery.constant.LotteryConstants;

public class TicketUtilTest {

	TicketUtilSupport support; 
	
	@Before
	public void setUp() throws Exception {
		support = new TicketUtilSupport();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		
		LocalDate fromDate = LocalDate.of(2019, 11, 5);
		
		LocalDate toDate = LocalDate.now();
		
		String[] drawnDates = 
				//this.support.getDrawnDateForSuperlotto()
				this.support.getDrawnDateForFantasy5()
				//this.support.getDrawnDateForPowerball()
				
				;
		
		int numdates = TicketUtils.numberOfDrawnDatesFrom(fromDate.plusDays(1), toDate, support.isDaily(drawnDates) ? null : drawnDates);
		
		System.out.println(numdates);
		
	}

	
	
	public static class TicketUtilSupport implements LotteryConstants {
		
		public String[] getDrawnDateForSuperlotto() {
			return getDrawingDaysForLottoryType(LotteryType.SUPERLOTTO);
		}
		
		public String[] getDrawnDateForPowerball() {
			return getDrawingDaysForLottoryType(LotteryType.POWERBALL);
		}
		
		public String[] getDrawnDateForFantasy5() {
			return getDrawingDaysForLottoryType(LotteryType.FANTASY5);
		}
		
		public boolean isDaily(String[] drawnDates) {
			return isDrawnDateDaily(drawnDates);
		}
	}
	
}



