package com.ocam.ws.auth.model;

import org.apache.commons.lang3.StringUtils;

public class UserContext {
	private final String username;
	private final String email;

	private UserContext(String username, String email) {
		this.username = username;
		this.email = email;
	}

	public static UserContext create(String username, String email) {
		if (StringUtils.isBlank(username)) {
			throw new IllegalArgumentException(
					"Username is blank: " + username);
		}
		return new UserContext(username, email);
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}
}
