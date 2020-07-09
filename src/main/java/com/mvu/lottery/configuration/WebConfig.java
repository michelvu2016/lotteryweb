package com.mvu.lottery.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mvu.lottery.util.JsonStringToOptionalTicketStateHolderList;
import com.mvu.lottery.util.UserToUserDetailsConverter;

@Configuration
@ComponentScan(basePackages = {"com.mvu.lottery.controller","com.mvu.lottery.customserializer"})
public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
	private UserToUserDetailsConverter userToUserDetailsConverter;
	
	@Autowired
	private JsonStringToOptionalTicketStateHolderList jsonStringToOptionalTicketStateHolderList;

	public WebConfig() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.addFormatters(registry);
		registry.addConverter(this.userToUserDetailsConverter);
		registry.addConverter(this.jsonStringToOptionalTicketStateHolderList);

	}
	
	

}
