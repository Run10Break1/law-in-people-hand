package com.project.lawinpeoplehand.model.dto;

import lombok.Data;


@Data
public class FilteredByResponse<T, V> {
	
	private T response;
	
	private String filterName;
	
	private int order;
	
	private V value;
}
