package br.com.hotel.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HospedeDto {
	
	private Long id;
	private String nome;
	private String documento;
	private String telefone;
	private BigDecimal valorGasto;
	private BigDecimal valorUltimaHospedagem;	
	
}
