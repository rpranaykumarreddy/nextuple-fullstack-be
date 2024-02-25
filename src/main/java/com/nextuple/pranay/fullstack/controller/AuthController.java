package com.nextuple.pranay.fullstack.controller;

import com.nextuple.pranay.fullstack.dto.JWTAuthResponse;
import com.nextuple.pranay.fullstack.dto.LoginAuthRequest;
import com.nextuple.pranay.fullstack.model.Users;
import com.nextuple.pranay.fullstack.security.UserInfoUserDetailsService;
import com.nextuple.pranay.fullstack.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@CrossOrigin("*")
@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping(value = "/adduser")
    public ResponseEntity<?> addNewUser(@RequestBody Users user){
        return authService.addUser(user);
    }
    @PostMapping(value = "/addadmin")
    public ResponseEntity<?> addNewAdmin(@RequestBody Users user){
        return authService.addAdmin(user);
    }
    @PostMapping(value = "/signin")
    public ResponseEntity<?> login(@RequestBody LoginAuthRequest loginDto){
        String token = authService.login(loginDto);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }
    @PostMapping(value = "/audit")
    public ResponseEntity<?> testAPIAudit(){
        return new ResponseEntity<>("Entered Audit API", HttpStatus.ACCEPTED);
    }
    @PostMapping(value = "/wallet")
    public ResponseEntity<?> testAPIWallet(){
        return new ResponseEntity<>("Entered Wallet API", HttpStatus.ACCEPTED);
    }
}
