package br.com.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import br.com.hotel.repository.CheckInRepository;

@RestController
public class CheckInController {
	
	@Autowired
	private CheckInRepository repository;
}
