package com.cooksys.twitter_api.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ProfileMapper.class, CredentialsMapper.class })
public interface UserMapper {
  
    @Mapping(target = "username", source = "credentials.username")
    UserResponseDto entityToDto(User user);
    
    User DtoToEntity(UserRequestDto userRequestDto);

    List<User> DtosToEntities(List<UserRequestDto> userRequestDtos);
    
    List<UserResponseDto> entitiesToDtos(List<User> users);

    
}
