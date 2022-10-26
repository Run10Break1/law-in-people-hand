package com.example.springboot.service;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
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
	public void addProcessStage(Pageable pageable) throws IOException {
		
		List<Bill> billList = billRepository.findAll(pageable);
		
		for(Bill bill : billList) {
			ProcessStage processStage = parseHTML(bill.getUrl());
			bill.setStage(processStage);
		}
		
		billRepository.saveAll(billList);
	}
	
	ProcessStage parseHTML(String url) throws IOException {
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
		return ProcessStage.get(korName);
	}
}
