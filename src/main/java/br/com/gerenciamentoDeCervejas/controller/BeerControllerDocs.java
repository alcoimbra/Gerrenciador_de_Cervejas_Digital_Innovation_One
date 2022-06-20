package br.com.gerenciamentoDeCervejas.controller;

import br.com.gerenciamentoDeCervejas.dto.BeerDTO;
import br.com.gerenciamentoDeCervejas.exception.BeerAlreadyRegisteredException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Manages Beer Stock")
public interface BeerControllerDocs {
	
	@ApiOperation(value = "Hello World!!")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns a Simple Hello World")
	})
	BeerDTO createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException;	
}