package com.cooksys.twitter_api.services.impl;

import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.*;
import com.cooksys.twitter_api.exceptions.NotAuthorizedException;
import com.cooksys.twitter_api.exceptions.NotFoundException;
import com.cooksys.twitter_api.mappers.TweetMapper;
import com.cooksys.twitter_api.mappers.UserMapper;
import com.cooksys.twitter_api.repositories.TweetRepository;
import com.cooksys.twitter_api.repositories.UserRepository;
import com.cooksys.twitter_api.services.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository; 
	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	private final UserMapper userMapper;
	
	@Override
	public ResponseEntity<List<TweetResponseDto>> getAllTweetsCreatedByUser(String username) {
		
		//Retrieve user object by username
		Optional<User> user = userRepository.findByCredentialsUsername(username);
		
		//If the username does not exist or the account has been deleted, return error
		if(user.isEmpty() || user.get().isDeleted()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		//Retrieve all of the tweets created by the user
		Optional<List<Tweet>> tweetsByUser = tweetRepository.findAllTweetsByAuthor(user.get());
		
		//If the user has not created a tweet, return status code: found and nothing for the response body
		if(tweetsByUser.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		
		//Add all non-deleted tweets to result
		List<TweetResponseDto> tweetsToReturn = new ArrayList<>();
		for(Tweet tweet : tweetsByUser.get()) {
			if(!tweet.isDeleted()) {
				tweetsToReturn.add(tweetMapper.entityToDto(tweet));
			}
		}
		
		//Sort tweets by posted and then reverse to achieve descending order
		Collections.sort(tweetsToReturn); 	
		return new ResponseEntity<>(tweetsToReturn.reversed(), HttpStatus.OK);
		
	}
	
	
	//Need to fix the followers/following table
	@Override
	public ResponseEntity<List<UserResponseDto>> getUserFollowers(String username) {
		
		//Validate username exists and is an active account
		Optional<User> user = userRepository.findByCredentialsUsername(username);
		if(user.isEmpty() || user.get().isDeleted()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		//Get follower list and check if its empty
		List<User> followers = user.get().getFollowers();
		if(followers.size() == 0) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		
		//Iterate through the follower list, add and convert non-deleted followers to List<UserResponseDto>
		List<UserResponseDto> result = new ArrayList<>();
		for(User follower : followers) {
			if(!follower.isDeleted()) {
				result.add(userMapper.entityToDto(follower));
			}
		}
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}


	@Override
	public UserResponseDto getUserByUsername(String username) {
		Optional<User> user = userRepository.findByCredentialsUsername(username);
		if(user.isEmpty() || user.get().isDeleted()) {
			throw new NotFoundException();
		}
		
		return userMapper.entityToDto(user.get());
	}


	@Override
	public UserResponseDto deleteUser(String username, CredentialsDto credentialsDto) {
		Optional<User> user = userRepository.findByCredentialsUsername(username);
		if(user.isEmpty() || user.get().isDeleted()) {
			throw new NotFoundException();
		}
		
		if(user.get().getCredentials().getUsername().equals(credentialsDto.getUsername()) && 
				user.get().getCredentials().getPassword().equals(credentialsDto.getPassword())) {
			user.get().setDeleted(true);
			return userMapper.entityToDto(userRepository.saveAndFlush(user.get()));		
		}else {
			throw new NotAuthorizedException();
		}
	}

//    @Override
//    public List<ProfileDto> getProfiles() {
//        return null;
//    }
//
//
//
//    @Override
//    public List<User> getUsers() {
//        return null;
//    }
}
