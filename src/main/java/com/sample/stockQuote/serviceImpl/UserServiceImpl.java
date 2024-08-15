package com.sample.stockQuote.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService {
	//kept temp data
	public static Map<String, String> userDatabase = new HashMap<>();

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (userDatabase.containsKey(username)) {
			return new User(username, userDatabase.get(username), new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found");
		}
	}
}
