package com.project.lawinpeoplehand.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.lawinpeoplehand.model.User;


public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByPhoneAndPasswordAndWithdrawalFalse(String phone, String password);
	Optional<User> findBySocialIdAndWithdrawalFalse(String socialId); 
}
