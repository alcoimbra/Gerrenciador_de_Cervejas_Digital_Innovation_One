package br.com.gerenciamentoDeCervejas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BeerStockExceededException extends Exception {
	private static final long serialVersionUID = 5905935580142953817L;

	public BeerStockExceededException(Long id, Integer quantityToIncrement) {
		super(String.format("Beers with %s ID to Increment Informed Exceeds the Max Stock Capacity: %s", id, quantityToIncrement));
	}
}