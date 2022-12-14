package com.project.lawinpeoplehand.model.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.lawinpeoplehand.model.User;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserResponse {
	
	private Long id;
	
	private String name;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private LocalDate birthday;
	
	public UserResponse(User user) {
		id = user.getId();
		name = user.getName();
		birthday = user.getBirthday();
	}
}
