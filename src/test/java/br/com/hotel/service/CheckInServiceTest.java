package br.com.hotel.service;

import static org.mockito.Mockito.times;

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
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.hotel.dto.CheckInDto;
import br.com.hotel.dto.HospedeDto;
import br.com.hotel.model.CheckIn;
import br.com.hotel.model.DiasSemana;
import br.com.hotel.model.Hospede;
import br.com.hotel.model.TabelaValores;
import br.com.hotel.repository.CheckInRepository;
import br.com.hotel.repository.HospedeRepository;
import br.com.hotel.repository.TabelaValoresRepository;

@SpringBootTest
class CheckInServiceTest {
	
	@InjectMocks
	private CheckInService service;
	
	@Mock
	private CheckInRepository checkInRepository;
	
	@Mock
	private HospedeRepository hospedeRepository;
	
	@Mock
	private TabelaValoresRepository tabelaValoresRepository;
	
	@Spy
	private ModelMapper modelMapper;
	
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
	private static LocalDateTime dataInicio2 = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.SATURDAY)).withHour(15);
	private static LocalDateTime dataApos1 = dataInicio1.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
	private static LocalDateTime dataApos2 = dataInicio2.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
	private static LocalDateTime dataFim1 = LocalDateTime.of(dataApos1.getYear(), dataApos1.getMonthValue(), dataApos1.getDayOfMonth(), 15, 30);
	private static LocalDateTime dataFim2 = LocalDateTime.of(dataApos2.getYear(), dataApos2.getMonthValue(), dataApos2.getDayOfMonth(), 18, 30);
	private static boolean adicionaVeiculo1 = true;
	private static boolean adicionaVeiculo2 = true;
	private static BigDecimal valorTotal1 = BigDecimal.valueOf(270.00);
	private static BigDecimal valorTotal2 = BigDecimal.valueOf(475.00);
	
	
	@BeforeEach
	void setUp(){
		MockitoAnnotations.openMocks(this);
		startCheckIn();
	}

	@Test
	@DisplayName("Deve retornar todos os hospedes em uma page")
	void testObterTodos() {
		Mockito.when(checkInRepository.findAll(paginacao)).thenReturn(page);
		
		Page<CheckInDto> response = service.obterTodos(paginacao);
		
		Assertions.assertNotNull(response);
		Assertions.assertEquals(CheckInDto.class, response.getContent().get(0).getClass());
		Assertions.assertEquals(2, response.getSize());
		Assertions.assertEquals(id1, response.getContent().get(0).getId());
	}

	@Test
	@DisplayName("Deve retornar o checkIn pelo seu ID")
	void testObterPorId() {
		Mockito.when(checkInRepository.findById(1L)).thenReturn(optionalCheckIn);
		
		CheckInDto response = service.obterPorId(id1);
		Assertions.assertNotNull(response);
		Assertions.assertEquals(CheckInDto.class, response.getClass());	
		Assertions.assertEquals(id1, response.getId());
		Assertions.assertEquals(hospedeDto1.getDocumento(), response.getHospede().getDocumento());
		Assertions.assertEquals(dataInicio1, response.getDataEntrada());
		Assertions.assertEquals(dataFim1, response.getDataSaida());
		Assertions.assertEquals(adicionaVeiculo1, response.isAdicionaVeiculo());
		Assertions.assertEquals(valorTotal1, response.getValorTotalHospedagem());
	}

	@Test
	@DisplayName("Deve retornar o hospede pelo seu ID")
	void testObterHospedePorId() {
		Mockito.when(hospedeRepository.findById(Mockito.anyLong())).thenReturn(optionalHospede);
		Mockito.when(modelMapper.map(optionalHospede, HospedeDto.class)).thenReturn(hospedeDto1);
		
		HospedeDto response = service.obterHospedePorId(id1);
		Assertions.assertNotNull(response);
		Assertions.assertEquals(HospedeDto.class, response.getClass());	
		Assertions.assertEquals(id1, response.getId());
		Assertions.assertEquals(hospede1.getNome(), response.getNome());
		Assertions.assertEquals(hospede1.getDocumento(), response.getDocumento());
		Assertions.assertEquals(hospede1.getTelefone(), response.getTelefone());
	}
	
	@Test
	@DisplayName("Deve retornar o hospede nulo o ID não existir")
	void testObterHospedePorIdNaoExistente() {
		Mockito.when(hospedeRepository.findById(Mockito.anyLong())).thenReturn(optionalHospede.empty());
		
		HospedeDto response = service.obterHospedePorId(id1);
		Assertions.assertNull(response);
	}

	@Test
	@DisplayName("Deve buscar o hospede por nome, telefone e documento")
	void testObterHospedePorFiltro() {
		Mockito.when(hospedeRepository.findByNome(Mockito.anyString())).thenReturn(hospede1);
		Mockito.when(hospedeRepository.findByTelefone(Mockito.anyString())).thenReturn(hospede1);
		Mockito.when(hospedeRepository.findByDocumento(Mockito.anyString())).thenReturn(hospede1);
		
		HospedeDto response = service.obterHospedePorFiltro("nome", hospede1.getNome());
		Assertions.assertNotNull(response);
		Assertions.assertEquals(HospedeDto.class, response.getClass());	
		Assertions.assertEquals(id1, response.getId());
		Assertions.assertEquals(hospede1.getNome(), response.getNome());
		
		response = service.obterHospedePorFiltro("telefone", hospede1.getNome());
		Assertions.assertNotNull(response);
		Assertions.assertEquals(HospedeDto.class, response.getClass());	
		Assertions.assertEquals(id1, response.getId());
		Assertions.assertEquals(hospede1.getNome(), response.getNome());
		
		response = service.obterHospedePorFiltro("documento", hospede1.getNome());
		Assertions.assertNotNull(response);
		Assertions.assertEquals(HospedeDto.class, response.getClass());	
		Assertions.assertEquals(id1, response.getId());
		Assertions.assertEquals(hospede1.getNome(), response.getNome());
	}

	@Test
	@DisplayName("Deve realizar o checkIn e calcular os valores da diaria para o hospede com estacionamento de dia de semana")
	void testRealizarCheckIn() {
		Mockito.when(checkInRepository.save(checkIn1)).thenReturn(checkIn1);
		Mockito.when(hospedeRepository.atualizaValores(Mockito.anyLong(), Mockito.any(BigDecimal.class), Mockito.any(BigDecimal.class))).thenReturn(1);
		Mockito.when(tabelaValoresRepository.findAll()).thenReturn(tabelaValores);
		
		CheckInDto response = service.realizarCheckIn(checkInDto1);
		Assertions.assertNotNull(response);
		Assertions.assertEquals(CheckInDto.class, response.getClass());	
		Assertions.assertEquals(id1, response.getId());
		Assertions.assertEquals(hospedeDto1.getDocumento(), response.getHospede().getDocumento());
		Assertions.assertEquals(dataInicio1, response.getDataEntrada());
		Assertions.assertEquals(dataFim1, response.getDataSaida());
		Assertions.assertEquals(adicionaVeiculo1, response.isAdicionaVeiculo());
		Assertions.assertEquals(valorTotal1, response.getValorTotalHospedagem());
	}
	
	@Test
	@DisplayName("Deve realizar o checkIn e calcular os valores da diaria para o hospede com estacionamento, no final de semana e saída após horário")
	void testRealizarCheckInFimDeSemana() {
		Mockito.when(checkInRepository.save(checkIn2)).thenReturn(checkIn2);
		Mockito.when(hospedeRepository.atualizaValores(Mockito.anyLong(), Mockito.any(BigDecimal.class), Mockito.any(BigDecimal.class))).thenReturn(1);
		Mockito.when(tabelaValoresRepository.findAll()).thenReturn(tabelaValores);
		
		CheckInDto response = service.realizarCheckIn(checkInDto2);
		Assertions.assertNotNull(response);
		Assertions.assertEquals(CheckInDto.class, response.getClass());	
		Assertions.assertEquals(id2, response.getId());
		Assertions.assertEquals(hospedeDto2.getDocumento(), response.getHospede().getDocumento());
		Assertions.assertEquals(dataInicio2, response.getDataEntrada());
		Assertions.assertEquals(dataFim2, response.getDataSaida());
		Assertions.assertEquals(adicionaVeiculo2, response.isAdicionaVeiculo());
		Assertions.assertEquals(valorTotal2, response.getValorTotalHospedagem());
	}
	
	@Test
	@DisplayName("Deve realizar o checkIn e calcular os valores da diaria para o hospede com valor anterior preenchido")
	void testRealizarCheckInComValorAnterior() {
		checkIn1.getHospede().setValorUltimaHospedagem(BigDecimal.valueOf(120.00));
		checkIn1.setValorTotalHospedagem(BigDecimal.valueOf(390.00));
		Mockito.when(checkInRepository.save(checkIn1)).thenReturn(checkIn1);
		Mockito.when(hospedeRepository.atualizaValores(Mockito.anyLong(), Mockito.any(BigDecimal.class), Mockito.any(BigDecimal.class))).thenReturn(1);
		Mockito.when(tabelaValoresRepository.findAll()).thenReturn(tabelaValores);
		Mockito.when(hospedeRepository.buscaValorTotal(id1)).thenReturn(BigDecimal.valueOf(120.00));
		
		CheckInDto response = service.realizarCheckIn(checkInDto1);
		Assertions.assertNotNull(response);
		Assertions.assertEquals(CheckInDto.class, response.getClass());	
		Assertions.assertEquals(id1, response.getId());
		Assertions.assertEquals(hospedeDto1.getDocumento(), response.getHospede().getDocumento());
		Assertions.assertEquals(dataInicio1, response.getDataEntrada());
		Assertions.assertEquals(dataFim1, response.getDataSaida());
		Assertions.assertEquals(adicionaVeiculo1, response.isAdicionaVeiculo());
		Assertions.assertEquals(valorTotal1, response.getValorTotalHospedagem());
	}

	@Test
	@DisplayName("Deve ser executado apenas 1 vez a ação de excluir por ID")
	void testExcluiCheckIn() {
		Mockito.doNothing().when(checkInRepository).deleteById(Mockito.anyLong());
		
		service.excluirCheckIn(id1);
		Mockito.verify(checkInRepository, times(1)).deleteById(Mockito.anyLong());
	}
	
	private void startCheckIn() {
		hospede1 = new Hospede(id1,"Joao","77777777777","44555559999", BigDecimal.ZERO, BigDecimal.ZERO);
		hospede2 = new Hospede(id2,"Maria","6666666666","99999999999", BigDecimal.ZERO, BigDecimal.ZERO);
		hospedeDto1 = new HospedeDto(id1,"Joao","77777777777","44555559999", BigDecimal.ZERO, BigDecimal.ZERO);
		hospedeDto2 = new HospedeDto(id2,"Maria","6666666666","99999999999", BigDecimal.ZERO, BigDecimal.ZERO);
		checkIn1 = new CheckIn(id1, hospede1, dataInicio1, dataFim1, adicionaVeiculo1, valorTotal1);
		checkIn2 = new CheckIn(id2, hospede2, dataInicio2, dataFim2, adicionaVeiculo2, valorTotal2);
		checkInDto1 = new CheckInDto(id1, hospedeDto1, dataInicio1, dataFim1, adicionaVeiculo1, valorTotal1);
		checkInDto2 = new CheckInDto(id2, hospedeDto2, dataInicio2, dataFim2, adicionaVeiculo2, valorTotal2);
		optionalHospede = Optional.of(hospede1);
		optionalCheckIn = Optional.of(checkIn1);
		paginacao = Pageable.ofSize(10);
		page = new PageImpl(List.of(checkIn1, checkIn2));
		tabelaValores.add(new TabelaValores(1L, DiasSemana.SEGUNDA, DiasSemana.SEXTA, BigDecimal.valueOf(120.00), BigDecimal.valueOf(15.00)));
		tabelaValores.add(new TabelaValores(2L, DiasSemana.SABADO, DiasSemana.DOMINGO, BigDecimal.valueOf(150.00), BigDecimal.valueOf(20.00)));
	}

}
