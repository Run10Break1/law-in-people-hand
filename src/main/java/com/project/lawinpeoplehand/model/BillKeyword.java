package com.project.lawinpeoplehand.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
public class BillKeyword {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn
	private Bill bill;
	
	@ManyToOne
	@JoinColumn
	private Keyword keyword;

	
	
	public BillKeyword(Bill bill, Keyword keyword) {
		this.bill = bill;
		this.keyword = keyword;
	}
}
