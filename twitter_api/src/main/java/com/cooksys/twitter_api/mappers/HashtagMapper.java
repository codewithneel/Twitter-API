package com.cooksys.twitter_api.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.entities.Hashtag;


@Mapper(componentModel = "spring")
public interface HashtagMapper {

	HashtagDto entityToDto(Hashtag entity);
	
	List<HashtagDto> entitiesToDto(List<Hashtag> entity);
}
