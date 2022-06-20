package br.com.gerenciamentoDeCervejas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.gerenciamentoDeCervejas.builder.BeerDTOBuilder;
import br.com.gerenciamentoDeCervejas.dto.BeerDTO;
import br.com.gerenciamentoDeCervejas.entity.Beer;
import br.com.gerenciamentoDeCervejas.exception.BeerAlreadyRegisteredException;
import br.com.gerenciamentoDeCervejas.mapper.BeerMapper;
import br.com.gerenciamentoDeCervejas.repository.BeerRepository;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTest {
	
	@Autowired
	private BeerRepository beerRepository;
	
	private BeerMapper beerMapper = BeerMapper.INSTANCE;
	
	@InjectMocks
	private BeerService beerService;
	
	
	@Test
	void whenNewBeerInformedThenShouldBeCreated() throws BeerAlreadyGeneratedException {
		BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
		Beer expectedSavedBeer = beerMapper.toModel(beerDTO);
		
		when(beerRepository.findByName(beerDTO.getName())).thenReturn(Optional.empty());
		when(beerRepository.save(expectedSavedBeer)).thenReturn(expectedSavedBeer);
		
		BeerDTO createdBeerDTO = this.beerService.createBeer(beerDTO);
		
		assertEquals(beerDTO.getId(), createdBeerDTO.getId());
		assertEquals(beerDTO.getName(), createdBeerDTO.getName());
		assertEquals(beerDTO.getBeerType(), createdBeerDTO.getBeerType());
	}
	
	
	@Test
	void whenAlreadyRegisteredBeerInformedThenAnExceptionShouldBeThrown() {
		BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
		Beer duplicateBeer = this.beerMapper.toModel(beerDTO);
		
		when(this.beerRepository.findByName(beerDTO.getName())).thenReturn(Optional.of(duplicateBeer));
		
		assertThrows(BeerAlreadyRegisteredException.class, () -> beerService.createBeer(beerDTO));
	}
}