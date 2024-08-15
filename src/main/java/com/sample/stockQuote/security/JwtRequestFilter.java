package com.sample.stockQuote.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private final UserDetailsService userDetailsService;

	private final JwtUtil jwtUtil;

	public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		String userName = null;
		String jwtToken = null;
		boolean error = false;
		String errorMsg = "";
		try {

			final String requestTokenHeader = request.getHeader("Authorization");

			if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
				jwtToken = requestTokenHeader.substring(7);
				try {
					userName = jwtUtil.extractUserNameFromJwt(jwtToken);
				} catch (IllegalArgumentException e) {
					error = true;
					errorMsg = "Unable to get JWT Token From Request";
					e.printStackTrace();
					System.out.println("Unable to get JWT Token From Request");
				} catch (ExpiredJwtException e) {
					error = true;
					errorMsg = e.getMessage();
					e.printStackTrace();
					System.out.println("JWT Token has already expired");
				}
			} else {
				logger.warn("JWT Token does not begin with Bearer String");
			}

			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

				if (userDetails != null) {

					if (Boolean.TRUE.equals(jwtUtil.validateJwtToken(jwtToken, userDetails.getUsername()))) {

						UsernamePasswordAuthenticationToken userNamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						userNamePasswordAuthenticationToken
								.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(userNamePasswordAuthenticationToken);
					} else {
						logger.warn("JWT Token Validation Failed");
					}
				} else {
					logger.warn("User Details Null");
				}
			}
			try {
				chain.doFilter(request, response);
			} finally {
				if (error) {
					handleAuthError(request, response, errorMsg);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			handleAuthError(request, response, e.getMessage());
		}
	}

	private void handleAuthError(HttpServletRequest request, HttpServletResponse response, String Msg)
			throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		final Map<String, Object> body = new HashMap<>();
		body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
		body.put("error", "Unauthorized");
		body.put("message", Msg);
		body.put("path", request.getServletPath());
		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), body);
	}
}
