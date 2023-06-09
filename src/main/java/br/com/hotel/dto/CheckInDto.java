package br.com.hotel.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckInDto {

	private Long id;
	private HospedeDto hospede;
	private LocalDateTime dataEntrada;
	private LocalDateTime dataSaida;
	private boolean adicionaVeiculo;
	private BigDecimal valorTotalHospedagem;

}
