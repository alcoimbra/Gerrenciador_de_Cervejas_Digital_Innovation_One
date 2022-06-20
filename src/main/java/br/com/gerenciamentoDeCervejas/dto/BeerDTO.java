package br.com.gerenciamentoDeCervejas.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.gerenciamentoDeCervejas.enums.BeerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerDTO {
	
	private Long id;
	
	@NotNull
	@Size(min = 1, max = 200)
	private String name;
	
	@NotNull
	@Size(min = 1, max = 200)
	private String brand;
	
	@NotNull
	@Max(2)
	private Integer max;

	@NotNull
	@Max(100)
	private Integer quantity;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private BeerType beerType;

	public BeerDTO(Long id, String name, String brand, Integer max, Integer quantity, BeerType beerType) {
		this.id = id;
		this.name = name;
		this.brand = brand;
		this.max = max;
		this.quantity = quantity;
		this.beerType = beerType;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BeerType getBeerType() {
		return beerType;
	}

	public void setBeerType(BeerType beerType) {
		this.beerType = beerType;
	}
}