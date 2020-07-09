package com.mvu.lottery.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CorsMiddleware extends HandlerInterceptorAdapter {

	public CorsMiddleware() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		this.appHandle(request, response, handler);
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		this.appHandle(request, response, handler);
		return super.preHandle(request, response, handler);
	}
	
	private void appHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(request.getMethod().equals("OPTIONS")) {
			response.addHeader("Access-Controll-Allow-Origins", "*");
			response.addHeader("Access-Controll-Allow-Credentials", "true");
			response.addHeader("Access-Controll-Allow-Methods", "GET, POST, PUT, OPTIONS, DELETE");
			response.addHeader("Access-Controll-Allow-Headers", "Content-Type, Accept, X-Request-With, remember-me");
			response.addHeader("Access-Controll-Allow-Max-Age", "3600");
			response.addHeader("Access-Controll-Allow-Origins", "*");
			
			
		}
		
		
	}

}
