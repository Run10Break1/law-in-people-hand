package com.example.springboot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.service.DatabaseMigrationService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/bill")
@RequiredArgsConstructor
public class BillController {
	
	private final DatabaseMigrationService databaseMigrationService;
	
	@GetMapping("/migrate")
	public ResponseEntity<String> migrate(@RequestParam("startPage") Integer startPage, @RequestParam(value = "endPage", required = false) Integer endPage) {
		
		try {
			databaseMigrationService.migrate(startPage, endPage);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return ResponseEntity.ok("ok");
	}
}
