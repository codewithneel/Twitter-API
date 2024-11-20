package com.cooksys.twitter_api.mappers;

import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User DtoToEntity(UserRequestDto userRequestDto);

    List<User> DtosToEntities(List<UserRequestDto> userRequestDtos);

    
}
