package com.ocam.ws.auth.jwt;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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
	 * Factoria para generar tokens
	 * 
	 * @param userContext
	 * @return
	 */
	public AccessJwtToken createAccessJwtToken(UserContext userContext) {
		if (StringUtils.isBlank(userContext.getUsername()))
			throw new IllegalArgumentException(
					"Cannot create JWT Token without username");

		Claims claims = Jwts.claims().setSubject(userContext.getUsername());

		String token = Jwts.builder().setClaims(claims)
				.setIssuer(ConfigurationSettings.TOKEN_ISSUER)
				.setIssuedAt(new Date()) //
				.setExpiration(this.getExpirationDate())
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

		Claims claims = Jwts.claims().setSubject(userContext.getUsername());
		claims.put("scopes", Arrays.asList(Scopes.REFRESH_TOKEN.authority()));

		String token = Jwts.builder().setClaims(claims)
				.setIssuer(ConfigurationSettings.TOKEN_ISSUER)
				.setId(UUID.randomUUID().toString()) //
				.setIssuedAt(new Date()) //
				.setExpiration(getExpirationDate())
				.signWith(SignatureAlgorithm.HS512,
						ConfigurationSettings.TOKEN_SIGNIN_KEY)
				.compact();

		System.out.println("Renueva el token");
		return new AccessJwtToken(token, claims);
	}

	private Date getExpirationDate() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR,
				ConfigurationSettings.TOKEN_EXPIRATION_TIME);
		return c.getTime();
	}
}
