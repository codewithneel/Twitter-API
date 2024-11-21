package com.cooksys.twitter_api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.twitter_api.services.ValidateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/validate")
public class ValidateController {
	
	private final ValidateService validateService; 

	@GetMapping("/username/available/@{username}")
	public boolean isUsernameAvailable(@PathVariable String username) {
		return validateService.isUsernameAvailable(username); 
	}
	
}
