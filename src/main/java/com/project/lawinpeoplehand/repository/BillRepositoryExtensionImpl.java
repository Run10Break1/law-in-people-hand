package com.project.lawinpeoplehand.repository;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.project.lawinpeoplehand.model.Bill;



public class BillRepositoryExtensionImpl extends QuerydslRepositorySupport implements BillRepositoryExtension {

	public BillRepositoryExtensionImpl() {
		super(Bill.class);
	}

}

class BillIdAndCount {
	final String billId;
 	final Long count;
	
	public BillIdAndCount(String billId, Long count) {
		this.billId = billId;
		this.count = count;
	}
}