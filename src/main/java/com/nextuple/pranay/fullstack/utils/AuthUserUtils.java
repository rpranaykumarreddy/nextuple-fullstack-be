package com.nextuple.pranay.fullstack.utils;

import com.nextuple.pranay.fullstack.dto.CreateWalletResponse;
import com.nextuple.pranay.fullstack.model.Users;
import com.nextuple.pranay.fullstack.model.Wallets;
import com.nextuple.pranay.fullstack.repo.UsersRepo;
import com.nextuple.pranay.fullstack.security.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
@Component
public class AuthUserUtils {
    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private UsersRepo usersRepo;
    public String getUserId(String token){
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")){
            token = token.substring(7, token.length());
            if(jwtTokenProvider.validateToken(token)){
                String username = jwtTokenProvider.getUsername(token);
                // Create Wallet
                Users user = usersRepo.findByUsernameOrEmail(username,username).orElseThrow(
                        () -> new RuntimeException("User not found")
                );
                return user.getId();
            }else{
                throw new RuntimeException("Token is not valid");
            }
        }else{
            throw new RuntimeException("Token is not valid");
        }
    }
}
