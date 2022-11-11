package com.project.lawinpeoplehand.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.lawinpeoplehand.model.User;
import com.project.lawinpeoplehand.model.Vote;
import com.project.lawinpeoplehand.model.dto.VotedBillResponse;


public interface VoteRepositoryExtension {
	List<VotedBillResponse> findAllByMostVoted(LocalDate from, LocalDate to, Integer max);
	VotedBillResponse find(Long userId, String billId);
}
