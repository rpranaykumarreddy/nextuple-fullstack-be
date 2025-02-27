package com.nextuple.pranay.fullstack.services;


import com.nextuple.pranay.fullstack.TestUtil;
import com.nextuple.pranay.fullstack.dto.LoginAuthResponse;
import com.nextuple.pranay.fullstack.dto.MessageResponse;
import com.nextuple.pranay.fullstack.exception.CustomException;
import com.nextuple.pranay.fullstack.model.Users;
import com.nextuple.pranay.fullstack.model.Wallets;
import com.nextuple.pranay.fullstack.repo.UsersRepo;
import com.nextuple.pranay.fullstack.repo.WalletsRepo;
import com.nextuple.pranay.fullstack.security.JWTTokenProvider;
import com.nextuple.pranay.fullstack.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthServiceTests {
    @Mock
    private UsersRepo usersRepo;
    @Mock
    private WalletsRepo walletsRepo;
    @Spy
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JWTTokenProvider jwtTokenProvider;
    @InjectMocks
    private AuthService authService;

    @Test
    public void testAddUser_Success() {
        when(usersRepo.existsByUsernameIgnoreCase(anyString())).thenReturn(false);
        when(usersRepo.existsByEmailIgnoreCase(anyString())).thenReturn(false);
        when(usersRepo.save(any(Users.class))).thenReturn(TestUtil.UserTestData.getUser1Response());
        when(walletsRepo.save(any(Wallets.class))).thenReturn(TestUtil.WalletTestData.getWallet1Response());
        ResponseEntity<MessageResponse> response = authService.addUser(TestUtil.UserTestData.getUser1Request());
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(Objects.requireNonNull(response.getBody()).getMessage());
    }
    @Test
    public void testAddUser_UsernameExists() {
        when(usersRepo.existsByUsernameIgnoreCase(anyString())).thenReturn(true);
        assertThrows(CustomException.EntityExistsException.class, () ->
            authService.addUser(TestUtil.UserTestData.getUser1Request()), "Username already exists. Please try another username");
    }
    @Test
    public void testAddUser_EmailExists() {
        when(usersRepo.existsByUsernameIgnoreCase(anyString())).thenReturn(false);
        when(usersRepo.existsByEmailIgnoreCase(anyString())).thenReturn(true);
        assertThrows(CustomException.EntityExistsException.class, () ->
            authService.addUser(TestUtil.UserTestData.getUser1Request()), "Email Exists. Please try login with your email or try another email.");
    }
    @Test
    public void testAddUser_SaveNotSuccessful() {
        when(usersRepo.existsByUsernameIgnoreCase(anyString())).thenReturn(false);
        when(usersRepo.existsByEmailIgnoreCase(anyString())).thenReturn(false);
        when(usersRepo.save(any(Users.class))).thenReturn(TestUtil.UserTestData.getUser1Response());
        doThrow(new RuntimeException()).when(walletsRepo).save(any(Wallets.class));
        assertThrows(CustomException.UnableToSaveException.class, () ->
            authService.addUser(TestUtil.UserTestData.getUser1Request()), "Unable to save user");
    }
    @Test
    public void testLogin_Success() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtTokenProvider.generateToken(any())).thenReturn("token");
        ResponseEntity<LoginAuthResponse> response = authService.login(TestUtil.UserTestData.getLoginAuthRequest());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("token",response.getBody().getAccessToken());
    }
    @Test
    public void testRegenerate_Success() {
        when(jwtTokenProvider.regenerateToken(any())).thenReturn("token");
        ResponseEntity<LoginAuthResponse> response = authService.regenerate(TestUtil.TOKEN,TestUtil.USER1_NAME);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("token",response.getBody().getAccessToken());
    }
    @Test
    public void testCheckUsername_Success() {
        when(usersRepo.existsByUsernameIgnoreCase(anyString())).thenReturn(true);
        ResponseEntity<Boolean> response = authService.checkUsername("username");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody());
    }
}