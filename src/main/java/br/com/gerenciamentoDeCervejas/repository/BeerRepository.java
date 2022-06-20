package br.com.gerenciamentoDeCervejas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gerenciamentoDeCervejas.entity.Beer;

public interface BeerRepository extends JpaRepository<Beer, Long>{
	
	Optional<Beer> findByName(String name);
}