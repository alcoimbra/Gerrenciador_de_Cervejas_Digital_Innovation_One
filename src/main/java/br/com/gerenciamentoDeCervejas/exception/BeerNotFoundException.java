package br.com.gerenciamentoDeCervejas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BeerNotFoundException extends Exception {
	private static final long serialVersionUID = -6978817936932222338L;

	public BeerNotFoundException(String beerName) {
		super(String.format("Beer with Name %s Not Found in the System.", beerName));
	}
}