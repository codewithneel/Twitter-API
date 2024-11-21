package com.cooksys.twitter_api.services;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface UserService {

//    List<ProfileDto> getProfiles();
//
//    List<User> getUsers();

	public ResponseEntity<List<TweetResponseDto>> getAllTweetsCreatedByUser(String username);

	public ResponseEntity<List<UserResponseDto>> getUserFollowers(String username);


}
