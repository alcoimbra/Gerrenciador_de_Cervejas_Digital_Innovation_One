package br.com.gerenciamentoDeCervejas.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerRepository extends JpaRepository<Beer, Long>{
	
	Beer findByName(String name);
}