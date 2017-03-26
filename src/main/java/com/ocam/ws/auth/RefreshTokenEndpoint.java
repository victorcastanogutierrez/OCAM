package com.ocam.ws.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ocam.model.Hiker;
import com.ocam.repository.HikerRepository;
import com.ocam.ws.auth.config.ConfigurationSettings;
import com.ocam.ws.auth.exception.InvalidJwtToken;
import com.ocam.ws.auth.jwt.JwtToken;
import com.ocam.ws.auth.jwt.JwtTokenFactory;
import com.ocam.ws.auth.model.RawAccessJwtToken;
import com.ocam.ws.auth.model.RefreshToken;
import com.ocam.ws.auth.model.UserContext;
import com.ocam.ws.auth.util.Constants;

@RestController
public class RefreshTokenEndpoint {

	@Autowired
	private JwtTokenFactory tokenFactory;
	@Autowired
	private HikerRepository hikerRepository;

	@RequestMapping(value = "/api/auth/token", method = RequestMethod.GET,
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody JwtToken refreshToken(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		String tokenPayload = request
				.getHeader(Constants.JWT_TOKEN_HEADER_PARAM);
		if (StringUtils.isBlank(tokenPayload)) {
			throw new AuthenticationServiceException(
					"Authorization header cannot be blank!");
		}

		RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
		RefreshToken refreshToken = RefreshToken
				.create(rawToken, ConfigurationSettings.TOKEN_SIGNIN_KEY)
				.orElseThrow(() -> new InvalidJwtToken());

		String subject = refreshToken.getSubject();
		Hiker hiker = hikerRepository.findByLogin(subject);

		if (hiker == null) {
			throw new UsernameNotFoundException("User not found: " + subject);
		}

		UserContext userContext = UserContext.create(hiker.getLogin(),
				hiker.getEmail());

		return tokenFactory.createAccessJwtToken(userContext);
	}
}
