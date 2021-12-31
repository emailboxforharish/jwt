package com.gateway.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gateway.entity.UserCredential;
import com.gateway.repository.UserCredentialRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UserCredentialRepo userCredentialRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserCredential userCredential = userCredentialRepo.findByUserName(username);
		if(userCredential != null) {
			return new User(userCredential.getUserName(),userCredential.getPassword(), new ArrayList<>());	
		} else {
			throw new UsernameNotFoundException("Username Invalid!");
		}
	}
}
