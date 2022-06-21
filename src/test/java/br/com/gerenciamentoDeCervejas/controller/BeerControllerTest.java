package br.com.gerenciamentoDeCervejas.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import br.com.gerenciamentoDeCervejas.builder.BeerDTOBuilder;
import br.com.gerenciamentoDeCervejas.dto.BeerDTO;
import br.com.gerenciamentoDeCervejas.exception.BeerNotFoundException;
import br.com.gerenciamentoDeCervejas.service.BeerService;

@ExtendWith(MockitoExtension.class)
public class BeerControllerTest {
	
	private static final String BEER_API_URL_PATH = "/api/v1/beer";
	
	private MockMvc mockMvc;
	
	@Mock
	private BeerService beerService;
	
	@InjectMocks
	private BeerController beerController;
	
	
	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(beerController)
								.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
								.setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
								.build();
	}
	
	@Test
	void whenPostisCalledThenABeerIsCreated() throws Exception {
		BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
		
		when(beerService.createBeer(beerDTO)).thenReturn(beerDTO);
		
		mockMvc.perform(post(BEER_API_URL_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(BeerDTOBuilder.asJsonString(beerDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name", is(beerDTO.getName())))
				.andExpect(jsonPath("$.brand", is(beerDTO.getBrand())))
				.andExpect(jsonPath("$.type", is(beerDTO.getBeerType().toString())));
	}
	
	@Test
	void whenPostisCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
		BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
		beerDTO.setName(null);
		
		mockMvc.perform(post(BEER_API_URL_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(BeerDTOBuilder.asJsonString(beerDTO)))
				.andExpect(status().isBadRequest());
	}

	@Test
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        when(beerService.findByName(beerDTO.getName())).thenReturn(beerDTO);

        mockMvc.perform(get(BEER_API_URL_PATH + "/" + beerDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(beerDTO.getName())))
                .andExpect(jsonPath("$.brand", is(beerDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(beerDTO.getBeerType().toString())));
    }

    @Test
    void whenGETIsCalledWithNotRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        when(beerService.findByName(beerDTO.getName())).thenThrow(BeerNotFoundException.class);

        mockMvc.perform(get(BEER_API_URL_PATH + "/" + beerDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}