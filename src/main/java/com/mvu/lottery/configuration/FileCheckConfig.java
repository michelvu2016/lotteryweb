package com.mvu.lottery.configuration;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;

import com.mvu.lottery.constant.LotteryConfiguration;
import com.mvu.lottery.constant.LotteryConstants;

public class FileCheckConfig implements LotteryConstants {

	
	@Autowired
	private LotteryConfiguration lotteryConfiguration;
	
	
	
	public FileCheckConfig() {
		// TODO Auto-generated constructor stub
	}
	
	//File Check procedure
	//1. Data file
	//2. Open file. all line has date conform the rule:
	//	  Drawn date is after the current date
		  
	Predicate<LotteryType> dataFileVerifier = (LotteryType lotteryType) -> {
		
		return true;
	};
	

}
 