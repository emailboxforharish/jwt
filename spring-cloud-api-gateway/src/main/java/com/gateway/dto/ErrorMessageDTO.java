package com.gateway.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ErrorMessageDTO {

	private int statusCode;
	private Date timestamp;
	private String message;
	private String description;

}
