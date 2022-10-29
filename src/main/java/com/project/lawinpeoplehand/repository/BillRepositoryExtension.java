package com.project.lawinpeoplehand.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.lawinpeoplehand.model.Bill;
import com.project.lawinpeoplehand.model.dto.FilteredByViewBillResponse;
import com.project.lawinpeoplehand.model.dto.FilteredByVoteBillResponse;


public interface BillRepositoryExtension {
	List<FilteredByViewBillResponse> findAllFilteredByViewed(LocalDate from, LocalDate to, Integer max);
	List<FilteredByVoteBillResponse> findAllFilteredByVoted(LocalDate from, LocalDate to, Integer max);
	Page<Bill> findAllByUserId(Long id, Pageable pageable);
}
