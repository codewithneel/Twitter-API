package com.cooksys.twitter_api.services.impl;

import java.util.List;
import java.util.Optional;

import com.cooksys.twitter_api.entities.Hashtag;
import com.cooksys.twitter_api.exceptions.BadRequestException;
import com.cooksys.twitter_api.repositories.HashtagRepository;
import com.cooksys.twitter_api.repositories.TweetRepository;
import org.springframework.stereotype.Service;

import com.cooksys.twitter_api.entities.User;
import com.cooksys.twitter_api.repositories.UserRepository;
import com.cooksys.twitter_api.services.ValidateService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService{
	
	private final UserRepository userRepository;
	private final HashtagRepository hashtagRepository;

	@Override
	public boolean isUsernameAvailable(String username) {
		if(userRepository.existsByCredentialsUsername(username)) return false;
		return true;
	}

	@Override
	public boolean validateUsername(String username){
		boolean valid = false;
		if(userRepository.findByCredentialsUsername(username).isPresent()) {
			valid = true;
		};

		return valid;
	}

	public boolean tagExists(String label){
		List<Hashtag> hashtags = hashtagRepository.findAll();
		for(Hashtag hash : hashtags) {
			if(hash.getLabel().equals(label)) {
				return true;
			}
		}
		return false;
	}
	
}
