package com.ocam.util;

import org.springframework.http.HttpStatus;

/**
 * Clase que encapsula la lógica para enviar un mensaje de error al cliente que
 * consuma el servicio REST.
 * 
 * @author Victor
 *
 */
public class ApiError {

	private HttpStatus status;
	private String message;

	public ApiError(HttpStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
