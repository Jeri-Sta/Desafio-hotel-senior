package br.com.hotel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HospedeDto {
	
	private Long id;
	private String nome;
	private String documento;
	private String telefone;
}
