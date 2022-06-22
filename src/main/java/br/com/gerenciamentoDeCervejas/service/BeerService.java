package br.com.gerenciamentoDeCervejas.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gerenciamentoDeCervejas.dto.BeerDTO;
import br.com.gerenciamentoDeCervejas.entity.Beer;
import br.com.gerenciamentoDeCervejas.exception.BeerAlreadyRegisteredException;
import br.com.gerenciamentoDeCervejas.exception.BeerNotFoundException;
import br.com.gerenciamentoDeCervejas.exception.BeerStockExceededException;
import br.com.gerenciamentoDeCervejas.mapper.BeerMapper;
import br.com.gerenciamentoDeCervejas.repository.BeerRepository;

@Service
public class BeerService {
	
	@Autowired
	private BeerRepository beerRepository;
	
	private BeerMapper beerMapper = BeerMapper.INSTANCE;
	
	public BeerDTO createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
		verifyIfIsAlreadyRegistered(beerDTO.getName());
		
		Beer beer = this.beerMapper.toModel(beerDTO);
		Beer savedBeer = this.beerRepository.save(beer);
		
		return this.beerMapper.toDTO(savedBeer);
	}
	
	public BeerDTO findByName(String name) throws BeerNotFoundException {
		Beer foundBeer = this.beerRepository.findByName(name)
										.orElseThrow(() -> new BeerNotFoundException(name));
		
		return this.beerMapper.toDTO(foundBeer);
	}
	
	public List<BeerDTO> listAll() {
		return beerRepository.findAll()
							.stream()
							.map(beerMapper::toDTO)
							.collect(Collectors.toList());
	}
	
	public void deleteById(Long id) throws BeerNotFoundException {
		verifyIfExists(id);
		this.beerRepository.deleteById(id);
	}
	
	private void verifyIfIsAlreadyRegistered(String name) throws BeerAlreadyRegisteredException {
		Optional<Beer> optSavedBeer = this.beerRepository.findByName(name);
		
		if (optSavedBeer.isPresent()) {
			throw new BeerAlreadyRegisteredException(name);
		}
	}
	
	private Beer verifyIfExists(Long id) throws BeerNotFoundException {
		return this.beerRepository.findById(id)
								.orElseThrow(() -> new BeerNotFoundException(id));
	}
	
	public BeerDTO increment(Long id, Integer quantityToIncrement) throws BeerNotFoundException, BeerStockExceededException {
		Beer beerToIncrementStock = verifyIfExists(id);
		Integer beerStockAfterIncrement = quantityToIncrement + beerToIncrementStock.getQuantity();
		
		if (beerStockAfterIncrement <= beerToIncrementStock.getMax()) {
			Beer incrementBeerStock = this.beerRepository.save(beerToIncrementStock);
			
			return this.beerMapper.toDTO(incrementBeerStock);
		}
		
		throw new BeerStockExceededException(id, quantityToIncrement);
	}
	
	public BeerDTO decrement(Long id, Integer quantityToDecrement) throws BeerNotFoundException, BeerStockExceededException {
		Beer beerToDecrementStock = verifyIfExists(id);
		Integer beerStockAfterDecremented = beerToDecrementStock.getQuantity() - quantityToDecrement;
		
		if (beerStockAfterDecremented >= 0) {
			beerToDecrementStock.setQuantity(quantityToDecrement);
			
			Beer decrementedBeerStock = this.beerRepository.save(beerToDecrementStock);
			
			return this.beerMapper.toDTO(decrementedBeerStock);
		}
		
		throw new BeerStockExceededException(id, quantityToDecrement);
	}
}