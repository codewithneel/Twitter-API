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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
public class Tweet {
	
	@Id
	@GeneratedValue
	private Long id; 
	
	@ManyToOne
	@Column(nullable=false)
	private User author;
	
	@Column(nullable=false)
	private Timestamp posted; 
	
	@Column(nullable=false)
	private boolean deleted = false;
	
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
	private List<User> likes = new ArrayList<>();
	
	@ManyToMany
	private List<User> mentionedUsers = new ArrayList<>();
	
	@ManyToMany
	private List<Hashtag> hashtags = new ArrayList<>();
	
	
}
