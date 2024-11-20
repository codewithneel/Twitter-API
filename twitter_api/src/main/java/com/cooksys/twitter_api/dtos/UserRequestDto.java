package com.cooksys.twitter_api.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserRequestDto {

    private ProfileDto profile;
    
    private CredentialsDto credentials;

}
