package com.project.lawinpeoplehand.repository.query;

import com.project.lawinpeoplehand.model.VoteType;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Find {
	
	private VoteType voteType;
	private Integer totalCount;
}
