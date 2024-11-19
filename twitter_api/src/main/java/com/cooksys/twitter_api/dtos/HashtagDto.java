package com.cooksys.twitter_api.dtos;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HashtagDto {

	private String label;
	
	private Timestamp firstUsed;
	
	private Timestamp lastUsed;
}
