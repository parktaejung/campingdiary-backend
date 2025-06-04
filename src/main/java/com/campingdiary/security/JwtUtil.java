package com.campingdiary.security;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {
	@Value("${jwt.secret}")
	private String secretKeyBase64;
	private SecretKey secretKey;
	private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;
	
	@PostConstruct
	public void init() {
		byte[] decodedKey = Base64.getDecoder().decode(secretKeyBase64);
		this.secretKey = Keys.hmacShaKeyFor(decodedKey);
	}
	public String createToken(String username,String role) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
		
		return Jwts.builder()
				.setSubject(username)
				.claim("role",role)
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(secretKey, SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String getUsernameFromToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token);
			return true;
		}catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
	public String getRoleFromToken(String token) {
		return (String) Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.get("role");
	}
	public String createRefreshToken(String username, String role) {
	    return Jwts.builder()
	        .setSubject(username)
	        .setIssuedAt(new Date())
	        .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)) // 7일
	        .signWith(secretKey, SignatureAlgorithm.HS256)
	        .compact();
	}
	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
