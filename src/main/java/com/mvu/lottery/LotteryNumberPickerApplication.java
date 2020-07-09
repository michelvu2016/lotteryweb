package com.mvu.lottery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class LotteryNumberPickerApplication //xtends SpringBootServletInitializer 
{

	
	
	
	//@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		//return super.configure(builder);
		
		return builder.sources(SpringApplicationBuilder.class);
			
	}

	public static void main(String[] args) {
		SpringApplication.run(LotteryNumberPickerApplication.class, args);
	}

}
