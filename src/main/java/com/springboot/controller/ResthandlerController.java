package com.springboot.controller;


import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.entity.Contact;
import com.springboot.entity.Userdetails;
import com.springboot.repositry.Contactrepositry;
import com.springboot.repositry.Userrepositry;

@RestController
@RequestMapping("/api")
public class ResthandlerController {
	@Autowired
	private Userrepositry ur;
	@Autowired
	private Contactrepositry cr;
	
	  @GetMapping("/search-contact/{query}/{page}")
	    public ResponseEntity<Page<Contact>> searchContact(
	        @PathVariable String query,
	        @PathVariable int page,
	        Principal p
	    ) {
	        Userdetails u = ur.findByEmail(p.getName());
	        Pageable pageable = PageRequest.of(page, 5,Sort.by("firstname")); // 5 contacts per page
	        Page<Contact> contacts = cr.searchContacts(u, query, pageable);

	        return new ResponseEntity<>(contacts, HttpStatus.OK);
	    }
	}

	
	

