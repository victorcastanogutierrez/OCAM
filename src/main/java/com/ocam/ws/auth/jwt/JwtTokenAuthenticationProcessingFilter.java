package com.ocam.ws.auth.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.ocam.ws.auth.model.RawAccessJwtToken;
import com.ocam.ws.auth.util.Constants;

public class JwtTokenAuthenticationProcessingFilter
		extends AbstractAuthenticationProcessingFilter {
	private final AuthenticationFailureHandler failureHandler;

	@Autowired
	public JwtTokenAuthenticationProcessingFilter(
			AuthenticationFailureHandler failureHandler,
			RequestMatcher matcher) {
		super(matcher);
		this.failureHandler = failureHandler;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		String tokenPayload = request
				.getHeader(Constants.JWT_TOKEN_HEADER_PARAM);
		assertTokenValid(tokenPayload);
		RawAccessJwtToken token = new RawAccessJwtToken(tokenPayload);
		return getAuthenticationManager()
				.authenticate(new JwtAuthenticationToken(token));
	}

	private void assertTokenValid(String tokenPayload) {
		if (StringUtils.isBlank(tokenPayload)) {
			throw new AuthenticationServiceException(
					"Authorization header cannot be blank!");
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authResult);
		SecurityContextHolder.setContext(context);
		chain.doFilter(request, response);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		failureHandler.onAuthenticationFailure(request, response, failed);
	}
}
