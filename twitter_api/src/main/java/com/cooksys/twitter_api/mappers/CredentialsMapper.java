package com.cooksys.twitter_api.mappers;

import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.entities.Credentials;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {


    Credentials DtoToEntityCred(CredentialsDto credsDto);

    List<Credentials> DtosToEntitiesCred(List<CredentialsDto> credentialsDtos);

}
