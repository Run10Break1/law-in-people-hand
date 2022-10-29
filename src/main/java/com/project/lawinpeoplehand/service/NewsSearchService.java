package com.project.lawinpeoplehand.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.Getter;


public abstract class NewsSearchService {

	public static abstract class Request {
		public abstract String query();
	}
	
	public static class Response {
		
	}
	
	public abstract List<Response> search(Request request) throws URISyntaxException, IOException, InterruptedException;
	
	protected abstract List<Response> parseJson(String json) throws JsonMappingException, JsonProcessingException;
}
