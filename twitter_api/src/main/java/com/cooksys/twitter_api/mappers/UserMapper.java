package com.cooksys.twitter_api.mappers;

import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ProfileMapper.class, CredentialsMapper.class })
public interface UserMapper {

    @Mapping(target = "username", source = "credentials.username")
    UserResponseDto entityToDto(User user);

    User DtoToEntity(User user);

    List<User> DtosToEntities(List<User> users);

    
}
