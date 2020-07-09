package com.mvu.lottery.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.mvu.lottery.constant.LotteryConstants;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class StringToLocalDateConverter extends StdConverter<String, LocalDate> implements LotteryConstants {

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TICKET_DRAWN_FORMAT_PATTERN);
	
	@Override
	public LocalDate convert(String value) {
		
		return LocalDate.parse(value, formatter);
	}

}
