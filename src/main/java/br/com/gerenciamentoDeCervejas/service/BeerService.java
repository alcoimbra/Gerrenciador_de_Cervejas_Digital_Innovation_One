package br.com.gerenciamentoDeCervejas.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gerenciamentoDeCervejas.dto.BeerDTO;
import br.com.gerenciamentoDeCervejas.entity.Beer;
import br.com.gerenciamentoDeCervejas.exception.BeerAlreadyRegisteredException;
import br.com.gerenciamentoDeCervejas.mapper.BeerMapper;
import br.com.gerenciamentoDeCervejas.repository.BeerRepository;

@Service
public class BeerService {
	
	@Autowired
	private BeerRepository beerRepository;
	
	private BeerMapper beerMapper = BeerMapper.INSTANCE;
	
	public BeerDTO createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
		Optional<Beer> optSavedBeer = this.beerRepository.findByName(beerDTO.getName());
		
		if (optSavedBeer.isPresent()) {
			throw new BeerAlreadyRegisteredException(beerDTO.getName());
		}
		
		Beer beer = beerMapper.toModel(beerDTO);
		Beer savedBeer = this.beerRepository.save(beer);
		
		return this.beerMapper.toDTO(savedBeer);
	}
}