package com.springboot.exception;

public class Emailalreadyexistsexception extends RuntimeException {
	
	private String message;

	public Emailalreadyexistsexception(String message) {
		super(message);

	}

}
