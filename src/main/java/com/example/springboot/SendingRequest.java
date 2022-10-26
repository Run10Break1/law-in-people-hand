package com.example.springboot;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

public class SendingRequest {
	public static void main(String args[]) throws URISyntaxException, IOException, InterruptedException {
		
		HttpClient client = HttpClient.newBuilder().build();
		
		for(int startPage = 201; startPage <= 920; startPage += 50) {
			URI uri = new URI("http", "49.50.162.163:8081", "/bill/add-process-stage", String.format("startPage=%d&endPage=%d", startPage, startPage + 49), null);
			
			HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().build();
			
			String result = client.send(httpRequest, BodyHandlers.ofString()).body();
			
			System.out.println(String.format("startPage : %d, result : %s", startPage, result));
		}
	}
}