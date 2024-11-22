package com.cooksys.twitter_api.services.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.mappers.HashtagMapper;
import com.cooksys.twitter_api.repositories.HashtagRepository;
import com.cooksys.twitter_api.services.HashtagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
	
	private final HashtagRepository hashtagRepository;
	private final HashtagMapper hashtagMapper;
	
	@Override
	public ResponseEntity<List<HashtagDto>> getAllTags() {
		return new ResponseEntity<>(hashtagMapper.entitiesToDtos(hashtagRepository.findAll()), HttpStatus.OK);
	}
}
