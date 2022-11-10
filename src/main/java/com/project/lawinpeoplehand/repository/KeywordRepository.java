package com.project.lawinpeoplehand.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.lawinpeoplehand.model.Keyword;


public interface KeywordRepository extends JpaRepository<Keyword, String> {
	
}
