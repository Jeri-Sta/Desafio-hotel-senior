package br.com.hotel.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "tabela_valores")
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TabelaValores {	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private DiasSemana diaInicio;
	
	@Enumerated(EnumType.STRING)
	private DiasSemana diaFim;
	
	private BigDecimal valorDiaria;
	
	private BigDecimal valorVaga;

}
