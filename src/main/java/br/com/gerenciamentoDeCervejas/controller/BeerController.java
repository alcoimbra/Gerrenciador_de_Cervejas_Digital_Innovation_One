package br.com.gerenciamentoDeCervejas.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.gerenciamentoDeCervejas.dto.BeerDTO;
import br.com.gerenciamentoDeCervejas.dto.QuantityDTO;
import br.com.gerenciamentoDeCervejas.exception.BeerAlreadyRegisteredException;
import br.com.gerenciamentoDeCervejas.exception.BeerNotFoundException;
import br.com.gerenciamentoDeCervejas.exception.BeerStockExceededException;
import br.com.gerenciamentoDeCervejas.service.BeerService;

@RestController
@RequestMapping("/api/v1/beer")
public class BeerController implements BeerControllerDocs {
	
	@Autowired
	private BeerService beerService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BeerDTO createBeer(@RequestBody @Valid BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
		return this.beerService.createBeer(beerDTO);
	}
	
	@GetMapping("/{name}")
	public BeerDTO findByName(@PathVariable String name) throws BeerNotFoundException {
		return this.beerService.findByName(name);
	}
	
	@GetMapping
	public List<BeerDTO> listBeers(){
		return this.beerService.listAll();
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable Long id) throws BeerNotFoundException {
		this.beerService.deleteById(id);
	}
	
	@PatchMapping("/{id}/increment")
	public BeerDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) 
			throws BeerNotFoundException, BeerStockExceededException {
		return this.beerService.increment(id, quantityDTO.getQuantity());
	}
	
	@PatchMapping("/{id}/decrement")
	public BeerDTO decrement(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) 
			throws BeerNotFoundException, BeerStockExceededException {
		return this.beerService.decrement(id, quantityDTO.getQuantity());
	}
}