package com.project.lawinpeoplehand.model.dto;


import com.project.lawinpeoplehand.model.VoteType;

import lombok.Data;


@Data
public class VotedBillResponse {
	
	private long jsonUID = 11111112L;
	
	private BillResponse bill;
	private long totalCount;
	private int agreeCount = 0;
	private int disagreeCount = 0;
	private VoteType me;
	
}
