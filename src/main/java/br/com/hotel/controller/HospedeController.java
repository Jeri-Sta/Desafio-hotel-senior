package br.com.hotel.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hotel.dto.HospedeDto;
import br.com.hotel.service.HospedeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/hospedes")
public class HospedeController {
	
	@Autowired
	private HospedeService service;
	
	@GetMapping
	public List<HospedeDto> listarTodos(){
		List<HospedeDto> hospedes = service.obterTodos();
		return hospedes;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<HospedeDto> listarPorId(@PathVariable @NotNull Long id){
		HospedeDto hospede = service.obterPorId(id);
		return ResponseEntity.ok(hospede);
	}
	
	@GetMapping("/documento/{documento}")
	public ResponseEntity<HospedeDto> listarPorDocumento(@PathVariable @NotNull String documento){
		HospedeDto hospede = service.obterPorDocumento(documento);
		return ResponseEntity.ok(hospede);
	}
	
	@PostMapping
	public ResponseEntity<HospedeDto> criaHospede(@RequestBody @Valid HospedeDto hospede, UriComponentsBuilder uri) {
		HospedeDto hospedeCriado = service.criarHospede(hospede);	
		URI endereco = uri.path("hospedes/{id}").buildAndExpand(hospedeCriado.getId()).toUri();
		
		return ResponseEntity.created(endereco).body(hospedeCriado);
	}
}
