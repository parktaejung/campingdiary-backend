package com.campingdiary.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.campingdiary.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	private final JwtUtil jwtUtil;
	private final UserService userService;
	
	public JwtAuthenticationFilter(JwtUtil jwtUtil , UserService userService) {
		this.jwtUtil = jwtUtil;
		this.userService = userService;
	}
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			String token = parseToken(request);
			String uri = request.getRequestURI();
			 if (uri.contains("/api/users/login") || uri.contains("/api/users/register")) {
			        filterChain.doFilter(request, response);
			        return;
			    }
			if(token != null && jwtUtil.validateToken(token)) {
				String username = jwtUtil.getUsernameFromToken(token);
				
				var userDetails = userService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}catch(Exception e) {
			logger.warn("JWT 인증 실패: {}",e.getMessage());
		}
		filterChain.doFilter(request, response);
	}
	
	private String parseToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
