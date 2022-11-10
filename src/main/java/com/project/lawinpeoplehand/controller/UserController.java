package com.project.lawinpeoplehand.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.lawinpeoplehand.model.dto.UserCreateRequest;
import com.project.lawinpeoplehand.model.dto.UserLoginRequest;
import com.project.lawinpeoplehand.model.dto.UserResponse;
import com.project.lawinpeoplehand.service.UserService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@PostMapping("/create")
	public ResponseEntity<UserResponse> create(@RequestBody UserCreateRequest request) {
		
		UserResponse userResponse = userService.create(request);
		
		if(userResponse == null) {
			return ResponseEntity.status(400).body(null);
		}
		
		return ResponseEntity.ok(userResponse);
	}
	
	@PostMapping("/login")
	public ResponseEntity<UserResponse> login(@RequestBody UserLoginRequest request) {
		
		UserResponse userResponse = userService.login(request);
		
		if(userResponse == null) {
			return ResponseEntity.status(400).body(null);
		}
		
		return ResponseEntity.ok(userResponse);
	}
	
	@PostMapping("/login-if-exists")
	public ResponseEntity<UserResponse> loginIfExists(@RequestBody UserLoginRequest request) {
		
		UserResponse userResponse = userService.login(request);
		
		if(userResponse == null) {
			final UserResponse nullUserResponse = new UserResponse();
			nullUserResponse.setId(null);
			
			return ResponseEntity.ok(nullUserResponse);
		}
		
		return ResponseEntity.ok(userResponse);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserResponse> findById(@PathVariable("userId") Long userId) {
		
		UserResponse userResponse = userService.findById(userId);
		
		if(userResponse == null) {
			return ResponseEntity.status(400).body(null);
		}
		
		return ResponseEntity.ok(userResponse);
	}
	
}
