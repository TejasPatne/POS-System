package com.tejas.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConstants {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public static final String JWT_HEADER = "Authorization";

    public String getJwtSecret() {
        return jwtSecret;
    }
}