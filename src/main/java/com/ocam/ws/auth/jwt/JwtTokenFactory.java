package com.ocam.ws.auth.jwt;

import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.ocam.ws.auth.config.ConfigurationSettings;
import com.ocam.ws.auth.model.Scopes;
import com.ocam.ws.auth.model.UserContext;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenFactory {

	/**
	 * Factory method for issuing new JWT Tokens.
	 * 
	 * @param username
	 * @param roles
	 * @return
	 */
	public AccessJwtToken createAccessJwtToken(UserContext userContext) {
		if (StringUtils.isBlank(userContext.getUsername()))
			throw new IllegalArgumentException(
					"Cannot create JWT Token without username");

		Claims claims = Jwts.claims().setSubject(userContext.getUsername());

		DateTime currentTime = new DateTime();

		String token = Jwts.builder().setClaims(claims)
				.setIssuer(ConfigurationSettings.TOKEN_ISSUER)
				.setIssuedAt(currentTime.toDate())
				.setExpiration(currentTime
						.plusHours(ConfigurationSettings.TOKEN_EXPIRATION_TIME)
						.toDate())
				.signWith(SignatureAlgorithm.HS512,
						ConfigurationSettings.TOKEN_SIGNIN_KEY)
				.compact();

		return new AccessJwtToken(token, claims);
	}

	public JwtToken createRefreshToken(UserContext userContext) {
		if (StringUtils.isBlank(userContext.getUsername())) {
			throw new IllegalArgumentException(
					"Cannot create JWT Token without username");
		}

		DateTime currentTime = new DateTime();

		Claims claims = Jwts.claims().setSubject(userContext.getUsername());
		claims.put("scopes", Arrays.asList(Scopes.REFRESH_TOKEN.authority()));

		String token = Jwts.builder().setClaims(claims)
				.setIssuer(ConfigurationSettings.TOKEN_ISSUER)
				.setId(UUID.randomUUID().toString())
				.setIssuedAt(currentTime.toDate())
				.setExpiration(currentTime
						.plusMinutes(
								ConfigurationSettings.TOKEN_REFRESH_EXPIRATION_TIME)
						.toDate())
				.signWith(SignatureAlgorithm.HS512,
						ConfigurationSettings.TOKEN_SIGNIN_KEY)
				.compact();

		return new AccessJwtToken(token, claims);
	}
}
