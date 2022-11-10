package com.project.lawinpeoplehand.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.project.lawinpeoplehand.model.dto.BillResponse;
import com.project.lawinpeoplehand.service.BillService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/bill")
@RequiredArgsConstructor
public class BillController {
	
	private final BillService billService;
	
	@GetMapping("list")
	public ResponseEntity<Page<BillResponse>> list(Pageable pageable) {
		
		Page<BillResponse> response = billService.findAll(pageable);
		
		return ResponseEntity.ok(response);
	}
}
