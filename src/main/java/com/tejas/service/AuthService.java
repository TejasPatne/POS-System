package com.tejas.service;

import com.tejas.exceptions.UserException;
import com.tejas.payload.dto.UserDto;
import com.tejas.payload.response.AuthResponse;

public interface AuthService {
    AuthResponse signup(UserDto userDto) throws UserException;
    AuthResponse login(UserDto userDto) throws UserException;
}
