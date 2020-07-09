package com.mvu.lottery.security;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.mvu.lottery.configuration.LotteryAppConfiguration;
import com.mvu.lottery.configuration.WebConfig;
import com.mvu.lottery.constant.LotteryConfiguration;




public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	public WebInitializer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		// TODO Auto-generated method stub
		return new Class[] {LotteryConfiguration.class, LotteryAppConfiguration.class, WebAppSecurityConfig.class  };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		// TODO Auto-generated method stub
		return new Class[] {WebConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		
		return new String[] {"/"};
	}

}
