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
import com.project.lawinpeoplehand.model.dto.BillResponseWithKeywords;
import com.project.lawinpeoplehand.model.dto.VotedBillResponse;
import com.project.lawinpeoplehand.repository.query.Find;
import com.project.lawinpeoplehand.repository.query.FindAllByMostVoted;
import com.project.lawinpeoplehand.utils.TimeUtil;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPQLQuery;


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
		
		JPQLQuery<FindAllByMostVoted> query1 = from(vote)
				.select(
						Projections.fields(FindAllByMostVoted.class,
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

		List<FindAllByMostVoted> query1Result = query1.fetch();
		
		List<String> billIdList = query1Result.stream().map(e -> e.getBillID()).collect(Collectors.toList());
		
		JPQLQuery<Bill> query2 = from(bill).select(bill).where(bill.billID.in(billIdList));
		List<Bill> query2Result = query2.fetch();
		
		List<VotedBillResponse> result = new ArrayList<>();
		int order = 0;
		for(FindAllByMostVoted q : query1Result) {
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


	@Override
	public VotedBillResponse find(Long userId, String billId) {
		QVote vote = QVote.vote;
		QBill bill = QBill.bill;
		
		JPQLQuery<Find> query1 = from(vote)
				.select(Projections.fields(Find.class,
						vote.voteType,
						vote.voteType.count().intValue().as("totalCount")
				))
				.groupBy(vote.voteType)
				.where(vote.bill.billID.eq(billId));
				
		List<Find> result1 = query1.fetch();
		int agreeCount = 0, disagreeCount = 0;
		for(Find find : result1) {
			switch(find.getVoteType()) {
			case AGREE:
				agreeCount = find.getTotalCount();
				break;
			case DISAGREE:
				disagreeCount = find.getTotalCount();
				break;
			}
		}
		
		JPQLQuery<Bill> query2 = from(bill)
				.select(bill)
				.where(bill.billID.eq(billId));
		Bill result2 = query2.fetchFirst();
		
		JPQLQuery<VoteType> query3 = from(vote)
				.select(vote.voteType)
				.where(vote.bill.billID.eq(billId).and(vote.user.id.eq(userId)));
		VoteType result3 = query3.fetchFirst();
		
		VotedBillResponse response = new VotedBillResponse();
		response.setAgreeCount(agreeCount);
		response.setDisagreeCount(disagreeCount);
		response.setTotalCount(agreeCount + disagreeCount);
		response.setBill(new BillResponseWithKeywords(result2));
		response.setMe(result3);
		
		return response;
	}
	
	
}