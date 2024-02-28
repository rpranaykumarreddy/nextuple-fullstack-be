package com.nextuple.pranay.fullstack.controller;


import com.nextuple.pranay.fullstack.TestUtil;
import com.nextuple.pranay.fullstack.dto.MessageResponse;
import com.nextuple.pranay.fullstack.service.TransactionService;
import com.nextuple.pranay.fullstack.utils.AuthUserUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransactionControllerTests {

    @Mock
    private TransactionService transactionService;
    @Mock
    private AuthUserUtils authUserUtils;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    public void testCheckWallet_Success_validation() {
        when(transactionService.checkUsername(anyString())).thenReturn(new ResponseEntity<>(true, HttpStatus.OK));
        assertDoesNotThrow(() -> transactionController.checkWallet(TestUtil.USER1_NAME));
    }
    @Test
    public void testInitTransaction_Success_validation() {
        when(authUserUtils.getUserId(anyString())).thenReturn(TestUtil.USER1_NAME);
        when(transactionService.initTransaction(anyString(), any())).thenReturn(new ResponseEntity<>(TestUtil.TransactionTestData.getInitTransactionResponse(), HttpStatus.OK));
        assertDoesNotThrow(() -> transactionController.initTransaction(TestUtil.TOKEN,TestUtil.TransactionTestData.getInitTransactionRequest1()));
    }
    @Test
    public void testConfirmTransaction_Success_validation() {
        when(authUserUtils.getUserId(anyString())).thenReturn(TestUtil.USER1_NAME);
        when(transactionService.confirmTransaction(anyString(), anyString(), anyString())).thenReturn(new ResponseEntity<>(TestUtil.WalletTestData.getWallet1ResponseDto(), HttpStatus.OK));
        assertDoesNotThrow(() -> transactionController.confirmTransaction(TestUtil.TOKEN,TestUtil.TransactionTestData.TRANSACTION_ID,"123456"));
    }
    @Test
    public void testCancelTransaction_Success_validation() {
        when(authUserUtils.getUserId(anyString())).thenReturn(TestUtil.USER1_NAME);
        when(transactionService.cancelTransaction(anyString(), anyString())).thenReturn(
                new ResponseEntity<>(new MessageResponse("Transaction cancelled successfully"), HttpStatus.OK));
        assertDoesNotThrow(() -> transactionController.cancelTransaction(TestUtil.TransactionTestData.TRANSACTION_ID,TestUtil.TOKEN));
    }

}