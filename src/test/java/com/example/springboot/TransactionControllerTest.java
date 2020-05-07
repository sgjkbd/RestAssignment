package com.example.springboot;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private TransactionService transactionSession;
	
	@Before
	public void onSetUp() {
		System.out.println("do something");
	}

	@Test
	public void testCreateTransaction() throws Exception {
		String response = "";
		String content = 
			"{" +
					"\"customerID\": 1," +
					"\"transactionAmount\": 100," +
					"\"transactionDate\": \"2020-05-07\"" +
			"}";
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/transaction")
				.content(content)
				.header("content-type", MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();
		
		response = result.getResponse().getContentAsString();
		assertEquals("\"CREATED\"", response);
		
		content = 
				"{" +
						"\"customerID\": -1," +
						"\"transactionAmount\": 100," +
						"\"transactionDate\": \"2020-05-07\"" +
				"}";
		result =	mvc.perform(MockMvcRequestBuilders.post("/transaction")
					.content(content)
					.header("content-type", MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andReturn();
		
		response = result.getResponse().getContentAsString();
		assertEquals("\"BAD_REQUEST\"", response);
		
		content = 
				"{" +
						"\"customerID\": 1," +
						"\"transactionAmount\": -100," +
						"\"transactionDate\": \"2020-05-07\"" +
				"}";
		
		result =	mvc.perform(MockMvcRequestBuilders.post("/transaction")
					.content(content)
					.header("content-type", MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andReturn();
		
		response = result.getResponse().getContentAsString();
		assertEquals("\"BAD_REQUEST\"", response);
			
		content = 
				"{" +
						"\"customerID\": 1," +
						"\"transactionAmount\": 100," +
						"\"transactionDate\": \"2021-05-07\"" +
				"}";
		
		result =	mvc.perform(MockMvcRequestBuilders.post("/transaction")
					.content(content)
					.header("content-type", MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andReturn();
			
		content = 
				"{" +
						"\"customerID\": \"id\"," +
						"\"transactionAmount\": 100," +
						"\"transactionDate\": \"2020-05-07\"" +
				"}";
		
		mvc.perform(MockMvcRequestBuilders.post("/transaction")
					.content(content)
					.header("content-type", MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
		
		content = 
				"{" +
						"\"customerID\": 1," +
						"\"transactionAmount\": \"amount\"," +
						"\"transactionDate\": \"2020-05-07\"" +
				"}";
		
		mvc.perform(MockMvcRequestBuilders.post("/transaction")
					.content(content)
					.header("content-type", MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
	}
}
