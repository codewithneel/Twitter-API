package com.cooksys.twitter_api.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class UserResponseDto {

    private String username;

    private String firstName;

    private Timestamp timestamp;

    private String lastName;

    private String email;

}
