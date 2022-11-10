package com.project.lawinpeoplehand.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class CongressmanCreateRequest {
	
	private String name;
	
	private String profileUrl;
}
