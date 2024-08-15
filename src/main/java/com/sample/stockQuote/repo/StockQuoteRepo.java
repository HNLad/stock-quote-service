package com.sample.stockQuote.repo;

import java.util.List;

import org.apache.log4j.Logger;

import com.sample.stockQuote.model.StockQuoteModel;

public interface StockQuoteRepo {

	StockQuoteModel loadQuotesBySymbol(String symbol, Logger log) throws Exception;

	List<StockQuoteModel> getAllQuotesBySymbolsList(List<String> symbols, Logger log) throws Exception;

	int insertQuoteData(StockQuoteModel stockQuote, Logger log);

}
