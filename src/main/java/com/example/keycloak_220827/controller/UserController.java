package com.example.keycloak_220827.controller;

import com.example.keycloak_220827.dto.UserDto;
import com.example.keycloak_220827.service.UserService;
import com.example.keycloak_220827.vo.RequestUser;
import com.example.keycloak_220827.vo.ResponseUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/test")
public class UserController {

    private Environment env;
    private UserService userService;

    @Autowired
    public UserController(Environment env, UserService userService){
        this.env = env;
        this.userService = userService;
    }

//    @GetMapping("/health_check")
    @RolesAllowed("admin")
    @GetMapping("/status")
    public String status(){
        return "it's working in user service";
    }

    @PostMapping("/users")
    @RolesAllowed("admin")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user){

//        RequestUser -> UserDto 蹂??
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);

//        userDto -> ResponseUser
        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }


}
