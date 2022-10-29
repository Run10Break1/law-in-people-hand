package com.project.lawinpeoplehand.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.lawinpeoplehand.repository.BillRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class BillService {

	private final BillRepository billRepository;
	
	
}
