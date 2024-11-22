package com.cooksys.twitter_api.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.dtos.TweetRequestDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.entities.Credentials;
import com.cooksys.twitter_api.entities.Tweet;
import com.cooksys.twitter_api.entities.User;

@Mapper(componentModel = "spring", uses = { UserMapper.class, CredentialsMapper.class })

public interface TweetMapper {
	
	public Tweet dtoToEntity(TweetRequestDto tweetRequestDto);
	
	public TweetResponseDto entityToDto(Tweet tweet);
	
	public List<TweetResponseDto> entitiesToDtos(List<Tweet> tweets);
	
}
