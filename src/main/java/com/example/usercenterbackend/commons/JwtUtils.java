package com.example.usercenterbackend.commons;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {

    private static final String key = "anchor15927";

    public static String generateToken(Map<String, Object> claims,Date expiration) {
        return JWT.create()
                .withClaim("claims",claims)
                .withExpiresAt(expiration)
                .sign(Algorithm.HMAC256(key));
    }

    public static Map<String,Object> verifyToken(String token) {
        return JWT.require(Algorithm.HMAC256(key))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }
}
