package com.springboot.emailutils;

public interface EmailService {
	
	public String sendPasswordResetEmail(String rciepient,String resetlink);

}
