package com.ocam.ws.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ocam.model.Hiker;
import com.ocam.repository.HikerRepository;
import com.ocam.ws.auth.config.ConfigurationSettings;
import com.ocam.ws.auth.exception.InvalidJwtToken;
import com.ocam.ws.auth.jwt.AccessJwtToken;
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

	@RequestMapping(value = "/auth/token", method = RequestMethod.GET,
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> refreshToken(HttpServletRequest request,
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

		AccessJwtToken newToken = tokenFactory
				.createAccessJwtToken(userContext);

		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("token", newToken.getToken());
		responseMap.put("refreshToken", tokenPayload);
		responseMap.put("email", hiker.getEmail());
		responseMap.put("username", hiker.getLogin());

		return new ResponseEntity<Object>(responseMap, HttpStatus.OK);
	}
}
