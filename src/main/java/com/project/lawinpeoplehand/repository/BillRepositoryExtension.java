package com.project.lawinpeoplehand.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.lawinpeoplehand.model.dto.SeenBillResponse;
import com.project.lawinpeoplehand.model.dto.VotedBillResponse;

public interface BillRepositoryExtension {
	Page<SeenBillResponse> findAllBillCheckingSeen(Pageable pageable, Long userId);
	Page<VotedBillResponse> findAllBillCheckingVoted(Long userId, Pageable pageable);
}
