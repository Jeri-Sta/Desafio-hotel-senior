package br.com.hotel.dto;

import java.math.BigDecimal;

import br.com.hotel.model.DiasSemana;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TabelaValoresDto {

	private Long id;
	private DiasSemana diaInicio;
	private DiasSemana diaFim;	
	private BigDecimal valorDiaria;	
	private BigDecimal valorVaga;	
	
}
