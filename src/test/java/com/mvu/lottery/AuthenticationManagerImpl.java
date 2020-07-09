package com.mvu.lottery;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

public class AuthenticationManagerImpl implements AuthenticationManager {
	
	
	
	
	
	/**
	 * 
	 * @return
	 */
	public UserDetailsService userDetailService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("mvu").password("cel123").authorities("ROLE_USER", "ROLE_ADMIM").build());
		
		return manager;
	}

	public AuthenticationManagerImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UserDetails userDetails = this.userDetailService().loadUserByUsername("mvu");
		
		if (userDetails != null ) {
			if(userDetails.getPassword().equals(authentication.getCredentials()) ) {
				if(userDetails.getUsername().equals("mvu")) {
					return new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
							userDetails.getPassword(),
							userDetails.getAuthorities());
				}
			}
		}
		
		
		
		return null;
	}

}
