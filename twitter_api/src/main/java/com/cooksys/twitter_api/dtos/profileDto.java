package com.cooksys.twitter_api.dtos;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class profileDto {

    private String first_name;
    private String last_name;
    private String email;
    private String phone;
}
