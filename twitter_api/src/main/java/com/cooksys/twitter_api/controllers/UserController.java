package com.cooksys.twitter_api.controllers;

import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.dtos.ProfileDto;
import com.cooksys.twitter_api.dtos.TweetRequestDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.Tweet;
import com.cooksys.twitter_api.entities.User;
import com.cooksys.twitter_api.services.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	
	private final UserService userService;

//    @GetMapping("/@{username}/mentions")
//    public List<TweetRequestDto> retrieveUserMentions(@PathVariable String username) {
//        List<TweetRequestDto> tweets = new ArrayList<>();
//        return tweets;
//    }
//
//    @PostMapping("/@{username}/unfollow")
//    public UserResponseDto unfollowUser(@PathVariable String username){
//        UserResponseDto unfollowed = new UserResponseDto();
//        return unfollowed;
//    }
//
//    @GetMapping("/@{username}/feed")
//    public List<TweetResponseDto> retrieveFeed(@PathVariable String username) {
//        List<TweetResponseDto> fullFeed = new ArrayList<>();
//        return fullFeed;
//    }
//
    @PostMapping("/@{username}/follow")
    public void followUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto){
        userService.followUser(username, credentialsDto);
    }

    @GetMapping("/@{username}/tweets")
    public ResponseEntity<List<TweetResponseDto>> getAllTweetsCreatedByUser(@PathVariable String username){
        return	userService.getAllTweetsCreatedByUser(username);
        
    }

    @DeleteMapping("/@{username}")
    public UserResponseDto deleteUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto){
        return userService.deleteUser(username, credentialsDto);
    }

    @GetMapping("/@{username}/followers")
    public ResponseEntity<List<UserResponseDto>> getUserFollowers(@PathVariable String username){
        return userService.getUserFollowers(username);
    }

    @GetMapping("/@{username}")
    public UserResponseDto getUserByUsername(@PathVariable String username) {
    	return userService.getUserByUsername(username);
    }
    
    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
    	return userService.createUser(userRequestDto);
    }
    


}
