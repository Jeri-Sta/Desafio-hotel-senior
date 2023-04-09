package br.com.hotel.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.hotel.dto.HospedeDto;
import br.com.hotel.error.UniqueKeyException;
import br.com.hotel.model.Hospede;
import br.com.hotel.repository.HospedeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HospedeService {	
	
	@Autowired
	private HospedeRepository repository;
	
	@Autowired
    private ModelMapper modelMapper;
	
	public Page<HospedeDto> obterTodos(Pageable paginacao) {
        return repository.findAll(paginacao)
        		.map(p -> modelMapper.map(p, HospedeDto.class));
    }
	
	public HospedeDto obterPorId(Long id) {
        Optional<Hospede> optional = repository.findById(id);;
        return modelMapper.map(optional.get(), HospedeDto.class);
    }
	
	public HospedeDto cadastrarHospede(HospedeDto dto) {
		Hospede hospede = modelMapper.map(dto, Hospede.class);  
        return modelMapper.map(hospede, HospedeDto.class);
    }
	
	public HospedeDto obterPorDocumento(String documento) {
		Hospede hospede = repository.findByDocumento(documento);
		
		return modelMapper.map(hospede, HospedeDto.class);
	}

	public Page<HospedeDto> obterPresentes(Pageable paginacao) {
		return repository.obterPresentes(paginacao)
				.map(p -> modelMapper.map(p, HospedeDto.class));
	}
	
	public Page<HospedeDto> obterAusentes(Pageable paginacao) {
		return repository.obterAusentes(paginacao)
				.map(p -> modelMapper.map(p, HospedeDto.class));
	}

}
