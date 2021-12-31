package com.gateway.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.dto.CredentialDTO;
import com.gateway.dto.TokenResponseDTO;
import com.gateway.service.CustomUserDetailsService;
import com.gateway.util.JwtUtil;

@RestController
@RequestMapping(path ="/authenticate")
public class AuthenticateController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	@PostMapping
	public ResponseEntity<Object> authenticate(@RequestBody CredentialDTO credentialDTO) {
		
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentialDTO.getUserName(), credentialDTO.getPassword()));
		TokenResponseDTO TokenResponseDTO = new TokenResponseDTO();
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(credentialDTO.getUserName());
		TokenResponseDTO.setToken(jwtUtil.generateToken(userDetails));
		return new ResponseEntity<Object>(TokenResponseDTO, HttpStatus.OK);	
		
	}
	
	@GetMapping(path = "/test")
	public ResponseEntity<String> test(){
		
		return new ResponseEntity<String>("All is well!", HttpStatus.OK);
	}
}
