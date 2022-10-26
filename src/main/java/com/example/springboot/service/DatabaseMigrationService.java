package com.example.springboot.service;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.json.JsonParserFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.springboot.model.Bill;
import com.example.springboot.repository.BillRepository;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;


@Service
@AllArgsConstructor
public class DatabaseMigrationService {

	private final BillRepository billRepository;
	
	@Async
	public void migrate(int startPage, Integer endPage) throws Exception {
		
		Request.RequestBuilder requestBuilder = new Request.RequestBuilder();
		
		Response response;
		do {
			Request request = requestBuilder.pageIndex(startPage).build();
			response = getFromSource(request);
			sendToTarget(response.billList);
			
			System.out.println(String.format("완료된 페이지 : %d, bill 개수 : %d", startPage, response.count));
			
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
		
		HttpResponse<String> response = client.send(httpRequest, BodyHandlers.ofString());
		
		return parseJson(response.body(), request.getPageSize());
	}
	
	private Response parseJson(String json, int maxCount) throws JsonProcessingException, IllegalArgumentException {
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
		
		JsonNode root = mapper.readTree(json);
		
		if(root.has("RESULT")) {
			// 해당하는 데이터가 없는 경우
			return new Response(0, null);
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
	private final String KEY = "d83074a6969742279ca72e2e36fb56d2";
	
	private final String type;
	private final int pageIndex;
	private final int pageSize;
	
	private final String billId;
	private final String billNo;
	private final String billName;
	private final String proposerKind;
	private final String currCommitteeId;
	private final String currCommittee;
	private final String procResultCode;
	private final LocalDate procDate;
	
	@Builder
	public Request(String type, Integer pageIndex, Integer pageSize, String billId, String billNo, String billName,
			String proposerKind, String currCommitteeId, String currCommittee, String procResultCode,
			LocalDate procDate) {
		this.type = type != null ? type : "json";
		this.pageIndex = pageIndex != null ? pageIndex : 0;
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
		ObjectMapper mapper = new ObjectMapper();
		
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