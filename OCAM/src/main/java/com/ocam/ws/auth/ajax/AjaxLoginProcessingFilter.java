package com.ocam.ws.auth.ajax;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.Base64Utils;

import com.ocam.ws.auth.exception.AuthMethodNotSupportedException;

public class AjaxLoginProcessingFilter
		extends AbstractAuthenticationProcessingFilter {

	private final AuthenticationSuccessHandler successHandler;
	private final AuthenticationFailureHandler failureHandler;

	public AjaxLoginProcessingFilter(String defaultProcessUrl,
			AuthenticationSuccessHandler successHandler,
			AuthenticationFailureHandler failureHandler) {
		super(defaultProcessUrl);
		this.successHandler = successHandler;
		this.failureHandler = failureHandler;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		if (!HttpMethod.POST.name().equals(request.getMethod())) {
			throw new AuthMethodNotSupportedException(
					"Authentication method not supported");
		}

		String usernameCoded = request.getHeader("username");
		String passwordCoded = request.getHeader("password");

		if (!assertUsernamePassword(usernameCoded, passwordCoded)) {
			throw new AuthenticationServiceException(
					"Username or Password not provided");
		}

		String username = decode64(usernameCoded);
		String password = decode64(passwordCoded);

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				username, password);

		return this.getAuthenticationManager().authenticate(token);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		successHandler.onAuthenticationSuccess(request, response, authResult);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		failureHandler.onAuthenticationFailure(request, response, failed);
	}

	private String decode64(String coded) {
		return new String(Base64Utils.decodeFromString(coded),
				StandardCharsets.UTF_8);
	}

	private boolean assertUsernamePassword(String usernameCoded,
			String passwordCoded) {
		return !StringUtils.isBlank(usernameCoded)
				&& !StringUtils.isBlank(passwordCoded);
	}
}
