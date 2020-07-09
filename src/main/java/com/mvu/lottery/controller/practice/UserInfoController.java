package com.mvu.lottery.controller.practice;

import java.net.URI;

import javax.ws.rs.Path;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Path("/UserInfo")
public class UserInfoController {

	public UserInfoController() {
		// TODO Auto-generated constructor stub
	}

	@PutMapping("createUser/{id}")
	public ResponseEntity createUser(@PathVariable("id") String id) {
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
							.path("/{id}")
							.buildAndExpand(id)
							.toUri();
		
		ResponseEntity entity = ResponseEntity.created(location).build();
		
		
		
		return entity;
	}
	
	
}
