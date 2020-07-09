package com.mvu.lottery.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CustomRestTemplate {
	
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate template = new RestTemplate();
		List<ClientHttpRequestInterceptor> interceptors = template.getInterceptors();
		
		if(CollectionUtils.isEmpty(interceptors)) {
			interceptors = new LinkedList<>();
		}
		
		
		interceptors.add(createGlobalHeaderModifierInterceptor());
		
		return template;
		
	}
	
	/**
	 * Create and return the ClientHttpRequesttInterceptor
	 * @return
	 */
	private ClientHttpRequestInterceptor createGlobalHeaderModifierInterceptor() {
		return new ClientHttpRequestInterceptor() {
			
			@Override
			public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
					throws IOException {
				
				ClientHttpResponse response = execution.execute(request, body);
				
				response.getHeaders().add("Access-Control-Allow-Origin", "*");
				
				return response;
			}
		};
	}
	
}
