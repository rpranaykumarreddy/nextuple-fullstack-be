package com.nextuple.pranay.fullstack.services;

import com.nextuple.pranay.fullstack.TestUtil;
import com.nextuple.pranay.fullstack.dto.GetWalletDetailsResponse;
import com.nextuple.pranay.fullstack.dto.InitTransactionResponse;
import com.nextuple.pranay.fullstack.exception.CustomException;
import com.nextuple.pranay.fullstack.repo.TransactionsRepo;
import com.nextuple.pranay.fullstack.repo.WalletsRepo;
import com.nextuple.pranay.fullstack.service.TransactionService;
import dev.samstevens.totp.code.CodeVerifier;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionsServiceTests {
    @Mock
    private WalletsRepo walletsRepo;
    @Mock
    private TransactionsRepo transactionsRepo;
    @Mock
    private CodeVerifier verifier;
    @InjectMocks
    private TransactionService transactionService;
    @Test
    public void testCheckUsername_Success() {
        when(walletsRepo.existsByUsername(TestUtil.USER1_NAME)).thenReturn(true);
        ResponseEntity<Boolean> responseEntity = transactionService.checkUsername(TestUtil.USER1_NAME);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(true, responseEntity.getBody());
        verify(walletsRepo, times(1)).existsByUsername(TestUtil.USER1_NAME);
    }
    @Test
    public void testInitTransaction_Success() {
        when(walletsRepo.findById(any())).thenReturn(Optional.of(TestUtil.WalletTestData.getRechargedWallet1WithTotp()));
        when(walletsRepo.findById(TestUtil.USER2_NAME)).thenReturn(Optional.of(TestUtil.WalletTestData.getWallet1Response()));
        ResponseEntity<InitTransactionResponse> responseEntity = transactionService.initTransaction(TestUtil.USER1_NAME, TestUtil.TransactionTestData.getInitTransactionRequest1());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(TestUtil.TransactionTestData.getTransaction1_InitStatus().getFromUId(), Objects.requireNonNull(responseEntity.getBody()).getFrom());
        assertEquals(TestUtil.TransactionTestData.getTransaction1_InitStatus().getToUId(), responseEntity.getBody().getTo());
        assertEquals(TestUtil.TransactionTestData.getTransaction1_InitStatus().getAmount(), responseEntity.getBody().getAmount());
        assertEquals(LocalDateTime.class, responseEntity.getBody().getCreated().getClass());
    }
    @Test
    public void testInitTransaction_InvalidAmountException() {
        when(walletsRepo.findById(any())).thenReturn(Optional.of(TestUtil.WalletTestData.getRechargedWallet1WithTotp()));
        assertThrows(CustomException.BadRequestException.class, () -> transactionService.initTransaction(TestUtil.USER1_NAME, TestUtil.TransactionTestData.getInitTransactionRequest_InvalidAmount()));
    }
    @Test
    public void testInitTransaction_InvalidUsernameException() {
        when(walletsRepo.findById(any())).thenReturn(Optional.of(TestUtil.WalletTestData.getRechargedWallet1WithTotp()));
        assertThrows(CustomException.BadRequestException.class, () -> transactionService.initTransaction(TestUtil.USER1_NAME, TestUtil.TransactionTestData.getInitTransactionRequest_InvalidUsername()));
    }
    @Test
    public void testInitTransaction_EntityNotFoundException_From() {
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(Optional.empty());
        assertThrows(CustomException.EntityNotFoundException.class, () -> transactionService.initTransaction(TestUtil.USER1_NAME, TestUtil.TransactionTestData.getInitTransactionRequest1()));
    }
    @Test
    public void testInitTransaction_InsufficientBalanceException() {
        when(walletsRepo.findById(any())).thenReturn(Optional.of(TestUtil.WalletTestData.getWallet1Response()));
        assertThrows(CustomException.BadRequestException.class, () -> transactionService.initTransaction(TestUtil.USER1_NAME, TestUtil.TransactionTestData.getInitTransactionRequest1()));
    }
    @Test
    public void testInitTransaction_EntityNotFoundException_To() {
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(Optional.of(TestUtil.WalletTestData.getRechargedWallet1WithTotp()));
        when(walletsRepo.findById(TestUtil.USER2_NAME)).thenReturn(Optional.empty());
        assertThrows(CustomException.EntityNotFoundException.class, () -> transactionService.initTransaction(TestUtil.USER1_NAME, TestUtil.TransactionTestData.getInitTransactionRequest1()));
    }
    @Test
    public void testInitTransaction_UnableToSaveException() {
        when(walletsRepo.findById(any())).thenReturn(Optional.of(TestUtil.WalletTestData.getRechargedWallet1WithTotp()));
        when(walletsRepo.findById(TestUtil.USER2_NAME)).thenReturn(Optional.of(TestUtil.WalletTestData.getWallet1Response()));
        when(transactionsRepo.save(any())).thenThrow(new RuntimeException("A database error"));
        assertThrows(CustomException.UnableToSaveException.class, () -> transactionService.initTransaction(TestUtil.USER1_NAME, TestUtil.TransactionTestData.getInitTransactionRequest1()));
    }
    //Todo: Add more tests for confirmTransaction
    @Test
    public void testConfirmTransaction_Success() {
        when(transactionsRepo.findById(TestUtil.TransactionTestData.TRANSACTION_ID)).thenReturn(Optional.of(TestUtil.TransactionTestData.getTransaction1_InitStatus_TimeNow()));
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(Optional.of(TestUtil.WalletTestData.getRechargedWallet1WithTotp()));
        when(walletsRepo.findById(TestUtil.USER2_NAME)).thenReturn(Optional.of(TestUtil.WalletTestData.getRechargedWallet2()));
        when(verifier.isValidCode(any(), any())).thenReturn(true);
        ResponseEntity<GetWalletDetailsResponse> responseEntity = transactionService.confirmTransaction(TestUtil.USER1_NAME, TestUtil.TransactionTestData.getTransaction1().getId(),"123456");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @Test
    public void testConfirmTransaction_TransactionNotFoundException(){
        when(transactionsRepo.findById(TestUtil.TransactionTestData.TRANSACTION_ID)).thenReturn(Optional.empty());
        assertThrows(CustomException.EntityNotFoundException.class, () -> transactionService.confirmTransaction(TestUtil.USER1_NAME, TestUtil.TransactionTestData.TRANSACTION_ID,"123456"));
    }
    @Test
    public void testConfirmTransaction_UnauthorizedException(){
        when(transactionsRepo.findById(TestUtil.TransactionTestData.TRANSACTION_ID)).thenReturn(Optional.of(TestUtil.TransactionTestData.getTransaction1_InitStatus_TimeNow()));
        assertThrows(CustomException.UnauthorizedException.class, () -> transactionService.confirmTransaction(TestUtil.USER2_NAME, TestUtil.TransactionTestData.TRANSACTION_ID,"123456"));
    }
    @Test
    public void testConfirmTransaction_InvalidTransactionStatusException(){
        when(transactionsRepo.findById(TestUtil.TransactionTestData.TRANSACTION_ID)).thenReturn(Optional.of(TestUtil.TransactionTestData.getTransaction1()));
        assertThrows(CustomException.BadRequestException.class, () -> transactionService.confirmTransaction(TestUtil.USER1_NAME, TestUtil.TransactionTestData.TRANSACTION_ID,"123456"));
    }
    @Test
    public void testConfirmTransaction_TransactionTimeoutException(){
        when(transactionsRepo.findById(TestUtil.TransactionTestData.TRANSACTION_ID)).thenReturn(Optional.of(TestUtil.TransactionTestData.getTransaction1_InitStatus()));
        assertThrows(CustomException.BadRequestException.class, () -> transactionService.confirmTransaction(TestUtil.USER1_NAME, TestUtil.TransactionTestData.TRANSACTION_ID,"123456"));
    }
    @Test
    public void testConfirmTransaction_Timeout_UnableToSaveException(){when(transactionsRepo.findById(TestUtil.TransactionTestData.TRANSACTION_ID)).thenReturn(Optional.of(TestUtil.TransactionTestData.getTransaction1_InitStatus()));
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(Optional.of(TestUtil.WalletTestData.getRechargedWallet1WithTotp()));
        when(transactionsRepo.save(any())).thenThrow(new RuntimeException("A database error"));
        assertThrows(CustomException.UnableToSaveException.class, () -> transactionService.confirmTransaction(TestUtil.USER1_NAME, TestUtil.TransactionTestData.TRANSACTION_ID,"123456"));
    }
    @Test
    public void testConfirmTransaction_FromWalletNotFoundException(){
        when(transactionsRepo.findById(TestUtil.TransactionTestData.TRANSACTION_ID)).thenReturn(Optional.of(TestUtil.TransactionTestData.getTransaction1_InitStatus_TimeNow()));
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(Optional.empty());
        assertThrows(CustomException.EntityNotFoundException.class, () -> transactionService.confirmTransaction(TestUtil.USER1_NAME, TestUtil.TransactionTestData.TRANSACTION_ID,"123456"));
    }
    @Test
    public void testConfirmTransaction_ToWalletNotFoundException(){
        when(transactionsRepo.findById(TestUtil.TransactionTestData.TRANSACTION_ID)).thenReturn(Optional.of(TestUtil.TransactionTestData.getTransaction1_InitStatus_TimeNow()));
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(Optional.of(TestUtil.WalletTestData.getRechargedWallet1WithTotp()));
        when(walletsRepo.findById(TestUtil.USER2_NAME)).thenReturn(Optional.empty());
        assertThrows(CustomException.EntityNotFoundException.class, () -> transactionService.confirmTransaction(TestUtil.USER1_NAME, TestUtil.TransactionTestData.TRANSACTION_ID,"123456"));
    }
    @Test
    public void testConfirmTransaction_InsufficientBalanceException(){
        when(transactionsRepo.findById(TestUtil.TransactionTestData.TRANSACTION_ID)).thenReturn(Optional.of(TestUtil.TransactionTestData.getTransaction1_InitStatus_TimeNow()));
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(Optional.of(TestUtil.WalletTestData.getWallet1Response()));
        when(walletsRepo.findById(TestUtil.USER2_NAME)).thenReturn(Optional.of(TestUtil.WalletTestData.getRechargedWallet2()));
        assertThrows(CustomException.BadRequestException.class, () -> transactionService.confirmTransaction(TestUtil.USER1_NAME, TestUtil.TransactionTestData.TRANSACTION_ID,"123456"));
    }
    @Test
    public void testConfirmTransaction_EmptyCodeException(){
        when(transactionsRepo.findById(TestUtil.TransactionTestData.TRANSACTION_ID)).thenReturn(Optional.of(TestUtil.TransactionTestData.getTransaction1_InitStatus_TimeNow()));
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(Optional.of(TestUtil.WalletTestData.getRechargedWallet1WithTotp()));
        when(walletsRepo.findById(TestUtil.USER2_NAME)).thenReturn(Optional.of(TestUtil.WalletTestData.getRechargedWallet2()));
        assertThrows(CustomException.BadRequestException.class, () -> transactionService.confirmTransaction(TestUtil.USER1_NAME, TestUtil.TransactionTestData.TRANSACTION_ID,""));
    }
    @Test
    public void testConfirmTransaction_InvalidCodeException(){
        when(transactionsRepo.findById(TestUtil.TransactionTestData.TRANSACTION_ID)).thenReturn(Optional.of(TestUtil.TransactionTestData.getTransaction1_InitStatus_TimeNow()));
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(Optional.of(TestUtil.WalletTestData.getRechargedWallet1WithTotp()));
        when(walletsRepo.findById(TestUtil.USER2_NAME)).thenReturn(Optional.of(TestUtil.WalletTestData.getRechargedWallet2()));
        when(verifier.isValidCode(any(), any())).thenReturn(false);
        assertThrows(CustomException.BadRequestException.class, () -> transactionService.confirmTransaction(TestUtil.USER1_NAME, TestUtil.TransactionTestData.TRANSACTION_ID,"123456"));
    }
    @Test
    public void testConfirmTransaction_UnableToSaveException(){
        when(transactionsRepo.findById(TestUtil.TransactionTestData.TRANSACTION_ID)).thenReturn(Optional.of(TestUtil.TransactionTestData.getTransaction1_InitStatus_TimeNow()));
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(Optional.of(TestUtil.WalletTestData.getRechargedWallet1WithTotp()));
        when(walletsRepo.findById(TestUtil.USER2_NAME)).thenReturn(Optional.of(TestUtil.WalletTestData.getRechargedWallet2()));
        when(verifier.isValidCode(any(), any())).thenReturn(true);
        when(walletsRepo.save(any())).thenThrow(new RuntimeException("A database error"));
        assertThrows(CustomException.UnableToSaveException.class, () -> transactionService.confirmTransaction(TestUtil.USER1_NAME, TestUtil.TransactionTestData.TRANSACTION_ID,"123456"));
    }
    @Test
    public void testCancelTransaction_Success() {
        when(walletsRepo.findById(any())).thenReturn(Optional.of(TestUtil.WalletTestData.getRechargedWallet1WithTotp()));
        when(transactionsRepo.findById(TestUtil.TransactionTestData.TRANSACTION_ID)).thenReturn(Optional.of(TestUtil.TransactionTestData.getTransaction1_InitStatus()));
        ResponseEntity<String> responseEntity = transactionService.cancelTransaction(TestUtil.USER1_NAME, TestUtil.TransactionTestData.TRANSACTION_ID);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Transaction cancelled", responseEntity.getBody());
    }
    @Test
    public void testCancelTransaction_EntityNotFoundException() {
        when(walletsRepo.findById(any())).thenReturn(Optional.of(TestUtil.WalletTestData.getRechargedWallet1WithTotp()));
        when(transactionsRepo.findById(TestUtil.TransactionTestData.TRANSACTION_ID)).thenReturn(Optional.empty());
        assertThrows(CustomException.EntityNotFoundException.class, () -> transactionService.cancelTransaction(TestUtil.USER1_NAME, TestUtil.TransactionTestData.TRANSACTION_ID));
    }
    @Test
    public void testCancelTransaction_UnauthorizedException() {
        when(walletsRepo.findById(any())).thenReturn(Optional.of(TestUtil.WalletTestData.getRechargedWallet1WithTotp()));
        when(transactionsRepo.findById(TestUtil.TransactionTestData.TRANSACTION_ID)).thenReturn(Optional.of(TestUtil.TransactionTestData.getTransaction1_InitStatus()));
        assertThrows(CustomException.UnauthorizedException.class, () -> transactionService.cancelTransaction(TestUtil.USER2_NAME, TestUtil.TransactionTestData.TRANSACTION_ID));
    }
    @Test
    public void testCancelTransaction_BadRequestException() {
        when(walletsRepo.findById(any())).thenReturn(Optional.of(TestUtil.WalletTestData.getRechargedWallet1WithTotp()));
        when(transactionsRepo.findById(TestUtil.TransactionTestData.TRANSACTION_ID)).thenReturn(Optional.of(TestUtil.TransactionTestData.getTransaction1()));
        assertThrows(CustomException.BadRequestException.class, () -> transactionService.cancelTransaction(TestUtil.USER1_NAME, TestUtil.TransactionTestData.TRANSACTION_ID));
    }
}