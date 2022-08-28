package com.example.keycloak_220827.service;


import com.example.keycloak_220827.dto.UserDto;
import com.example.keycloak_220827.jpa.UserEntity;
import com.example.keycloak_220827.jpa.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto){
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd("encrypted_password");

        userRepository.save(userEntity);


        return null;
    }

    @Override
    public Iterable<UserEntity> getUserByAll(){
        return userRepository.findAll();
    }
}
