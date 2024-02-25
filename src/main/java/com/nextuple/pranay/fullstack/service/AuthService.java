package com.nextuple.pranay.fullstack.service;

import com.nextuple.pranay.fullstack.dto.LoginAuthRequest;
import com.nextuple.pranay.fullstack.model.Users;
import com.nextuple.pranay.fullstack.repo.UsersRepo;
import com.nextuple.pranay.fullstack.security.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    public ResponseEntity<?> addUser(Users user){
        System.out.println(user);
        if(usersRepo.existsByUsername(user.getUsername())){
            throw new RuntimeException("Username Exists");
        }
        if(usersRepo.existsByEmail(user.getEmail())){
            throw new RuntimeException("Email Exists");
        }
        Users userDB = new Users();
        userDB.setUsername(user.getUsername());
        userDB.setEmail(user.getEmail());
        userDB.setPassword(passwordEncoder.encode(user.getPassword()));
        userDB.setRoles("ROLE_USER");
        userDB.setCreated(LocalDateTime.now());
        usersRepo.save(userDB);
        return new ResponseEntity<>(userDB, HttpStatus.CREATED);
    }
    public ResponseEntity<?> addAdmin(Users user){
        System.out.println(user);
        if(usersRepo.existsByUsername(user.getUsername())){
            throw new RuntimeException("Username Exists");
        }
        if(usersRepo.existsByEmail(user.getEmail())){
            throw new RuntimeException("Email Exists");
        }
        Users userDB = new Users();
        userDB.setUsername(user.getUsername());
        userDB.setEmail(user.getEmail());
        userDB.setPassword(passwordEncoder.encode(user.getPassword()));
        userDB.setRoles("ROLE_ADMIN, ROLE_USER");
        userDB.setCreated(LocalDateTime.now());
        usersRepo.save(userDB);
        return new ResponseEntity<>(userDB, HttpStatus.CREATED);
    }
    public String login(LoginAuthRequest loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword())
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        return token;
    }
}
