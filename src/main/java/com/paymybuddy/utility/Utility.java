package com.paymybuddy.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.paymybuddy.model.Response;

public class Utility {
	
	public Double roundAmount(Double value) {
		BigDecimal roundAmount = BigDecimal.valueOf(value);
	    roundAmount = roundAmount.setScale(2, RoundingMode.HALF_UP);
		return roundAmount.doubleValue();
	}

	public ResponseEntity<Response> createResponseWithErrors(String errorCode, String errorDescription) {
		Response response = new Response(); 
		response.setData(null);
		response.setStatus(HttpStatus.FORBIDDEN);
		response.setErrorCode(String.format("%s", errorCode));
		response.setErrorDescription(errorDescription);
		
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}
	
	public <T>void createResponseWithSuccess(Response response, T data) {
		response.setData(data);
		response.setStatus(HttpStatus.OK);
		response.setErrorCode(null);
		response.setErrorDescription(null);
	}
}
