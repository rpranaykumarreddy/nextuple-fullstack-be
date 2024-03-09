package com.nextuple.pranay.fullstack.service;

import com.nextuple.pranay.fullstack.dto.AddUserRequest;
import com.nextuple.pranay.fullstack.dto.LoginAuthResponse;
import com.nextuple.pranay.fullstack.dto.LoginAuthRequest;
import com.nextuple.pranay.fullstack.dto.MessageResponse;
import com.nextuple.pranay.fullstack.exception.CustomException;
import com.nextuple.pranay.fullstack.model.Users;
import com.nextuple.pranay.fullstack.model.Wallets;
import com.nextuple.pranay.fullstack.repo.UsersRepo;
import com.nextuple.pranay.fullstack.repo.WalletsRepo;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private WalletsRepo walletsRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Transactional
    public ResponseEntity<MessageResponse> addUser(AddUserRequest addUserRequest){
        if(usersRepo.existsByUsernameIgnoreCase(addUserRequest.getUsername())){
           throw new CustomException.EntityExistsException("Username already exists. Please try another username");
        }
        if(usersRepo.existsByEmailIgnoreCase(addUserRequest.getEmail())){
            throw new CustomException.EntityExistsException("Email already exists. Please try login with another email.");
        }
        Users userDB = addUserRequest.toUser();
        userDB.setPassword(passwordEncoder.encode(userDB.getPassword()));
        userDB.setRoles("ROLE_USER");
        userDB.setCreated(LocalDateTime.now());
        Wallets wallet = new Wallets(userDB.getUsername(), 0.0, null,false, LocalDateTime.now(), LocalDateTime.now());
        Users userResponse;
        try {
            userResponse=usersRepo.save(userDB);
            walletsRepo.save(wallet);
        } catch (Exception e) {
            throw new CustomException.UnableToSaveException("Unable to register user");
        }
        return new ResponseEntity<>(
                new MessageResponse("Registration successful with username: "+userResponse.getUsername()),
                HttpStatus.CREATED);
    }
    public ResponseEntity<LoginAuthResponse> login(LoginAuthRequest loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword())
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        LoginAuthResponse jwtAuthResponse = new LoginAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }

    public ResponseEntity<Boolean> checkUsername(String username) {
        return new ResponseEntity<>(!(usersRepo.existsByUsernameIgnoreCase(username)), HttpStatus.OK);
    }

}
