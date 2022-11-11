package com.project.lawinpeoplehand.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.project.lawinpeoplehand.model.Bill;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class BillResponseWithKeywords extends BillResponse {

	private List<String> keywords;
	
	public BillResponseWithKeywords(Bill bill) {
		super(bill);
		keywords = bill.getBillKeywordList().stream().map(e -> {
			return e.getKeyword().getId();
		}).collect(Collectors.toList());
	}
	
}
