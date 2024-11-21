package com.cooksys.twitter_api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.twitter_api.entities.Tweet;
import com.cooksys.twitter_api.entities.User;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

	Optional<List<Tweet>> findAllTweetsByAuthor(User user);


}
