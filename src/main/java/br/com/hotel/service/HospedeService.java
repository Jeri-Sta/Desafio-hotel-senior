package br.com.hotel.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.hotel.dto.HospedeDto;
import br.com.hotel.model.Hospede;
import br.com.hotel.repository.HospedeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HospedeService {	
	
	@Autowired
	private HospedeRepository repository;
	
	@Autowired
    private final ModelMapper modelMapper;
	
	public List<HospedeDto> obterTodos() {
        return repository.findAll().stream()
                .map(p -> modelMapper.map(p, HospedeDto.class))
                .collect(Collectors.toList());
    }
	
	public HospedeDto obterPorId(Long id) {
        Hospede hospede = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(hospede, HospedeDto.class);
    }
	
	public HospedeDto criarHospede(HospedeDto dto) {
		Hospede hospede = modelMapper.map(dto, Hospede.class);
        repository.save(hospede);

        return modelMapper.map(hospede, HospedeDto.class);
    }
	
	public HospedeDto obterPorDocumento(String documento) {
		Hospede hospede = repository.findByDocumento(documento);
		
		return modelMapper.map(hospede, HospedeDto.class);
	}

}
