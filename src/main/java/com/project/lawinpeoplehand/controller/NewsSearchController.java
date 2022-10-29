package com.project.lawinpeoplehand.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.lawinpeoplehand.service.NaverNewsSearchService;
import com.project.lawinpeoplehand.service.NewsSearchService;
import com.project.lawinpeoplehand.service.NewsSearchService.Request;
import com.project.lawinpeoplehand.service.NewsSearchService.Response;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/search/news")
@RequiredArgsConstructor
public class NewsSearchController {

	private final NewsSearchService newsSearchService;
	
	@GetMapping("")
	public ResponseEntity<List<Response>> search(@RequestParam("query") final String query) throws Exception {
		
		List<Response> response;
		
		try {
			Request request = new NaverNewsSearchService.NaverRequest(query, null, null);
			response = newsSearchService.search(request);
		} catch(Exception e) {
			System.err.println(e);
			throw e;
		}
		
		return ResponseEntity.ok(response);
	}
	
}
