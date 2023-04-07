package br.com.hotel.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hotel.dto.TabelaValoresDto;
import br.com.hotel.service.TabelaValoresService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tabelaValores")
public class TabelaValoresController {
	
	@Autowired
	private TabelaValoresService service;
	
	@GetMapping
	public List<TabelaValoresDto> listaTodos (){
		return service.listaTodos();
	}
	
	@PostMapping
	public ResponseEntity<TabelaValoresDto> criaTabelaValores(@RequestBody @Valid TabelaValoresDto dto, UriComponentsBuilder uriBuilder) {
		TabelaValoresDto retorno = service.cadastraTabelaValores(dto);
		URI endereco = uriBuilder.path("/tabelaValores/{id}").buildAndExpand(retorno.getId()).toUri();
		return ResponseEntity.created(endereco).body(retorno);
	}

}
