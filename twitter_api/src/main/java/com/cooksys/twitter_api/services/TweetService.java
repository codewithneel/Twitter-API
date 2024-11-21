package com.cooksys.twitter_api.services;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;

public interface TweetService {
	
	public List<TweetResponseDto> getAllTweets();
//	
//	public TweetResponseDto createSimpleTweet(TweetRequestDto tweetRequestDto);
//
	public TweetResponseDto getTweet(Long id);
//	
//	public TweetResponseDto deleteTweet(Long id, CredentialsDto credentialsDto);
//	
//	public void likeTweet(Long id, CredentialsDto credentialsDto);
//	
//	public TweetResponseDto replyToTweet(Long id, CredentialsDto credentialsDto);
//	
//	public TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto);
//
	public List<HashtagDto> getHashtagsAssociatedWtihTweet(Long id);
//
//	public List<UserResponseDto> getUsersWhoLikedTweet(Long id);
//	
//	public ContextDto getContextOfTweet(Long id);
//
//	public List<TweetResponseDto> getDirectRepliesToTweet(Long id);
//
//	public List<TweetResponseDto> getDirectRepostsOfTweet(Long id);
//
	public List<UserResponseDto> getUsersMentionedInTweet(Long id);
}
