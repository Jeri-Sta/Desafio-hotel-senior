package br.com.hotel.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.hotel.dto.TabelaValoresDto;
import br.com.hotel.service.TabelaValoresService;

@RestController
@RequestMapping("/tabelaValores")
public class TabelaValoresController {
	
	@Autowired
	private TabelaValoresService service;
	
	@GetMapping
	public Page<TabelaValoresDto> listaTodos (@ParameterObject @PageableDefault(size = 10) Pageable paginacao){
		return service.listaTodos(paginacao);
	}

}
