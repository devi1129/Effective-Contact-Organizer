package com.springboot.security;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.entity.Userdetails;
import com.springboot.repositry.Userrepositry;



@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private Userrepositry ur;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Userdetails u=ur.findByEmail(email);
		if(u==null)
		{
			throw new UsernameNotFoundException("Could not Found User");
		}
		
		CustomUserDetails cu=new CustomUserDetails(u);
		
		return cu;
	}

   
}
