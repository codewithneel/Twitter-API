package com.cooksys.twitter_api.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.twitter_api.dtos.ContextDto;
import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetRequestDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;

import com.cooksys.twitter_api.entities.Credentials;
import com.cooksys.twitter_api.entities.Hashtag;
import com.cooksys.twitter_api.entities.Tweet;
import com.cooksys.twitter_api.entities.User;
import com.cooksys.twitter_api.exceptions.BadRequestException;

import com.cooksys.twitter_api.exceptions.NotFoundException;
import com.cooksys.twitter_api.mappers.CredentialsMapper;
import com.cooksys.twitter_api.mappers.HashtagMapper;
import com.cooksys.twitter_api.mappers.TweetMapper;
import com.cooksys.twitter_api.mappers.UserMapper;
import com.cooksys.twitter_api.repositories.HashtagRepository;
import com.cooksys.twitter_api.repositories.TweetRepository;
import com.cooksys.twitter_api.repositories.UserRepository;
import com.cooksys.twitter_api.services.TweetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
	
	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	private final HashtagMapper hashtagMapper;
	private final CredentialsMapper credentialsMapper;
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final HashtagRepository hashtagRepository;


	@Override
	public List<TweetResponseDto> getAllTweets() {
		List<TweetResponseDto> ret = new ArrayList<>(); 
		List<Tweet> checkDeleted = tweetRepository.findAll();
		for(Tweet t : checkDeleted) {
			if(t.isDeleted() == false) {
				ret.add(tweetMapper.entityToDto(t));
			}
		}
		return ret;
	}
