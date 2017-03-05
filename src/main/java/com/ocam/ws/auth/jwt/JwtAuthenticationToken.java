package com.ocam.ws.auth.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import com.ocam.ws.auth.model.RawAccessJwtToken;
import com.ocam.ws.auth.model.UserContext;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
	private static final long serialVersionUID = 2877954820905567501L;

	private RawAccessJwtToken rawAccessToken;
	private UserContext userContext;

	// For requests containing a token in its header
	public JwtAuthenticationToken(RawAccessJwtToken unsafeToken) {
		super(null);
		this.rawAccessToken = unsafeToken;
		this.setAuthenticated(false);
	}

	// Creating a new authenticated token
	public JwtAuthenticationToken(UserContext userContext) {
		super(null);
		this.eraseCredentials();
		this.userContext = userContext;
		super.setAuthenticated(true);
	}

	@Override
	public void setAuthenticated(boolean authenticated) {
		if (authenticated) {
			throw new IllegalArgumentException(
					"Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
		}
		super.setAuthenticated(false);
	}

	@Override
	public Object getCredentials() {
		return rawAccessToken;
	}

	@Override
	public Object getPrincipal() {
		return this.userContext;
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		this.rawAccessToken = null;
	}
}
