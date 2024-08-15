package com.sample.stockQuote.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.sample.stockQuote.model.StockQuoteModel;
import com.sample.stockQuote.repo.StockQuoteRepo;
import com.sample.stockQuote.service.StockQuoteService;

@Service("StockQuoteService")
public class StockQuoteServiceImpl implements StockQuoteService {

	private final StockQuoteRepo repo;

	private final RestTemplate restTemplate;

	@Value("${stock.api.url}")
	private String apiUrl;

	@Value("${stock.api.key}")
	private String apiKey;

	public StockQuoteServiceImpl(StockQuoteRepo repository, RestTemplate restTemplate) {
		this.repo = repository;
		this.restTemplate = restTemplate;
	}

	@Override
	public StockQuoteModel getQuoteBySymbol(String symbol, Logger log) {
		StockQuoteModel stockQuoteObj = null;
		log.info("getQuoteBySymbol() method started for symbol:" + symbol);
		try {
			stockQuoteObj = repo.loadQuotesBySymbol(symbol, log);
			if (stockQuoteObj != null) {
				log.info("getQuoteBySymbol() data present in database for symbol:" + symbol);
				return stockQuoteObj;
			} else {
				log.info("getQuoteBySymbol() data not found in database for symbol:" + symbol + ", fetching started");
				return fetchAndSaveQuote(symbol, log);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<StockQuoteModel> getBatchQuotes(List<String> symbolsList, Logger log) {
		log.info("getQuoteBySymbol()--> method started list size: " + symbolsList.size());
		try {
			List<StockQuoteModel> stockQuotesList = repo.getAllQuotesBySymbolsList(symbolsList, log);

			List<String> missingSymbolsList = symbolsList.stream()
					.filter(s -> stockQuotesList.stream().noneMatch(q -> q.getQuoteModel().getSymbol().equals(s)))
					.collect(Collectors.toList());

			log.info("getQuoteBySymbol()--> data not found in db list size: " + missingSymbolsList.size());

			List<StockQuoteModel> fetchedQuotesList = missingSymbolsList.stream().map(s -> fetchAndSaveQuote(s, log))
					.collect(Collectors.toList());

			stockQuotesList.addAll(fetchedQuotesList);
			log.info("getQuoteBySymbol()--> method ended with list size: " + stockQuotesList.size());
			return stockQuotesList;
		} catch (Exception e) {
			log.info("getQuoteBySymbol()--> Exception occured " + e.getMessage(), e);
			e.printStackTrace();
		}
		return null;
	}

	private StockQuoteModel fetchAndSaveQuote(String symbol, Logger log) {
		try {
//			String url = apiUrl + "?function=GLOBAL_QUOTE&symbol=" + symbol + "&interval=5min&apikey=" + apiKey;
			String urlNew = apiUrl + "?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;

			log.info("fetchAndSaveQuote() method started with URL: " + urlNew);
			System.out.println("fetchAndSaveQuote() method started with URL: " + urlNew);

			// req to alpha vantage to get data
			String res = restTemplate.getForObject(urlNew, String.class);

			StockQuoteModel quoteObj = res != null ? setRespToModel(res, symbol, log) : null;

			if (quoteObj != null) {
				int i = repo.insertQuoteData(quoteObj, log);
				if (i == 1) {
					System.out.println(" Data Saved Successfully : " + symbol);
					log.info("fetchAndSaveQuote() Data Saved Successfully For Symbol : " + symbol);
					return quoteObj;
				} else {
					System.out.println(" Data Insert Failed For : " + symbol);
					log.error("fetchAndSaveQuote() Data Insert Failed For Symbol : " + symbol);
					return null;
				}
			}
			System.out.println(" Data Not Found For Symbol : " + symbol);
			log.info("fetchAndSaveQuote() Data Not Found For Symbol : " + symbol);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static StockQuoteModel setRespToModel(String response, String symbol, Logger log) {
		log.info("setRespToModel()--> Method Started for Symbol: " + symbol);
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			
			//initialising
			int reqNo = 1 ;
			
			int avg = 30;
			
			//avg
			reqNo+=1;
			int newReqTime = 0;
			{
				newReqTime =  20;
			}
			
			avg = (avg*reqNo)/reqNo;
			
			/*
			 * avg -> req - 10 = avg 50
			 * time = 20
			 * 
			 * 5
			 * 
			 * 50 + 27 = 77
			 * 
			 * 7
			 * 
			 * 10*5
			 * 
			 * */

			StockQuoteModel stockQuoteModel = mapper.readValue(response, StockQuoteModel.class);

			if (stockQuoteModel == null || stockQuoteModel.getQuoteModel() == null) {
				log.error("setRespToModel()--> QuoteModel data is missing for Symbol: " + symbol);
				return null;
			}

			log.info("setRespToModel()--> Response set into model for Symbol: " + symbol);
			return stockQuoteModel;

		} catch (Exception e) {
			e.printStackTrace();
			log.error("setRespToModel()--> Error while setting response ", e);
		}
		return null;

	}
	
	
	
	

}
