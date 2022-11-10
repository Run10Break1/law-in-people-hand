package com.project.lawinpeoplehand.repository.query;

import com.project.lawinpeoplehand.model.Bill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindAllByMostVotedQuery1 {
	private String billID;
	private Long totalCount;
	private Integer agreeCount;
	private Integer disagreeCount;
}
