package com.tejas.controller;

import com.tejas.mapper.UserMapper;
import com.tejas.model.User;
import com.tejas.payload.dto.UserDto;
import com.tejas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile(@RequestHeader("Authorization") String jwt) {
        User user = userService.getUserFromJwtToken(jwt);
        return new ResponseEntity<>(UserMapper.toDto(user), HttpStatus.OK);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long Id) {
        User user = userService.getUserById(Id);
        return new ResponseEntity<>(UserMapper.toDto(user), HttpStatus.OK);
    }
}
