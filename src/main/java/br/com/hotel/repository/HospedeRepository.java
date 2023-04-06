package br.com.hotel.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.hotel.model.Hospede;

public interface HospedeRepository extends JpaRepository<Hospede, Long>{
	
	Hospede findByDocumento(String documento);
	
	Hospede findByNome(String nome);
	
	Hospede findByTelefone(String telefone);
	
	@Query("SELECT H.valorGasto FROM Hospede H WHERE H.Id = :id")
	BigDecimal buscaValorTotal(Long id);
	
	@Query("UPDATE Hospede H SET H.valorGasto = :valorGasto, H.valorUltimaHospedagem = :valorUltimaHospedagem")
	void atualizaValores(BigDecimal valorGasto, BigDecimal valorUltimaHospedagem);
}
