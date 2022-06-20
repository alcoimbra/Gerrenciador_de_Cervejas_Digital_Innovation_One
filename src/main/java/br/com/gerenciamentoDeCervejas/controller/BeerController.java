package br.com.gerenciamentoDeCervejas.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.gerenciamentoDeCervejas.dto.BeerDTO;
import br.com.gerenciamentoDeCervejas.exception.BeerAlreadyRegisteredException;
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
}