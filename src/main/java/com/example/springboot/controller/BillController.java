package com.example.springboot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public ResponseEntity<String> addProcessStage(@RequestParam("startPage") Integer startPage, @RequestParam(value = "endPage", required = false) Integer endPage) {
		
		try {
			addProcessStageService.addProcessStage(startPage, endPage, 1);
		} catch(Exception e) {
			System.err.println(e);
		}
		
		return ResponseEntity.ok("ok");
	}
	
	@GetMapping("/add-remain-process-stage")
	public ResponseEntity<String> addRemainProcessStage(@RequestParam("startPage") Integer startPage, @RequestParam(value = "endPage", required = false) Integer endPage) {
		
		try {
			addProcessStageService.addRemainProcessStage(startPage, endPage);
		} catch(Exception e) {
			System.err.println(e);
		}
		
		return ResponseEntity.ok("ok");
	}
	
	@PostMapping("/add-process-stage")
	public ResponseEntity<String> addRemainProcessStage(@RequestBody List<Integer> pageList) {
		
		for(Integer page : pageList) {
			try {
				addProcessStageService.addProcessStage(page, page, 1);
			} catch(Exception e) {
				System.err.println(e);
			}
		}
		
		return ResponseEntity.ok("ok");
	}
}
