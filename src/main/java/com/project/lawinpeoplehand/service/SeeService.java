package com.project.lawinpeoplehand.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.lawinpeoplehand.model.Bill;
import com.project.lawinpeoplehand.model.See;
import com.project.lawinpeoplehand.model.User;
import com.project.lawinpeoplehand.model.dto.SeenBillResponse;
import com.project.lawinpeoplehand.repository.BillRepository;
import com.project.lawinpeoplehand.repository.SeeRepository;
import com.project.lawinpeoplehand.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class SeeService {
	
	private final UserRepository userRepository;
	private final SeeRepository seeRepository;
	private final BillRepository billRepository;
	
	public void see(Long userId, String billId) {
		
		User user = userRepository.findById(userId).get();
		Bill bill = billRepository.findById(billId).get();
		
		See see = new See();
		see.setUser(user);
		see.setBill(bill);
		
		seeRepository.save(see);
	}
	
	public List<SeenBillResponse> findAllByMostSeen(LocalDate from, LocalDate to, Integer max) {
		return seeRepository.findAllByMostSeen(from, to, max);
	}
}
