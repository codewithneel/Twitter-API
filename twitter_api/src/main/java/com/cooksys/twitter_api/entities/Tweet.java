package com.cooksys.twitter_api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
public class Tweet {
	
	@Id
	@GeneratedValue
	@Getter
	private Long id; 
	
	@ManyToOne
	@JoinColumn
	@Column(nullable=false)
	@Getter
	@Setter
	private User author;
	
	@Column(nullable=false)
	@Getter 
	@Setter
	private Timestamp posted; 
	
	@Column(nullable=false)
	@Getter 
	@Setter
	private boolean deleted = false;
	
	@Getter 
	@Setter
	private String content; 
	
	//self referencing relationship
	@ManyToOne
	@JoinColumn
	@Getter 
	@Setter
	private Tweet inReplyTo;
	
	@ManyToOne
	@JoinColumn
	@Getter 
	@Setter
	private Tweet repostOf;
	
	@ManyToMany
	@JoinTable(
		name = "user_likes",
		joinColumns = @JoinColumn(name = "tweet_id"),
		inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	@Getter 
	@Setter
	private Set<User> likes = new HashSet<>();
	
	@ManyToMany
	@JoinTable(
		name = "user_mentions",
		joinColumns = @JoinColumn(name = "tweet_id"),
		inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	@Getter 
	@Setter
	private Set<User> mentions = new HashSet<>();
	
	@ManyToMany
	@JoinTable(
		name = "tweet_hashtags",
		joinColumns = @JoinColumn(name = "tweet_id"),
		inverseJoinColumns = @JoinColumn(name = "hashtag_id")
	)
	@Getter 
	@Setter
	private Set<Hashtag> hashtags = new HashSet<>();
}
