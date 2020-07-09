package com.mvu.lottery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.mvu.lottery.data.UserData;

@Component("userDetailsService")

public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
	
	@Autowired
	private ConversionService conversionService;
	
	@Autowired
	private UserData userData;

	public UserDetailsService() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		try {
		return this.conversionService.convert(
				userData.getUserByUsername(username),
				
				UserDetails.class
				
				);
		} catch(Exception e) {
			//e.printStackTrace();
			throw e;
		}
	}

}
