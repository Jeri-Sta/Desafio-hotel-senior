package br.com.hotel.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DefaultError {
	
	private String code;
	private String message;
}
