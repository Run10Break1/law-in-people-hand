package com.project.lawinpeoplehand.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.project.lawinpeoplehand.model.Bill;
import com.project.lawinpeoplehand.model.QBill;
import com.project.lawinpeoplehand.model.QSee;
import com.project.lawinpeoplehand.model.See;
import com.project.lawinpeoplehand.model.dto.BillResponse;
import com.project.lawinpeoplehand.model.dto.SeenBillResponse;
import com.project.lawinpeoplehand.repository.query.FindAllByMostSeenQuery1;
import com.project.lawinpeoplehand.utils.TimeUtil;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPQLQuery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class SeeRepositoryExtensionImpl extends QuerydslRepositorySupport implements SeeRepositoryExtension {

	@PersistenceContext
	private EntityManager entityManager;
	
	public SeeRepositoryExtensionImpl() {
		super(See.class);
	}
	
	@Override
	public List<SeenBillResponse> findAllByMostSeen(LocalDate from, LocalDate to, Integer max) {
//		String jpql = String.format(
//				"select NEW com.project.lawinpeoplehand.repository.BillAndCount(s.bill, count(*) as cnt) from See s where %s AND %s group by s.bill %s", 
//				from != null ? String.format("s.createdAt >= '%s'", TimeUtil.toString(TimeUtil.asStartOfDay(from))) : "1 = 1",
//				to != null ? String.format("s.createdAt <= '%s'", TimeUtil.toString(TimeUtil.asStartOfDay(to))) : "1 = 1",
//				max != null ? String.format("limit %d", max) : ""
//		);
//		
//		System.out.println("jpql : " + jpql);
//		
//		TypedQuery<BillAndCount> query1 = entityManager.createQuery(jpql, BillAndCount.class);
//	
//		
//		List<BillAndCount> query1Result = query1.getResultList();
		
		QSee see = QSee.see;
		QBill bill = QBill.bill;
		
		NumberPath<Long> countAlias = Expressions.numberPath(Long.class, "totalCount");
		
		JPQLQuery<FindAllByMostSeenQuery1> query1 = from(see)
				.select(
						Projections.fields(FindAllByMostSeenQuery1.class,
								see.bill.billID.as("billId"),
								see.bill.count().as(countAlias)
						))
				.groupBy(see.bill)
				.orderBy(countAlias.desc());
				
		if(from != null) {
			query1.where(see.createdAt.after(TimeUtil.asStartOfDay(from)));
		}
		if(to != null) {
			query1.where(see.createdAt.before(TimeUtil.asEndOfDay(to)));
		}
		if(max != null) {
			query1.limit(max);
		}
		
		List<FindAllByMostSeenQuery1> query1Result = query1.fetch();
		System.out.println(query1Result.toString());
		List<String> billIdList = query1Result.stream().map(e -> e.getBillId()).collect(Collectors.toList());
		
		JPQLQuery<Bill> query2 = from(bill).select(bill).where(bill.billID.in(billIdList));
		List<Bill> query2Result = query2.fetch();	
		
		List<SeenBillResponse> result = new ArrayList<>();
		int order = 0;
		for(FindAllByMostSeenQuery1 q : query1Result) {
			SeenBillResponse response = new SeenBillResponse();
			
			Bill b = null;
			for(Bill e : query2Result) {
				if(q.getBillId().contentEquals(e.getBillID())) {
					b = e;
					break;
				}
			}
			
			response.setBill(new BillResponse(b));
			response.setTotalCount(q.getTotalCount());
			
			result.add(response);
			
			order++;
		}

		return result;
	}
}