package com.project.lawinpeoplehand.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
public class Keyword {
	
	@Id
	private String id;
	
	@OneToMany(mappedBy = "keyword", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BillKeyword> billKeywordList = new ArrayList<>();
	
	
	public Keyword(String keyword) {
		this.id = keyword;
	}

}
