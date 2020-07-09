package com.mvu.lottery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SystemInfoController {

	public SystemInfoController() {
		// TODO Auto-generated constructor stub
	}

	@GetMapping("/info")
	@ResponseBody
	public String systemInfo() {
		return "LotterNumberPicker is running";
	}
}
