package com.project.lawinpeoplehand.model.dto;

import com.project.lawinpeoplehand.model.AccountType;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserLoginRequest {
	
	private AccountType accountType;
	
	private String phone;
	
	private String password;
	
	private String socialId;
}
