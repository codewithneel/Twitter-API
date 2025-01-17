package com.cooksys.twitter_api.dtos;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ContextDto {
	
	private TweetResponseDto target;
	
	private List<TweetResponseDto> before; 
	
	private List<TweetResponseDto> after; 
}
