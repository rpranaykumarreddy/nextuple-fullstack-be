package com.nextuple.pranay.fullstack.utils;

import com.nextuple.pranay.fullstack.TestUtil;
import com.nextuple.pranay.fullstack.exception.CustomException;
import com.nextuple.pranay.fullstack.repo.UsersRepo;
import com.nextuple.pranay.fullstack.security.JWTTokenProvider;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthUserUtilsTests {
    @Mock
    private JWTTokenProvider jwtTokenProvider;
    @Mock
    private UsersRepo usersRepo;
    @InjectMocks
    private AuthUserUtils authUserUtils;
    @Test
    public void testGetUserId_Success() {
        when(jwtTokenProvider.validateToken(any())).thenReturn(true);
        when(jwtTokenProvider.getUsername(any())).thenReturn(TestUtil.USER1_NAME);
        when(usersRepo.findByUsernameOrEmail(TestUtil.USER1_NAME, TestUtil.USER1_NAME))
                .thenReturn(Optional.of(TestUtil.UserTestData.getUser1Response()));
        String userId = authUserUtils.getUserId(TestUtil.TOKEN);
        assertEquals(TestUtil.USER1_NAME, userId);
    }
    @Test
    public void testGetUserId_UserNotFoundException() {
        when(jwtTokenProvider.validateToken(any())).thenReturn(true);
        when(jwtTokenProvider.getUsername(any())).thenReturn(TestUtil.USER1_NAME);
        when(usersRepo.findByUsernameOrEmail(TestUtil.USER1_NAME, TestUtil.USER1_NAME))
                .thenReturn(Optional.empty());
        assertThrows(CustomException.EntityNotFoundException.class, () -> authUserUtils.getUserId(TestUtil.TOKEN));
    }
    @Test
    public void testGetUserId_UnauthorizedException() {
        when(jwtTokenProvider.validateToken(any())).thenReturn(false);
        assertThrows(CustomException.UnauthorizedException.class, () -> authUserUtils.getUserId(TestUtil.TOKEN));
    }
    @Test
    public void testGetUserId_UnauthorizedException2() {
        when(jwtTokenProvider.validateToken(any())).thenReturn(true);
        when(jwtTokenProvider.getUsername(any())).thenReturn(TestUtil.USER1_NAME);
        when(usersRepo.findByUsernameOrEmail(TestUtil.USER1_NAME, TestUtil.USER1_NAME))
                .thenReturn(Optional.of(TestUtil.UserTestData.getUser1Response()));
        assertThrows(CustomException.UnauthorizedException.class, () -> authUserUtils.getUserId("Invalid token"));
    }
    @Test
    public void testGetUserId_UnauthorizedException3() {
        when(jwtTokenProvider.validateToken(any())).thenReturn(true);
        when(jwtTokenProvider.getUsername(any())).thenReturn(TestUtil.USER1_NAME);
        when(usersRepo.findByUsernameOrEmail(TestUtil.USER1_NAME, TestUtil.USER1_NAME))
                .thenReturn(Optional.of(TestUtil.UserTestData.getUser1Response()));
        assertThrows(CustomException.UnauthorizedException.class, () -> authUserUtils.getUserId("Bear"));
    }


}