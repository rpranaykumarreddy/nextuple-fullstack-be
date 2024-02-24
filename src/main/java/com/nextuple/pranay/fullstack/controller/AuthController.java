package com.nextuple.pranay.fullstack.controller;

import com.nextuple.pranay.fullstack.dto.LoginAuthRequest;
import com.nextuple.pranay.fullstack.model.Users;
import com.nextuple.pranay.fullstack.security.UserInfoUserDetailsService;
import com.nextuple.pranay.fullstack.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController

public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping(value = "/adduser")
    public ResponseEntity<?> addNewUser(@RequestBody Users user){
        user.setRoles("USER");
        user.setCreated(LocalDateTime.now());
        return authService.addUser(user);
    }
    @PostMapping(value = "/addadmin")
    public ResponseEntity<?> addNewAdmin(@RequestBody Users user){
        user.setRoles("ROLE_ADMIN");
        user.setCreated(LocalDateTime.now());
        return authService.addUser(user);
    }
    @PostMapping(value = "/signin")
    public ResponseEntity<?> login(@RequestBody LoginAuthRequest loginDto){
        return authService.login(loginDto);
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
