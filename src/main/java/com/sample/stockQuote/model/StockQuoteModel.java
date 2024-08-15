package com.sample.stockQuote.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StockQuoteModel {

	@JsonProperty("Global Quote")
	private QuoteModel quoteModel;

}
