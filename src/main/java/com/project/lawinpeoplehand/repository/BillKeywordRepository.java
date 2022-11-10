package com.project.lawinpeoplehand.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.lawinpeoplehand.model.Bill;
import com.project.lawinpeoplehand.model.BillKeyword;
import com.project.lawinpeoplehand.model.Keyword;


public interface BillKeywordRepository extends JpaRepository<BillKeyword, Long> {
	
	Optional<BillKeyword> findFirstByBillAndKeyword(Bill bill, Keyword keyword);
}
