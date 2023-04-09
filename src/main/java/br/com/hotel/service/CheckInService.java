package br.com.hotel.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.hotel.dto.CheckInDto;
import br.com.hotel.dto.HospedeDto;
import br.com.hotel.model.CheckIn;
import br.com.hotel.model.Hospede;
import br.com.hotel.model.TabelaValores;
import br.com.hotel.repository.CheckInRepository;
import br.com.hotel.repository.HospedeRepository;
import br.com.hotel.repository.TabelaValoresRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckInService {

	@Autowired
	private CheckInRepository checkInRepository;

	@Autowired
	private HospedeRepository hospedeRepository;

	@Autowired
	private TabelaValoresRepository tabelaValoresRepository;

	@Autowired
	private ModelMapper modelMapper;

	public Page<CheckInDto> obterTodos(Pageable paginacao) {
		return checkInRepository.findAll(paginacao).map(p -> modelMapper.map(p, CheckInDto.class));
	}

	public CheckInDto obterPorId(Long id) {
		CheckIn checkIn = checkInRepository.findById(id).orElseThrow(EntityNotFoundException::new);

		return modelMapper.map(checkIn, CheckInDto.class);
	}

	public HospedeDto obterHospedePorId(Long id) {
		Optional<Hospede> hospede = hospedeRepository.findById(id);
		if (hospede.isEmpty()) {
			return null;
		} else {
			return modelMapper.map(hospede, HospedeDto.class);
		}
	}

	public HospedeDto obterHospedePorFiltro(String filtro, String info) {
		Hospede hospede = new Hospede();
		switch (filtro) {
		case "documento": {
			hospede = hospedeRepository.findByDocumento(info);
			break;
		}
		case "telefone": {
			hospede = hospedeRepository.findByTelefone(info);
			break;
		}
		case "nome": {
			hospede = hospedeRepository.findByNome(info);
			break;
		}
	}
		return modelMapper.map(hospede, HospedeDto.class);
	}

	public CheckInDto realizarCheckIn(CheckInDto checkInDto) {
		CheckIn checkIn = modelMapper.map(checkInDto, CheckIn.class);

		calculaValorHospedagem(checkIn);
		atualizaValoresHospede(checkIn);
		checkInRepository.save(checkIn); 
		return modelMapper.map(checkIn, CheckInDto.class);
	}

	private void atualizaValoresHospede(CheckIn checkIn) {
		BigDecimal valorTotalAnt = hospedeRepository.buscaValorTotal(checkIn.getHospede().getId());
		valorTotalAnt = (valorTotalAnt != null && valorTotalAnt != BigDecimal.ZERO ? valorTotalAnt : BigDecimal.ZERO);
		hospedeRepository.atualizaValores(checkIn.getHospede().getId(),
				valorTotalAnt.add(checkIn.getValorTotalHospedagem()), checkIn.getValorTotalHospedagem());
	}

	private void calculaValorHospedagem(CheckIn checkIn) {
		BigDecimal totalDiaria = BigDecimal.ZERO;
		List<TabelaValores> tabelas = tabelaValoresRepository.findAll();
		Duration periodo = Duration.between(checkIn.getDataEntrada(), checkIn.getDataSaida());
		LocalDateTime dia = checkIn.getDataEntrada();
		for (int i = 0; i <= Math.round(periodo.toDays()); i++) {
			for (TabelaValores tabelaValores : tabelas) {
				if (dia.getDayOfWeek().getValue() >= tabelaValores.getDiaInicio().getValor()
						&& dia.getDayOfWeek().getValue() <= tabelaValores.getDiaFim().getValor()) {
					if (dia.toLocalDate().isEqual(checkIn.getDataSaida().toLocalDate())) {
						if (depoisDoHorario(checkIn.getDataSaida(), dia)) {
							totalDiaria = totalDiaria.add(tabelaValores.getValorDiaria());
							if (checkIn.isAdicionaVeiculo()) {
								totalDiaria = totalDiaria.add(tabelaValores.getValorVaga());
							}
							checkIn.setValorTotalHospedagem(totalDiaria);
						}
					} else {
						totalDiaria = totalDiaria.add(tabelaValores.getValorDiaria());
						if (checkIn.isAdicionaVeiculo()) {
							totalDiaria = totalDiaria.add(tabelaValores.getValorVaga());
						}
						checkIn.setValorTotalHospedagem(totalDiaria);
					}
				}
			}
			dia = dia.plusDays(1);
		}
	}

	private boolean depoisDoHorario(LocalDateTime dataFim, LocalDateTime diaCont) {
		LocalTime hora = dataFim.toLocalTime();
		return hora.isAfter(LocalTime.of(16, 30)) ? true : false;
	}

	public void excluirCheckIn(Long id) {
		checkInRepository.deleteById(id);
	}

}
