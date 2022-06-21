package br.com.gerenciamentoDeCervejas.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gerenciamentoDeCervejas.dto.BeerDTO;
import br.com.gerenciamentoDeCervejas.entity.Beer;
import br.com.gerenciamentoDeCervejas.exception.BeerAlreadyRegisteredException;
import br.com.gerenciamentoDeCervejas.exception.BeerNotFoundException;
import br.com.gerenciamentoDeCervejas.mapper.BeerMapper;
import br.com.gerenciamentoDeCervejas.repository.BeerRepository;

@Service
public class BeerService {
	
	@Autowired
	private BeerRepository beerRepository;
	
	private BeerMapper beerMapper = BeerMapper.INSTANCE;
	
	public BeerDTO createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
		verifyIfIsAlreadyRegistered(beerDTO);
		
		Beer beer = this.beerMapper.toModel(beerDTO);
		Beer savedBeer = this.beerRepository.save(beer);
		
		return this.beerMapper.toDTO(savedBeer);
	}
	
	public BeerDTO findByName(String name) throws BeerNotFoundException {
		Beer foundBeer = this.beerRepository.findByName(name)
										.orElseThrow(() -> new BeerNotFoundException(name));
		
		return this.beerMapper.toDTO(foundBeer);
	}
	
	private void verifyIfIsAlreadyRegistered(BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
		Optional<Beer> optSavedBeer = this.beerRepository.findByName(beerDTO.getName());
		
		if (optSavedBeer.isPresent()) {
			throw new BeerAlreadyRegisteredException(beerDTO.getName());
		}
	}
}