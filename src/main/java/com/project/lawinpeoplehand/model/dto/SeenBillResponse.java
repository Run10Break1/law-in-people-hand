package com.project.lawinpeoplehand.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class SeenBillResponse {

	private BillResponse bill;
	
	private Long totalCount;
}
