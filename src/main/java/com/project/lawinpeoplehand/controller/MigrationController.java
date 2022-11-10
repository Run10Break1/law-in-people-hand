package com.project.lawinpeoplehand.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.lawinpeoplehand.service.MigrationService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MigrationController {
	
    private final MigrationService migrationService;
		
	@GetMapping("/migrate")
	public ResponseEntity<String> migrate(@RequestParam("startPage") Integer startPage, @RequestParam(value = "endPage", required = false) Integer endPage) {
		
		try {
			migrationService.migrate(startPage, endPage);
		} catch (Exception e) {
			System.err.println(e);
		}
		
		return ResponseEntity.ok("ok");
	}
	
	@GetMapping("/add-process-stage")
	public ResponseEntity<String> addProcessStage(@RequestParam("startPage") Integer startPage, @RequestParam(value = "endPage", required = false) Integer endPage) {
		
		try {
			migrationService.addProcessStage(startPage, endPage, 1);
		} catch(Exception e) {
			System.err.println(e);
		}
		
		return ResponseEntity.ok("ok");
	}

	@GetMapping("/add-remain-process-stage")
	public ResponseEntity<String> addRemainProcessStage(@RequestParam("startPage") Integer startPage, @RequestParam(value = "endPage", required = false) Integer endPage) {
		
		try {
			migrationService.addRemainProcessStage(startPage, endPage);
		} catch(Exception e) {
			System.err.println(e);
		}
		
		return ResponseEntity.ok("ok");
	}
	
	@GetMapping("/add-overview")
	public ResponseEntity<String> addOverview(@RequestParam("startPage") Integer startPage, @RequestParam(value = "endPage", required = false) Integer endPage) {
		
		try {
			migrationService.addOverview(startPage, endPage, 1);
		} catch(Exception e) {
			System.err.println(e);
		}
		
		return ResponseEntity.ok("ok");
	
	}
	
	@GetMapping("/add-keyword")
	public ResponseEntity<String> addKeyword(@RequestParam("startPage") Integer startPage, @RequestParam(value = "endPage", required = false) Integer endPage) {
		
		try {
			migrationService.addKeyword(startPage, endPage);
		} catch(Exception e) {
			System.err.println(e);
		}
		
		return ResponseEntity.ok(String.format("startPage:endPage=%d:%d", startPage, endPage));
	}
}
