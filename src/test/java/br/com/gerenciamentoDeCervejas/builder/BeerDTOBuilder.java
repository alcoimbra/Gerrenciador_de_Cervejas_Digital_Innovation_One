package br.com.gerenciamentoDeCervejas.builder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.gerenciamentoDeCervejas.dto.BeerDTO;
import br.com.gerenciamentoDeCervejas.enums.BeerType;
import lombok.Builder;

@Builder
public class BeerDTOBuilder {
	
	@Builder.Default
	private Long id = 1L;
	
	@Builder.Default
	private String name = "Brahma";

	@Builder.Default
	private String brand = "Ambev";

	@Builder.Default
	private Integer max = 50;

	@Builder.Default
	private Integer quantity = 10;

	@Builder.Default
	private BeerType beerType = BeerType.LAGER;
	
	public BeerDTO toBeerDTO() {
		return new BeerDTO(id, name, brand, max, quantity, beerType);
	}
	
	public static String asJsonString(BeerDTO beerDTO) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			objectMapper.registerModules(new JavaTimeModule());
			
			return objectMapper.writeValueAsString(beerDTO);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}