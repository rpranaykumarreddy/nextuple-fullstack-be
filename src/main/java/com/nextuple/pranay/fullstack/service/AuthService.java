package com.nextuple.pranay.fullstack.service;

import com.nextuple.pranay.fullstack.dto.AddUserRequest;
import com.nextuple.pranay.fullstack.dto.AddUserResponse;
import com.nextuple.pranay.fullstack.dto.JWTAuthResponse;
import com.nextuple.pranay.fullstack.dto.LoginAuthRequest;
import com.nextuple.pranay.fullstack.exception.CustomException;
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

    public ResponseEntity<?> addUser(AddUserRequest addUserRequest){
        if(usersRepo.existsByUsername(addUserRequest.getUsername())){
           throw new CustomException.EntityExistsException("Username already exists. Please try another username");
        }
        if(usersRepo.existsByEmail(addUserRequest.getEmail())){
            throw new CustomException.EntityExistsException("Email Exists. Please try login with your email or try another email.");
        }
        Users userDB = addUserRequest.toUser();
        userDB.setPassword(passwordEncoder.encode(userDB.getPassword()));
        userDB.setRoles("ROLE_USER");
        userDB.setCreated(LocalDateTime.now());
        Users saveResponse = null;
        try {
            saveResponse=usersRepo.save(userDB);
        } catch (Exception e) {
            throw new CustomException.UnableToSaveException("Unable to save user");
        }
        return new ResponseEntity<>(
                new AddUserResponse("User Created Successfully with username: " + saveResponse.getUsername(),saveResponse.getUsername())
                , HttpStatus.CREATED);
    }
    public ResponseEntity<?> addAdmin(AddUserRequest addUserRequest){
        if(usersRepo.existsByUsername(addUserRequest.getUsername())){
            throw new CustomException.EntityExistsException("Username already exists. Please try another username");
        }
        if(usersRepo.existsByEmail(addUserRequest.getEmail())){
            throw new CustomException.EntityExistsException("Email Exists. Please try login with your email or try another email.");
        }
        Users userDB = addUserRequest.toUser();
        userDB.setPassword(passwordEncoder.encode(userDB.getPassword()));
        userDB.setRoles("ROLE_ADMIN");
        userDB.setCreated(LocalDateTime.now());
        Users saveResponse = null;
        try {
            saveResponse = usersRepo.save(userDB);
        } catch (Exception e) {
            throw new CustomException.UnableToSaveException("Unable to save user");
        }
        usersRepo.save(userDB);
        return new ResponseEntity<>(
                new AddUserResponse("Admin Created Successfully with username: " + saveResponse.getUsername(),saveResponse.getUsername())
                , HttpStatus.CREATED);
    }
    public ResponseEntity<?> login(LoginAuthRequest loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword())
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }
}
