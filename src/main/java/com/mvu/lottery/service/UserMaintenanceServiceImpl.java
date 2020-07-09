package com.mvu.lottery.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvu.lottery.constant.LotteryConstants;
import com.mvu.lottery.data.UserData;
import com.mvu.lottery.data.model.Authority;
import com.mvu.lottery.data.model.User;

@Service
public class UserMaintenanceServiceImpl implements LotteryConstants {

	@Autowired
	private UserData userDataImpl;
	
	@Autowired
	private StrongPasswordEncryptor strongPasswordEncryptor;
	
	public UserMaintenanceServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Return the encrypted password
	 * @param pass
	 * @return
	 */
	private String encrypt(String pass) {
		return this.strongPasswordEncryptor.encryptPassword(pass);
		//return pass;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param roles
	 */
	public void createUserMultipleRole(String username, String password, AuthorityType[] roles) {
		
		
		Set<Authority> auths = new HashSet<>();
		
		for (AuthorityType role: roles) {
			auths.add(new Authority(role));
		}
		
		
		User user = new User(username,this.encrypt(password), auths);
		
		this.userDataImpl.saveUser(user);
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param authType
	 */
	public void createUser(String username, String password,
			AuthorityType authType) {
		
		Authority auth = new Authority();
		auth.setName(authType);
		
		Set<Authority> auths = new HashSet<>();
		auths.add(auth);
		
		User user = new User(username,this.encrypt(password), auths);
		
		this.userDataImpl.saveUser(user);
		
	}

}
