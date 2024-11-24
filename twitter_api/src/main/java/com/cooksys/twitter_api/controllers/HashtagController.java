package com.cooksys.twitter_api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.services.HashtagService;


import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class HashtagController {

	private final HashtagService hashtagService;
	
	@GetMapping()
	public ResponseEntity<List<HashtagDto>> getAllTags(){
		return hashtagService.getAllTags();
	}
	
	@GetMapping("/{label}")
	public List<TweetResponseDto> getTweetsFromTag(@PathVariable String label){
		return hashtagService.getTweetsFromTag(label);
	}

	@GetMapping("/validate/tag/exists/{label}")
	public boolean tagExists(@PathVariable String label){
		return hashtagService.validateTag(label);
	}
}
