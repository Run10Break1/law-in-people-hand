package com.example.springboot.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Bill;
import com.example.springboot.model.ProcessStage;
import com.example.springboot.repository.BillRepository;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class AddProcessStageService {
	
	private final BillRepository billRepository;
	
	@Async
	public void addRemainProcessStage(int startPage, Integer endPage) throws IOException {
		
		final int pageSize = 100;
		
		long before, after;
		
		for(int i = startPage; endPage == null ? true : i <= endPage; i++) {
			before = System.currentTimeMillis();
			
			Pageable pageable = PageRequest.of(i, pageSize);
			
			Page<Bill> billPage = billRepository.findAllByStageNotNull(pageable);
			if(!billPage.hasContent()) {
				System.out.println(String.format("%d 페이지에서 더 이상 bill가 존재하지 않습니다.", i));
				return;
			}

			List<Bill> billList = billPage.getContent();
			
			for(Bill bill : billList) {
				if(bill.getStage() != null) continue;
				
				ProcessStage processStage = parseHTML(bill.getUrl());
				bill.setStage(processStage);
			}
			
			billRepository.saveAll(billList);
			
			after = System.currentTimeMillis();
			long executeTime = (after - before) / 1000;
			
			System.out.println(String.format("완료된 페이지 : %d/%d, bill 개수 : %d, 걸린 시간 : %ds", i, endPage, billPage.getSize(), executeTime));
		}
	}
	
	@Async
	public void addProcessStage(int startPage, Integer endPage, int depth) throws IOException {
		if(depth == 6) {
			System.out.println(String.format("[#] 마지막까지 실패한 페이지 : %d", startPage));
			return;
		}
		
		final int pageSize = 100;
		
		long before, after;
		
		List<Integer> lessExecuteTimePageList = new ArrayList<>();
		
		for(int i = startPage; endPage == null ? true : i <= endPage; i++) {
			before = System.currentTimeMillis();
			
			Pageable pageable = PageRequest.of(i, pageSize);
			
			Page<Bill> billPage = billRepository.findAll(pageable);
			if(!billPage.hasContent()) {
				System.out.println(String.format("%d 페이지에서 더 이상 bill가 존재하지 않습니다.", i));
				return;
			}

			List<Bill> billList = billPage.getContent();
			
			int continueCount = 0;
			for(Bill bill : billList) {
//				if(bill.getStage() != null) {
//					continueCount++;
//					continue;
//				}
				
				ProcessStage processStage = parseHTML(bill.getUrl());
				bill.setStage(processStage);
			}
			
			billRepository.saveAll(billList);
			
			after = System.currentTimeMillis();
			long executeTime = (after - before) / 1000;
			if(executeTime < (10 - continueCount * 0.07)) {
				lessExecuteTimePageList.add(i);
			}
			
			System.out.println(String.format("완료된 페이지 : %d/%d, bill 개수 : %d, 걸린 시간 : %ds", i, endPage, billPage.getSize(), executeTime));
		}
		
		if(lessExecuteTimePageList.isEmpty()) {
			return;
		}
		
		System.out.println(String.format("[depth-%d] 걸린 시간이 기대보다 작은 페이지들 : %s", depth, lessExecuteTimePageList.toString()));
		
		try {
			Thread.sleep(1020 * depth);
		} catch (InterruptedException e) {
			System.out.println(e);
		}
		for(int lessExecuteTimePage : lessExecuteTimePageList) {
			addProcessStage(lessExecuteTimePage, lessExecuteTimePage, depth + 1);
		}
	}
	
	private ProcessStage parseHTML(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		
		Element wrapElem = doc.selectFirst("body > div > div.contentWrap > div.subContents > div > div.boxType01 > div");
		if(wrapElem == null) {
			System.out.println("심사진행단계 요소를 찾을 수 없습니다.");
			return null;
		}
		
		Element onElem = wrapElem.selectFirst("span.on");
		if(onElem == null) {
			System.out.println("현재진행단계 요소를 찾을 수 없습니다.");
			return null;
		}
		
		String korName = onElem.text();
		ProcessStage ps = ProcessStage.get(korName);
		if(ps == null) {
			System.out.println(String.format("%s를 url로 가지는 bill에 대해 stage를 결정할 수 없습니다. korName : %s", url, korName));
		}
		
		return ps;
	}
}
