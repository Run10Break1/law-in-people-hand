package com.project.lawinpeoplehand.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.project.lawinpeoplehand.model.Bill;
import com.project.lawinpeoplehand.model.QBill;
import com.project.lawinpeoplehand.model.QSee;
import com.project.lawinpeoplehand.model.QUser;
import com.project.lawinpeoplehand.model.QVote;
import com.project.lawinpeoplehand.model.VoteType;
import com.project.lawinpeoplehand.model.dto.BillResponse;
import com.project.lawinpeoplehand.model.dto.SeenBillResponse;
import com.project.lawinpeoplehand.model.dto.VotedBillResponse;
import com.project.lawinpeoplehand.repository.query.FindAllByUser;
import com.project.lawinpeoplehand.repository.query.FindBillCheckingSeen;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;



public class BillRepositoryExtensionImpl extends QuerydslRepositorySupport implements BillRepositoryExtension {

	public BillRepositoryExtensionImpl() {
		super(Bill.class);
	}

	@Override
	public Page<SeenBillResponse> findAllBillCheckingSeen(Pageable pageable, Long userId) {
		QSee see = QSee.see;
		QBill bill = QBill.bill;

		JPQLQuery<FindBillCheckingSeen> query = from(bill).leftJoin(see)
				.on(bill.eq(see.bill))
				.select(
						Projections.fields(FindBillCheckingSeen.class,
								bill,
								see.user.id.as("userId")
				))
				.where(see.user.id.isNull().or(see.user.id.eq(userId)));
		
		long totalCount = query.fetchCount();
		List<FindBillCheckingSeen> result = getQuerydsl().applyPagination(pageable, query).fetch();
		List<SeenBillResponse> mappedResult = result.stream().map(e -> {
			SeenBillResponse response = new SeenBillResponse();
			
			response.setBill(new BillResponse(e.getBill()));
			response.setTotalCount(e.getUserId() == null ? 0L : 1L);
			
			return response;
		}).collect(Collectors.toList());
		
		return new PageImpl<>(mappedResult, pageable, totalCount);
	}
	
	@Override
	public Page<VotedBillResponse> findAllBillCheckingVoted(Long userId, Pageable pageable) {
		QVote vote1 = new QVote("vote1");
		QVote vote2 = new QVote("vote2");
		QBill bill = QBill.bill;

		JPQLQuery<FindAllByUser> query = from(vote1, bill)
				.select(Projections.fields(FindAllByUser.class,
						bill,
						ExpressionUtils.as(
                                from(vote2)
                                .select(vote2.count().intValue())
                                .where(vote2.bill.eq(vote1.bill).and(vote2.voteType.eq(VoteType.AGREE))),
                                "agreeCount"
						),
						ExpressionUtils.as(
                                from(vote2)
                                .select(vote2.count().intValue())
                                .where(vote2.bill.eq(vote1.bill).and(vote2.voteType.eq(VoteType.DISAGREE))),
                                "disagreeCount"
						),
						vote1.voteType.as("me")
				))
				.where(vote1.user.id.eq(userId).and(vote1.bill.eq(bill)));
		
		long totalCount = query.fetchCount();
		List<FindAllByUser> result = getQuerydsl().applyPagination(pageable, query).fetch();
		List<VotedBillResponse> mappedResult = result.stream().map(e -> {
			VotedBillResponse response = new VotedBillResponse();
			
			response.setBill(new BillResponse(e.getBill()));
			response.setAgreeCount(e.getAgreeCount());
			response.setDisagreeCount(e.getDisagreeCount());
			response.setTotalCount(e.getAgreeCount() + e.getDisagreeCount());
			response.setMe(e.getMe());
			
			return response;
		}).collect(Collectors.toList());
				
		return new PageImpl<>(mappedResult, pageable, totalCount);
	}
}

class BillIdAndCount {
	final String billId;
 	final Long count;
	
	public BillIdAndCount(String billId, Long count) {
		this.billId = billId;
		this.count = count;
	}
}