package com.sample.stockQuote.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.sample.stockQuote.model.StockQuoteModel;

public interface StockQuoteService {

	StockQuoteModel getQuoteBySymbol(String symbol, Logger log);

	List<StockQuoteModel> getBatchQuotes(List<String> symbols, Logger log);

}
