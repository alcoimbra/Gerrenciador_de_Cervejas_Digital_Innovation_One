package br.com.gerenciamentoDeCervejas.controller;

import org.springframework.web.bind.annotation.PathVariable;

import br.com.gerenciamentoDeCervejas.dto.BeerDTO;
import br.com.gerenciamentoDeCervejas.exception.BeerAlreadyRegisteredException;
import br.com.gerenciamentoDeCervejas.exception.BeerNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Manages Beer Stock")
public interface BeerControllerDocs {
	
	@ApiOperation(value = "Beer Creation Operation")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Sucess Beer Creation"),
			@ApiResponse(code = 400, message = "Missing Required Fields or Wrong Field Range Value.")
	})
	BeerDTO createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException;
	
	@ApiOperation(value = "Returns Beer Found by a Given Name")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Sucess Beer Found in the System"),
			@ApiResponse(code = 404, message = "Beer with a Given Name Not Found.")
	})
	BeerDTO findByName(@PathVariable String name) throws BeerNotFoundException;
}