package com.project.lawinpeoplehand.model.dto;

import java.time.LocalDate;

import com.project.lawinpeoplehand.model.User;

import lombok.Data;


@Data
public class UserResponse {
	
	private Long id;
	
	private String name;
	
	private LocalDate birthday;
	
	public UserResponse(User user) {
		id = user.getId();
		name = user.getName();
		birthday = user.getBirthday();
	}
}
