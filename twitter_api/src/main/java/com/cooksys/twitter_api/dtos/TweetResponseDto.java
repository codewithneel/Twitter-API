package com.cooksys.twitter_api.dtos;

import java.sql.Timestamp;

import com.cooksys.twitter_api.entities.Tweet;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetResponseDto implements java.lang.Comparable<TweetResponseDto> {
	private Long id; 
	
	private UserResponseDto author;
	
	private Timestamp posted;
	
	private String content;
	
	private TweetResponseDto inReplyTo;
	
	private TweetResponseDto repostOf;

	@Override
	public int compareTo(TweetResponseDto o) {
		return this.getPosted().compareTo(o.getPosted());
	}

}
