package com.cooksys.twitter_api.entities;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name="user_table")
public class User {

    @Id
    @GeneratedValue
    private long id;
    
    @CreationTimestamp
    private Timestamp joined;

    private boolean deleted;
    
    public void delete() {
    	deleted = true;
    }
    
    public boolean isDeleted() {
    	return deleted;
    }

    @ManyToMany
    @JoinTable(
			name = "follower_following", 
			joinColumns = @JoinColumn(name = "following_id"),
			inverseJoinColumns = @JoinColumn(name = "follower_id")
	)
    private List<User> followers = new ArrayList<>();
    
    @ManyToMany(mappedBy = "followers")
    private List<User> following = new ArrayList<>();
    
    @OneToMany(mappedBy="author")
    private List<Tweet> tweets = new ArrayList<>(); 
    
    @ManyToMany(mappedBy = "tlikes")
    private List<Tweet> likes = new ArrayList<>();
    
    public void addLike(Tweet post) {
    	likes.add(post);
    }
    
    @ManyToMany(mappedBy = "mentionedUsers")
    private List<Tweet> mentioned = new ArrayList<>();
    
    @Embedded
    private Credentials credentials; 
    
    public Credentials getCredentials() {
    	return credentials;
    }
    
    @Embedded
    private Profile profile; 
    
}
