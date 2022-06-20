package br.com.gerenciamentoDeCervejas.controller;

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
	String helloApi();
}