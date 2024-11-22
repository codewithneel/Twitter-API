package com.cooksys.twitter_api.services.impl;

import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.dtos.ProfileDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.*;
import com.cooksys.twitter_api.exceptions.BadRequestException;
import com.cooksys.twitter_api.exceptions.NotAuthorizedException;
import com.cooksys.twitter_api.exceptions.NotFoundException;
import com.cooksys.twitter_api.mappers.CredentialsMapper;
import com.cooksys.twitter_api.mappers.ProfileMapper;
import com.cooksys.twitter_api.mappers.TweetMapper;
import com.cooksys.twitter_api.mappers.UserMapper;
import com.cooksys.twitter_api.repositories.TweetRepository;
import com.cooksys.twitter_api.repositories.UserRepository;
import com.cooksys.twitter_api.services.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
	private final CredentialsMapper credentialsMapper;
	private final ProfileMapper profileMapper;
	
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


	//*********** Joined date not set
	@Override
	public UserResponseDto createUser(UserRequestDto userRequestDto) {
		
		if(userRequestDto == null || userRequestDto.getCredentials() == null || userRequestDto.getProfile() == null ||
				userRequestDto.getCredentials().getUsername() == null || userRequestDto.getCredentials().getPassword() == null
				|| userRequestDto.getProfile().getFirstName() == null || userRequestDto.getProfile().getLastName() == null
				|| userRequestDto.getProfile().getEmail() == null || userRequestDto.getProfile().getPhone() == null) {
			throw new BadRequestException("Invalid Request");
		}
		//if username does not exist, create new user
		Optional<User> user = userRepository.findByCredentialsUsername(userRequestDto.getCredentials().getUsername());
		if(user.isEmpty()) {
			User newUser = new User();
			newUser.setDeleted(false);
			//newUser.setJoined(new Timestamp(System.currentTimeMillis()));
			newUser.setCredentials(credentialsMapper.DtoToEntityCred(userRequestDto.getCredentials()));
			newUser.setProfile(profileMapper.ProfDtoToEnt(userRequestDto.getProfile()));
			return userMapper.entityToDto(userRepository.saveAndFlush(newUser));
		}
		
		/*
		 * if the account is not active and the passwords match, re-activate account 
		 * otherwise, throw error 
		 */
		if(user.get().isDeleted() && userRequestDto.getCredentials().getPassword().equals(user.get().getCredentials().getPassword())) {
			user.get().setDeleted(false);
			return userMapper.entityToDto(userRepository.saveAndFlush(user.get()));
		}
		
		throw new BadRequestException("Invalid");
	}


	@Override
	public void followUser(String username, CredentialsDto credentialsDto) {
		if(credentialsDto == null || credentialsDto.getUsername() == null || credentialsDto.getPassword() == null) {
			throw new BadRequestException("missing fields in credentials");
		}
		
		Optional<User> user = userRepository.findByCredentialsUsername(credentialsDto.getUsername());
		if(user.isEmpty() || user.get().isDeleted() || !user.get().getCredentials().getPassword().equals(credentialsDto.getPassword())) {
			throw new BadRequestException("invalid credentials");
		}
		
		Optional<User> followUser = userRepository.findByCredentialsUsername(username);
		if(user.isEmpty() || user.get().isDeleted()){
			throw new BadRequestException("cannot follow user -> user does not exist!");
		}
		
		List<User> userFollowingList = user.get().getFollowing();
		for(User followingUser : userFollowingList) {
			if(followingUser.equals(followUser.get())) {
				throw new BadRequestException("user already follows this account!!");
			}
		}
		user.get().getFollowing().add(followUser.get());
		followUser.get().getFollowers().add(user.get());
		userRepository.saveAndFlush(user.get());
		userRepository.saveAndFlush(followUser.get());
		
	}

//    @Override
//    public List<ProfileDto> getProfiles() {
//        return null;
//    }
//
//
//
    @Override
    public List<UserResponseDto> getUsers() {
        List<User> allUsers = userRepository.findAll(); 
        List<User> activeUsers = new ArrayList<>();
        for(User u : allUsers) {
        	if(!u.isDeleted()) {
        		activeUsers.add(u);
        	}
        }
        return userMapper.entitiesToDtos(activeUsers);
    }
    
   @Override
   public UserResponseDto changeUsername(UserRequestDto userRequestDto, String username) {
	   User userToChange = userMapper.DtoToEntity(userRequestDto);
	   Credentials userCreds = credentialsMapper.DtoToEntityCred(userRequestDto.getCredentials());
	   Profile userProf = profileMapper.ProfDtoToEnt(userRequestDto.getProfile());
	   if(!userRepository.existsByCredentialsUsername(userCreds.getUsername()) || userCreds == null || userProf == null || userCreds.getUsername() == null 
			   || userCreds.getPassword() == null || !userRepository.findByCredentialsUsername(userCreds.getUsername()).get().getCredentials().getPassword().equals(userCreds.getPassword()) 
			   || !userRepository.findByCredentialsUsername(userCreds.getUsername()).get().getCredentials().getUsername().equals(userCreds.getUsername())) {
		   
		   throw new BadRequestException("incorrect username/password");
	   }
	   userToChange.getCredentials().setUsername(username);
	   userRepository.save(userToChange);
	   return userMapper.entityToDto(userToChange);
   }
}
