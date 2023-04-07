package br.com.hotel.repository;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.hotel.model.Hospede;
import jakarta.transaction.Transactional;

@Transactional
public interface HospedeRepository extends JpaRepository<Hospede, Long>{
	
	Hospede findByDocumento(String documento);
	
	Hospede findByNome(String nome);
	
	Hospede findByTelefone(String telefone);
	
	@Query("SELECT H.valorGasto FROM Hospede H WHERE H.Id = :id")
	BigDecimal buscaValorTotal(@Param("id") Long id);
	
	@Modifying
	@Query("UPDATE Hospede H SET H.valorGasto = :valorGasto, H.valorUltimaHospedagem = :valorUltimaHospedagem")
	int atualizaValores(@Param("valorGasto") BigDecimal valorGasto, @Param("valorUltimaHospedagem") BigDecimal valorUltimaHospedagem);
	
	@Query(value = "SELECT H.* FROM HOSPEDES H, CHECK_IN C\r\n"
			+ "WHERE H.ID = C.HOSPEDE_ID\r\n"
			+ "AND C.DATA_SAIDA > CURRENT_DATE\r\n"
			+ "GROUP BY H.ID", nativeQuery = true)
	Page<Hospede> buscaPresentes(Pageable paginacao);
	
	@Query(value = "SELECT h.* FROM HOSPEDES H, CHECK_IN C\r\n"
			+ "WHERE H.ID = C.HOSPEDE_ID\r\n"
			+ "AND C.DATA_SAIDA < CURRENT_DATE\r\n"
			+ "AND NOT EXISTS (SELECT 1 FROM HOSPEDES H, CHECK_IN C\r\n"
			+ "WHERE H.ID = C.HOSPEDE_ID\r\n"
			+ "AND C.DATA_SAIDA > CURRENT_DATE\r\n"
			+ "GROUP BY H.ID)\r\n"
			+ "GROUP BY H.ID", nativeQuery = true)
	Page<Hospede> buscaAusentes(Pageable paginacao);
	
	
}
