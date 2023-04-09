package br.com.hotel.controller;

import java.net.URI;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/checkIn")
public class CheckInController {
	
	@Autowired
	private CheckInService service;
	
	@GetMapping
	public Page<CheckInDto> obterTodos(@ParameterObject @PageableDefault(size = 10) Pageable paginacao) {
		return service.obterTodos(paginacao);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CheckInDto> obterPorId(@PathVariable @Valid Long id) {
		CheckInDto checkIn = service.obterPorId(id);
		return ResponseEntity.ok(checkIn);
	}
	
	@GetMapping("/hospede/{filtro}/{info}")
	public ResponseEntity<HospedeDto> obterHospedePorFiltro(@PathVariable @Valid String filtro,
							@PathVariable @Valid String info) {
		HospedeDto hospede = service.obterHospedePorFiltro(filtro, info);
		return ResponseEntity.ok(hospede);
	}
	
	@PostMapping
	public ResponseEntity<CheckInDto> realizarCheckIn(@RequestBody @Valid CheckInDto checkInDto, UriComponentsBuilder uri){
		HospedeDto hospede = service.obterHospedePorId(checkInDto.getHospede().getId());
		if(hospede == null) {
			throw new ResourceNotFoundException("Hospede não encontrado. ID: " + checkInDto.getHospede().getId());
		} else if(!hospede.getDocumento().equalsIgnoreCase(checkInDto.getHospede().getDocumento())) {
			throw new ResourceNotFoundException("Documento do hospede de ID: " + checkInDto.getHospede().getId() + " está incorreto.");
		}
		CheckInDto retorno = service.realizarCheckIn(checkInDto);
		URI endereco = uri.path("/checkIn/{id}").buildAndExpand(retorno.getId()).toUri();
		
		return ResponseEntity.created(endereco).body(retorno);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<CheckInDto> excluirCheckIn(@PathVariable @NotNull Long id) {
		service.excluirCheckIn(id);
		return ResponseEntity.noContent().build();
	}
	
}
