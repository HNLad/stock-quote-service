package com.sample.stockQuote.controller;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.stockQuote.common.LoggerConfig;
import com.sample.stockQuote.model.LogInRequest;
import com.sample.stockQuote.model.LogInResponse;
import com.sample.stockQuote.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class LogInController {
	
	private final Logger log = Logger.getLogger(getClass());

	private final AuthenticationManager authManager;

	private final JwtUtil jwtUtil;

	private final UserDetailsService userDetailsService;

	public LogInController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
			UserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
		this.authManager = authenticationManager;
	}

	@PostMapping(value = "/dologin")
	public ResponseEntity<LogInResponse> doLogIn(@RequestBody LogInRequest req) throws Exception {
		LoggerConfig.setLogger("doLogIn", log);
		LogInResponse res = new LogInResponse();
		try {
			log.info("doLogIn() --> started for user: " + req.getUserName());
			authUserDetails(req.getUserName(), req.getPassword());

			UserDetails userDetails = userDetailsService.loadUserByUsername(req.getUserName());
			if (userDetails != null) {
				String token = jwtUtil.generateJwtToken(userDetails.getUsername());

				res.setUserName(userDetails.getUsername());
				res.setJwtToken(token);
				res.setMessage("Log In Success");
				log.info("doLogIn() --> user: " + req.getUserName() + " logedIn");
				return ResponseEntity.ok(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("doLogIn() --> user: " + req.getUserName(), e);
			res.setMessage("Please Enter Valid User Credentials");
			return ResponseEntity.badRequest().body(res);
		}
		return null;
	}

	private void authUserDetails(String username, String password) throws Exception {
		try {
			System.out.println(" authentication for user: " + username);
			authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (BadCredentialsException e) {
			throw new Exception("Invalid Credentials", e);
		} catch (Exception e) {
			throw new Exception("Exception", e);
		}
	}
}
