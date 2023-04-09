package br.com.hotel.controller;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hotel.dto.HospedeDto;
import br.com.hotel.model.Hospede;
import br.com.hotel.service.HospedeService;

@SpringBootTest
class HospedeControllerTest {

	@InjectMocks
	private HospedeController controller;
	@Mock
	private HospedeService service;
	@Mock
	private ModelMapper modelMapper;
	@Mock
	private UriComponentsBuilder uriComponentsBuilder;
	@Mock
	private UriComponents uriComponents;

	private Hospede hospede;
	private HospedeDto hospedeDto;
	private Optional<Hospede> optionalHospede;
	private Pageable paginacao;
	private Page page;

	private static Long id = 1L;
	private static String nome = "Joao";
	private static String documento = "11144455599";
	private static String telefone = "77899999999";

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		startHospede();
	}

	@Test
	@DisplayName("Deverá retornar uma page dos hospedes")
	void testListarTodos() {
		Mockito.when(service.obterTodos(paginacao)).thenReturn(page);
		Mockito.when(modelMapper.map(any(Page.class), HospedeDto.class)).thenReturn(hospedeDto);

		Page<HospedeDto> response = controller.listarTodos(paginacao);
		assertNotNull(response.getContent().get(0));
		assertEquals(HospedeDto.class, response.getContent().get(0).getClass());
	}

	@Test
	@DisplayName("Deve retornar uma ReponseEntity do hospede")
	void testListarPorId() {
		Mockito.when(service.obterPorId(Mockito.anyLong())).thenReturn(hospedeDto);

		ResponseEntity<HospedeDto> response = controller.listarPorId(1L);
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(id, response.getBody().getId());
		assertEquals(nome, response.getBody().getNome());
		assertEquals(documento, response.getBody().getDocumento());
		assertEquals(telefone, response.getBody().getTelefone());
	}

	@Test
	@DisplayName("Deve retornar uma ResponseEntity do hospede ao buscar por documento")
	void testListarPorDocumento() {
		Mockito.when(service.obterPorDocumento(documento)).thenReturn(hospedeDto);

		ResponseEntity<HospedeDto> response = controller.listarPorDocumento(documento);
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(id, response.getBody().getId());
		assertEquals(nome, response.getBody().getNome());
		assertEquals(documento, response.getBody().getDocumento());
		assertEquals(telefone, response.getBody().getTelefone());
	}

	@Test
	@DisplayName("Deve retornar uma page de hospedes ao buscar pelos presentes")
	void testBuscaHospedesPresentes() {
		Mockito.when(service.obterPresentes(paginacao)).thenReturn(page);

		Page<HospedeDto> response = controller.buscaHospedesPresentes(paginacao);
		assertNotNull(response.getContent().get(0));
		assertEquals(HospedeDto.class, response.getContent().get(0).getClass());
		assertEquals(id, response.getContent().get(0).getId());
		assertEquals(nome, response.getContent().get(0).getNome());
		assertEquals(documento, response.getContent().get(0).getDocumento());
		assertEquals(telefone, response.getContent().get(0).getTelefone());
	}

	@Test
	@DisplayName("Deve retornar uma page de hospedes ao buscar pelos ausentes")
	void testBuscaHospedesAusentes() {
		Mockito.when(service.obterAusentes(paginacao)).thenReturn(page);

		Page<HospedeDto> response = controller.buscaHospedesAusentes(paginacao);
		assertNotNull(response.getContent().get(0));
		assertEquals(HospedeDto.class, response.getContent().get(0).getClass());
		assertEquals(id, response.getContent().get(0).getId());
		assertEquals(nome, response.getContent().get(0).getNome());
		assertEquals(documento, response.getContent().get(0).getDocumento());
		assertEquals(telefone, response.getContent().get(0).getTelefone());
	}

	@Test
	@DisplayName("Deverá criar um hospede e retornar com o status 201")
	void testCriaHospede() throws Exception {
		Mockito.when(service.criarHospede(hospedeDto)).thenReturn(hospedeDto);
		
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("http").host("localhost").port("8080");		
		ResponseEntity<HospedeDto> response = controller.criaHospede(hospedeDto,builder);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getHeaders().get("Location"));
	}

	private void startHospede() {
		hospede = new Hospede(id, nome, documento, telefone, BigDecimal.ZERO, BigDecimal.ZERO);
		hospedeDto = new HospedeDto(id, nome, documento, telefone, BigDecimal.ZERO, BigDecimal.ZERO);
		optionalHospede = Optional.of(hospede);
		paginacao = Pageable.ofSize(10);
		page = new PageImpl(List.of(hospedeDto));
	}

}