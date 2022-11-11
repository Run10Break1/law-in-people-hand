package com.project.lawinpeoplehand.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.lawinpeoplehand.model.VoteType;
import com.project.lawinpeoplehand.model.dto.VoteRequest;
import com.project.lawinpeoplehand.model.dto.VotedBillResponse;
import com.project.lawinpeoplehand.service.VoteService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("vote")
@RequiredArgsConstructor
public class VoteController {
	
	private final VoteService voteService;
	
	@PostMapping("")
	public ResponseEntity<String> vote(@RequestBody VoteRequest request) {
		Long userId = request.getUserId();
		String billId = request.getBillId();
		VoteType voteType = request.getVoteType();
		
		voteService.vote(userId, billId, voteType);
		
		return ResponseEntity.ok("ok");
	}
	
	@GetMapping("")
	public ResponseEntity<VotedBillResponse> find(@RequestParam("userId") Long userId, @RequestParam("billId") String billId) {
		VotedBillResponse response = voteService.find(userId, billId);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/most")
	public ResponseEntity<List<VotedBillResponse>> findAllByMostVoted(
			@RequestParam(value = "from", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate from, 
			@RequestParam(value = "to", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate to, 
			@RequestParam(value = "max", required = false) Integer max) {
		List<VotedBillResponse> response = voteService.findAllByMostVoted(from, to, max);
		
		return ResponseEntity.ok(response);
	}
	
	
	// 자신이 어떤 투표를 했는지만 얻을 수 있을 뿐, 전체 통계를 얻을 수 없습니다.
	// 이를 위해서는 BillController의 findAllBillCheckingVoted를 확인해보세요.
	@Deprecated()
	@GetMapping("list/{userId}")
	public ResponseEntity<Page<VotedBillResponse>> list(@PathVariable("userId") Long userId, Pageable pageable) {
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Order.desc("createdAt")));
		
		Page<VotedBillResponse> response = voteService.findAllByUser(userId, pageable);
		
		return ResponseEntity.ok(response);
	}
	
}
