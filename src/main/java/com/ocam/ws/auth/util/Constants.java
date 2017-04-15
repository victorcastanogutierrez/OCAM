package com.ocam.ws.auth.util;

public class Constants {

	public static final String JWT_TOKEN_HEADER_PARAM = "authorization";
	public static final String EMAIL_HEADER_PARAM = "emailauth";
	public static final String JWT_TOKEN_REFRESH_HEADER_PARAM = "authorizationrefresh";
	public static final String FORM_BASED_LOGIN_ENTRY_POINT = "/api/auth/login";
	public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/api/**";
	public static final String TOKEN_REFRESH_ENTRY_POINT = "/api/auth/token";
}
