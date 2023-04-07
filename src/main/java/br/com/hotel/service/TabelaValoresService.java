package br.com.hotel.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.hotel.dto.TabelaValoresDto;
import br.com.hotel.model.TabelaValores;
import br.com.hotel.repository.TabelaValoresRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TabelaValoresService {
	
	@Autowired
	private TabelaValoresRepository repository;
	
	@Autowired
    private final ModelMapper modelMapper;
	
	public List<TabelaValoresDto> listaTodos() {
		return repository.findAll().stream()
				.map(p -> modelMapper.map(p, TabelaValoresDto.class))
				.collect(Collectors.toList());
	}
	
	public TabelaValoresDto cadastraTabelaValores(TabelaValoresDto dto) {
		TabelaValores tabela = modelMapper.map(dto, TabelaValores.class);
		repository.save(tabela);
		return modelMapper.map(tabela, TabelaValoresDto.class);
	}

}
