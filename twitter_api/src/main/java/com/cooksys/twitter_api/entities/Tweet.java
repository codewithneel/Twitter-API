package com.cooksys.twitter_api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


import org.hibernate.annotations.CreationTimestamp;

@Entity
@NoArgsConstructor
@Data
public class Tweet{
	
	@Id
	@GeneratedValue
	private Long id; 
	
	@ManyToOne
	@JoinColumn(nullable=false)
	private User author;
	
	@Column(nullable=false)
	@CreationTimestamp
	private Timestamp posted; 
	
	@Column(nullable=false)
	private boolean deleted = false;
	
	public boolean isDeleted() {
		return deleted;
	}
	
	private String content; 
	
	//self referencing relationship
	@ManyToOne
	private Tweet inReplyTo;
	
	@OneToMany(mappedBy = "inReplyTo")
	private List<Tweet> replies = new ArrayList<>();
	
	@ManyToOne
	private Tweet repostOf;
	
	@OneToMany(mappedBy = "repostOf")
	private List<Tweet> reposts = new ArrayList<>();
	
	@ManyToMany
	@JoinTable(
		name = "user_likes", 
		joinColumns = @JoinColumn(name = "tweet_id"),
		inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private List<User> tlikes = new ArrayList<>();
	
	@ManyToMany
	@JoinTable(
		name = "user_mentions", 
		joinColumns = @JoinColumn(name = "tweet_id"),
		inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private List<User> mentionedUsers = new ArrayList<>();
	
	@ManyToMany
	@JoinTable(
		name = "tweet_hashtags", 
		joinColumns = @JoinColumn(name = "tweet_id"),
		inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private List<Hashtag> hashtags = new ArrayList<>();
	
	public List<Hashtag> getHashtags(){
		return hashtags;
	}
		
}
