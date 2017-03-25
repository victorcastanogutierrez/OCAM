package com.ocam.ws.auth.ajax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ocam.model.Hiker;
import com.ocam.model.exception.BusinessException;
import com.ocam.service.HikerService;
import com.ocam.ws.auth.exception.DecodeDataException;
import com.ocam.ws.auth.model.UserContext;

@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider {

	private final HikerService hikerService;

	@Autowired
	public AjaxAuthenticationProvider(final HikerService hikerService) {
		this.hikerService = hikerService;
	}

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		Assert.notNull(authentication, "No authentication data provided");
		String username = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();

		Hiker hiker = null;
		try {
			hiker = hikerService.findHikerByLoginPassword(username, password);
		} catch (BusinessException e) {
			throw new DecodeDataException(e.getMessage());
		}

		if (hiker == null) {
			throw new BadCredentialsException(
					"Authentication Failed. Username or Password not valid.");
		}

		UserContext userContext = UserContext.create(hiker.getLogin(),
				hiker.getEmail());

		return new UsernamePasswordAuthenticationToken(userContext, null);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class
				.isAssignableFrom(authentication));
	}
}
