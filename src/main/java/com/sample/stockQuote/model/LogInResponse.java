package com.sample.stockQuote.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LogInResponse {

	private String userName;
	private String jwtToken;
	private String message;
}
