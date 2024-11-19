package com.cooksys.twitter_api.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.twitter_api.dtos.TweetRequestDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.entities.Tweet;

//@Mapper(componentModel = "spring", uses = { User.class })
public interface TweetMapper {
	
	public Tweet dtoToEntity(TweetRequestDto tweetRequestDto);
	
	public TweetResponseDto entityToDto(Tweet tweet);
	
	public List<TweetResponseDto> entitiesToDtos(List<Tweet> tweets);
}
