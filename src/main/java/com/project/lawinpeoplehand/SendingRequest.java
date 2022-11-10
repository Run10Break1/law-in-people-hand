package com.project.lawinpeoplehand;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SendingRequest {
	public static void main(String args[]) throws URISyntaxException, IOException, InterruptedException {
		
		for(int startPage = 0; startPage <= 9; startPage += 10) {
			
			int endPage = startPage + 10;
						
			HttpClient client = HttpClient.newBuilder().build();
			
			List<CompletableFuture<Void>> taskList = new ArrayList<>();
			List<Integer> startPageList = new ArrayList<>();
		
			
			for(int i = startPage; i <= endPage; i += 1) {
				URI uri = new URI("http", "127.0.0.1:8081", "/add-keyword", String.format("startPage=%d&endPage=%d", i, i), null);
				
				HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().build();
				
				var task = client.sendAsync(httpRequest, BodyHandlers.ofString()).thenApply(HttpResponse::body).thenAccept((e) -> {
					Integer sp = Integer.parseInt(e.split("=")[1].split(":")[0]);
					startPageList.add(sp);
				});
				
				taskList.add(task);
			}
			
			CompletableFuture.allOf(taskList.toArray(new CompletableFuture<?>[0])).join();
			
			for(int i = startPage; i <= endPage; i++) {
				if(startPageList.contains(i)) continue;
				
				System.out.println(String.format("missed page : %d", i));
			}
			
			Thread.sleep(30000);
			
			System.out.println("end");
		}
		
		
		
	}
}
