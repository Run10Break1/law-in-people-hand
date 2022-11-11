package com.project.lawinpeoplehand.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.lawinpeoplehand.model.dto.SeeRequest;
import com.project.lawinpeoplehand.model.dto.SeenBillResponse;
import com.project.lawinpeoplehand.service.SeeService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("see")
@RequiredArgsConstructor
public class SeeController {

	private final SeeService seeService;
	
	@PostMapping("")
	public ResponseEntity<String> checkAsSeen(@RequestBody SeeRequest request) {
		Long userId = request.getUserId();
		String billId = request.getBillId();
		
		seeService.see(userId, billId);
		
		return ResponseEntity.ok("ok");
	}

	@GetMapping("/most")
	public ResponseEntity<List<SeenBillResponse>> findAllByMostSeen(
			@RequestParam(value = "from", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate from, 
			@RequestParam(value = "to", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate to, 
			@RequestParam(value = "max", required = false) Integer max) {
		List<SeenBillResponse> response = seeService.findAllByMostSeen(from, to, max);
		
		return ResponseEntity.ok(response);
	}
}
