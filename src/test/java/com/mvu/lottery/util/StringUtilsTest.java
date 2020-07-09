package com.mvu.lottery.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.junit.jupiter.DisabledIf;

public class StringUtilsTest {

	String lastDrawnNumberApiUrl  = "http://www.calottery.com//api/DrawGameApi/DrawGamePastDrawResults/{lotteryTypeCode}/1/{NumberOfLastDrawn}";
	
	
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	@DisabledIf("true")
	public void test() {
		String result = StringUtils.createStringTeamplate()
		.markerAndValue("lotteryTypeCode", String.valueOf(8))
		
		.markerAndValue("NumberOfLastDrawn", String.valueOf(17))
		.format(lastDrawnNumberApiUrl)
		;
		
		assertEquals("http://www.calottery.com//api/DrawGameApi/DrawGamePastDrawResults/8/1/17", result);
	}
	
	@Test
	public void testFilterJsonString() {
		String result = StringUtils.filterTopCommentFromJsonString("//--- coment for some commonet {\"name\": \"mike\"}");
		
		assertEquals("{\"name\": \"mike\"}", result);
		
	}

}
