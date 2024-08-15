package com.sample.stockQuote.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QuoteModel {

	@JsonProperty("01. symbol")
	private String symbol ;
	

	@JsonProperty("02. open")
	private BigDecimal open;

	@JsonProperty("03. high")
	private BigDecimal high;

	@JsonProperty("04. low")
	private BigDecimal low;
	

	@JsonProperty("05. price")
	private BigDecimal price;

	@JsonProperty("06. volume")
	private BigDecimal volume;

	@JsonProperty("07. latest trading day")
	private String latestTradingDay;
	

	@JsonProperty("08. previous close")
	private BigDecimal previousClose;
	

	@JsonProperty("09. change")
	private BigDecimal change;

	
	@JsonProperty("10. change percent")
	@JsonDeserialize(using = PercentageDeserializer.class)
    private BigDecimal percentageChange;
	
	
	public Timestamp getTimestamp(String latestTradingDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		LocalDateTime dateTime = LocalDate.parse(latestTradingDate, formatter).atStartOfDay();
		
		return Timestamp.valueOf(dateTime);
	}

}
