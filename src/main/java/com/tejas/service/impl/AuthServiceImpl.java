package com.tejas.service.impl;

import com.tejas.configuration.JwtProvider;
import com.tejas.domain.UserRole;
import com.tejas.exceptions.UserException;
import com.tejas.mapper.UserMapper;
import com.tejas.model.User;
import com.tejas.payload.dto.UserDto;
import com.tejas.payload.response.AuthResponse;
import com.tejas.repository.UserRepository;
import com.tejas.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserImplementation customUserImplementation;

    @Override
    public AuthResponse signup(UserDto userDto) throws UserException {
        // validation - existing user + no-admin role during signup
        User user = userRepository.findByEmail(userDto.getEmail());
        if (user != null) {
            throw new UserException("There's already a user with that email. Highlander rules apply.");
        }
        if (userDto.getRole().equals(UserRole.ROLES_ADMIN)) {
            throw new UserException("Self-promotion to ADMIN detected. Access denied. We don't live in the Matrix.");
        }

        // new user creation (db)
        User newUser = new User();
        newUser.setFullName(userDto.getFullName());
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setRole(userDto.getRole());
        newUser.setPhone(userDto.getPhone());
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());
        newUser.setLastLogin(LocalDateTime.now());
        User savedUser = userRepository.save(newUser);

        // spring security - add authentication object for the new user
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // return response
        String jwt = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("And just like that... you're officially one of us.");
        authResponse.setUser(UserMapper.toDto(savedUser));

        return authResponse;
    }

    @Override
    public AuthResponse login(UserDto userDto) throws UserException {
        String email = userDto.getEmail();
        String password = userDto.getPassword();

        // spring security - validate + add authentication object for the user
        Authentication authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // update last login time of user
        User user = userRepository.findByEmail(email);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // return AuthResponse
        String jwt = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Welcome back, chosen one.");
        authResponse.setUser(UserMapper.toDto(user));

        return authResponse;
    }

    private Authentication authenticate(String email, String password) throws UserException {
        UserDetails userDetails = customUserImplementation.loadUserByUsername(email);
        if (userDetails == null) {
            throw new UserException("The credentials aren't worthy of becoming Pirate King.");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new UserException("The credentials aren't worthy of becoming Pirate King.");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
