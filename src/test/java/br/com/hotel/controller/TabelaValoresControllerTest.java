package br.com.hotel.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.hotel.dto.TabelaValoresDto;
import br.com.hotel.model.DiasSemana;
import br.com.hotel.model.TabelaValores;
import br.com.hotel.service.TabelaValoresService;

@SpringBootTest
class TabelaValoresControllerTest {
	
	@InjectMocks
	private TabelaValoresController controller;
	
	@Mock
	private TabelaValoresService service;
	
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
	@DisplayName("Quando listar todos, deverá retornar uma page")
	void testObterTodos() {
		Mockito.when(service.obterTodos(paginacao)).thenReturn(page);
		
		Page<TabelaValoresDto> response = controller.obterTodos(paginacao);
		assertNotNull(response.getContent().get(0));
		assertEquals(TabelaValoresDto.class, response.getContent().get(0).getClass());
	}
	
	private void startTabelaValores() {
		semanaDto = new TabelaValoresDto(1L, DiasSemana.SEGUNDA, DiasSemana.SEXTA, BigDecimal.valueOf(120.00), BigDecimal.valueOf(15.00));
		fimDeSemanaDto = new TabelaValoresDto(2L, DiasSemana.SABADO, DiasSemana.DOMINGO, BigDecimal.valueOf(150.00), BigDecimal.valueOf(20.00));
		paginacao = Pageable.ofSize(10);
		page = new PageImpl(List.of(semanaDto, fimDeSemanaDto));
	}

}
