package com.cooksys.twitter_api.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.entities.Tweet;
import com.cooksys.twitter_api.exceptions.NotFoundException;
import com.cooksys.twitter_api.mappers.HashtagMapper;
import com.cooksys.twitter_api.mappers.TweetMapper;
import com.cooksys.twitter_api.repositories.TweetRepository;
import com.cooksys.twitter_api.services.TweetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
	
	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	private final HashtagMapper hashtagMapper;

	@Override
	public List<TweetResponseDto> getAllTweets() {
		List<TweetResponseDto> ret = new ArrayList<>(); 
		List<Tweet> checkDeleted = tweetRepository.findAll();
		for(Tweet t : checkDeleted) {
			if(t.getDeleted() == false) {
				ret.add(tweetMapper.entityToDto(t));
			}
		}
		return ret;
	}
//
//	@Override
//	public TweetResponseDto createSimpleTweet(TweetRequestDto tweetRequestDto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
	@Override
	public TweetResponseDto getTweet(Long id) {
		if(tweetRepository.findById(id).get() == null) {
			throw new NotFoundException();//"Tweet does not exist");
		}
		Tweet ret = tweetRepository.findById(id).get();
		if(ret.getDeleted() == true) {
			throw new NotFoundException();//"Tweet has been deleted");
		}
		return tweetMapper.entityToDto(ret);
	}
//
//	@Override
//	public TweetResponseDto deleteTweet(Long id, CredentialsDto credentialsDto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void likeTweet(Long id, CredentialsDto credentialsDto) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public TweetResponseDto replyToTweet(Long id, CredentialsDto credentialsDto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
	@Override
	public List<HashtagDto> getHashtagsAssociatedWtihTweet(Long id) {
		if(tweetRepository.findById(id).get() == null) {
			throw new NotFoundException();//"Tweet does not exist");
		}
		Tweet tempTweet = tweetRepository.findById(id).get();
		if(tempTweet.getDeleted() == true) {
			throw new NotFoundException();//"Tweet has been deleted");
		}
		List<HashtagDto> ret = hashtagMapper.entitiesToDto(tempTweet.getHashtags());
		return ret;
	}
//
//	@Override
//	public List<UserResponseDto> getUsersWhoLikedTweet(Long id) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	@Override
//	public ContextDto getContextOfTweet(Long id) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<TweetResponseDto> getDirectRepliesToTweet(Long id) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<TweetResponseDto> getDirectRepostsOfTweet(Long id) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<UserResponseDto> getUsersMentionedInTweet(Long id) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
