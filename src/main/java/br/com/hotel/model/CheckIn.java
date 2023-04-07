package br.com.hotel.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = ("check_in"))
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckIn {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@ManyToOne(optional = false)
	@JsonIgnore
	private Hospede hospede;
	
	//@FutureOrPresent
	private LocalDateTime dataEntrada;
	
	//@Future
	private LocalDateTime dataSaida;
	
	@NotNull
	private boolean adicionaVeiculo;
	
	private BigDecimal valorTotalHospedagem;

}
