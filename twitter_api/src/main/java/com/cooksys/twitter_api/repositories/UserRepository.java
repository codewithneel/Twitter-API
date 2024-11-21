package com.cooksys.twitter_api.repositories;

import com.cooksys.twitter_api.entities.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	public Optional<User> findByCredentialsUsername(String username);

}

