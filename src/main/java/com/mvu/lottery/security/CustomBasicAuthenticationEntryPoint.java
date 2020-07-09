package com.mvu.lottery.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;



public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

	public CustomBasicAuthenticationEntryPoint() {
	
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.setRealmName("spring");
		super.afterPropertiesSet();
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		response.addHeader("WWWW-Authenticate", "Basic realm=\""+this.getRealmName()+ "\"");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		PrintWriter writer = response.getWriter();
		writer.println("Basic Authentication: You are not authorized. "+authException.getMessage());
		
	}
	
	

}
