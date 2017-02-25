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
import com.ocam.repository.HikerRepository;
import com.ocam.ws.auth.model.UserContext;

@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider {

	private final HikerRepository hikerRepository;

	@Autowired
	public AjaxAuthenticationProvider(final HikerRepository hikerRepository) {
		this.hikerRepository = hikerRepository;
	}

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		Assert.notNull(authentication, "No authentication data provided");
		String username = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();

		Hiker hiker = hikerRepository.findByLoginAndPassword(username,
				password);
		if (hiker == null) {
			throw new BadCredentialsException(
					"Authentication Failed. Username or Password not valid.");
		}

		UserContext userContext = UserContext.create(hiker.getLogin());

		return new UsernamePasswordAuthenticationToken(userContext, null);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class
				.isAssignableFrom(authentication));
	}
}
