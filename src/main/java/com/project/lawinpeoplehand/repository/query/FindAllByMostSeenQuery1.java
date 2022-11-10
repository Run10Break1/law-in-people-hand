package com.project.lawinpeoplehand.repository.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindAllByMostSeenQuery1 {
	private String billId;
 	private Long totalCount;
}
