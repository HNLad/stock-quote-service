package com.sample.stockQuote.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sample.stockQuote.serviceImpl.UserServiceImpl;

import jakarta.annotation.PostConstruct;

@Component
public class StaticUserDetails {

	private final PasswordEncoder passwordEncoder;

	public StaticUserDetails(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@PostConstruct
	public void userServiceStatic() {
		UserServiceImpl.userDatabase.put("user", passwordEncoder.encode("password"));
		UserServiceImpl.userDatabase.put("thinkhumble", passwordEncoder.encode("123"));
		UserServiceImpl.userDatabase.put("test", passwordEncoder.encode("12345"));
	}

}
