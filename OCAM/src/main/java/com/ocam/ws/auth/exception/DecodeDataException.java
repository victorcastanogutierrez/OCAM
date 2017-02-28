package com.ocam.ws.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class DecodeDataException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4415437367488257761L;

	public DecodeDataException(String msg) {
		super(msg);
	}

}
