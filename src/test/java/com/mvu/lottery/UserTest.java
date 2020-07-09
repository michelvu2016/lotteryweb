package com.mvu.lottery;

import static org.junit.Assert.*;

import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.mvu.lottery.configuration.LotteryAppConfiguration;
import com.mvu.lottery.constant.LotteryConstants;
import com.mvu.lottery.service.UserMaintenanceServiceImpl;

@RunWith(SpringRunner.class)
@ComponentScan(basePackages= {"com.mvu.lottery"})
@ContextConfiguration(classes = {LotteryNumberPickerApplication.class, LotteryAppConfiguration.class})
@SpringBootTest
public class UserTest implements LotteryConstants {

	@Autowired
	private UserMaintenanceServiceImpl service;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	@Test
	public void test() {
		this.service.createUserMultipleRole("mvu", "cel123", 
				 Arrays.array(AuthorityType.ROLE_USER, AuthorityType.ROLE_ADMIN)
				);
		
		this.service.createUserMultipleRole("celina", "cel123", 
				 Arrays.array(AuthorityType.ROLE_USER, AuthorityType.ROLE_USER)
				);
		
		this.service.createUserMultipleRole("tony", "cel123", 
				 Arrays.array(AuthorityType.ROLE_USER, AuthorityType.ROLE_USER)
				);
	}
	
	
}
