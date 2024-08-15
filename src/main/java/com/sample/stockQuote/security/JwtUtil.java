package com.sample.stockQuote.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {


	@Value("${secret.key}")
	private String secret_key;

    public String extractUserNameFromJwt(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    public Date extractExpirationOfJwt(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpirationOfJwt(token).before(new Date());
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret_key).parseClaimsJws(token).getBody();
    }

    //Gen Token
    public String generateJwtToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createJwtToken(claims, username);
    }

    //Actual Token Gen
    private String createJwtToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS512, secret_key)
                .compact();
    }

    public Boolean validateJwtToken(String token, String username) {
        final String extractedUsername = extractUserNameFromJwt(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
