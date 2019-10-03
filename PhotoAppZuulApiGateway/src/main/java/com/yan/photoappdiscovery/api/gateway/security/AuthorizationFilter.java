package com.yan.photoappdiscovery.api.gateway.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;

public class AuthorizationFilter extends BasicAuthenticationFilter{

	private @Autowired Environment env;
	
	public AuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String authorizationheader = request.getHeader(env.getProperty("authorization.token.header.name"));
		if(authorizationheader == null || !authorizationheader.startsWith(env.getProperty("authorization.token.header.prefix"))) {
			super.doFilterInternal(request, response, chain);
			return;
		} else {
			UsernamePasswordAuthenticationToken auth =  getAuthentication(request);
			SecurityContextHolder.getContext().setAuthentication(auth);
		
		}
		
		
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String authorizationheader = request.getHeader(env.getProperty("authorization.token.header.name"));
		if(authorizationheader == null) { return null; }
		
		String token = authorizationheader.replace(env.getProperty("authorization.token.header.prefix"), "");
		
		String userId = Jwts.parser()
				.setSigningKey(env.getProperty("token.secret"))
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
		
		if(userId == null) {
			return null;
		}
		
		return new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
	}
	
	

}
