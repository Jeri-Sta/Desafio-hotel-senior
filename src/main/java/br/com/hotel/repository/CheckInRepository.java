package br.com.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.hotel.model.CheckIn;

public interface CheckInRepository extends JpaRepository<CheckIn, Long>{

}
