package com.cooksys.twitter_api.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.twitter_api.entities.User;
import com.cooksys.twitter_api.repositories.UserRepository;
import com.cooksys.twitter_api.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService{
	
	private final UserRepository userRepository;
	
	@Override
	public boolean isUsernameAvailable(String username) {
		if(userRepository.existsByCredentialsUsername(username)) return false;
		return true; 
	}

	
}
