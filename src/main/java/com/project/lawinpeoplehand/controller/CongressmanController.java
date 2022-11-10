package com.project.lawinpeoplehand.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.lawinpeoplehand.model.dto.CongressmanCreateRequest;
import com.project.lawinpeoplehand.service.CongressmanService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/congressman")
@RequiredArgsConstructor
public class CongressmanController {

	private final CongressmanService congressmanService;
	
	@PostMapping("create")
	public ResponseEntity<Integer> create(@RequestBody List<CongressmanCreateRequest> requestList) {
		Integer createdSize = congressmanService.create(requestList);
		
		return ResponseEntity.ok(createdSize);
	}
}
