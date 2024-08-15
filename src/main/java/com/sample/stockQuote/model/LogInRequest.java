package com.sample.stockQuote.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LogInRequest {

	private String userName;
	private String password;
}
