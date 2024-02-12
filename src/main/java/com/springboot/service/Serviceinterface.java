package com.springboot.service;

import org.springframework.web.multipart.MultipartFile;

import com.springboot.entity.Contact;
import com.springboot.entity.Userdetails;

import jakarta.validation.Valid;



public interface Serviceinterface {

	void saveregister(Userdetails user);

	void savecontact( Contact contact, Userdetails u, MultipartFile file);

	void updatecontact(Contact contact, Userdetails u, MultipartFile file, Long cid);

	void deletecontact(Userdetails user, Contact contact);

	void saveuserprofile(Userdetails u, MultipartFile file);

	void deleteuser(Userdetails user);
	  String getBaseUrl();
	  
	  public void sendPasswordResetEmail(String userEmail);
}
