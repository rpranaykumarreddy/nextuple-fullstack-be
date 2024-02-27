package com.nextuple.pranay.fullstack.controller;

import com.nextuple.pranay.fullstack.dto.AddUserRequest;
import com.nextuple.pranay.fullstack.dto.LoginAuthRequest;
import com.nextuple.pranay.fullstack.dto.LoginAuthResponse;
import com.nextuple.pranay.fullstack.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping(value = "/register")
    public ResponseEntity<String> addNewUser(@RequestBody AddUserRequest addUserRequest){
        addUserRequest.validate();
        return authService.addUser(addUserRequest);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginAuthResponse> login(@RequestBody LoginAuthRequest loginDto){
        loginDto.validate();
        return authService.login(loginDto);
    }
    @GetMapping(value="/check-username/{username}")
    public ResponseEntity<Boolean> checkUsername(@PathVariable String username){
        return authService.checkUsername(username);
    }
}
