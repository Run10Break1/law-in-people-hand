package com.project.lawinpeoplehand.repository.query;

import com.project.lawinpeoplehand.model.Bill;
import com.project.lawinpeoplehand.model.VoteType;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class FindAllByUser {
	
	private Bill bill;
	private int agreeCount = 0;
	private int disagreeCount = 0;
	private VoteType me;
}
