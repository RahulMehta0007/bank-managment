package com.bank.exception;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<Object> authenticationFailedExceptionHandler(AuthenticationException ex) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("message", ex.getMessage());
		return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> globalExceptionHandler(Exception ex) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("message", ex.getMessage());
		return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
