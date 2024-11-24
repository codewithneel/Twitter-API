package com.cooksys.twitter_api.services;

public interface ValidateService {

	boolean isUsernameAvailable(String username);

	public boolean validateUsername(String username);

	public boolean tagExists(String tag);

}
