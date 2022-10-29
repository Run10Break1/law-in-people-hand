package com.project.lawinpeoplehand.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Component
public class NaverNewsSearchService extends NewsSearchService {

	private final HttpClient httpClient = HttpClient.newHttpClient(); 
	private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
	
	public static class NaverRequest extends NewsSearchService.Request {
		
		private static final String CLIENT_ID = "qJspzv9M_H9mtvoNk_HR";
		private static final String CLIENT_SECRET = "Dts7SHEW7s";
		
		private String query;
		
		private int display;
		
		private int start;
		
		public NaverRequest(String query, Integer display, Integer start) {
			this.query = query;
			this.display = display != null ? display : 10;
			this.start = start != null ? start : 1;
		}
		
		@Override
		public String query() {
			return String.format("query=%s&display=%d&start=%d", query, display, start);
		}
		
	}
	
	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class NaverResponse extends NewsSearchService.Response {
		
		private String title;
		
		@JsonProperty("originallink")
		private String originalLink;
		
		private String link;
		
		private String description;
		
		private String pubDate;
	}
	
	@Override
	public List<Response> search(Request request) throws URISyntaxException, IOException, InterruptedException {
		if(!(request instanceof NaverRequest)) {
			throw new RuntimeException("request는 NaverRequest여야 합니다.");
		}
		
		URI url = new URI("https", "openapi.naver.com", "/v1/search/news.json", request.query(), null);
		
		HttpRequest httpRequest = HttpRequest.newBuilder(url)
				.GET()
				.header("X-Naver-Client-Id", NaverRequest.CLIENT_ID)
				.header("X-Naver-Client-Secret", NaverRequest.CLIENT_SECRET)
				.build();
		
		HttpResponse<String> httpResponse = httpClient.send(httpRequest, BodyHandlers.ofString());
		
		return parseJson(httpResponse.body());
	}

	@Override
	protected List<Response> parseJson(String json) throws JsonMappingException, JsonProcessingException {
		
		JsonNode root = mapper.readTree(json);
		
		JsonNode arr = root.get("items");
		
		List<Response> responseList = new ArrayList<>();
		for(JsonNode item : arr) {
			NaverResponse response = mapper.treeToValue(item, NaverResponse.class);
			responseList.add(response);
		}
		
		return responseList;
	}
	
	
}
