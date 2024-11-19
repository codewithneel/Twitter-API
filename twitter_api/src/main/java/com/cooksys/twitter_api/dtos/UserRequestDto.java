package com.cooksys.twitter_api.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserRequestDto {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

}
