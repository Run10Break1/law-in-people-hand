package com.project.lawinpeoplehand.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.lawinpeoplehand.model.dto.SeenBillResponse;

public interface SeeRepositoryExtension {
	List<SeenBillResponse> findAllByMostSeen(LocalDate from, LocalDate to, Integer max);
}
