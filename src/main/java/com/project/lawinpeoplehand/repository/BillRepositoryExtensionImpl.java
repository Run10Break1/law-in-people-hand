package com.project.lawinpeoplehand.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.project.lawinpeoplehand.model.Bill;
import com.project.lawinpeoplehand.model.QBill;
import com.project.lawinpeoplehand.model.QVote;
import com.project.lawinpeoplehand.model.Vote;
import com.project.lawinpeoplehand.model.dto.BillResponse;
import com.project.lawinpeoplehand.model.dto.FilteredByViewBillResponse;
import com.project.lawinpeoplehand.model.dto.FilteredByVoteBillResponse;
import com.project.lawinpeoplehand.utils.TimeUtil;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;



public class BillRepositoryExtensionImpl extends QuerydslRepositorySupport implements BillRepositoryExtension {

	public BillRepositoryExtensionImpl() {
		super(Bill.class);
	}

	@Override
	public List<FilteredByViewBillResponse> findAllFilteredByViewed(LocalDate from, LocalDate to, Integer max) {
		QVote vote = QVote.vote;
		
		JPQLQuery<BillAndCount> query = from(vote)
				.select(
						Projections.fields(BillAndCount.class,
								vote.bill,
								vote.bill.count().as("count")
						))
				.groupBy(vote.bill)
				.orderBy(vote.bill.count().as("count").desc());
				
		if(from != null) {
			query.where(vote.viewAt.after(TimeUtil.asStartOfDay(from)));
		}
		if(to != null) {
			query.where(vote.viewAt.before(TimeUtil.asEndOfDay(to)));
		}
		if(max != null) {
			query.limit(max);
		}
		
		List<BillAndCount> queryResult = query.fetch();
		
		List<FilteredByViewBillResponse> result = new ArrayList<>();
		int order = 0;
		for(BillAndCount billAndCount : queryResult) {
			FilteredByViewBillResponse response = new FilteredByViewBillResponse();
			
			response.setResponse(new BillResponse(billAndCount.bill));
			response.setFilterName("view");
			response.setOrder(order);
			response.setValue(billAndCount.count);
			
			result.add(response);
			
			order++;
		}
		return result;
	}

	@Override
	public List<FilteredByVoteBillResponse> findAllFilteredByVoted(LocalDate from, LocalDate to, Integer max) {
		QVote vote = QVote.vote;
		
		JPQLQuery<BillAndCount> query = from(vote)
				.select(
						Projections.fields(BillAndCount.class,
								vote.bill,
								vote.bill.count().as("count")
						))
				.where(vote.voteType.isNotNull())
				.groupBy(vote.bill)
				.orderBy(vote.bill.count().as("count").desc());
				
		if(from != null) {
			query.where(vote.viewAt.after(TimeUtil.asStartOfDay(from)));
		}
		if(to != null) {
			query.where(vote.viewAt.before(TimeUtil.asEndOfDay(to)));
		}
		if(max != null) {
			query.limit(max);
		}
		
		List<BillAndCount> queryResult = query.fetch();
		
		List<FilteredByVoteBillResponse> result = new ArrayList<>();
		int order = 0;
		for(BillAndCount billAndCount : queryResult) {
			FilteredByVoteBillResponse response = new FilteredByVoteBillResponse();
			
			response.setResponse(new BillResponse(billAndCount.bill));
			response.setFilterName("vote");
			response.setOrder(order);
			response.setValue(billAndCount.count);
			
			result.add(response);
			
			order++;
		}
		return result;
	}

	@Override
	public Page<Bill> findAllByUserId(Long id, Pageable pageable) {
		QVote vote = QVote.vote;
		
		JPQLQuery<Bill> query = from(vote)
				.select(vote.bill)
				.where(vote.user.id.eq(id));
		
		long totalCount = query.fetchCount();
		List<Bill> queryResult = getQuerydsl().applyPagination(pageable, query).fetch();
		
		return new PageImpl<>(queryResult, pageable, totalCount);
	}

}

class BillAndCount {
	final Bill bill;
	final Integer count;
	
	BillAndCount(Bill bill, Integer count) {
		this.bill = bill;
		this.count = count;
	}
}
