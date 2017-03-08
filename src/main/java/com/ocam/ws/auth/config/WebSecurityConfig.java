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
import com.ocam.ws.auth.util.Constants;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
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
	private CORSFilter filtro;

	@Autowired
	private AuthenticationManager authenticationManager;

	protected AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter()
			throws Exception {
		AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(
				Constants.FORM_BASED_LOGIN_ENTRY_POINT, successHandler,
				failureHandler);
		filter.setAuthenticationManager(this.authenticationManager);
		return filter;
	}

	protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter()
			throws Exception {
		List<String> pathsToSkip = Arrays.asList(
				Constants.TOKEN_REFRESH_ENTRY_POINT,
				Constants.FORM_BASED_LOGIN_ENTRY_POINT);
		SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip,
				Constants.TOKEN_BASED_AUTH_ENTRY_POINT);
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
				.antMatchers(Constants.FORM_BASED_LOGIN_ENTRY_POINT).permitAll() // Login
				// end-point
				.antMatchers(Constants.TOKEN_REFRESH_ENTRY_POINT).permitAll() // Token
				// refresh
				// end-point
				.and().authorizeRequests()
				.antMatchers(Constants.TOKEN_BASED_AUTH_ENTRY_POINT)
				.authenticated() // Protected
				// API
				// End-points
				.and()
				.addFilterBefore(filtro,
						UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(buildAjaxLoginProcessingFilter(),
						UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(),
						UsernamePasswordAuthenticationFilter.class);
	}
}
