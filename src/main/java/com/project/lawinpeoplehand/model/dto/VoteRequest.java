package com.project.lawinpeoplehand.model.dto;

import com.project.lawinpeoplehand.model.VoteType;

import lombok.Data;


@Data
public class VoteRequest {

	private Long userId;
	
	private String billId;
	
	private VoteType voteType;
}
