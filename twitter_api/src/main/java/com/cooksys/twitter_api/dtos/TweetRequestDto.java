package com.cooksys.twitter_api.dtos;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.cooksys.twitter_api.entities.Tweet;

public class TweetRequestDto {
	
	private UserRequestDto author ;
	
	private String content;
	
	private TweetRequestDto inReplyTo;
	
	private TweetRequestDto repostOf;
	
}
