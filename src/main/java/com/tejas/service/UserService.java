package com.tejas.service;

import com.tejas.exceptions.UserException;
import com.tejas.model.User;

import java.util.List;

public interface UserService {
    User getUserFromJwtToken(String jwt) throws UserException;
    User getUserByEmail(String email) throws UserException;
    User getCurrentUser() throws UserException;
    User getUserById(Long Id) throws UserException;
    List<User> getAllUsers();
}
