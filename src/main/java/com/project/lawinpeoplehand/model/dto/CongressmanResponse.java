package com.project.lawinpeoplehand.model.dto;

import com.project.lawinpeoplehand.model.Congressman;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class CongressmanResponse {
	
	private Long id;
	
	private String name;
	
	private String profileUrl;
	
	public CongressmanResponse(Congressman entity) {
		id = entity.getId();
		name = entity.getName();
		profileUrl = entity.getProfileUrl();
	}
}
