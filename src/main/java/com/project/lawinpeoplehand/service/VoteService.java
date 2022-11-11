package com.project.lawinpeoplehand.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.lawinpeoplehand.model.Bill;
import com.project.lawinpeoplehand.model.User;
import com.project.lawinpeoplehand.model.Vote;
import com.project.lawinpeoplehand.model.VoteType;
import com.project.lawinpeoplehand.model.dto.BillResponse;
import com.project.lawinpeoplehand.model.dto.VotedBillResponse;
import com.project.lawinpeoplehand.repository.BillRepository;
import com.project.lawinpeoplehand.repository.UserRepository;
import com.project.lawinpeoplehand.repository.VoteRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class VoteService {
	
	private final UserRepository userRepository;
	private final VoteRepository voteRepository;
	private final BillRepository billRepository;
	
	public void vote(Long userId, String billId, VoteType voteType) {
		
		User user = userRepository.findById(userId).get();
		Bill bill = billRepository.findById(billId).get();
		
		if(voteRepository.findFirstByUserAndBill(user, bill).isPresent()) {
			throw new RuntimeException(String.format("사용자(%d)는 같은 법안(%s)에 대해 중복하여 투표할 수 없습니다.", userId, billId));
		}
		
		Vote vote = new Vote();
		vote.setUser(user);
		vote.setBill(bill);
		vote.setVoteType(voteType);
		
		voteRepository.save(vote);
	}
	
	public List<VotedBillResponse> findAllByMostVoted(LocalDate from, LocalDate to, Integer max) {
		return voteRepository.findAllByMostVoted(from, to, max);
	}
	
	public VotedBillResponse find(Long userId, String billId) {
		return voteRepository.find(userId, billId);
	}
	
	@Deprecated()
	// pageable의 sort를 제대로 이용할 수 없음; bill속성으로 sort 불가능
	public Page<VotedBillResponse> findAllByUser(Long userId, Pageable pageable) {
	
		User user = userRepository.findById(userId).get();
		
		Page<Vote> votePage = voteRepository.findAllByUser(user, pageable);
		// 'vote+사용자'는 하나의 bill에 대응되므로 Page<Vote>는 Page<Bill>에 대응된다고 할 수 있습니다.
		
		List<VotedBillResponse> content = votePage.getContent().stream().map(e -> {
			VotedBillResponse response = new VotedBillResponse();
			
			response.setBill(new BillResponse(e.getBill()));
			response.setMe(e.getVoteType());
			
			return response;
		}).collect(Collectors.toList());
		
		return new PageImpl<>(content, votePage.getPageable(), votePage.getTotalElements());
	}
}
