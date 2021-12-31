package com.gateway.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import com.gateway.dto.ErrorMessageDTO;

import io.jsonwebtoken.SignatureException;

@ControllerAdvice
public class ControllerExceptionHandler {

	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessageDTO> globalExceptionHandler(Exception ex, WebRequest request){
		
		ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO(
		        HttpStatus.INTERNAL_SERVER_ERROR.value(),
		        new Date(),
		        ex.getMessage(),
		        request.getDescription(false));
		return new ResponseEntity<ErrorMessageDTO>(errorMessageDTO, HttpStatus.BAD_REQUEST);
	}
}
