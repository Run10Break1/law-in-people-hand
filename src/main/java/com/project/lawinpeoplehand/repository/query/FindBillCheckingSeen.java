package com.project.lawinpeoplehand.repository.query;

import com.project.lawinpeoplehand.model.Bill;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class FindBillCheckingSeen {
	
	private Bill bill;
	
	private Long userId;
}
