package br.com.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.hotel.model.TabelaValores;

public interface TabelaValoresRepository extends JpaRepository<TabelaValores, Long> {

}
