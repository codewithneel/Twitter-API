package com.cooksys.twitter_api.mappers;


import com.cooksys.twitter_api.dtos.ProfileDto;
import com.cooksys.twitter_api.entities.Profile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileDto ProfEntToDto(Profile prof);

    Profile ProfDtoToEnt(ProfileDto);

    List<ProfileDto> ProfEntstoDtos(List<Profile> profiles);

    List<Profile> ProfDtosToEnts(List<ProfileDto> profileDtos);

}
