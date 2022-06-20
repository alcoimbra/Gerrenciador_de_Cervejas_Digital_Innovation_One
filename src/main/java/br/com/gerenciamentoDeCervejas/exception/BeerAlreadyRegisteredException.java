package br.com.gerenciamentoDeCervejas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BeerAlreadyRegisteredException extends Exception {
	private static final long serialVersionUID = -920737412739836658L;

	public BeerAlreadyRegisteredException(String beerName) {
		super(String.format("Beer with Name %s already Registered in the System", beerName));
	}
}