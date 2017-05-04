package com.ocam.ws.auth.ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocam.ws.auth.exception.AuthMethodNotSupportedException;
import com.ocam.ws.auth.exception.JwtExpiredTokenException;
import com.ocam.ws.auth.model.ErrorCode;
import com.ocam.ws.auth.model.ErrorResponse;

@Component
public class AjaxAwareAuthenticationFailureHandler
		implements AuthenticationFailureHandler {
	private final ObjectMapper mapper;

	@Autowired
	public AjaxAwareAuthenticationFailureHandler(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException e)
			throws IOException, ServletException {

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		if (e instanceof BadCredentialsException) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			mapper.writeValue(response.getWriter(),
					ErrorResponse.of("Invalid username or password",
							ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
		} else if (e instanceof JwtExpiredTokenException) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			mapper.writeValue(response.getWriter(),
					ErrorResponse.of("Token has expired",
							ErrorCode.JWT_TOKEN_EXPIRED, HttpStatus.FORBIDDEN));
		} else if (e instanceof AuthMethodNotSupportedException) {
			response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
			mapper.writeValue(response.getWriter(),
					ErrorResponse.of(e.getMessage(), ErrorCode.AUTHENTICATION,
							HttpStatus.UNPROCESSABLE_ENTITY));
		}
	}
}
