package com.project.lawinpeoplehand.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.lawinpeoplehand.model.AccountType;
import com.project.lawinpeoplehand.model.User;
import com.project.lawinpeoplehand.model.dto.UserCreateRequest;
import com.project.lawinpeoplehand.model.dto.UserLoginRequest;
import com.project.lawinpeoplehand.model.dto.UserResponse;
import com.project.lawinpeoplehand.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	
	public UserResponse create(UserCreateRequest request) {
		
		if(find(request.getAccountType(), request.getPhone(), request.getPassword(), request.getSocialId()) != null) {
			return null;
		}
		
		User.UserBuilder builder = User.builder();
		builder
		.accountType(request.getAccountType())
		.phone(request.getPhone())
		.password(request.getPassword())
		.socialId(request.getSocialId())
		.birthday(request.getBirthday())
		.fcmToken(request.getFcmToken());
		
		User user = builder.build();
		
		return new UserResponse(userRepository.save(user));
	}
	
	public UserResponse login(UserLoginRequest request) {
		return find(request.getAccountType(), request.getPhone(), request.getPassword(), request.getSocialId());
	}
	
	public UserResponse find(AccountType accountType, String phone, String password, String socialId) {
		if(accountType == AccountType.PHONE) {
			if(phone == null || password == null || password.isEmpty()) {
				throw new RuntimeException(String.format("전화번호로 사용자를 찾기 위한 정보가 부족합니다. (phone, password) = (%s, %s)", phone, password));
			}
			return findViaPhone(phone, password);
		} else {
			if(accountType == null) {
				throw new RuntimeException("accountType이 null 입니다.");
			}
			
			if(socialId == null) {
				throw new RuntimeException(String.format("소셜로 사용자를 찾기 위한 위한 정보가 부족합니다. (socialId) = (%s)", socialId));
			}
			return findViaSocial(socialId);
		}
	}
	
	private UserResponse findViaPhone(String phone, String password) {
		Optional<User> userOptional = userRepository.findByPhoneAndPasswordAndWithdrawalFalse(phone, password);
		
		if(userOptional.isEmpty()) {
			return null;
		}
		
		return new UserResponse(userOptional.get());
	}
	
	private UserResponse findViaSocial(String socialId) {
		Optional<User> userOptional = userRepository.findBySocialIdAndWithdrawalFalse(socialId);
		
		if(userOptional.isEmpty()) {
			return null;
		}
		
		return new UserResponse(userOptional.get());
	}
	
	public UserResponse findById(Long userId) {
		Optional<User> userOptional = userRepository.findById(userId);
		
		if(userOptional.isEmpty()) {
			return null;
		}
		
		return new UserResponse(userOptional.get());
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
