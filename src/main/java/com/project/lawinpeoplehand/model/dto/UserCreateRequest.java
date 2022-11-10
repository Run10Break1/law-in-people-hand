package com.project.lawinpeoplehand.model.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.lawinpeoplehand.model.AccountType;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserCreateRequest {

	private AccountType accountType;
	
	private String phone;
	
	private String password;
	
	private String socialId;
	
	private String name;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private LocalDate birthday;
	
	private String fcmToken;
}
