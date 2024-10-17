package com.dart.product.security;


import com.dart.product.repository.RedisProductCacheRepo;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService  implements UserDetailsService {

    private final RedisProductCacheRepo redisTemplate;
    private final FilterService filterService;
    private final BCryptPasswordEncoder encoder;

    public CustomUserDetailsService(RedisProductCacheRepo redisTemplate, FilterService filterService) {
        this.redisTemplate = redisTemplate;
        this.filterService = filterService;
        this.encoder = new BCryptPasswordEncoder(12);
    }

    @Override
    public UserDetails loadUserByUsername(String token) {

        if(redisTemplate.isTokenBlacklisted(token)) {
            return new User(
                    filterService.extractEmail(token),
                    encoder.encode(filterService.extractUUID(token)),
                    Collections.emptyList()
            );
        }

        throw new CustomRuntimeException(
                new ErrorHandler(false, "Token BlackListed", "The token use is blacklisted."),
                HttpStatus.BAD_REQUEST
        );

    }
}