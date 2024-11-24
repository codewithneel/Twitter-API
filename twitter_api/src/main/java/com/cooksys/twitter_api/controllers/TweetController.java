package com.cooksys.twitter_api.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.twitter_api.dtos.ContextDto;
import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetRequestDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.services.TweetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tweets")
@RequiredArgsConstructor
public class TweetController {
	
	private final TweetService tweetService; 
	
	@GetMapping
	public List<TweetResponseDto> getAllTweets(){
		return tweetService.getAllTweets();
	}
//	
	@PostMapping
	public TweetResponseDto createSimpleTweet(@RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.createSimpleTweet(tweetRequestDto);
	}
//	
	@GetMapping("/{id}")
	public TweetResponseDto getTweet(@PathVariable Long id) {
		return tweetService.getTweet(id); 
	}
//	
	@DeleteMapping("/{id}")
	public TweetResponseDto deleteTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		return tweetService.deleteTweet(id, credentialsDto);
	}
//	
	@PostMapping("/{id}/like")
	public void likeTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		tweetService.likeTweet(id, credentialsDto); 
	}

	@PostMapping("/{id}/reply")
	public TweetResponseDto replyToTweet(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.replyTweet(id, tweetRequestDto);
	}
	
	@PostMapping("/{id}/repost")
	public TweetResponseDto repostTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		return tweetService.repostTweet(id, credentialsDto);
	}
	
	@GetMapping("/{id}/tags")
	public List<HashtagDto> getHashtagsAssociatedWithTweet(@PathVariable Long id){
		return tweetService.getHashtagsAssociatedWtihTweet(id);
	}
//	
	@GetMapping("/{id}/likes")
	public List<UserResponseDto> getUsersWhoLikedTweet(@PathVariable Long id){
		return tweetService.getUsersWhoLikedTweet(id);
	}
	
	@GetMapping("/{id}/context")
	public ContextDto getContextOfTweet(@PathVariable Long id) {
		return tweetService.getContextOfTweet(id);
	}
	
	@GetMapping("/{id}/replies")
	public List<TweetResponseDto> getDirectRepliesToTweet(@PathVariable Long id){
		return tweetService.getDirectRepliesToTweet(id);
	}

	@GetMapping("/{id}/reposts")
	public List<TweetResponseDto> getDirectRepostsOfTweet(@PathVariable Long id){
		return tweetService.getDirectRepostsOfTweet(id);
	}

	@GetMapping("/{id}/mentions")
	public List<UserResponseDto> getUsersMentionedInTweet(@PathVariable Long id){
		return tweetService.getUsersMentionedInTweet(id);
	}
}
