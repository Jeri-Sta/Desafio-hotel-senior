package br.com.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.hotel.model.Hospede;

public interface HospedeRepository extends JpaRepository<Hospede, Long>{
	
	Hospede findByDocumento(String documento);
	
	Hospede findByNome(String nome);
	
	Hospede findByTelefone(String telefone);
}
