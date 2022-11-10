package com.project.lawinpeoplehand.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.lawinpeoplehand.model.Bill;
import com.project.lawinpeoplehand.model.dto.BillResponse;
import com.project.lawinpeoplehand.repository.BillRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class BillService {

	private final BillRepository billRepository;

	public Page<BillResponse> findAll(Pageable pageable) {
		
		Page<Bill> billPage = billRepository.findAll(pageable);
		
		List<BillResponse> content = billPage.getContent()
				.stream()
				.map(e -> new BillResponse(e))
				.collect(Collectors.toList());
		long total = billPage.getTotalElements();
		
		return new PageImpl<BillResponse>(content, pageable, total);
	}
}
