package br.com.hotel.controller;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hotel.dto.CheckInDto;
import br.com.hotel.dto.HospedeDto;
import br.com.hotel.error.ResourceNotFoundException;
import br.com.hotel.model.CheckIn;
import br.com.hotel.model.DiasSemana;
import br.com.hotel.model.Hospede;
import br.com.hotel.model.TabelaValores;
import br.com.hotel.repository.HospedeRepository;
import br.com.hotel.repository.TabelaValoresRepository;
import br.com.hotel.service.CheckInService;

@SpringBootTest
class CheckInControllerTest {

	@InjectMocks
	private CheckInController controller;
	@Mock
	private CheckInService service;
	@Mock
	private HospedeRepository hospedeRepository;
	@Mock
	private TabelaValoresRepository tabelaValoresRepository;
	@Mock
	private ModelMapper modelMapper;
	@Mock
	private UriComponentsBuilder uriComponentsBuilder;
	@Mock
	private UriComponents uriComponents;

	private Hospede hospede1;
	private Hospede hospede2;
	private HospedeDto hospedeDto1;
	private HospedeDto hospedeDto2;
	private Optional<Hospede> optionalHospede;
	private CheckIn checkIn1;
	private CheckIn checkIn2;
	private CheckInDto checkInDto1;
	private CheckInDto checkInDto2;
	private Optional<CheckIn> optionalCheckIn;
	private Pageable paginacao;
	private Page page;
	private List<TabelaValores> tabelaValores = new ArrayList();

