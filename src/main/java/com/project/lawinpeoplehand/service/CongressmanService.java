package com.project.lawinpeoplehand.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.lawinpeoplehand.model.Congressman;
import com.project.lawinpeoplehand.model.dto.CongressmanCreateRequest;
import com.project.lawinpeoplehand.repository.CongressmanRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CongressmanService {

	private final CongressmanRepository congressmanRepository;
	
	public int create(List<CongressmanCreateRequest> requestList) {
		
		List<Congressman> manList = new ArrayList<>();
		
		for(CongressmanCreateRequest request : requestList) {
			Congressman man = new Congressman();
			
			man.setName(request.getName());
			man.setProfileUrl(request.getProfileUrl());
			
			manList.add(man);
		}
		
		int createdSize = congressmanRepository.saveAll(manList).size();
		return createdSize;
	}
}
