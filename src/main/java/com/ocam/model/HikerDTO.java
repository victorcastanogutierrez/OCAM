package com.ocam.model;

public class HikerDTO {

	private String password;
	private String username;
	private String newPassword;

	public HikerDTO() {

	}

	public HikerDTO(String password, String username, String newPassword) {
		super();
		this.password = password;
		this.username = username;
		this.newPassword = newPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}