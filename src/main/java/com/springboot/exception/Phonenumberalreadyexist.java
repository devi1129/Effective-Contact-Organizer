package com.springboot.exception;

public class Phonenumberalreadyexist extends RuntimeException {
	
	private String message;

	public Phonenumberalreadyexist(String message) {
		super(message);

	}

}


