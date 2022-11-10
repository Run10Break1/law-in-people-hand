package com.project.lawinpeoplehand.repository;

import java.time.LocalDate;
import java.util.List;

import com.project.lawinpeoplehand.model.dto.VotedBillResponse;


public interface VoteRepositoryExtension {
	List<VotedBillResponse> findAllByMostVoted(LocalDate from, LocalDate to, Integer max);
}