	private static Long id1 = 1L;
	private static Long id2 = 2L;
	private static LocalDateTime dataInicio1 = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
	private static LocalDateTime dataInicio2 = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
	private static LocalDateTime dataApos1 = dataInicio1.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
	private static LocalDateTime dataApos2 = dataInicio2.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
	private static LocalDateTime dataFim1 = LocalDateTime.of(dataApos1.getYear(), dataApos1.getMonthValue(),
			dataApos1.getDayOfMonth(), 15, 30);
	private static LocalDateTime dataFim2 = LocalDateTime.of(dataApos2.getYear(), dataApos2.getMonthValue(),
			dataApos2.getDayOfMonth(), 18, 30);
	private static boolean adicionaVeiculo1 = true;
	private static boolean adicionaVeiculo2 = true;
	private static BigDecimal valorTotal1 = BigDecimal.valueOf(270.00);
	private static BigDecimal valorTotal2 = BigDecimal.valueOf(475.00);

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		startCheckIn();
	}

	@Test
	@DisplayName("Deverá retornar uma page de checkIn's")
	void testObterTodos() {
		Mockito.when(service.obterTodos(paginacao)).thenReturn(page);
		Mockito.when(modelMapper.map(any(Page.class), CheckInDto.class)).thenReturn(checkInDto1);

		Page<CheckInDto> response = controller.obterTodos(paginacao);
		assertNotNull(response.getContent().get(0));
		assertEquals(CheckInDto.class, response.getContent().get(0).getClass());
	}

	@Test
	@DisplayName("Deve retornar uma ReponseEntity do checkIn")
	void testObterPorId() {
		Mockito.when(service.obterPorId(Mockito.anyLong())).thenReturn(checkInDto1);

		ResponseEntity<CheckInDto> response = controller.obterPorId(1L);
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(checkInDto1.getId(), response.getBody().getId());
		assertEquals(checkInDto1.getHospede(), response.getBody().getHospede());
		assertEquals(checkInDto1.getDataEntrada(), response.getBody().getDataEntrada());
		assertEquals(checkInDto1.getDataSaida(), response.getBody().getDataSaida());
		assertEquals(checkInDto1.getValorTotalHospedagem(), response.getBody().getValorTotalHospedagem());
	}

	@Test
	@DisplayName("Deverá retornar uma ReponseEntity de um hospede filtrando pelo nome")
	void testObterHospedePorFiltroNome() {
		Mockito.when(service.obterHospedePorFiltro("nome", hospedeDto1.getNome())).thenReturn(hospedeDto1);

		ResponseEntity<HospedeDto> response = controller.obterHospedePorFiltro("nome", hospedeDto1.getNome());
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(hospedeDto1.getId(), response.getBody().getId());
		assertEquals(hospedeDto1.getNome(), response.getBody().getNome());
		assertEquals(hospedeDto1.getDocumento(), response.getBody().getDocumento());
		assertEquals(hospedeDto1.getTelefone(), response.getBody().getTelefone());
	}

	@Test
	@DisplayName("Deverá retornar uma ReponseEntity de um hospede filtrando pelo documento")
	void testObterHospedePorFiltroDocumento() {
		Mockito.when(service.obterHospedePorFiltro("documento", hospedeDto1.getDocumento())).thenReturn(hospedeDto1);

		ResponseEntity<HospedeDto> response = controller.obterHospedePorFiltro("documento", hospedeDto1.getDocumento());
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(hospedeDto1.getId(), response.getBody().getId());
		assertEquals(hospedeDto1.getNome(), response.getBody().getNome());
		assertEquals(hospedeDto1.getDocumento(), response.getBody().getDocumento());
		assertEquals(hospedeDto1.getTelefone(), response.getBody().getTelefone());
	}

	@Test
	@DisplayName("Deverá retornar uma ReponseEntity de um hospede filtrando pelo telefone")
	void testObterHospedePorFiltroTelefone() {
		Mockito.when(service.obterHospedePorFiltro("telefone", hospedeDto1.getTelefone())).thenReturn(hospedeDto1);

		ResponseEntity<HospedeDto> response = controller.obterHospedePorFiltro("telefone", hospedeDto1.getTelefone());
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(hospedeDto1.getId(), response.getBody().getId());
		assertEquals(hospedeDto1.getNome(), response.getBody().getNome());
		assertEquals(hospedeDto1.getDocumento(), response.getBody().getDocumento());
		assertEquals(hospedeDto1.getTelefone(), response.getBody().getTelefone());
	}

	@Test
	@DisplayName("Deve realizar o checkIn e calcular os valores da diaria para o hospede com estacionamento de dia de semana")
	void testRealizarCheckIn() {
		Mockito.when(service.realizarCheckIn(checkInDto1)).thenReturn(checkInDto1);
		Mockito.when(service.obterHospedePorId(Mockito.anyLong())).thenReturn(hospedeDto1);

		UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("http").host("localhost").port("8080");
		ResponseEntity<CheckInDto> response = controller.realizarCheckIn(checkInDto1, builder);
		Mockito.when(service.obterHospedePorId(Mockito.anyLong())).thenReturn(hospedeDto1);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getHeaders().get("Location"));
		Assertions.assertNotNull(response);
		Assertions.assertEquals(CheckInDto.class, response.getBody().getClass());
		Assertions.assertEquals(id1, response.getBody().getId());
		Assertions.assertEquals(hospede1, response.getBody().getHospede());
		Assertions.assertEquals(dataInicio1, response.getBody().getDataEntrada());
		Assertions.assertEquals(dataFim1, response.getBody().getDataSaida());
		Assertions.assertEquals(adicionaVeiculo1, response.getBody().isAdicionaVeiculo());
		Assertions.assertEquals(valorTotal1, response.getBody().getValorTotalHospedagem());
	}

	@Test
	@DisplayName("Deve realizar o checkIn e calcular os valores da diaria para o hospede com estacionamento, no final de semana e saída após horário")
	void testRealizarCheckInFimDeSemana() {
		Mockito.when(service.realizarCheckIn(checkInDto2)).thenReturn(checkInDto2);
		Mockito.when(service.obterHospedePorId(Mockito.anyLong())).thenReturn(hospedeDto2);
		
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("http").host("localhost").port("8080");
		ResponseEntity<CheckInDto> response = controller.realizarCheckIn(checkInDto2, builder);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getHeaders().get("Location"));
		Assertions.assertNotNull(response);
		Assertions.assertEquals(CheckInDto.class, response.getBody().getClass());
		Assertions.assertEquals(id2, response.getBody().getId());
		Assertions.assertEquals(hospede2, response.getBody().getHospede());
		Assertions.assertEquals(dataInicio2, response.getBody().getDataEntrada());
		Assertions.assertEquals(dataFim2, response.getBody().getDataSaida());
		Assertions.assertEquals(adicionaVeiculo2, response.getBody().isAdicionaVeiculo());
		Assertions.assertEquals(valorTotal2, response.getBody().getValorTotalHospedagem());
	}

	@Test
	@DisplayName("Deve realizar o checkIn e calcular os valores da diaria para o hospede com valor anterior preenchido")
	void testRealizarCheckInComValorAnterior() {
		checkIn1.getHospede().setValorUltimaHospedagem(BigDecimal.valueOf(120.00));
		checkIn1.setValorTotalHospedagem(BigDecimal.valueOf(390.00));
		Mockito.when(service.realizarCheckIn(checkInDto1)).thenReturn(checkInDto1);
		Mockito.when(service.obterHospedePorId(Mockito.anyLong())).thenReturn(hospedeDto1);
		
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("http").host("localhost").port("8080");
		ResponseEntity<CheckInDto> response = controller.realizarCheckIn(checkInDto1, builder);

		Assertions.assertNotNull(response);
		Assertions.assertEquals(CheckInDto.class, response.getBody().getClass());
		Assertions.assertEquals(id1, response.getBody().getId());
		Assertions.assertEquals(hospede1, response.getBody().getHospede());
		Assertions.assertEquals(dataInicio1, response.getBody().getDataEntrada());
		Assertions.assertEquals(dataFim1, response.getBody().getDataSaida());
		Assertions.assertEquals(adicionaVeiculo1, response.getBody().isAdicionaVeiculo());
		Assertions.assertEquals(valorTotal1, response.getBody().getValorTotalHospedagem());
	}
	
	@Test
	@DisplayName("Deve retornar uma exception de Resource Not Found")
	void testRealizarCheckInComHospedeNaoCadastrado() {
		Mockito.when(service.realizarCheckIn(checkInDto1)).thenReturn(checkInDto1);
		Mockito.when(service.obterHospedePorId(1L)).thenReturn(null);
		
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("http").host("localhost").port("8080");
		ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> controller.realizarCheckIn(checkInDto1, builder));
		assertEquals("Hospede nao encontrado. ID: " + checkInDto1.getHospede().getId(), thrown.getMessage());
		
	}

	@Test
	@DisplayName("Deverá excluir um checkIn e retornar com o status 201")
	void testExcluirCheckIn() {
		Mockito.doNothing().when(service).excluirCheckIn(1L);

		ResponseEntity<CheckInDto> response = controller.excluirCheckIn(1L);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	private void startCheckIn() {
		hospede1 = new Hospede(id1, "Joao", "77777777777", "44555559999", BigDecimal.ZERO, BigDecimal.ZERO);
		hospede2 = new Hospede(id2, "Maria", "6666666666", "99999999999", BigDecimal.ZERO, BigDecimal.ZERO);
		hospedeDto1 = new HospedeDto(id1, "Joao", "77777777777", "44555559999", BigDecimal.ZERO, BigDecimal.ZERO);
		hospedeDto2 = new HospedeDto(id2, "Maria", "6666666666", "99999999999", BigDecimal.ZERO, BigDecimal.ZERO);
		checkIn1 = new CheckIn(id1, hospede1, dataInicio1, dataFim1, adicionaVeiculo1, valorTotal1);
		checkIn2 = new CheckIn(id2, hospede2, dataInicio2, dataFim2, adicionaVeiculo2, valorTotal2);
		checkInDto1 = new CheckInDto(id1, hospede1, dataInicio1, dataFim1, adicionaVeiculo1, valorTotal1);
		checkInDto2 = new CheckInDto(id2, hospede2, dataInicio2, dataFim2, adicionaVeiculo2, valorTotal2);
		optionalHospede = Optional.of(hospede1);
		optionalCheckIn = Optional.of(checkIn1);
		paginacao = Pageable.ofSize(10);
		page = new PageImpl<>(List.of(checkInDto1, checkInDto2));
		tabelaValores.add(new TabelaValores(1L, DiasSemana.SEGUNDA, DiasSemana.SEXTA, BigDecimal.valueOf(120.00),
				BigDecimal.valueOf(15.00)));
		tabelaValores.add(new TabelaValores(2L, DiasSemana.SABADO, DiasSemana.DOMINGO, BigDecimal.valueOf(150.00),
				BigDecimal.valueOf(20.00)));
	}

}
