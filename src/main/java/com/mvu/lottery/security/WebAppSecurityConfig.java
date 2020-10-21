package com.mvu.lottery.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"com.mvu.lottery.security", 
		"com.mvu.lottery.service", 
		"com.mvu.lottery.data",
		"com.mvu.lottery.configuration",
		"com.mvu.lottery.aop",
		"com.mvu.lottery.util"})
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

	//@Autowired
	//private BasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;

	//@Autowired
	//private DigestAuthenticationEntryPoint customDigestAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().regexMatchers("/resources/.*");
	}

	FilterSecurityInterceptor filterSecurityInterceptor() throws Exception {
		FilterSecurityInterceptor fSecInterceptor = new FilterSecurityInterceptor();
		
		fSecInterceptor.setAuthenticationManager(super.authenticationManager());
		
		// Function return the ConfigAttribute for the role name
		Function<String[], Collection<ConfigAttribute>> configAttribForRole = (roleNameArray) -> {
			
			List<ConfigAttribute> configAttributeList = new LinkedList<>();
			
			for (String roleName: roleNameArray) {
				configAttributeList.add(
						 new ConfigAttribute() {
							
							@Override
							public String getAttribute() {
								
								return roleName;
							}
						});
			}
			
			return configAttributeList;
			    
			};
	
		Map<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();

		requestMap.put(new AntPathRequestMatcher("/setup**"), configAttribForRole.apply(new String[] {"ROLE_AMIN"}));
		
		fSecInterceptor.setSecurityMetadataSource(new DefaultFilterInvocationSecurityMetadataSource((LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>) requestMap));
				
				    
				
		
		return fSecInterceptor;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		System.out.println(">>>>security configure called");
		BasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint = new CustomBasicAuthenticationEntryPoint();
		http.cors().and().csrf().disable().authorizeRequests()
				.antMatchers("/**").permitAll()
				//.antMatchers("/api/*")

				// .permitAll()
				//.hasRole("ADMIN").and().httpBasic().authenticationEntryPoint(customBasicAuthenticationEntryPoint)
				//.and().addFilter(this.basicAuthenticationFilter(super.authenticationManager(), customBasicAuthenticationEntryPoint))

		;
		/*
		 * authorizeRequests() .antMatchers("/**")
		 * .authenticated().and().csrf().disable()
		 * 
		 * .httpBasic()
		 * 
		 * ;
		 */

	}

	/**
	 * 
	 * @return
	 */
	public DigestAuthenticationFilter digestAuthenticationFilter() {
		DigestAuthenticationFilter digestAuthenticationFilter = new DigestAuthenticationFilter();
		DigestAuthenticationEntryPoint customDigestAuthenticationEntryPoint = new CustomDigestAuthenticationEntryPoint();
		digestAuthenticationFilter.setAuthenticationEntryPoint(customDigestAuthenticationEntryPoint);
		digestAuthenticationFilter.setUserDetailsService(this.userDetailsService);

		return digestAuthenticationFilter;
	}

	/**
	 * 
	 * @param authManager
	 * @return
	 */
	public BasicAuthenticationFilter basicAuthenticationFilter(AuthenticationManager authManager, BasicAuthenticationEntryPoint basicAuthenticationEntryPoint) {

		BasicAuthenticationFilter basicAuthenticationFilter = new BasicAuthenticationFilter(authManager,
				basicAuthenticationEntryPoint);

		return basicAuthenticationFilter;
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(
				Arrays.asList("http://localhost:4200", "http://localhost:8080", "http://127.0.0.1:4200"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "DELETE"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(
				Arrays.asList("Content-Type", "Accept", "X-Request-With", "remember-me", "Authorization"));
		configuration.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
