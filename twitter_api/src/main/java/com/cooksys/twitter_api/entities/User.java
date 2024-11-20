package com.cooksys.twitter_api.entities;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue
    private long id;

    @GeneratedValue
    private Timestamp timestamp;

    private boolean deleted;

    @ManyToMany
    private List<User> followers = new ArrayList<>();
    
    @ManyToMany(mappedBy = "followers")
    private List<User> following = new ArrayList<>();
    
    @OneToMany(mappedBy="author")
    private List<Tweet> tweets = new ArrayList<>(); 
    
    @ManyToMany(mappedBy = "likes")
    private List<Tweet> likes = new ArrayList<>();
    
    @ManyToMany(mappedBy = "mentionedUsers")
    private List<Tweet> mentioned = new ArrayList<>();
    
    @Embedded
    private Credentials credentials; 
    
    @Embedded
    private Profile profile; 
    
}
