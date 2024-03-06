package com.nextuple.pranay.fullstack.controller;


import com.nextuple.pranay.fullstack.TestUtil;
import com.nextuple.pranay.fullstack.dto.MessageResponse;
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
import static org.mockito.ArgumentMatchers.anyInt;
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
        when(walletService.createTotp(anyString())).thenReturn(new ResponseEntity<>(new MessageResponse("Totp created successfully"), HttpStatus.OK));
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
        when(walletService.getStatement(anyString(), anyInt(), anyInt())).thenReturn(new ResponseEntity<>(TestUtil.StatementTestData.needStatementResponse(), HttpStatus.OK));
        assertDoesNotThrow(() -> walletController.getStatement(TestUtil.TOKEN,1,2024));
    }
}