//
	@Override
	public TweetResponseDto createSimpleTweet(TweetRequestDto tweetRequestDto) {
		
		Credentials credCheckBody = credentialsMapper.DtoToEntityCred(tweetRequestDto.getCredentials());
		List<User> allUsers = userRepository.findAll();
		boolean check = false;
		User tweeter = new User();
		for(User u : allUsers) {
			if(!u.isDeleted()) {
				if(u.getCredentials().equals(credCheckBody)) {
					check = true;
					tweeter = u;
				}
			}
		}
		if(!check) {
			throw new BadRequestException("wrong credentials");
		}
		
		Tweet ret = tweetMapper.dtoToEntity(tweetRequestDto);
		if(ret.getContent() == null) throw new BadRequestException("No content provided!");
		ret.setAuthor(tweeter);
		
		//get the content and split it to go through and check for hashtags and mentions
		String content = ret.getContent();
		String[] contents = content.split(" ");
		for(String s : contents) {
			
			//if we have a hashtag we need to see if that is already in the database and assign the connections
			if(s.startsWith("#")) {
				List<Hashtag> allTags = hashtagRepository.findAll();
				boolean isTag = false;
				for(Hashtag h : allTags) {
					if(h.getLabel().equals(s)) {
						String temp = s.substring(1);
						h.setLabel(temp);
						h.addTweet(ret);
						ret.addTag(h);
						isTag = true;
						hashtagRepository.saveAndFlush(h);
					}
				}
				
				//if the tag doesnt exist yet we need to create one
				if(!isTag) {
					Hashtag tempTag = new Hashtag();
					tempTag.setLabel(s.substring(1));
					tempTag.addTweet(ret);
					ret.addTag(tempTag);
					hashtagRepository.saveAndFlush(tempTag);
				}
			}
			
			//check if the content mentions a user and if that user is real and active then we assign that user
			if(s.startsWith("@")) {
				String mentionedName = s.substring(1);
				for(User mentUser : allUsers) {
					if(mentUser.getCredentials().getUsername().equals(mentionedName)) {
						ret.addMentioned(mentUser);
						mentUser.addMentioned(ret);
						userRepository.saveAndFlush(mentUser);
					}
				}
			}
		}
		tweetRepository.save(ret);
		return tweetMapper.entityToDto(ret);
	}

	@Override
	public TweetResponseDto getTweet(Long id) {
		if(tweetRepository.findById(id).get() == null) {
			throw new NotFoundException("Tweet does not exist");
		}
		Tweet ret = tweetRepository.findById(id).get();
		if(ret.isDeleted() == true) {
			throw new NotFoundException("Tweet has been deleted");
		}
		return tweetMapper.entityToDto(ret);
	}

	@Override
	public TweetResponseDto deleteTweet(Long id, CredentialsDto credentialsDto) {
		if(tweetRepository.findById(id).get() == null) {
			throw new NotFoundException("Tweet does not exist");
		}
		Tweet ret = tweetRepository.findById(id).get();
		if(ret.isDeleted() == true) {
			throw new NotFoundException("Tweet has been deleted");
		}
		Credentials credCheckBody = credentialsMapper.DtoToEntityCred(credentialsDto);
		Credentials credCheckTweet = ret.getAuthor().getCredentials();
		if(!credCheckBody.equals(credCheckTweet)) {
			throw new BadRequestException("wrong credentials");
		}
		ret.delete();
		tweetRepository.save(ret);
		return tweetMapper.entityToDto(ret);
	}

	@Override
	public void likeTweet(Long id, CredentialsDto credentialsDto) {
		if(tweetRepository.findById(id).get() == null) {
			throw new NotFoundException("Tweet does not exist");
		}
		Tweet ret = tweetRepository.findById(id).get();
		if(ret.isDeleted() == true) {
			throw new NotFoundException("Tweet has been deleted");
		}
		Credentials credCheckBody = credentialsMapper.DtoToEntityCred(credentialsDto);
		List<User> allUsers = userRepository.findAll();
		boolean check = false;
		User liker = new User();
		for(User u : allUsers) {
			if(!u.isDeleted()) {
				if(u.getCredentials().equals(credCheckBody)) {
					check = true;
					liker = u;
				}
			}
		}
		if(!check) {
			throw new BadRequestException("wrong credentials");
		}
		if(!liker.getLikes().contains(ret)) {
			liker.addLike(ret);
			userRepository.saveAndFlush(liker);
			ret.likedBy(liker);
			tweetRepository.saveAndFlush(ret);
		}
	}

	@Override
	public TweetResponseDto replyTweet(Long id, TweetRequestDto tweetRequestDto) {
		// TODO Auto-generated method stub
		if(id==null || tweetRequestDto == null || tweetRequestDto.getContent() == null || tweetRequestDto.getCredentials() == null || tweetRequestDto.getCredentials().getUsername() == null || tweetRequestDto.getCredentials().getPassword() == null) {
			
			throw new BadRequestException("Invalid request");
		}

		Credentials credCheckBody = credentialsMapper.DtoToEntityCred(tweetRequestDto.getCredentials());
		String username = credCheckBody.getUsername();
		Optional<User> check = userRepository.findByCredentialsUsername(username);
		if(check.isEmpty() || check.get().isDeleted() || !check.get().getCredentials().getPassword().equals(credCheckBody.getPassword())) {
			throw new BadRequestException("wrong credentials");
		}

		Optional<Tweet> ret = tweetRepository.findById(id);
		if(ret.isEmpty() || ret.get().isDeleted()) {
			throw new NotFoundException("Tweet does not exist");
		}

        Tweet reply = new Tweet();
		reply.setAuthor(check.get());
		reply.setContent(tweetRequestDto.getContent());
		reply.setInReplyTo(ret.get());
		reply.setDeleted(false);
		reply.setRepostOf(null);
		

		String[] contentcheck = tweetRequestDto.getContent().split(" ");
		for(String s : contentcheck) {

			//if we have a hashtag we need to see if that is already in the database and assign the connections
			if(s.startsWith("#")) {
				List<Hashtag> allTags = hashtagRepository.findAll();
				boolean isTag = false;
				for(Hashtag h : allTags) {
					if(h.getLabel().equals(s)) {
						String temp = s.substring(1);
						h.setLabel(temp);
						h.addTweet(reply);
						reply.addTag(h);
						isTag = true;
						hashtagRepository.saveAndFlush(h);
					}
				}

				//if the tag doesnt exist yet we need to create one
				if(!isTag) {
					Hashtag tempTag = new Hashtag();
					tempTag.setLabel(s.substring(1));
					tempTag.addTweet(reply);
					reply.addTag(tempTag);
					hashtagRepository.saveAndFlush(tempTag);
				}
			}

			//check if the content mentions a user and if that user is real and active then we assign that user
			if(s.startsWith("@")) {
				List<User> allUsers = userRepository.findAll();
				String mentionedName = s.substring(1);
				for(User mentUser : allUsers) {
					if(mentUser.getCredentials().getUsername().equals(mentionedName)) {
						reply.addMentioned(mentUser);
						mentUser.addMentioned(reply);
						userRepository.saveAndFlush(mentUser);
					}
				}
			}
		}

		ret.get().getReplies().add(reply);
		tweetRepository.saveAndFlush(ret.get());
		tweetRepository.saveAndFlush(reply);

		return tweetMapper.entityToDto(reply);
	}

	@Override
	public TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto) {
		if(credentialsDto.getUsername() == null || credentialsDto.getPassword() == null) {
			throw new BadRequestException("Missing fields in request");
		}
		Optional<User> user = userRepository.findByCredentialsUsername(credentialsDto.getUsername());
		if(user.isEmpty() || user.get().isDeleted() || !credentialsDto.getPassword().equals(user.get().getCredentials().getPassword())) {
			throw new NotFoundException("Invalid credentials -> No user found");
		}
		Optional<Tweet> tweet = tweetRepository.findById(id);
		if(tweet.isEmpty() || tweet.get().isDeleted()) {
			throw new NotFoundException("Invalid tweet id -> tweet does not exist");
		}
		Tweet ret = new Tweet();
		ret.setAuthor(user.get());
		ret.setContent(null);
		ret.setDeleted(false);
		ret.setRepostOf(tweet.get());
		
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(ret));
	}

	@Override
	public List<HashtagDto> getHashtagsAssociatedWtihTweet(Long id) {
		if(tweetRepository.findById(id).get() == null) {
			throw new NotFoundException("Tweet does not exist");
		}
		Tweet tempTweet = tweetRepository.findById(id).get();
		if(tempTweet.isDeleted() == true) {
			throw new NotFoundException("Tweet has been deleted");
		}
		List<HashtagDto> ret = hashtagMapper.entitiesToDtos(tempTweet.getHashtags());
		return ret;
	}
