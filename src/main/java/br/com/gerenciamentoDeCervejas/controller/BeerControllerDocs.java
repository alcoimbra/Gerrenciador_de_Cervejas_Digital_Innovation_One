package br.com.gerenciamentoDeCervejas.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.gerenciamentoDeCervejas.dto.BeerDTO;
import br.com.gerenciamentoDeCervejas.dto.QuantityDTO;
import br.com.gerenciamentoDeCervejas.exception.BeerAlreadyRegisteredException;
import br.com.gerenciamentoDeCervejas.exception.BeerNotFoundException;
import br.com.gerenciamentoDeCervejas.exception.BeerStockExceededException;
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
			@ApiResponse(code = 404, message = "Beer with Given Name Not Found.")
	})
	BeerDTO findByName(@PathVariable String name) throws BeerNotFoundException;
	
	@ApiOperation(value = "Returns a List of all Beers Registered in the System")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "List of all Beers Registered in the System")
	})
	List<BeerDTO> listBeers();
	
	@ApiOperation(value = "Delete a Beer Found by Given Valid ID")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Sucess Beer Deleted in the System"),
			@ApiResponse(code = 404, message = "Beer with Given ID Not Found.")
	})
	void deleteById(@PathVariable Long id) throws BeerNotFoundException;
	
	@ApiOperation(value = "Increment Beer by Given ID Quantity in a Stock")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Sucess Beer Incremented in Stock"),
			@ApiResponse(code = 400, message = "Beer Not Sucessfully Increment in Stock"),
			@ApiResponse(code = 404, message = "Beer with Given ID Not Found.")
	})
	BeerDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) 
			throws BeerNotFoundException, BeerStockExceededException;
}