package com.project.lawinpeoplehand.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.project.lawinpeoplehand.model.Bill;
import com.project.lawinpeoplehand.model.QBill;
import com.project.lawinpeoplehand.model.QVote;
import com.project.lawinpeoplehand.model.Vote;
import com.project.lawinpeoplehand.model.VoteType;
import com.project.lawinpeoplehand.model.dto.BillResponse;
import com.project.lawinpeoplehand.model.dto.VotedBillResponse;
import com.project.lawinpeoplehand.repository.query.FindAllByMostVotedQuery1;
import com.project.lawinpeoplehand.repository.query.FindAllByMostVotedQuery1;
import com.project.lawinpeoplehand.utils.TimeUtil;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPQLQuery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class VoteRepositoryExtensionImpl extends QuerydslRepositorySupport implements VoteRepositoryExtension {

	@PersistenceContext
	private EntityManager entityManager;
	
	public VoteRepositoryExtensionImpl() {
		super(Vote.class);
	}

	
	@Override
	public List<VotedBillResponse> findAllByMostVoted(LocalDate from, LocalDate to, Integer max) {
		QVote vote = QVote.vote;
		QBill bill = QBill.bill;
		
		NumberPath<Long> countAlias = Expressions.numberPath(Long.class, "totalCount");
		
		NumberExpression<Integer> agree = vote.voteType.
		        when(VoteType.AGREE).then(Integer.valueOf(1)).otherwise(Integer.valueOf(0));
		
		NumberExpression<Integer> disagree = vote.voteType.
		        when(VoteType.DISAGREE).then(Integer.valueOf(1)).otherwise(Integer.valueOf(0));
		
		JPQLQuery<FindAllByMostVotedQuery1> query1 = from(vote)
				.select(
						Projections.fields(FindAllByMostVotedQuery1.class,
								vote.bill.billID,
								vote.bill.count().as(countAlias),
								agree.sum().as("agreeCount"),
								disagree.sum().as("disagreeCount")
						))
				.groupBy(vote.bill)
				.orderBy(countAlias.desc());
				
		if(from != null) {
			query1.where(vote.createdAt.after(TimeUtil.asStartOfDay(from)));
		}
		if(to != null) {
			query1.where(vote.createdAt.before(TimeUtil.asEndOfDay(to)));
		}
		if(max != null) {
			query1.limit(max);
		}

		List<FindAllByMostVotedQuery1> query1Result = query1.fetch();
		
		List<String> billIdList = query1Result.stream().map(e -> e.getBillID()).collect(Collectors.toList());
		
		JPQLQuery<Bill> query2 = from(bill).select(bill).where(bill.billID.in(billIdList));
		List<Bill> query2Result = query2.fetch();
		
		List<VotedBillResponse> result = new ArrayList<>();
		int order = 0;
		for(FindAllByMostVotedQuery1 q : query1Result) {
			VotedBillResponse response = new VotedBillResponse();
			
			Bill b = null;
			for(Bill e : query2Result) {
				if(q.getBillID().contentEquals(e.getBillID())) {
					b = e;
					break;
				}
			}
			
			response.setBill(new BillResponse(b));
			response.setTotalCount(q.getTotalCount());
			response.setAgreeCount(q.getAgreeCount());
			response.setDisagreeCount(q.getDisagreeCount());
			
			result.add(response);
			
			order++;
		}
		return result;
	}
}