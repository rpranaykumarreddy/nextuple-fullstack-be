package com.nextuple.pranay.fullstack.controller;

import com.nextuple.pranay.fullstack.dto.AddUserRequest;
import com.nextuple.pranay.fullstack.dto.LoginAuthRequest;
import com.nextuple.pranay.fullstack.dto.LoginAuthResponse;
import com.nextuple.pranay.fullstack.dto.MessageResponse;
import com.nextuple.pranay.fullstack.service.AuthService;
import com.nextuple.pranay.fullstack.utils.AuthUserUtils;
import com.nextuple.pranay.fullstack.utils.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping(Globals.authControllerMap)
public class AuthController {

    @Autowired
    AuthService authService;
    @Autowired
    private AuthUserUtils authUserUtils;

    @PostMapping(Globals.authController_addNewUserMap)
    public ResponseEntity<MessageResponse> addNewUser(@RequestBody AddUserRequest addUserRequest){
        addUserRequest.validate();
        return authService.addUser(addUserRequest);
    }

    @PostMapping(Globals.authController_loginMap)
    public ResponseEntity<LoginAuthResponse> login(@RequestBody LoginAuthRequest loginDto){
        loginDto.validate();
        return authService.login(loginDto);
    }

    @PostMapping(Globals.authController_regenerateMap)
    public ResponseEntity<LoginAuthResponse> regenerate(@RequestHeader("Authorization") String token){
        String username = authUserUtils.getUserId(token);
        return authService.regenerate(token,username);
    }
    @GetMapping(Globals.authController_checkUsernameMap)
    public ResponseEntity<Boolean> checkUsername(@PathVariable String username){
        String usernameIgnoreCase = username.toLowerCase();
        return authService.checkUsername(usernameIgnoreCase);
    }
}
