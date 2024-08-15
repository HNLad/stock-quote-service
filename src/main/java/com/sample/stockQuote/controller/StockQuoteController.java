package com.sample.stockQuote.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.stockQuote.common.LoggerConfig;
import com.sample.stockQuote.model.StockQuoteModel;
import com.sample.stockQuote.service.StockQuoteService;

@RestController
@RequestMapping("/stockquote/v1")
public class StockQuoteController {

	private final Logger log = Logger.getLogger(getClass());

	private final StockQuoteService service;

	public StockQuoteController(StockQuoteService service) {
		this.service = service;
	}

	@GetMapping("/getquote/{symbol}")
	public ResponseEntity<StockQuoteModel> getQuoteBySymbol(@PathVariable String symbol) {
		LoggerConfig.setLogger("getQuoteBySymbol", log);
		StockQuoteModel resModel = null;
		try {
			log.info("getQuoteBySymbol() method started for Variable Symbol: " + symbol);

			resModel = service.getQuoteBySymbol(symbol, log);

			log.info("getQuoteBySymbol() method Ended for Variable Symbol: " + symbol + ", With Response : "
					+ (resModel != null ? resModel.toString() : null));
			return ResponseEntity.ok(resModel);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("getQuoteBySymbol() Exception Occurred With MSG: " + e.getMessage() + " for symbol: " + symbol, e);
			return ResponseEntity.badRequest().body(null);
		}
	}

	@PostMapping("/getbatch")
	public ResponseEntity<List<StockQuoteModel>> getBatchQuotes(@RequestBody List<String> symbols) {
		LoggerConfig.setLogger("getBatchQuotes", log);
		List<StockQuoteModel> resList = null;
		try {
			log.info("getBatchQuotes() method started for symbols list: " + symbols.toString());
			
			resList = service.getBatchQuotes(symbols, log);
			
			log.info("getBatchQuotes() method Ended for symbols list: " + symbols + ", With Response list: "
					+ (resList != null ? (resList.isEmpty() ? null : resList.toString()) : null));
			return ResponseEntity.ok(resList);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("getBatchQuotes() Exception Occurred With MSG: " + e.getMessage() + " for symbol: "
					+ symbols.toString(), e);
			return ResponseEntity.badRequest().body(null);
		}
	}

}
