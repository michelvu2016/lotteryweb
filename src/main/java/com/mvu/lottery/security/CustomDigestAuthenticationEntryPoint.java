package com.mvu.lottery.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.stereotype.Component;


@Component
public class CustomDigestAuthenticationEntryPoint extends DigestAuthenticationEntryPoint {

	public CustomDigestAuthenticationEntryPoint() {
	
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.setRealmName("spring-digest");
		super.setKey("1234567890");
		super.setNonceValiditySeconds(10);
		super.afterPropertiesSet();
	}


	

}
