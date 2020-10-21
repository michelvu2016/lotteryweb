package com.mvu.lottery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserMaintenanceController {

	public UserMaintenanceController() {
		// TODO Auto-generated constructor stub
	}
	
	
	//public void registerNewUser(@ModelAttribute UserRegistration userRegistration ) {
		
	//}
	
	@GetMapping("/test")
	@ResponseBody
	public String test() {
		return "It works";
	}
	

}
