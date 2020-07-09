package com.mvu.lottery;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mvu.lottery.data.setup.DataInitializer;

public class TestStringPadding {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		String result = DataInitializer.padLeadingZero("53", 2);
		
		assertEquals("53", result);
		
	}

}
