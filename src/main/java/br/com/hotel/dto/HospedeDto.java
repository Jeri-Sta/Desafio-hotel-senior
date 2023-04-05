package br.com.hotel.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.hotel.model.CheckIn;
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
	private List<CheckIn> checkIn = new ArrayList<>();
}
