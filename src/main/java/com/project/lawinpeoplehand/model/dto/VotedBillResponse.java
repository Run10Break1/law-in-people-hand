package com.project.lawinpeoplehand.model.dto;


import com.project.lawinpeoplehand.model.VoteType;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class VotedBillResponse {

	private BillResponse bill;
	private long totalCount;
	private int agreeCount = 0;
	private int disagreeCount = 0;
	private VoteType me;
	
}
