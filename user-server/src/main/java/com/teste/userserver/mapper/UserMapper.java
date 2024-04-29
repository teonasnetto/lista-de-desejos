package com.teste.userserver.mapper;

import org.mapstruct.Mapper;

import com.teste.userserver.dto.AccountDto;
import com.teste.userserver.dto.UserDto;
import com.teste.userserver.dto.security.RegistrationRequest;
import com.teste.userserver.dto.security.RegistrationResponse;
import com.teste.userserver.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapRegistrationRequestToUser(RegistrationRequest registrationRequest);

    RegistrationResponse mapUserToRegistrationResponse(User user);

    AccountDto mapUserToAccountDto(User user);

    UserDto mapUserToUserDto(User user);
}