package com.mvu.lottery.data;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mvu.lottery.data.setup.DataInitializer;
import com.mvu.lottery.stateholder.LastDrawnTicketStateHolder;

public class DataInitializerTester {

	private DataInitializer dataInit;
	
	private static String jsonFile = "C:\\projects\\Lotto\\jsondata/lottoJson.json";
	
	@Before
	public void setUp() throws Exception {
		this.dataInit = new DataInitializer();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		 Optional<List<LastDrawnTicketStateHolder>> optionList = this.dataInit.initData(jsonFile);
		
		 assertTrue(optionList.isPresent());
		 
		 optionList.get().forEach(ticket -> {
			 System.out.println(ticket);
		 });
		 
	}

}
