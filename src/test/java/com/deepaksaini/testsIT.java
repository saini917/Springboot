package com.deepaksaini;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.deepaksaini.controller.Library;

import junit.framework.Assert;

@SpringBootTest
public class testsIT {

	@Test
	public void getAuthorNameBooksIntegrationTest() throws JSONException {
		
		String expected = "[\r\n"
				+ "    {\r\n"
				+ "        \"book_name\": \"Cypress\",\r\n"
				+ "        \"id\": \"abcd12\",\r\n"
				+ "        \"isbn\": \"abcd\",\r\n"
				+ "        \"aisle\": 12,\r\n"
				+ "        \"author\": \"kamal singh\"\r\n"
				+ "    },\r\n"
				+ "    {\r\n"
				+ "        \"book_name\": \"Devops\",\r\n"
				+ "        \"id\": \"hjgy12\",\r\n"
				+ "        \"isbn\": \"hjgy\",\r\n"
				+ "        \"aisle\": 12,\r\n"
				+ "        \"author\": \"kamal singh\"\r\n"
				+ "    }\r\n"
				+ "]";

		TestRestTemplate restTemplate=new TestRestTemplate();
		ResponseEntity<String> response=restTemplate.getForEntity("http://localhost:8080/getBooks/author?authorname=kamal singh", String.class);
		System.out.println(response.getStatusCode());
		System.out.println(response.getBody());
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}
	
	@Test
	public void addBookIntegrationTest() {
		
		TestRestTemplate restTemplate=new TestRestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<Library> request= new HttpEntity<Library>(buildLibrary(),headers);
		ResponseEntity<String> response=restTemplate.postForEntity("http://localhost:8080/addBook", request, String.class);
		System.out.println(response.getStatusCode());
		Assert.assertEquals(HttpStatus.CREATED,response.getStatusCode());
		//System.out.println(response.getHeaders().get("unique").get(0));
		Assert.assertEquals(buildLibrary().getId(), response.getHeaders().get("unique").get(0));
		
	}
	
	public Library buildLibrary() {
		Library library=new Library();
		library.setAisle(389);
		library.setAuthor("Neha");
		library.setBook_name("Core java");
		library.setIsbn("fgh");
		library.setId("fgh389");
		return library;

	}


}
