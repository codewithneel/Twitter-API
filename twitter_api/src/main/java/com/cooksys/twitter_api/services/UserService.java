package com.cooksys.twitter_api.services;
import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.dtos.ProfileDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface UserService {

//    List<ProfileDto> getProfiles();
//
    public List<UserResponseDto> getUsers();

	public ResponseEntity<List<TweetResponseDto>> getAllTweetsCreatedByUser(String username);

	public List<UserResponseDto> getUserFollowers(String username);

	public UserResponseDto getUserByUsername(String username);

	public UserResponseDto deleteUser(String username, CredentialsDto credentialsDto);

	public UserResponseDto createUser(UserRequestDto userRequestDto);

	public void followUser(String username, CredentialsDto credentialsDto);

	public UserResponseDto changeUsername(UserRequestDto userRequestDto, String username);

	public List<TweetResponseDto> userMentions(String username);

	public void unfollowUser(String username, CredentialsDto credentialsDto);

	public List<TweetResponseDto> userfeed(String username);

	public List<UserResponseDto> getUsersFollowing(String username);

}
