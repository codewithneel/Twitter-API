package com.cooksys.twitter_api.dtos;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileDto {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
