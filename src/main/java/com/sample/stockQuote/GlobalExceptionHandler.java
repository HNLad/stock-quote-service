package com.sample.stockQuote;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
		ex.printStackTrace();
		return new ResponseEntity<>("Internal Server Error With Msg: " + ex.getMessage() + ", Please try again later.",
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<?> handleSQLException(SQLException ex, WebRequest request) {
		ex.printStackTrace();
		return new ResponseEntity<>("Database Error. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
