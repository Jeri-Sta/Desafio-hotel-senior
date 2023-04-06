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

import br.com.hotel.dto.CheckInDto;
import br.com.hotel.dto.HospedeDto;
import br.com.hotel.error.ResourceNotFoundException;
import br.com.hotel.service.CheckInService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/checkIn")
public class CheckInController {
	
	@Autowired
	private CheckInService service;
	
	@GetMapping
	public List<CheckInDto> listarTodos() {
		return service.obterTodos();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CheckInDto> listaPorId(@PathVariable @Valid Long id) {
		CheckInDto checkIn = service.obterPorId(id);
		return ResponseEntity.ok(checkIn);
	}
	
	@GetMapping("/hospede/{filtro}/{info}")
	public ResponseEntity<HospedeDto> listaHospedePorFiltro(@PathVariable @Valid String filtro,
							@PathVariable @Valid String info) {
		HospedeDto hospede = service.obterHospedePorFiltro(filtro, info);
		return ResponseEntity.ok(hospede);
	}
	
	@PostMapping
	public ResponseEntity<CheckInDto> realizaCheckIn(@RequestBody @Valid CheckInDto checkInDto, UriComponentsBuilder uri){
		HospedeDto hospede = service.obterHospedePorId(checkInDto.getHospede().getId());
		if(hospede == null) {
			throw new ResourceNotFoundException("Hospede nao encontrado. ID: " + checkInDto.getHospede().getId());
		}
		
		CheckInDto retorno = service.realizarCheckIn(checkInDto);
		URI endereco = uri.path("/checkIn/{id}").buildAndExpand(retorno.getId()).toUri();
		
		return ResponseEntity.created(endereco).body(retorno);
	}
	
}
