package com.cooksys.twitter_api.entities;


import java.sql.Timestamp;
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
    private String username;
    private String password;

    @GeneratedValue
    private Timestamp timestamp;

    private boolean deleted;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @ManyToMany
    @JoinTable(name="followers_following", joinColumns = @JoinColumn(name="followers_id"), inverseJoinColumns = @JoinColumn(name="following_id"))
    private List<User> followers;
    
    @OneToMany(mappedBy="author")
    private List<Tweet> tweets; 
    
    @Embedded
    private Credentials credentials; 
    
    @Embedded
    private Profile profile; 
    
    
}
