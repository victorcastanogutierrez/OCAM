package com.ocam.ws.auth.util;

public interface TokenVerifier {
	public boolean verify(String jti);
}
