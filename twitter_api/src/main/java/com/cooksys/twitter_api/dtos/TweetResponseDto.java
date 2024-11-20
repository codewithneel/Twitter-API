package com.cooksys.twitter_api.dtos;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.cooksys.twitter_api.entities.Tweet;
import com.cooksys.twitter_api.entities.User;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
public class TweetResponseDto {
	private Long id; 
	
	private UserResponseDto author;
	
	private Timestamp posted;
	
	private String content;
	
	private TweetResponseDto inReplyTo;
	
	private TweetResponseDto repostOf;
}
