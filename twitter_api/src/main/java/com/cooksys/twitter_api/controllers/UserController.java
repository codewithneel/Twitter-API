package com.cooksys.twitter_api.controllers;

import com.cooksys.twitter_api.dtos.TweetRequestDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.Tweet;
import com.cooksys.twitter_api.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @GetMapping("/@{username}/mentions}")
    public List<TweetRequestDto> retrieveUserMentions(@PathVariable String username) {
        List<TweetRequestDto> tweets = new ArrayList<>();
        return tweets;
    }

    @PostMapping("/@{username}/unfollow")
    public UserResponseDto unfollowUser(@PathVariable String username){
        UserResponseDto unfollowed = new UserResponseDto();
        return unfollowed;
    }

    @GetMapping("/@{username}/feed}")
    public List<TweetResponseDto> retrieveFeed(@PathVariable String username) {
        List<TweetResponseDto> fullFeed = new ArrayList<>();
        return fullFeed;
    }

    @PostMapping("/@{username}/follow")
    public UserResponseDto followUser(@PathVariable String username){
        UserResponseDto followed = new UserResponseDto();
        return followed;
    }

    @GetMapping("/@{username}/tweets")
    public List<TweetResponseDto> retrieveAllTweets(@PathVariable String username){
        List<TweetResponseDto> allTweets = new ArrayList<>();
        return allTweets;
    }

    @DeleteMapping("user/@{username}")
    public UserResponseDto deleteUser(@PathVariable String username){
        UserResponseDto deletedUser = new UserResponseDto();
        return deletedUser;
    }

    @GetMapping("users/@{username}/followers")
    public List<UserResponseDto> retrieveFollowers(@PathVariable String username){
        List<UserResponseDto> followers = new ArrayList<>();
        return followers;
    }

    // Needs to add Patch Users, Get Users Following and Get Users username


}
