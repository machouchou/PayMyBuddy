package com.paymybuddy.model;
import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class Response {
	private HttpStatus status;
	private Object data;
	private String error;
}
