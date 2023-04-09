package br.com.hotel.service;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.hotel.dto.TabelaValoresDto;
import br.com.hotel.model.DiasSemana;
import br.com.hotel.model.TabelaValores;
import br.com.hotel.repository.TabelaValoresRepository;

@SpringBootTest
class TabelaValoresServiceTest {

	@InjectMocks
	private TabelaValoresService service;

	@Mock
	private TabelaValoresRepository repository;

	@Spy
	private ModelMapper modelMapper;
	
	private TabelaValores semana;
	private TabelaValores fimDeSemana;
	private TabelaValoresDto semanaDto;
	private TabelaValoresDto fimDeSemanaDto;
	private Pageable paginacao;
	private Page page;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		startTabelaValores();
	}

	@Test
	void testListaTodos() {
		Mockito.when(repository.findAll(paginacao)).thenReturn(page);
		
		Page<TabelaValoresDto> response = service.listaTodos(paginacao);
		Assertions.assertNotNull(response);
		Assertions.assertEquals(TabelaValoresDto.class, response.getContent().get(0).getClass());
		Assertions.assertEquals(2, response.getSize());
		Assertions.assertEquals(1L, response.getContent().get(0).getId());
		Assertions.assertEquals(DiasSemana.SEGUNDA, response.getContent().get(0).getDiaInicio());
		Assertions.assertEquals(DiasSemana.SEXTA, response.getContent().get(0).getDiaFim());
		Assertions.assertEquals(BigDecimal.valueOf(120.00), response.getContent().get(0).getValorDiaria());
		Assertions.assertEquals(BigDecimal.valueOf(15.00), response.getContent().get(0).getValorVaga());
		
		Assertions.assertEquals(2L, response.getContent().get(1).getId());
		Assertions.assertEquals(DiasSemana.SABADO, response.getContent().get(1).getDiaInicio());
		Assertions.assertEquals(DiasSemana.DOMINGO, response.getContent().get(1).getDiaFim());
		Assertions.assertEquals(BigDecimal.valueOf(150.00), response.getContent().get(1).getValorDiaria());
		Assertions.assertEquals(BigDecimal.valueOf(20.00), response.getContent().get(1).getValorVaga());
	}

	private void startTabelaValores() {
		semana = new TabelaValores(1L, DiasSemana.SEGUNDA, DiasSemana.SEXTA, BigDecimal.valueOf(120.00), BigDecimal.valueOf(15.00));
		fimDeSemana = new TabelaValores(2L, DiasSemana.SABADO, DiasSemana.DOMINGO, BigDecimal.valueOf(150.00), BigDecimal.valueOf(20.00));
		semanaDto = new TabelaValoresDto(1L, DiasSemana.SEGUNDA, DiasSemana.SEXTA, BigDecimal.valueOf(120.00), BigDecimal.valueOf(15.00));
		fimDeSemanaDto = new TabelaValoresDto(2L, DiasSemana.SABADO, DiasSemana.DOMINGO, BigDecimal.valueOf(150.00), BigDecimal.valueOf(20.00));
		paginacao = Pageable.ofSize(10);
		page = new PageImpl(List.of(semana, fimDeSemana));
	}

}
