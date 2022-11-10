package com.project.lawinpeoplehand.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.lawinpeoplehand.model.Bill;
import com.project.lawinpeoplehand.model.BillKeyword;
import com.project.lawinpeoplehand.model.Keyword;
import com.project.lawinpeoplehand.model.ProcessStage;
import com.project.lawinpeoplehand.repository.BillKeywordRepository;
import com.project.lawinpeoplehand.repository.BillRepository;
import com.project.lawinpeoplehand.repository.KeywordRepository;

import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class MigrationService {
	
	private final BillRepository billRepository;
	private final BillKeywordRepository billKeywordRepository;
	private final KeywordRepository keywordRepository;
	private final Komoran komoran;
	private final HanjaToHangleService hanjaToHangleService;
	
	
	// ProcessStage 저장하기
	@Async
	public void addRemainProcessStage(int startPage, Integer endPage) throws IOException {
		
		final int pageSize = 100;
		
		long before, after;
		
		for(int i = startPage; endPage == null ? true : i <= endPage; i++) {
			before = System.currentTimeMillis();
			
			Pageable pageable = PageRequest.of(i, pageSize);
			
			Page<Bill> billPage = billRepository.findAllByStageNull(pageable);
			if(!billPage.hasContent()) {
				System.out.println(String.format("%d 페이지에서 더 이상 bill가 존재하지 않습니다.", i));
				return;
			}

			List<Bill> billList = billPage.getContent();
			
			for(Bill bill : billList) {
				if(bill.getStage() != null) continue;
				
				ProcessStage processStage = parseHTMLForProcessStage(bill.getUrl());
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
				
				ProcessStage processStage = parseHTMLForProcessStage(bill.getUrl());
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
	
	private ProcessStage parseHTMLForProcessStage(String url) throws IOException {
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

	
	// Overview 저장하기
	@Async
	public void addOverview(int startPage, Integer endPage, int depth) throws IOException {
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
			
			Page<Bill> billPage = billRepository.findAllByOverviewNull(pageable);
			if(!billPage.hasContent()) {
				System.out.println(String.format("%d 페이지에서 더 이상 bill가 존재하지 않습니다.", i));
				return;
			}

			List<Bill> billList = billPage.getContent();
			
			for(Bill bill : billList) {
				String overview = parseHTMLForOverview(bill.getUrl());
				if(overview == null) continue;
				
				bill.setOverview(overview);
			}
			
			billRepository.saveAll(billList);
			
			after = System.currentTimeMillis();
			long executeTime = (after - before) / 1000;
			if(executeTime < 10) {
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

	private String parseHTMLForOverview(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		
		Element elem = doc.selectFirst("#summaryContentDiv");
		
		if(elem == null) {
			System.out.println(String.format("제안이유 및 주요내용 요소를 찾을 수 없습니다. url : %s", url));
			return null;
		}
		
		String overview = elem.text();
		return overview;
	}

	// Keyword 저장하기
	//@Async
	public void addKeyword(int startPage, Integer endPage) {
		
		final int pageSize = 100;
		
		for(int i = startPage; endPage == null ? true : i <= endPage; i++) {
			
			Pageable pageable = PageRequest.of(i, pageSize, Sort.by("billID"));
			
			Page<Bill> billPage = billRepository.findAllByOverviewNotNull(pageable);
			if(!billPage.hasContent()) {
				System.out.println(String.format("%d 페이지에서 더 이상 bill가 존재하지 않습니다.", i));
				return;
			}

			List<Bill> billList = billPage.getContent();
			
			for(Bill bill : billList) {
				
				String overview = bill.getOverview();
				if(hanjaToHangleService.containHanja(overview)) {
					List<HanjaOrHangle> words = hanjaToHangleService.split(overview);
					
					StringBuilder stringBuilder = new StringBuilder();
					for(HanjaOrHangle word : words) {
						try {
							stringBuilder.append(word.isHanja ? hanjaToHangleService.toHangle(word.string) : word.string);
						} catch (UnsupportedEncodingException e) {
							System.out.println(String.format("한자를 파싱하는데 오류 발생 url : %s", bill.getUrl()));
							return;
						}
					}
					
					overview = stringBuilder.toString();
				}
				
				KomoranResult analyzeResultList = komoran.analyze(overview);
				
				List<String> words = getWords(analyzeResultList);
				
				//System.out.println(String.format("%s, url : %s", words.toString(), bill.getUrl()));

				for(String word : words) {
					Keyword keyword = new Keyword(word);
					
					if(keywordRepository.findById(keyword.getId()).isEmpty()) {
						keywordRepository.save(keyword);
					}
					
					Optional<BillKeyword> optional = billKeywordRepository.findFirstByBillAndKeyword(bill, keyword);
					if(optional.isPresent()) {
						continue;
					}
				
					BillKeyword billKeyword = new BillKeyword(bill, keyword);
					billKeywordRepository.save(billKeyword);
				}
			}
		}
		
		System.out.println("----------------------------------");
	}
	
	
	
	private List<String> getWords(KomoranResult analyzeResultList) {
		List<String> nouns = analyzeResultList.getNouns();
		
		Map<String, Integer> map = new HashMap<>();
		
		for(String noun : nouns) {
			if(!map.containsKey(noun)) {
				map.put(noun, 0);
			}
			
			map.put(noun, map.get(noun) + 1);
		}
		
		List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<>() {
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return -(o1.getValue() - o2.getValue());
			}
		});
		
		if(list.isEmpty()) {
			throw new RuntimeException("source에 단어들이 존재하지 않습니다.");
		}
		
		if(list.size() > 5 && list.get(4).getValue() >= 4) {
			list = list.stream().filter((e) -> e.getValue() >= 4).collect(Collectors.toList());
		} else {
			list = list.subList(0, Math.min(5, list.size()));
		}
		
		int score = 0;
		List<String> words = new ArrayList<>();
		for(var e : list) {
			score += e.getValue();
			words.add(e.getKey());
		}
		
		return words;
	}
	
	
	@Async
	public void migrate(int startPage, Integer endPage) throws Exception {
		
		Request.RequestBuilder requestBuilder = new Request.RequestBuilder();

		Response response;
		do {
			Request request = requestBuilder.pageIndex(startPage).build();
			response = getFromSource(request);
			sendToTarget(response.billList);
			
			System.out.println(String.format("완료된 페이지 : %d/%d, bill 개수 : %d", startPage, endPage, response.count));
			
			startPage++;
		} while(response.count != 0 && (endPage != null ? (startPage <= endPage) : true));
	}
	
	void sendToTarget(List<Bill> billList) {
		billRepository.saveAll(billList);
	}
	
	Response getFromSource(Request requestParam) throws IOException, URISyntaxException, InterruptedException {
		final Request request = requestParam != null ? requestParam : new Request.RequestBuilder().build();
		
		HttpRequest httpRequest = HttpRequest.newBuilder(request.toURI()).GET().build();
		HttpClient client = HttpClient.newBuilder().build();
		
		System.out.println(httpRequest.uri());
		
		HttpResponse<String> response = client.send(httpRequest, BodyHandlers.ofString());
		
		return parseJson(response.body(), request.getPageSize());
	}
	
	private Response parseJson(String json, int maxCount) throws JsonProcessingException, IllegalArgumentException {
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
		
		JsonNode root = mapper.readTree(json);
		
		if(root.has("RESULT")) {
			// 해당하는 데이터가 없는 경우
			return new Response(0, List.of());
		} else {
			 JsonNode arr = root.get("TVBPMBILL11");
			 
			 JsonNode meta = arr.get(0);
			 JsonNode data = arr.get(1);
			 
			 // 공공데이터 API에서 일단 조건에 맞는 데이터를 모두 가져온 후(이 값은 list_total_count에 저장됨), limit를 거는 것으로 파악됨
			 int count = Math.min(maxCount, meta.get("head").get(0).get("list_total_count").asInt());
			 
			 List<Bill> billList = new ArrayList<>(count);
			 JsonNode row = data.get("row");
			 for(JsonNode billJsonNode : row) {
				 Bill bill = mapper.treeToValue(billJsonNode, Bill.class);
				 billList.add(bill);
			 }
			 return new Response(count, billList);
		}
	}
	
}

class Response {
	final int count;
	final List<Bill> billList;
	
	Response(int count, List<Bill> billList) {
		this.count = count;
		this.billList = billList;
	};
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("count : " + count);
		for(Bill bill : billList) {
			sb.append("\n\t" + bill.toString());
		}
		return sb.toString();
	}
}


@Data
class Request {
	private final static ObjectMapper mapper = new ObjectMapper();
	private final static String KEY = "d83074a6969742279ca72e2e36fb56d2";
	@JsonProperty("Key")
	private final String key;
	
	// 필수
	@JsonProperty("Type")
	private final String type;
	@JsonProperty("pIndex")
	private final int pageIndex;
	@JsonProperty("pSize")
	private final int pageSize;
	// 선택
	@JsonProperty("BILL_ID")
	private final String billId;
	@JsonProperty("BILL_NO")
	private final String billNo;
	@JsonProperty("BILL_NAME")
	private final String billName;
	@JsonProperty("PROPOSER_KIND")
	private final String proposerKind;
	@JsonProperty("CURR_COMMITTEE_ID")
	private final String currCommitteeId;
	@JsonProperty("CURR_COMMITTEE")
	private final String currCommittee;
	@JsonProperty("PROC_RESULT_CD")
	private final String procResultCode;
	@JsonProperty("PROC_DT")
	private final LocalDate procDate;
	
	@Builder
	public Request(String type, Integer pageIndex, Integer pageSize, String billId, String billNo, String billName,
			String proposerKind, String currCommitteeId, String currCommittee, String procResultCode,
			LocalDate procDate) {
		this.key = KEY;
		this.type = type != null ? type : "json";
		this.pageIndex = pageIndex != null ? pageIndex : 1;
		this.pageSize = pageSize != null ? pageSize : 100;
		this.billId = billId;
		this.billNo = billNo;
		this.billName = billName;
		this.proposerKind = proposerKind;
		this.currCommitteeId = currCommitteeId;
		this.currCommittee = currCommittee;
		this.procResultCode = procResultCode;
		this.procDate = procDate;
	}
	
	private String query() {
		
		Map<String, Object> map = mapper.convertValue(this, Map.class);
		
		List<String> arr = new ArrayList<>();
		for(Map.Entry<String, Object> entry : map.entrySet()) {
			if(entry.getValue() == null) continue;
			
			arr.add(entry.getKey() + "=" + entry.getValue());
		}
		
		return String.join("&", arr);
	}
	
	public URI toURI() throws URISyntaxException {
		return new URI("https", "open.assembly.go.kr", "/portal/openapi/TVBPMBILL11", query(), null);
	}
	
//	public Request copyWith(String KEY, String type, Integer pageIndex, Integer pageSize, String billId, String billNo, String billName,
//			String proposerKind, String currCommitteeId, String currCommittee, String procResultCode,
//			LocalDate procDate) {
//		return new Request(
//				type != null ? type : this.type,
//				pageIndex != null ? pageIndex : this.pageIndex,
//				pageSize != null ? pageSize : this.pageSize,
//				billId != null ? billId : this.billId,
//				billNo != null ? billNo : this.billNo,
//				billName != null ? billName : this.billName,
//				proposerKind != null ? proposerKind : this.proposerKind,
//				currCommitteeId != null ? currCommitteeId : this.currCommitteeId,
//				currCommittee != null ? currCommittee : this.currCommittee,
//				procResultCode != null ? procResultCode : this.procResultCode,
//				procDate != null ? procDate : this.procDate
//				);
//	}
}

//@Data
//class Keyword {
//	
//	private final List<String> words;
//	
//	private final Integer score;
//	
//	public String toURLEncoding() {
//		try {
//			return URLEncoder.encode(String.join(" ", words), "utf-8");
//		} catch(UnsupportedEncodingException e) {
//			System.out.println(e);
//			return null;
//		}
//	}
//}

class HanjaOrHangle {
	final String string;
	final boolean isHanja;
	
	public HanjaOrHangle(String string, boolean isHanja) {
		this.string = string;
		this.isHanja = isHanja;
	}
}
