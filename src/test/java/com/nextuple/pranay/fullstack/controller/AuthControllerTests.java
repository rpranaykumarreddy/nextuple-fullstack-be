package com.nextuple.pranay.fullstack.controller;


import com.nextuple.pranay.fullstack.TestUtil;
import com.nextuple.pranay.fullstack.dto.AddUserRequest;
import com.nextuple.pranay.fullstack.dto.LoginAuthRequest;
import com.nextuple.pranay.fullstack.dto.LoginAuthResponse;
import com.nextuple.pranay.fullstack.dto.MessageResponse;
import com.nextuple.pranay.fullstack.exception.CustomException;
import com.nextuple.pranay.fullstack.service.AuthService;
import com.nextuple.pranay.fullstack.utils.AuthUserUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthControllerTests {

    @Mock
    private AuthService authService;
    @Mock
    private AuthUserUtils authUserUtils;

    @InjectMocks
    private AuthController authController;

    @Test
    public void testAddNewUser_Success_validation() {
        when(authService.addUser(any())).thenReturn(new ResponseEntity<>(new MessageResponse("User added successfully"), HttpStatus.OK));
        assertDoesNotThrow(() -> authController.addNewUser(TestUtil.UserTestData.getUser1Request()));
    }

    @Test
    public void testAddNewUser_UserNameEmpty_validation() {
        AddUserRequest addUserRequest = TestUtil.UserTestData.getUser1Request();
        addUserRequest.setUsername("");
        CustomException.ValidationException validationException = assertThrows(CustomException.ValidationException.class, () -> authController.addNewUser(addUserRequest));
        assertEquals("Username cannot be empty", validationException.getMessage());
    }
    @Test
    public void testAddNewUser_InvalidUserName_validation_1() {
        AddUserRequest addUserRequest = TestUtil.UserTestData.getUser1Request();
        addUserRequest.setUsername("test user");
        CustomException.ValidationException validationException = assertThrows(CustomException.ValidationException.class, () -> authController.addNewUser(addUserRequest));
        assertEquals("Username can only contain alphanumeric characters", validationException.getMessage());
    }
    @Test
    public void testAddNewUser_InvalidUserName_validation_2() {
        AddUserRequest addUserRequest = TestUtil.UserTestData.getUser1Request();
        addUserRequest.setUsername("test@user");
        CustomException.ValidationException validationException = assertThrows(CustomException.ValidationException.class, () -> authController.addNewUser(addUserRequest));
        assertEquals("Username can only contain alphanumeric characters", validationException.getMessage());
    }

    @Test
    public void testAddNewUser_EmailEmpty_validation() {
        AddUserRequest addUserRequest = TestUtil.UserTestData.getUser1Request();
        addUserRequest.setEmail("");
        CustomException.ValidationException validationException = assertThrows(CustomException.ValidationException.class, () -> authController.addNewUser(addUserRequest));
        assertEquals("Email cannot be empty", validationException.getMessage());
    }

    @Test
    public void testAddNewUser_InvalidEmail_validation() {
        AddUserRequest addUserRequest = TestUtil.UserTestData.getUser1Request();
        addUserRequest.setEmail("test.com");
        CustomException.ValidationException validationException = assertThrows(CustomException.ValidationException.class, () -> authController.addNewUser(addUserRequest));
        assertEquals("Invalid Email", validationException.getMessage());
    }
    @Test
    public void testAddNewUser_PasswordEmpty_validation() {
        AddUserRequest addUserRequest = TestUtil.UserTestData.getUser1Request();
        addUserRequest.setPassword("");
        CustomException.ValidationException validationException = assertThrows(CustomException.ValidationException.class, () -> authController.addNewUser(addUserRequest));
        assertEquals("Password cannot be empty", validationException.getMessage());
    }
    @Test
    public void testAddNewUser_PasswordLength_validation() {
        AddUserRequest addUserRequest = TestUtil.UserTestData.getUser1Request();
        addUserRequest.setPassword("12347");
        CustomException.ValidationException validationException = assertThrows(CustomException.ValidationException.class, () -> authController.addNewUser(addUserRequest));
        assertEquals("Password should be at least 8 characters long", validationException.getMessage());
    }
    @Test
    public void testLogin_Success_validation() {
        LoginAuthResponse loginAuthResponse = new LoginAuthResponse(TestUtil.TOKEN,"Bearer");
        when(authService.login(any())).thenReturn(new ResponseEntity<>(loginAuthResponse, HttpStatus.OK));
        when(authUserUtils.getUserId(anyString())).thenReturn(TestUtil.USER1_NAME);
        assertDoesNotThrow(() -> authController.login(TestUtil.UserTestData.getLoginAuthRequest()));
    }
    @Test
    public void testLogin_UserNameEmpty_validation() {
        LoginAuthRequest loginAuthRequest = TestUtil.UserTestData.getLoginAuthRequest();
        loginAuthRequest.setUsername("");
        CustomException.ValidationException validationException = assertThrows(CustomException.ValidationException.class, () -> authController.login(loginAuthRequest));
        assertEquals("Username or Email cannot be empty", validationException.getMessage());
    }
    @Test
    public void testLogin_PasswordEmpty_validation() {
        LoginAuthRequest loginAuthRequest = TestUtil.UserTestData.getLoginAuthRequest();
        loginAuthRequest.setPassword("");
        CustomException.ValidationException validationException = assertThrows(CustomException.ValidationException.class, () -> authController.login(loginAuthRequest));
        assertEquals("Password cannot be empty", validationException.getMessage());
    }
    @Test
    public void testRegenerate_Success_validation() {
        LoginAuthResponse loginAuthResponse = new LoginAuthResponse(TestUtil.TOKEN,"Bearer");
        when(authUserUtils.getUserId(anyString())).thenReturn(TestUtil.USER1_NAME);
        when(authService.regenerate(anyString(),anyString())).thenReturn(new ResponseEntity<>(loginAuthResponse, HttpStatus.OK));
        assertDoesNotThrow(() -> authController.regenerate("Bearer "+TestUtil.TOKEN));
    }
    @Test
    public void testCheckUsername_Success_validation() {
        when(authService.checkUsername(anyString())).thenReturn(new ResponseEntity<>(true, HttpStatus.OK));
        assertDoesNotThrow(() -> authController.checkUsername(TestUtil.USER1_NAME));
    }
}