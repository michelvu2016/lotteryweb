package com.mvu.lottery.util;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.mvu.lottery.data.model.Authority;
import com.mvu.lottery.data.model.User;
import com.mvu.lottery.data.model.UserDetailsImpl;

@Component("userToUserDetailsConverter")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserToUserDetailsConverter implements Converter<User, UserDetails>{
	
	public UserToUserDetailsConverter() {
		
	}

	@Override
	public UserDetails convert(User user) {
		UserDetailsImpl userDetails = new UserDetailsImpl(
				 user.getUsername(),
				 user.getPassword(),
				 this.convertToGrantAuth(
						 user.getAuthorities()
						)
				);
		
		return userDetails;
	}
	
	/**
	 * 
	 * @param setOfAuth
	 * @return
	 */
	private Set<GrantedAuthority> convertToGrantAuth(Set<Authority> setOfAuth) {
		
		Function<String, GrantedAuthority> grantedAuthMaker = (authName) -> { 
			
			
			GrantedAuthority grantedAuth = new GrantedAuthority() {
				
				@Override
				public String getAuthority() {
					// TODO Auto-generated method stub
					return authName;
				}
			};
			
			return grantedAuth;
		};
		
		Set<GrantedAuthority> setOfGrantedAuth = new HashSet<>();
		
		setOfAuth.forEach(auth -> {
			setOfGrantedAuth.add(grantedAuthMaker.apply(auth.getName().toString()));
		});
		
		
		return setOfGrantedAuth;
	}

}
