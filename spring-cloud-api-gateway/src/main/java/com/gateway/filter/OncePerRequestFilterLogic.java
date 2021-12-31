package com.gateway.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.dto.ErrorMessageDTO;
import com.gateway.service.CustomUserDetailsService;
import com.gateway.util.JwtUtil;

import io.jsonwebtoken.SignatureException;

@Component
public class OncePerRequestFilterLogic extends OncePerRequestFilter{

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String userName = null;
		String jwtToken = null;
		String Authorization =request.getHeader("Authorization");
		
		if(Authorization != null && Authorization.startsWith("Bearer ")) {
			
			jwtToken = Authorization.substring(7);
			try {
				userName = jwtUtil.extractUsername(jwtToken);
				if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					
					UserDetails userDetails =customUserDetailsService.loadUserByUsername(userName);
					
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword());
					usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					
				} else {
					//@ControllerAdvice can't to be used. It's because the filter comes before Servlet is invoked. ControllerAdvice only applies to the Controller classes.
					ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO(
					        HttpStatus.INTERNAL_SERVER_ERROR.value(),
					        new Date(),
					        "Invalid Authorization Key!",
					        "Invalid Authorization Key!");
					response.getWriter().write(convertObjectToJson(errorMessageDTO));
				}
				filterChain.doFilter(request, response);
			} catch(Exception ex) {
				
				//@ControllerAdvice can't to be used. It's because the filter comes before Servlet is invoked. ControllerAdvice only applies to the Controller classes.
				ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO(
				        HttpStatus.INTERNAL_SERVER_ERROR.value(),
				        new Date(),
				        ex.getMessage(),
				        "Invalid Authorization Key!");
				response.getWriter().write(convertObjectToJson(errorMessageDTO));
			}
			
			
		} else {
			filterChain.doFilter(request, response);
		}
		
	}
	 private String convertObjectToJson(Object object) throws JsonProcessingException {
	        if (object == null) {
	            return null;
	        }
	        ObjectMapper mapper = new ObjectMapper();
	        return mapper.writeValueAsString(object);
	    }
}
