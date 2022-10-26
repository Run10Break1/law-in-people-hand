package com.example.springboot.controller;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.service.AddProcessStageService;
import com.example.springboot.service.DatabaseMigrationService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/bill")
@RequiredArgsConstructor
public class BillController {
	
	private final DatabaseMigrationService databaseMigrationService;
	private final AddProcessStageService addProcessStageService;
	
	@GetMapping("/migrate")
	public ResponseEntity<String> migrate(@RequestParam("startPage") Integer startPage, @RequestParam(value = "endPage", required = false) Integer endPage) {
		
		try {
			databaseMigrationService.migrate(startPage, endPage);
		} catch (Exception e) {
			System.err.println(e);
		}
		
		return ResponseEntity.ok("ok");
	}
	
	@GetMapping("/add-process-stage")
	public ResponseEntity<String> addProcessStage(Pageable pageable) {
		
		try {
			addProcessStageService.addProcessStage(pageable);
		} catch(Exception e) {
			System.err.println(e);
		}
		
		return ResponseEntity.ok("ok");
	}
}