//
	@Override
	public List<UserResponseDto> getUsersWhoLikedTweet(Long id) {
		if(tweetRepository.findById(id).get() == null) {
			throw new NotFoundException("Tweet does not exist");
		}
		Tweet tempTweet = tweetRepository.findById(id).get();
		if(tempTweet.isDeleted() == true) {
			throw new NotFoundException("Tweet has been deleted");
		}
		List<User> tempLikers = tempTweet.getLikers();
		List<User> existingLikers = new ArrayList<>();
		for(User u : tempLikers) {
			if(!u.isDeleted()) {
				existingLikers.add(u);
			}
		}
		List<UserResponseDto> ret = userMapper.entitiesToDtos(existingLikers);
		return ret;
	}
	
	@Override
	public ContextDto getContextOfTweet(Long id) {

		Optional<Tweet> tweet = tweetRepository.findById(id);
		if(tweet.isEmpty() || tweet.get().isDeleted()) {
			throw new NotFoundException("tweet does not exist");
		}
		ContextDto ret = new ContextDto();
		ret.setTarget(tweetMapper.entityToDto(tweet.get()));
		List<TweetResponseDto> after = new ArrayList<>();
		for(Tweet reply : tweet.get().getReplies()) {
			if(!reply.isDeleted()) {
				after.add(tweetMapper.entityToDto(reply));
			}
		}
		ret.setAfter(after);
		ArrayList<TweetResponseDto> before = new ArrayList<>();
		Tweet inReplyTo = tweet.get().getInReplyTo();
		if(inReplyTo == null) {
			ret.setBefore(before);
		} else {
			if(inReplyTo != null || !inReplyTo.isDeleted()) {
				before.add(tweetMapper.entityToDto(inReplyTo));
				ret.setBefore(before);
			}
		}

		return ret; 
	}

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
	@Override
	public List<UserResponseDto> getUsersMentionedInTweet(Long id) {
		
		/*
		 * Validate tweet - 
		 * 1. Does the tweet exist? 
		 * 2. Is it a non-deleted tweet?
		 * */
		Optional<Tweet> tweet = tweetRepository.findById(id);
		if(tweet.isEmpty() || tweet.get().isDeleted()) {
			throw new NotFoundException("id is not valid");
		}
		
		/*
		 * Fetch mentioned users in tweet 
		 * Iterate through list and add non-deleted mentioned users to result
		 * */
		List<UserResponseDto> res = new ArrayList<>();
		for(User mentionedUser: tweet.get().getMentionedUsers()) {
			if(!mentionedUser.isDeleted()) {
				res.add(userMapper.entityToDto(mentionedUser));
			}
		}
	
		return res;
	}

	@Override
	public List<TweetResponseDto> getDirectRepliesToTweet(Long id) {
		if(id == null || tweetRepository.findById(id).get() == null) {
			throw new NotFoundException("Tweet does not exist");
		}
		List<Tweet> replies= tweetRepository.findById(id).get().getReplies();
		List<TweetResponseDto> ret = new ArrayList<>();
		for(Tweet t : replies){
			ret.add(tweetMapper.entityToDto(t));
		}

		return ret;
	}

	// Not fully functional
	@Override
	public List<TweetResponseDto> getDirectRepostsOfTweet(Long id){
		Tweet firstTweet = tweetRepository.findById(id).get();
		List<Tweet> reposts = firstTweet.getReposts();
		if(firstTweet.isDeleted() == true) {
			throw new NotFoundException("Tweet has been deleted");
		} else if(reposts.isEmpty()) {
			throw new NotFoundException("There are no repost");
		} else {
			List<TweetResponseDto> ret = new ArrayList<>();
			for(Tweet t : reposts) {
				ret.add(tweetMapper.entityToDto(t));
			}
			return ret;
		}

	}




}
