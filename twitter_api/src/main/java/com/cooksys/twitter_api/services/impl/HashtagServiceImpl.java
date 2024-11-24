package com.cooksys.twitter_api.services.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.entities.Hashtag;
import com.cooksys.twitter_api.exceptions.NotFoundException;
import com.cooksys.twitter_api.mappers.HashtagMapper;
import com.cooksys.twitter_api.mappers.TweetMapper;
import com.cooksys.twitter_api.repositories.HashtagRepository;
import com.cooksys.twitter_api.services.HashtagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
	
	private final HashtagRepository hashtagRepository;
	private final HashtagMapper hashtagMapper;
	private final TweetMapper tweetMapper;

	@Override
	public ResponseEntity<List<HashtagDto>> getAllTags() {
		return new ResponseEntity<>(hashtagMapper.entitiesToDtos(hashtagRepository.findAll()), HttpStatus.OK);
	}

	@Override
	public List<TweetResponseDto> getTweetsFromTag(String label){
		List<Hashtag> allTags = hashtagRepository.findAll();
		Hashtag tagForTweets = new Hashtag();
		boolean isTag = false;
		for(Hashtag h : allTags) {
			if(h.getLabel().equals(label)) {
				tagForTweets = h;
				isTag = true;
			}
		}
		if(!isTag) {
			throw new NotFoundException("no hashtag with that label exists");
		}
		
		List<TweetResponseDto> ret = tweetMapper.entitiesToDtos(tagForTweets.getTweets());
		return ret;
	}

	@Override
	public boolean validateTag(String tag) {
		if(tag == null) {
			return false;
		}
		if(tag.substring(0,1) == "#"){
			tag = tag.substring(1);
		}

		List<Hashtag> allTags = hashtagRepository.findAll();
		for(Hashtag h : allTags) {
			if(h.getLabel().equals(tag)) {
				return true;
			}
		}
		return false;
	}

}
