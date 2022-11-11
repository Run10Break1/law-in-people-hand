package com.project.lawinpeoplehand.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.lawinpeoplehand.model.dto.BillResponse;
import com.project.lawinpeoplehand.model.dto.SeenBillResponse;
import com.project.lawinpeoplehand.model.dto.VotedBillResponse;
import com.project.lawinpeoplehand.service.BillService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/bill")
@RequiredArgsConstructor
public class BillController {
	
	private final BillService billService;
	
	@GetMapping("/list")
	public ResponseEntity<Page<BillResponse>> list(Pageable pageable) {
		
		Page<BillResponse> response = billService.findAll(pageable);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/list-seen")
	public ResponseEntity<Page<SeenBillResponse>> listSeen(Pageable pageable, @RequestParam("userId") Long userId) {
		
		Page<SeenBillResponse> response = billService.findAllBillCheckingSeen(pageable, userId);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/list-voted")
	public ResponseEntity<Page<VotedBillResponse>> listVoted(Pageable pageable, @RequestParam("userId") Long userId) {
		
		Page<VotedBillResponse> response = billService.findAllBillCheckingVoted(pageable, userId);
		
		return ResponseEntity.ok(response);
	}
}
