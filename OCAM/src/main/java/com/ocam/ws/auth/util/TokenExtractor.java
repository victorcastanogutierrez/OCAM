package com.ocam.ws.auth.util;

public interface TokenExtractor {
	public String extract(String payload);
}
