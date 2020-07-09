package com.mvu.lottery.service;

import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Component;

@Component
@Primary
public class MyConversionService extends DefaultConversionService implements ConversionService {

	public MyConversionService() {
		// TODO Auto-generated constructor stub
	}

}
