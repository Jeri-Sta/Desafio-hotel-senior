package br.com.hotel.service;

import static org.hamcrest.CoreMatchers.any;

import java.math.BigDecimal;
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

import br.com.hotel.dto.HospedeDto;
import br.com.hotel.model.Hospede;
import br.com.hotel.repository.HospedeRepository;

@SpringBootTest
class HospedeServiceTest {
	
	@InjectMocks
	private HospedeService service;
	
	@Mock
	private HospedeRepository repository;
	
	@Spy
	private ModelMapper modelMapper;
	
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
	void setUp(){
		MockitoAnnotations.openMocks(this);
		startHospede();
	}
	
	@Test
	@DisplayName("Deve retornar todos os usuários")
	void testObterTodos() {
		Mockito.when(repository.findAll(paginacao)).thenReturn(page);		
		Page<HospedeDto> response = service.obterTodos(paginacao);
		
		Assertions.assertNotNull(response);
		Assertions.assertEquals(HospedeDto.class, response.getContent().get(0).getClass());
		Assertions.assertEquals(1, response.getSize());
		Assertions.assertEquals(id, response.getContent().get(0).getId());
		Assertions.assertEquals(nome, response.getContent().get(0).getNome());
		Assertions.assertEquals(documento, response.getContent().get(0).getDocumento());
		Assertions.assertEquals(telefone, response.getContent().get(0).getTelefone());
	}
	
	@Test
	@DisplayName("Deve retornar o usuário pela ID")
	void testObterPorId() {
		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(optionalHospede);
		Mockito.when(modelMapper.map(any(Optional.class), HospedeDto.class)).thenReturn(hospedeDto);
		
		HospedeDto response = service.obterPorId(id);
		Assertions.assertNotNull(response);
		Assertions.assertEquals(HospedeDto.class, response.getClass());	
		Assertions.assertEquals(id, response.getId());
		Assertions.assertEquals(nome, response.getNome());
		Assertions.assertEquals(documento, response.getDocumento());
		Assertions.assertEquals(telefone, response.getTelefone());
	}
	
	@Test
	@DisplayName("Deve criar um usuário com sucesso")
	void testCriarHospede() {
		Mockito.when(repository.save(hospede)).thenReturn(hospede);
		Mockito.when(modelMapper.map(any(Hospede.class), HospedeDto.class)).thenReturn(hospedeDto);
		
		HospedeDto response = service.criarHospede(hospedeDto);
		Assertions.assertNotNull(response);
		Assertions.assertEquals(HospedeDto.class, response.getClass());	
		Assertions.assertEquals(id, response.getId());
		Assertions.assertEquals(nome, response.getNome());
		Assertions.assertEquals(documento, response.getDocumento());
		Assertions.assertEquals(telefone, response.getTelefone());
		
		
	}

	@Test
	void testObterPorDocumento() {
		Mockito.when(repository.findByDocumento(documento)).thenReturn(hospede);
		
		HospedeDto response = service.obterPorDocumento(documento);
		Assertions.assertNotNull(response);
		Assertions.assertEquals(HospedeDto.class, response.getClass());	
		Assertions.assertEquals(id, response.getId());
		Assertions.assertEquals(nome, response.getNome());
		Assertions.assertEquals(documento, response.getDocumento());
		Assertions.assertEquals(telefone, response.getTelefone());
	}

	@Test
	@DisplayName("Deve retornar uma page  ")
	void testObterPresentes() {
		Mockito.when(repository.buscaPresentes(paginacao)).thenReturn(page);		
		Page<HospedeDto> response = service.obterPresentes(paginacao);
		
		Assertions.assertNotNull(response);
		Assertions.assertEquals(HospedeDto.class, response.getContent().get(0).getClass());
		Assertions.assertEquals(1, response.getSize());
		Assertions.assertEquals(id, response.getContent().get(0).getId());
		Assertions.assertEquals(nome, response.getContent().get(0).getNome());
		Assertions.assertEquals(documento, response.getContent().get(0).getDocumento());
		Assertions.assertEquals(telefone, response.getContent().get(0).getTelefone());
	}

	@Test
	void testObterAusentes() {
		Mockito.when(repository.buscaAusentes(paginacao)).thenReturn(page);		
		Page<HospedeDto> response = service.obterAusentes(paginacao);
		
		Assertions.assertNotNull(response);
		Assertions.assertEquals(HospedeDto.class, response.getContent().get(0).getClass());
		Assertions.assertEquals(1, response.getSize());
		Assertions.assertEquals(id, response.getContent().get(0).getId());
		Assertions.assertEquals(nome, response.getContent().get(0).getNome());
		Assertions.assertEquals(documento, response.getContent().get(0).getDocumento());
		Assertions.assertEquals(telefone, response.getContent().get(0).getTelefone());
	}
	
	private void startHospede() {
		hospede = new Hospede(id, nome, documento, telefone, BigDecimal.ZERO, BigDecimal.ZERO); 
		hospedeDto = new HospedeDto(id, nome, documento, telefone, BigDecimal.ZERO, BigDecimal.ZERO);
		optionalHospede = Optional.of(hospede);
		paginacao = Pageable.ofSize(10);
		page = new PageImpl(List.of(hospede));
	}

}
