package com.sample.stockQuote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sample.stockQuote.model.StockQuoteModel;
import com.sample.stockQuote.service.StockQuoteService;

@SpringBootTest
class StockQuoteApplicationTests {

	@Autowired
	private StockQuoteService stockQuoteService;

	private static Logger log = Logger.getLogger(StockQuoteApplicationTests.class);

	@Test
	void contextLoads() {
	}

	@Test
	public void testGetQuoteBySymbol() {
		String symbol = "IBM";
		try {
			StockQuoteModel quoteObj = stockQuoteService.getQuoteBySymbol(symbol, log);
			assertNotNull(quoteObj, "StockQuote Data Not found for : " + symbol);
			assertEquals("IBM", quoteObj.getQuoteModel().getSymbol(),
					"Expected symbol does not match the retrieved symbol.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetQuoteByList() {
		List<String> list = Arrays.asList("IBM", "GOOGL", "AAPL");
		try {
			List<StockQuoteModel> quoteList = stockQuoteService.getBatchQuotes(list, log);

			assertNotNull(quoteList, "StockQuote Data Not found for :" + list.toString());

			assertEquals(list.size(), quoteList.size(), "The StockQuote list should contain 3 elements");

			List<String> symbolsList = quoteList.stream().map(l -> l.getQuoteModel().getSymbol())
					.collect(Collectors.toList());

			boolean match = IntStream.range(0, list.size()).allMatch(i -> list.get(i).equals(symbolsList.get(i)));
			assertTrue(match, "The symbols in the StockQuote list do not match the provided symbols");

			if (match) {
				System.out.println("Test passed: The symbols in the quote list match the expected symbols.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
