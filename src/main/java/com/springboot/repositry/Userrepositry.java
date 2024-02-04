package com.springboot.repositry;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.entity.Userdetails;


public interface Userrepositry extends JpaRepository<Userdetails, Long>{

	
	    boolean existsByEmail(String email);

   Userdetails findByEmail(String email);
	    
	// Optional<Userdetails>   findByEmail(String email);
   
   Userdetails findByResettoken(String resettoken);
	   
}
