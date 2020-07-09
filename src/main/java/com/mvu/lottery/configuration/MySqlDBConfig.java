package com.mvu.lottery.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("mysqldb")
public class MySqlDBConfig {

	@Value("${dburl}")
	private String dbUrl;
	
	@Value("${user}")
	private String userName;
	
	@Value("${password}")
	private String password;
	
	
	
	public MySqlDBConfig() {
		// TODO Auto-generated constructor stub
	}



	public String getDbUrl() {
		return dbUrl;
	}



	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
