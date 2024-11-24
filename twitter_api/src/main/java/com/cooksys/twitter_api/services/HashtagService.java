package com.cooksys.twitter_api.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;

public interface HashtagService {

	ResponseEntity<List<HashtagDto>> getAllTags();

	List<TweetResponseDto> getTweetsFromTag(String label);

    boolean validateTag(String label);
}
