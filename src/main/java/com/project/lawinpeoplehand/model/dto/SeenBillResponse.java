package com.project.lawinpeoplehand.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class SeenBillResponse {
	
	private long jsonUID = 11111113L;

	private BillResponse bill;
	
	private Long totalCount;
}
