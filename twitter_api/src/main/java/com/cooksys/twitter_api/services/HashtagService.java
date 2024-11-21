package com.cooksys.twitter_api.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cooksys.twitter_api.dtos.HashtagDto;

public interface HashtagService {

	ResponseEntity<List<HashtagDto>> getAllTags();

}
