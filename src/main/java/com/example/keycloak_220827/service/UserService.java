package com.example.keycloak_220827.service;

import com.example.keycloak_220827.dto.UserDto;
import com.example.keycloak_220827.jpa.UserEntity;

public interface UserService {

    UserDto createUser(UserDto userDto);

    Iterable<UserEntity> getUserByAll();
}
