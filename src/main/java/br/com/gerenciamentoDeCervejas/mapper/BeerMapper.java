package br.com.gerenciamentoDeCervejas.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.gerenciamentoDeCervejas.dto.BeerDTO;
import br.com.gerenciamentoDeCervejas.entity.Beer;

@Mapper
public interface BeerMapper {
	
	BeerMapper INSTANCE = Mappers.getMapper(BeerMapper.class);
	Beer toModel(BeerDTO beerDTO);
	BeerDTO toDTO(Beer beer);
}