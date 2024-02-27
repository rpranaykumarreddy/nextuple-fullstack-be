package com.nextuple.pranay.fullstack.services;

import com.nextuple.pranay.fullstack.TestUtil;
import com.nextuple.pranay.fullstack.dto.GetStatementResponse;
import com.nextuple.pranay.fullstack.dto.GetWalletDetailsResponse;
import com.nextuple.pranay.fullstack.exception.CustomException;
import com.nextuple.pranay.fullstack.repo.RechargesRepo;
import com.nextuple.pranay.fullstack.repo.TransactionsRepo;
import com.nextuple.pranay.fullstack.repo.WalletsRepo;
import com.nextuple.pranay.fullstack.service.RechargeService;
import com.nextuple.pranay.fullstack.service.WalletService;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RechargesServiceTests {
    @Mock
    private WalletsRepo walletsRepo;
    @Mock
    private RechargesRepo rechargesRepo;
    @InjectMocks
    private RechargeService rechargeService;
    @Test
    public void testRechargeWallet_Success() {
        when(walletsRepo.findById(TestUtil.USER1_NAME))
                .thenReturn(Optional.of(TestUtil.WalletTestData.getWallet1Response()));
        ResponseEntity<GetWalletDetailsResponse> responseEntity = rechargeService.rechargeWallet(TestUtil.USER1_NAME, 1000.0);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        GetWalletDetailsResponse getWalletDetailsResponse = responseEntity.getBody();
        assert(getWalletDetailsResponse.getBalance() > 1000.0);
        verify(walletsRepo, times(1)).findById(TestUtil.USER1_NAME);
    }
    @Test
    public void testRechargeWallet_EntityNotFoundException() {
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(Optional.empty());
        assertThrows(CustomException.EntityNotFoundException.class, () -> {
            rechargeService.rechargeWallet(TestUtil.USER1_NAME, 1000.0);
        });
    }
    @Test
    public void testRechargeWallet_SaveNotSuccessfulException() {
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(Optional.of(TestUtil.WalletTestData.getWallet1Response()));
        when(rechargesRepo.save(any())).thenThrow(new RuntimeException("A database error"));
        assertThrows(CustomException.UnableToSaveException.class, () -> {
            rechargeService.rechargeWallet(TestUtil.USER1_NAME, 1000.0);
        });
    }
}