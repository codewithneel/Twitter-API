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
import java.util.*;
import java.util.stream.Collectors;

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
		if (user.isEmpty() || user.get().isDeleted()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		//Retrieve all of the tweets created by the user
		Optional<List<Tweet>> tweetsByUser = tweetRepository.findAllTweetsByAuthor(user.get());

		//If the user has not created a tweet, return status code: found and nothing for the response body
		if (tweetsByUser.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.OK);
		}

		//Add all non-deleted tweets to result
		List<TweetResponseDto> tweetsToReturn = new ArrayList<>();
		for (Tweet tweet : tweetsByUser.get()) {
			if (!tweet.isDeleted()) {
				tweetsToReturn.add(tweetMapper.entityToDto(tweet));
			}
		}

		//Sort tweets by posted and then reverse to achieve descending order
		Collections.sort(tweetsToReturn);
		return new ResponseEntity<>(tweetsToReturn.reversed(), HttpStatus.OK);

	}


	@Override
	public List<UserResponseDto> getUserFollowers(String username) {

		//Validate username exists and is an active account
		Optional<User> user = userRepository.findByCredentialsUsername(username);
		if (user.isEmpty() || user.get().isDeleted()) {
			throw new NotFoundException("user does not exist");
		}

		//Get follower list and check if its empty
		List<User> followers = user.get().getFollowers();

		//Iterate through the follower list, add and convert non-deleted followers to List<UserResponseDto>
		List<UserResponseDto> result = new ArrayList<>();
		for (User follower : followers) {
			if (!follower.isDeleted()) {
				result.add(userMapper.entityToDto(follower));
			}
		}

		return result;
	}


	@Override
	public UserResponseDto getUserByUsername(String username) {
		Optional<User> user = userRepository.findByCredentialsUsername(username);
		if (user.isEmpty() || user.get().isDeleted()) {
			throw new NotFoundException();
		}

		return userMapper.entityToDto(user.get());
	}


	@Override
	public UserResponseDto deleteUser(String username, CredentialsDto credentialsDto) {
		Optional<User> user = userRepository.findByCredentialsUsername(username);
		if (user.isEmpty() || user.get().isDeleted()) {
			throw new NotFoundException();
		}

		if (user.get().getCredentials().getUsername().equals(credentialsDto.getUsername()) &&
				user.get().getCredentials().getPassword().equals(credentialsDto.getPassword())) {
			user.get().setDeleted(true);
			return userMapper.entityToDto(userRepository.saveAndFlush(user.get()));
		} else {
			throw new NotAuthorizedException();
		}
	}


	//*********** Joined date not set
	@Override
	public UserResponseDto createUser(UserRequestDto userRequestDto) {

		if (userRequestDto == null || userRequestDto.getCredentials() == null || userRequestDto.getProfile() == null ||
				userRequestDto.getCredentials().getUsername() == null || userRequestDto.getCredentials().getPassword() == null
				|| userRequestDto.getProfile().getFirstName() == null || userRequestDto.getProfile().getLastName() == null
				|| userRequestDto.getProfile().getEmail() == null || userRequestDto.getProfile().getPhone() == null) {
			throw new BadRequestException("Invalid Request");
		}
		//if username does not exist, create new user
		Optional<User> user = userRepository.findByCredentialsUsername(userRequestDto.getCredentials().getUsername());
		if (user.isEmpty()) {
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
		if (user.get().isDeleted() && userRequestDto.getCredentials().getPassword().equals(user.get().getCredentials().getPassword())) {
			user.get().setDeleted(false);
			return userMapper.entityToDto(userRepository.saveAndFlush(user.get()));
		}

		throw new BadRequestException("Invalid");
	}


	@Override
	public void followUser(String username, CredentialsDto credentialsDto) {
		if (credentialsDto == null || credentialsDto.getUsername() == null || credentialsDto.getPassword() == null) {
			throw new BadRequestException("missing fields in credentials");
		}

		Optional<User> user = userRepository.findByCredentialsUsername(credentialsDto.getUsername());
		if (user.isEmpty() || user.get().isDeleted() || !user.get().getCredentials().getPassword().equals(credentialsDto.getPassword())) {
			throw new BadRequestException("invalid credentials");
		}

		Optional<User> followUser = userRepository.findByCredentialsUsername(username);
		if (user.isEmpty() || user.get().isDeleted()) {
			throw new BadRequestException("cannot follow user -> user does not exist!");
		}

		List<User> userFollowingList = user.get().getFollowing();
		for (User followingUser : userFollowingList) {
			if (followingUser.equals(followUser.get())) {
				throw new BadRequestException("user already follows this account!!");
			}
		}
		user.get().getFollowing().add(followUser.get());
		followUser.get().getFollowers().add(user.get());
		userRepository.saveAndFlush(user.get());
		userRepository.saveAndFlush(followUser.get());

	}


	@Override
	public List<UserResponseDto> getUsers() {
		List<User> allUsers = userRepository.findAll();
		List<User> activeUsers = new ArrayList<>();
		for (User u : allUsers) {
			if (!u.isDeleted()) {
				activeUsers.add(u);
			}
		}
		return userMapper.entitiesToDtos(activeUsers);
	}

	//
	@Override
	public UserResponseDto changeUsername(UserRequestDto userRequestDto, String username) {

		if (userRequestDto == null || userRequestDto.getCredentials() == null || userRequestDto.getProfile() == null ||
				userRequestDto.getCredentials().getUsername() == null || userRequestDto.getCredentials().getPassword() == null) {
			throw new BadRequestException("bad info");
		}

		Optional<User> user = userRepository.findByCredentialsUsername(userRequestDto.getCredentials().getUsername());

		if (user.isEmpty() || user.get().isDeleted() || !userRequestDto.getCredentials().getPassword().equals(user.get().getCredentials().getPassword())) {
			throw new NotFoundException("no user is here");
		}

		user.get().getCredentials().setUsername(username);

		return userMapper.entityToDto(userRepository.saveAndFlush(user.get()));
	}

	// Returns the number of mentions of the user in all the tweets.
	@Override
	public List<TweetResponseDto> userMentions(String username) {
		if (username == null) {
			throw new BadRequestException("bad info");
		}
		if (username.isEmpty() || username.equals("")) {
			throw new NotFoundException("Unable to retrieve information");
		}
		if(username.charAt(0)=='@'){
			username=username.substring(1);
		}

		List<TweetResponseDto> searchTweets = new ArrayList<>();
		Optional<User> searched = userRepository.findByCredentialsUsername(username);
		for (Tweet tweet : tweetRepository.findAll()) {
			if (tweet.getMentionedUsers().contains(searched) && !tweet.isDeleted()) {
				searchTweets.add(tweetMapper.entityToDto(tweet));
			}
		}

		if (searchTweets.isEmpty()) {
			throw new NotFoundException("Unable to retrieve information");
		} else {
			return searchTweets;
		}


	}

	// Unfollows user using credentials
	@Override
	public void unfollowUser(String username, CredentialsDto credentialsDto) {
		if (username == null || credentialsDto == null || credentialsDto.getUsername() == null) {
			throw new BadRequestException("bad info");
		}

		if(username.charAt(0)=='@'){
			username=username.substring(1);
		}

		Optional<User> follower = userRepository.findByCredentialsUsername(credentialsDto.getUsername());
		if (follower.isEmpty()) {
			throw new BadRequestException("cannot unfollow user -> user does not exist!");
		} else {
			if (!follower.get().getCredentials().getPassword().equals(credentialsDto.getPassword())) {
				throw new BadRequestException("invalid credentials");
			}
		}

		Optional<User> followed = userRepository.findByCredentialsUsername(username);
		if (followed.isEmpty()) {
			throw new BadRequestException("cannot unfollow user -> user does not exist!");
		}

		if (followed.get().getFollowers().contains(follower.get())) {
			followed.get().getFollowers().remove(follower.get());
		} else {
			throw new BadRequestException("cannot unfollow user -> user not followed!");
		}

		follower.get().getFollowing().remove(followed.get());

	}

	// User Tweet feed
	@Override
	public List<TweetResponseDto> userfeed(String username) {
		if(username == null || username.isEmpty()) {
			throw new BadRequestException("bad info");
		}

		if(username.charAt(0)=='@'){
			username=username.substring(1);
		}

		Optional<User> user = userRepository.findByCredentialsUsername(username);
		if(user.isEmpty()) {
			throw new BadRequestException("cannot unfollow user -> user does not exist!");
		}

		List<Tweet> tweets = tweetRepository.findAll();
		List<TweetResponseDto> tweetResponseDtos = new ArrayList<>();
		for (Tweet tweet : tweets) {
			// captures original tweets from user
			if( tweet.getAuthor().equals(user) && !tweet.isDeleted()) {
				tweetResponseDtos.add(tweetMapper.entityToDto(tweet));
			}

			// capture replies to tweets from user
			if(tweet.getInReplyTo().getAuthor().equals(user) && !tweet.isDeleted()) {
				tweetResponseDtos.add(tweetMapper.entityToDto(tweet));
			}

			// captures repost of tweets by user
			if(tweet.getRepostOf().getAuthor().equals(user) && !tweet.isDeleted()) {}
		}

//		List<TweetResponseDto> sorted = sortTweetsByTimeStamp(tweetResponseDtos);
//
//		return sortTweetsByTimeStamp(tweetResponseDtos);

		return null;

	}

	// Not yet fully functional
	@Override
	public List<UserResponseDto> getUsersFollowing(String username) {
		if(username == null || username.isEmpty()) {
			throw new BadRequestException("bad info");
		}

		Optional<User> user = userRepository.findByCredentialsUsername(username);
		if(user.isEmpty()) {
			throw new BadRequestException("cannot unfollow user -> user does not exist!");
		}


		return userMapper.entitiesToDtos(user.get().getFollowing());
	}

	// Name validator, not yet fully functional
	@Override
	public boolean validateUsername(String username){
		if(username == null || username.isEmpty() || username.equals("")) {
			throw new BadRequestException("bad info");
		}

		Optional<User> target = userRepository.findByCredentialsUsername(username);
		if(target.isEmpty() || target.get().isDeleted()) {
			return false;
		} else {
			return true;
		}
	}

	public void sortTweetsByTimeStamp (List<TweetResponseDto> tweets) {
		Collections.sort(tweets, new Comparator<TweetResponseDto>() {
			@Override
			public int compare(TweetResponseDto t1, TweetResponseDto t2) {
				return t1.getPosted().compareTo(t2.getPosted());
			}
		});
	}

}
