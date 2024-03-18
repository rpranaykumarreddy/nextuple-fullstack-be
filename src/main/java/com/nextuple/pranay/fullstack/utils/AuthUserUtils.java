package com.nextuple.pranay.fullstack.utils;

import com.nextuple.pranay.fullstack.exception.CustomException;
import com.nextuple.pranay.fullstack.model.Users;
import com.nextuple.pranay.fullstack.repo.UsersRepo;
import com.nextuple.pranay.fullstack.security.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AuthUserUtils {
    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private UsersRepo usersRepo;
    public String getUserId(String token){
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")){
            token = token.substring(7);
            if(jwtTokenProvider.validateToken(token)){
                String username = jwtTokenProvider.getUsername(token);
                Users user = usersRepo.findByUsernameOrEmailIgnoreCase(username,username).orElseThrow(
                        () -> new CustomException.EntityNotFoundException("User not found")
                );
                return user.getUsername();
            }else{
                throw new CustomException.UnauthorizedException("Token is not valid");
            }
        }else{
            throw new CustomException.UnauthorizedException("Token is not valid");
        }
    }
}
