package com.mvu.lottery;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class AuthenticationManagerTester {

	public AuthenticationManagerTester() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		AuthenticationManagerImpl authMan = new AuthenticationManagerImpl();
		
		
		
		Authentication auth = authMan.authenticate(new UsernamePasswordAuthenticationToken("mvu", "cel123"));
		
		if (auth != null) {
			System.out.println(">>>Authenticated user:" + auth);
		}
		
	}
	
}
