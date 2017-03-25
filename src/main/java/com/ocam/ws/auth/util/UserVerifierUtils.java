package com.ocam.ws.auth.util;

import javax.servlet.http.HttpServletRequest;

import com.ocam.ws.auth.config.ConfigurationSettings;
import com.ocam.ws.auth.model.RawAccessJwtToken;

public class UserVerifierUtils {

	public static String getRequestUsername(HttpServletRequest request) {
		String tokenPayload = request
				.getHeader(Constants.JWT_TOKEN_HEADER_PARAM);

		RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
		return rawToken.parseClaims(ConfigurationSettings.TOKEN_SIGNIN_KEY)
				.getBody().getSubject();
	}

}
