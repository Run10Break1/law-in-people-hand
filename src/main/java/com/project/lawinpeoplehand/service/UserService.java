package com.project.lawinpeoplehand.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.lawinpeoplehand.model.AccountType;
import com.project.lawinpeoplehand.model.User;
import com.project.lawinpeoplehand.model.dto.UserResponse;
import com.project.lawinpeoplehand.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	
	public UserResponse login(AccountType accountType, String phone, String password, String socialId) {
		if(accountType == AccountType.PHONE) {
			if(phone == null || password == null || password.isEmpty()) {
				throw new RuntimeException(String.format("전화번호로 로그인하기 위한 정보가 부족합니다. (phone, password) = (%s, %s)", phone, password));
			}
			return loginViaPhone(phone, password);
		} else {
			if(socialId == null) {
				throw new RuntimeException(String.format("소셜로 로그인하기 위한 정보가 부족합니다. (socialId) = (%s)", socialId));
			}
			return loginViaSocial(socialId);
		}
	}
	
	private UserResponse loginViaPhone(String phone, String password) {
		Optional<User> userOptional = userRepository.findByPhoneAndPasswordAndWithdrawalFalse(phone, password);
		
		if(userOptional.isEmpty()) {
			return null;
		}
		
		User user = userOptional.get();
		return new UserResponse(user);
	}
	
	private UserResponse loginViaSocial(String socialId) {
		Optional<User> userOptional = userRepository.findBySocialIdAndWithdrawalFalse(socialId);
		
		if(userOptional.isEmpty()) {
			return null;
		}
		
		User user = userOptional.get();
		return new UserResponse(user);
	}
	
	public void withdrawal(Long userId) {
		Optional<User> userOptional = userRepository.findById(userId);
		
		if(userOptional.isEmpty()) {
			return;
		}
		
		User user = userOptional.get();
		user.setWithdrawal(true);
		user.setWithDrawalAt(LocalDate.now());
		
		userRepository.save(user);
	}
}
