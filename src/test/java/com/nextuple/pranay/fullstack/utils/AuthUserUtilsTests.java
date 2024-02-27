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
        assertThrows(CustomException.EntityNotFoundException.class, () -> {
            authUserUtils.getUserId(TestUtil.TOKEN);
        });
    }
    @Test
    public void testGetUserId_UnauthorizedException() {
        when(jwtTokenProvider.validateToken(any())).thenReturn(false);
        assertThrows(CustomException.UnauthorizedException.class, () -> {
            authUserUtils.getUserId(TestUtil.TOKEN);
        });
    }
    @Test
    public void testGetUserId_UnauthorizedException2() {
        when(jwtTokenProvider.validateToken(any())).thenReturn(true);
        when(jwtTokenProvider.getUsername(any())).thenReturn(TestUtil.USER1_NAME);
        when(usersRepo.findByUsernameOrEmail(TestUtil.USER1_NAME, TestUtil.USER1_NAME))
                .thenReturn(Optional.of(TestUtil.UserTestData.getUser1Response()));
        assertThrows(CustomException.UnauthorizedException.class, () -> {
            authUserUtils.getUserId("Invalid token");
        });
    }
    @Test
    public void testGetUserId_UnauthorizedException3() {
        when(jwtTokenProvider.validateToken(any())).thenReturn(true);
        when(jwtTokenProvider.getUsername(any())).thenReturn(TestUtil.USER1_NAME);
        when(usersRepo.findByUsernameOrEmail(TestUtil.USER1_NAME, TestUtil.USER1_NAME))
                .thenReturn(Optional.of(TestUtil.UserTestData.getUser1Response()));
        assertThrows(CustomException.UnauthorizedException.class, () -> {
            authUserUtils.getUserId("Bear");
        });
    }


//    @Test
//    public void testRechargeWallet_Success() {
//        when(walletsRepo.findById(TestUtil.USER1_NAME))
//                .thenReturn(Optional.of(TestUtil.WalletTestData.getWallet1Response()));
//        ResponseEntity<GetWalletDetailsResponse> responseEntity = rechargeService.rechargeWallet(TestUtil.USER1_NAME, 1000.0);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        GetWalletDetailsResponse getWalletDetailsResponse = (GetWalletDetailsResponse) responseEntity.getBody();
//        assert(getWalletDetailsResponse.getBalance() > 1000.0);
//        verify(walletsRepo, times(1)).findById(TestUtil.USER1_NAME);
//    }
//    @Test
//    public void testRechargeWallet_EntityNotFoundException() {
//        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(Optional.empty());
//        assertThrows(CustomException.EntityNotFoundException.class, () -> {
//            rechargeService.rechargeWallet(TestUtil.USER1_NAME, 1000.0);
//        });
//    }
//    @Test
//    public void testRechargeWallet_SaveNotSuccessfulException() {
//        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(Optional.of(TestUtil.WalletTestData.getWallet1Response()));
//        when(rechargesRepo.save(any())).thenThrow(new RuntimeException("A database error"));
//        assertThrows(CustomException.UnableToSaveException.class, () -> {
//            rechargeService.rechargeWallet(TestUtil.USER1_NAME, 1000.0);
//        });
//    }
}