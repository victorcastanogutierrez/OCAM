package com.ocam.ws.auth.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ocam.ws.auth.RestAuthenticationEntryPoint;
import com.ocam.ws.auth.ajax.AjaxAuthenticationProvider;
import com.ocam.ws.auth.ajax.AjaxLoginProcessingFilter;
import com.ocam.ws.auth.jwt.JwtAuthenticationProvider;
import com.ocam.ws.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import com.ocam.ws.auth.jwt.SkipPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	public static final String JWT_TOKEN_HEADER_PARAM = "ocam-token";
	public static final String FORM_BASED_LOGIN_ENTRY_POINT = "/api/auth/login";
	public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/api/**";
	public static final String TOKEN_REFRESH_ENTRY_POINT = "/api/auth/token";

	@Autowired
	private RestAuthenticationEntryPoint authenticationEntryPoint;
	@Autowired
	private AuthenticationSuccessHandler successHandler;
	@Autowired
	private AuthenticationFailureHandler failureHandler;
	@Autowired
	private AjaxAuthenticationProvider ajaxAuthenticationProvider;
	@Autowired
	private JwtAuthenticationProvider jwtAuthenticationProvider;

	@Autowired
	private AuthenticationManager authenticationManager;

	protected AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter()
			throws Exception {
		AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(
				FORM_BASED_LOGIN_ENTRY_POINT, successHandler, failureHandler);
		filter.setAuthenticationManager(this.authenticationManager);
		return filter;
	}

	protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter()
			throws Exception {
		List<String> pathsToSkip = Arrays.asList(TOKEN_REFRESH_ENTRY_POINT,
				FORM_BASED_LOGIN_ENTRY_POINT);
		SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip,
				TOKEN_BASED_AUTH_ENTRY_POINT);
		JwtTokenAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(
				failureHandler, matcher);
		filter.setAuthenticationManager(this.authenticationManager);
		return filter;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(ajaxAuthenticationProvider);
		auth.authenticationProvider(jwtAuthenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable() // We don't need CSRF for JWT based authentication
				.exceptionHandling()
				.authenticationEntryPoint(this.authenticationEntryPoint)

				.and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				.and().authorizeRequests()
				.antMatchers(FORM_BASED_LOGIN_ENTRY_POINT).permitAll() // Login
																		// end-point
				.antMatchers(TOKEN_REFRESH_ENTRY_POINT).permitAll() // Token
																	// refresh
																	// end-point
				.and().authorizeRequests()
				.antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated() // Protected
																			// API
																			// End-points
				.and()
				.addFilterBefore(buildAjaxLoginProcessingFilter(),
						UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(),
						UsernamePasswordAuthenticationFilter.class);
	}
}
