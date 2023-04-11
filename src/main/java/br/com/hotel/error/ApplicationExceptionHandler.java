package br.com.hotel.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.hotel.controller.DefaultError;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity handleException(Exception e) {
		DefaultError error = new DefaultError(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
		return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
	}
}
