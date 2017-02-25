package com.ocam.ws.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ocam.model.Hiker;
import com.ocam.repository.HikerRepository;
import com.ocam.ws.auth.config.ConfigurationSettings;
import com.ocam.ws.auth.config.WebSecurityConfig;
import com.ocam.ws.auth.exception.InvalidJwtToken;
import com.ocam.ws.auth.jwt.JwtToken;
import com.ocam.ws.auth.jwt.JwtTokenFactory;
import com.ocam.ws.auth.model.RawAccessJwtToken;
import com.ocam.ws.auth.model.RefreshToken;
import com.ocam.ws.auth.model.UserContext;
import com.ocam.ws.auth.util.TokenExtractor;
import com.ocam.ws.auth.util.TokenVerifier;

@RestController
public class RefreshTokenEndpoint {

	@Autowired
	private JwtTokenFactory tokenFactory;
	@Autowired
	private HikerRepository hikerRepository;
	@Autowired
	private TokenVerifier tokenVerifier;
	@Autowired
	@Qualifier("jwtHeaderTokenExtractor")
	private TokenExtractor tokenExtractor;

	@RequestMapping(value = "/api/auth/token", method = RequestMethod.GET,
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody JwtToken refreshToken(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String tokenPayload = tokenExtractor.extract(
				request.getHeader(WebSecurityConfig.JWT_TOKEN_HEADER_PARAM));

		RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
		RefreshToken refreshToken = RefreshToken
				.create(rawToken, ConfigurationSettings.TOKEN_SIGNIN_KEY)
				.orElseThrow(() -> new InvalidJwtToken());

		String jti = refreshToken.getJti();
		if (!tokenVerifier.verify(jti)) {
			throw new InvalidJwtToken();
		}

		String subject = refreshToken.getSubject();
		Hiker hiker = hikerRepository.findByLogin(subject);

		if (hiker == null) {
			throw new UsernameNotFoundException("User not found: " + subject);
		}

		UserContext userContext = UserContext.create(hiker.getLogin());

		return tokenFactory.createAccessJwtToken(userContext);
	}
}
