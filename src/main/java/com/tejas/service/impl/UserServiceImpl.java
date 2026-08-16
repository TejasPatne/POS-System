package com.tejas.service.impl;

import com.tejas.configuration.JwtProvider;
import com.tejas.exceptions.UserException;
import com.tejas.model.User;
import com.tejas.repository.UserRepository;
import com.tejas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    @Override
    public User getUserFromJwtToken(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);
        return getUserByEmail(email);
    }

    @Override
    public User getUserByEmail(String email) throws UserException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("I'm Batman. And even I can't find this user.");
        }
        return user;
    }

    @Override
    public User getCurrentUser() throws UserException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserByEmail(email);
    }

    @Override
    public User getUserById(Long Id) throws UserException {
        return userRepository.findById(Id)
                .orElseThrow(() -> new UserException("Even Superman's X-ray vision can't find this user."));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
