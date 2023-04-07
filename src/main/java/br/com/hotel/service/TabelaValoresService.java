package br.com.hotel.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.hotel.dto.TabelaValoresDto;
import br.com.hotel.repository.TabelaValoresRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TabelaValoresService {
	
	@Autowired
	private TabelaValoresRepository repository;
	
	@Autowired
    private final ModelMapper modelMapper;
	
	public Page<TabelaValoresDto> listaTodos(Pageable paginacao) {
		return repository.findAll(paginacao)
				.map(p -> modelMapper.map(p, TabelaValoresDto.class));
	}

}
