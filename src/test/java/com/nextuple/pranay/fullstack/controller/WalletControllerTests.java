package com.nextuple.pranay.fullstack.controller;


import com.nextuple.pranay.fullstack.TestUtil;
import com.nextuple.pranay.fullstack.service.RechargeService;
import com.nextuple.pranay.fullstack.service.WalletService;
import com.nextuple.pranay.fullstack.utils.AuthUserUtils;
import dev.samstevens.totp.exceptions.QrGenerationException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WalletControllerTests {

    @Mock
    private WalletService walletService;
    @Mock
    private AuthUserUtils authUserUtils;

    @InjectMocks
    private WalletController walletController;

    @Test
    public void testGetWalletDetails_Success_validation() {
        when(authUserUtils.getUserId(anyString())).thenReturn(TestUtil.USER1_NAME);
        when(walletService.getWalletDetails(anyString())).thenReturn(new ResponseEntity<>(TestUtil.WalletTestData.getWallet1ResponseDto(), HttpStatus.OK));
        assertDoesNotThrow(() -> walletController.getWalletDetails(TestUtil.TOKEN));
    }
    @Test
    public void testCreateTotp_Success_validation() throws QrGenerationException {
        when(authUserUtils.getUserId(anyString())).thenReturn(TestUtil.USER1_NAME);
        when(walletService.createTotp(anyString())).thenReturn(new ResponseEntity<>("Totp created successfully", HttpStatus.OK));
        assertDoesNotThrow(() -> walletController.createTotp(TestUtil.TOKEN));
    }
    @Test
    public void testConfirmTotp_Success_validation() {
        when(authUserUtils.getUserId(anyString())).thenReturn(TestUtil.USER1_NAME);
        when(walletService.confirmTotp(anyString(), anyString())).thenReturn(new ResponseEntity<>(true, HttpStatus.OK));
        assertDoesNotThrow(() -> walletController.confirmTotp(TestUtil.TOKEN, TestUtil.WalletTestData.TOTP_CODE));
    }
    @Test
    public void testGetStatement_Success_validation() {
        when(authUserUtils.getUserId(anyString())).thenReturn(TestUtil.USER1_NAME);
        when(walletService.getStatement(anyString())).thenReturn(new ResponseEntity<>(TestUtil.StatementTestData.needStatementResponse(), HttpStatus.OK));
        assertDoesNotThrow(() -> walletController.getStatement(TestUtil.TOKEN));
    }
//    @Test
//    public void testAddNewUser_Success_validation() {
//        when(authService.addUser(any())).thenReturn(new ResponseEntity<>("User added successfully", HttpStatus.OK));
//        assertDoesNotThrow(() -> authController.addNewUser(TestUtil.UserTestData.getUser1Request()));
//    }
//
//    @Test
//    public void testAddNewUser_UserNameEmpty_validation() {
//        AddUserRequest addUserRequest = TestUtil.UserTestData.getUser1Request();
//        addUserRequest.setUsername("");
//        CustomException.ValidationException validationException = assertThrows(CustomException.ValidationException.class, () -> authController.addNewUser(addUserRequest));
//        assertEquals("Username cannot be empty", validationException.getMessage());
//    }
//
//    @Test
//    public void testAddNewUser_EmailEmpty_validation() {
//        AddUserRequest addUserRequest = TestUtil.UserTestData.getUser1Request();
//        addUserRequest.setEmail("");
//        CustomException.ValidationException validationException = assertThrows(CustomException.ValidationException.class, () -> authController.addNewUser(addUserRequest));
//        assertEquals("Email cannot be empty", validationException.getMessage());
//    }
//
//    @Test
//    public void testAddNewUser_InvalidEmail_validation() {
//        AddUserRequest addUserRequest = TestUtil.UserTestData.getUser1Request();
//        addUserRequest.setEmail("test.com");
//        CustomException.ValidationException validationException = assertThrows(CustomException.ValidationException.class, () -> authController.addNewUser(addUserRequest));
//        assertEquals("Invalid Email", validationException.getMessage());
//    }
//    @Test
//    public void testAddNewUser_PasswordEmpty_validation() {
//        AddUserRequest addUserRequest = TestUtil.UserTestData.getUser1Request();
//        addUserRequest.setPassword("");
//        CustomException.ValidationException validationException = assertThrows(CustomException.ValidationException.class, () -> authController.addNewUser(addUserRequest));
//        assertEquals("Password cannot be empty", validationException.getMessage());
//    }
//    @Test
//    public void testAddNewUser_PasswordLength_validation() {
//        AddUserRequest addUserRequest = TestUtil.UserTestData.getUser1Request();
//        addUserRequest.setPassword("12347");
//        CustomException.ValidationException validationException = assertThrows(CustomException.ValidationException.class, () -> authController.addNewUser(addUserRequest));
//        assertEquals("Password should be at least 8 characters long", validationException.getMessage());
//    }
//    @Test
//    public void testLogin_Success_validation() {
//        LoginAuthResponse loginAuthResponse = new LoginAuthResponse(TestUtil.TOKEN,"Bearer");
//        when(authService.login(any())).thenReturn(new ResponseEntity<>(loginAuthResponse, HttpStatus.OK));
//        assertDoesNotThrow(() -> authController.login(TestUtil.UserTestData.getLoginAuthRequest()));
//    }
//    @Test
//public void testLogin_UserNameEmpty_validation() {
//        LoginAuthRequest loginAuthRequest = TestUtil.UserTestData.getLoginAuthRequest();
//        loginAuthRequest.setUsername("");
//        CustomException.ValidationException validationException = assertThrows(CustomException.ValidationException.class, () -> authController.login(loginAuthRequest));
//        assertEquals("Username cannot be empty", validationException.getMessage());
//    }
//    @Test
//    public void testLogin_PasswordEmpty_validation() {
//        LoginAuthRequest loginAuthRequest = TestUtil.UserTestData.getLoginAuthRequest();
//        loginAuthRequest.setPassword("");
//        CustomException.ValidationException validationException = assertThrows(CustomException.ValidationException.class, () -> authController.login(loginAuthRequest));
//        assertEquals("Password cannot be empty", validationException.getMessage());
//    }
//    @Test
//    public void testCheckUsername_Success_validation() {
//        when(authService.checkUsername(anyString())).thenReturn(new ResponseEntity<>(true, HttpStatus.OK));
//        assertDoesNotThrow(() -> authController.checkUsername(TestUtil.USER1_NAME));
//    }
}