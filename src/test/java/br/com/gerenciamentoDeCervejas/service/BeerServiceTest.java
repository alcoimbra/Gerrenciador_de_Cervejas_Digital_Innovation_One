package br.com.gerenciamentoDeCervejas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
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
import br.com.gerenciamentoDeCervejas.exception.BeerNotFoundException;
import br.com.gerenciamentoDeCervejas.exception.BeerStockExceededException;
import br.com.gerenciamentoDeCervejas.mapper.BeerMapper;
import br.com.gerenciamentoDeCervejas.repository.BeerRepository;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTest {
	
	private static final Long INVALID_BEER_ID = 1L;
	
	@Autowired
	private BeerRepository beerRepository;
	
	private BeerMapper beerMapper = BeerMapper.INSTANCE;
	
	@InjectMocks
	private BeerService beerService;
	
	
	@Test
	void whenNewBeerInformedThenShouldBeCreated() {
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
	
	void whenValidBeerNameIsGivenThenReturnABeer() throws BeerNotFoundException {
		BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
		Beer expectedFoundBeer = this.beerMapper.toModel(expectedBeerDTO);
		
		when(this.beerRepository.findByName(expectedBeerDTO.getName())).thenReturn(Optional.of(expectedFoundBeer));
		
		BeerDTO foundBeerDTO = this.beerService.findByName(expectedBeerDTO.getName());
		
		assertEquals(expectedBeerDTO, foundBeerDTO);
	}
	
	@Test
	void whenNotRegisteredBeerNameIsGivenThenThrowAsException() throws BeerNotFoundException {
		BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
		
		when(this.beerRepository.findByName(expectedBeerDTO.getName())).thenReturn(Optional.empty());
		
		assertThrows(BeerNotFoundException.class, () -> this.beerService.findByName(expectedBeerDTO.getName()));
	}
	
	@Test
	void whenListBeerIsCalledThenReturnAsListOfBeers() {
		BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
		Beer expectedFoundBeer = this.beerMapper.toModel(expectedBeerDTO);
		
		when(this.beerRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundBeer));
		
		List<BeerDTO> foundBeerDTO = this.beerService.listAll();
		
		assertFalse(foundBeerDTO.isEmpty());
		assertEquals(expectedBeerDTO, foundBeerDTO.get(0));
	}
	
	@Test
	void whenListBeerIsCalledThenReturnAnEmptyList() {
		when(this.beerRepository.findAll()).thenReturn(Collections.EMPTY_LIST);
		
		List<BeerDTO> foundBeerDTO = this.beerService.listAll();
		
		assertTrue(foundBeerDTO.isEmpty());
	}
	
	@Test
	void whenExclusionIsCalledWithValidIdThenABeerShouldBeDelete() throws BeerNotFoundException {
		BeerDTO expectedExcludedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
		Beer expectedExcludedBeer = this.beerMapper.toModel(expectedExcludedBeerDTO);
		
		when(this.beerRepository.findById(expectedExcludedBeerDTO.getId())).thenReturn(Optional.of(expectedExcludedBeer));
		doNothing().when(this.beerRepository).deleteById(expectedExcludedBeer.getId());
		
		this.beerService.deleteById(expectedExcludedBeerDTO.getId());
		
		verify(this.beerRepository, times(1)).findById(expectedExcludedBeerDTO.getId());
		verify(this.beerRepository, times(1)).deleteById(expectedExcludedBeerDTO.getId());
	}
	
	@Test
	void whenExclusionIsCalledWithInvalidIdThenExceptionShouldBeThrown() {
		when(this.beerRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());
		
		assertThrows(BeerNotFoundException.class, () -> this.beerService.deleteById(INVALID_BEER_ID));
	}
	
	@Test
	void whenIncrementIsCalledThenIncrementBeerStock() throws BeerNotFoundException, BeerStockExceededException {
		BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
		Beer expectedBeer = this.beerMapper.toModel(expectedBeerDTO);
		
		when(this.beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
		when(this.beerRepository.save(expectedBeer)).thenReturn(expectedBeer);
		
		Integer quantityToIncrement = 10;
		Integer expectedQuantityAfterIncrement = expectedBeerDTO.getQuantity() + quantityToIncrement;
		BeerDTO incrementBeerDTO = this.beerService.increment(expectedBeerDTO.getId(), quantityToIncrement);
		
		assertThat(expectedQuantityAfterIncrement, equalTo(incrementBeerDTO.getQuantity()));
		assertThat(expectedQuantityAfterIncrement, lessThan(expectedBeerDTO.getMax()));
	}
	
	@Test
	void whenIncrementIsGreatherThanMaxThenThrowException() {
		BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
		Beer expectedBeer = this.beerMapper.toModel(expectedBeerDTO);
		
		when(this.beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
		
		Integer quantityToIncrement = 80;
		
		assertThrows(BeerStockExceededException.class, () -> this.beerService.increment(expectedBeerDTO.getId(), quantityToIncrement));
	}
	
	@Test
	void whenIncrementIsCalledWithInvalidThenThrowException() {
		Integer quantityToIncrement = 10;
		
		when(this.beerRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());
		
		assertThrows(BeerNotFoundException.class, () -> this.beerService.increment(INVALID_BEER_ID, quantityToIncrement));
	}
	
	@Test
	void whenDecrementIsCalledThenDecrementBeerStock() throws BeerNotFoundException, BeerStockExceededException {
		BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
		Beer expectedBeer = this.beerMapper.toModel(expectedBeerDTO);
		
		when(this.beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
		when(this.beerRepository.save(expectedBeer)).thenReturn(expectedBeer);
		
		Integer quantityToDecrement = 5;
		Integer expectedQuantityAfterDecrement = expectedBeerDTO.getQuantity() - quantityToDecrement;
		BeerDTO incrementedBeerDTO = this.beerService.decrement(expectedBeerDTO.getId(), quantityToDecrement);
		
		assertThat(expectedQuantityAfterDecrement, equalTo(incrementedBeerDTO.getQuantity()));
		assertThat(expectedQuantityAfterDecrement, greaterThan(0));
	}
	
	@Test
	void whenDecrementIsCalledToEmptyStockThenEmptyBeerStock() throws BeerNotFoundException, BeerStockExceededException {
		BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
		Beer expectedBeer = this.beerMapper.toModel(expectedBeerDTO);
		
		when(this.beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
		when(this.beerRepository.save(expectedBeer)).thenReturn(expectedBeer);
		
		Integer quantityToDecrement = 10;
		Integer expectedQuantityAfterDecrement = expectedBeerDTO.getQuantity() - quantityToDecrement;
		BeerDTO incrementedBeerDTO = this.beerService.decrement(expectedBeerDTO.getId(), quantityToDecrement);
		
		assertThat(expectedQuantityAfterDecrement, equalTo(0));
		assertThat(expectedQuantityAfterDecrement, equalTo(incrementedBeerDTO.getQuantity()));
	}
	
	@Test
	void whenDecrementIsLowerThanZeroThenThrowException() {
		BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
		Beer expectedBeer = this.beerMapper.toModel(expectedBeerDTO);
		
		when(this.beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
		
		Integer quantityToDecrement = 80;
		
		assertThrows(BeerStockExceededException.class, () -> this.beerService.decrement(expectedBeerDTO.getId(), quantityToDecrement));
	}
	
	@Test
	void whenDecrementIsCalledWithInvalidThenThrowException() {
		Integer quantityToDecrement = 10;
		
		when(this.beerRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());
		
		assertThrows(BeerNotFoundException.class, () -> this.beerService.decrement(INVALID_BEER_ID, quantityToDecrement));
	}
}