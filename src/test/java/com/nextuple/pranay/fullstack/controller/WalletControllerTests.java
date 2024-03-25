package com.nextuple.pranay.fullstack.controller;


import com.nextuple.pranay.fullstack.TestUtil;
import com.nextuple.pranay.fullstack.dto.MessageResponse;
import com.nextuple.pranay.fullstack.exception.CustomException;
import com.nextuple.pranay.fullstack.service.WalletService;
import com.nextuple.pranay.fullstack.utils.AuthUserUtils;
import dev.samstevens.totp.exceptions.QrGenerationException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
    public void testDisableTotp_Success_validation() {
        when(authUserUtils.getUserId(anyString())).thenReturn(TestUtil.USER1_NAME);
        when(walletService.disableTotp(anyString())).thenReturn(new ResponseEntity<>(new MessageResponse("Totp disabled successfully"), HttpStatus.OK));
        assertDoesNotThrow(() -> walletController.disableTotp(TestUtil.TOKEN));
    }
    @Test
    public void testGetStatement_Success_validation() {
        when(authUserUtils.getUserId(anyString())).thenReturn(TestUtil.USER1_NAME);
        when(walletService.getStatement(anyString(), anyInt())).thenReturn(new ResponseEntity<>(TestUtil.StatementTestData.needStatementResponse(), HttpStatus.OK));
        assertDoesNotThrow(() -> walletController.getStatement(TestUtil.TOKEN,0));
    }
    @Test
    public void testGetStatement_Failure_validation() {
        when(authUserUtils.getUserId(anyString())).thenReturn(TestUtil.USER1_NAME);
        when(walletService.getStatement(anyString(),  anyInt())).thenReturn(new ResponseEntity<>(TestUtil.StatementTestData.needStatementResponse(), HttpStatus.OK));
        CustomException.ValidationException validationException = assertThrows(CustomException.ValidationException.class, () -> walletController.getStatement(TestUtil.TOKEN,-1));
        assertEquals("Invalid page request", validationException.getMessage());
    }
    @Test
    public void testGetCashback_Success_validation() {
        when(authUserUtils.getUserId(anyString())).thenReturn(TestUtil.USER1_NAME);
        when(walletService.getCashback(anyString(), any())).thenReturn(new ResponseEntity<>(TestUtil.CashbackTestData.needCashbackResponse(), HttpStatus.OK));
        assertDoesNotThrow(() -> walletController.getCashback(TestUtil.TOKEN,1));
    }
    @Test
    public void testGetCashback_Failure_validation() {
        when(authUserUtils.getUserId(anyString())).thenReturn(TestUtil.USER1_NAME);
        when(walletService.getCashback(anyString(), any())).thenReturn(new ResponseEntity<>(TestUtil.CashbackTestData.needCashbackResponse(), HttpStatus.OK));
        CustomException.ValidationException validationException = assertThrows(CustomException.ValidationException.class, () -> walletController.getCashback(TestUtil.TOKEN,-1));
        assertEquals("Invalid page request", validationException.getMessage());
    }
}