package br.com.hotel.controller;

import java.net.URI;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hotel.dto.HospedeDto;
import br.com.hotel.error.UniqueKeyException;
import br.com.hotel.service.HospedeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/hospedes")
public class HospedeController {
	
	@Autowired
	private HospedeService service;
	
	@GetMapping
	public Page<HospedeDto> obterTodos(@ParameterObject @PageableDefault(size = 10) Pageable paginacao){
		Page<HospedeDto> hospedes = service.obterTodos(paginacao);
		return hospedes;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<HospedeDto> obterPorId(@PathVariable @NotNull Long id){
		HospedeDto hospede = service.obterPorId(id);
		return ResponseEntity.ok(hospede);
	}
	
	@GetMapping("/documento/{documento}")
	public ResponseEntity<HospedeDto> obterPorDocumento(@PathVariable @NotNull String documento){
		HospedeDto hospede = service.obterPorDocumento(documento);
		return ResponseEntity.ok(hospede);
	}
	
	@GetMapping("/presentes")
	public Page<HospedeDto> obterHospedesPresentes(@ParameterObject @PageableDefault(size = 3) Pageable paginacao){
		return service.obterPresentes(paginacao);
	}
	
	@GetMapping("/ausentes")
	public Page<HospedeDto> obterHospedesAusentes(@ParameterObject @PageableDefault(size = 3) Pageable paginacao) {
		return service.obterAusentes(paginacao);
	}
	
	@PostMapping
	public ResponseEntity<HospedeDto> cadastrarHospede(@RequestBody @Valid HospedeDto hospede, UriComponentsBuilder uri) throws UniqueKeyException {
		if(service.obterPorDocumento(hospede.getDocumento()) != null) {
			throw new UniqueKeyException("Já existe um hospede com o documento " + hospede.getDocumento() + " cadastrado!");
		} else {
			HospedeDto hospedeCriado = service.cadastrarHospede(hospede);	
			URI endereco = uri.path("hospedes/{id}").buildAndExpand(hospedeCriado.getId()).toUri();		
			return ResponseEntity.created(endereco).body(hospedeCriado);
		}		
	}
	
}
