package com.nextuple.pranay.fullstack.services;

import com.nextuple.pranay.fullstack.dto.GetCashbackResponse;
import com.nextuple.pranay.fullstack.dto.GetStatementResponse;
import com.nextuple.pranay.fullstack.dto.GetWalletDetailsResponse;
import com.nextuple.pranay.fullstack.exception.CustomException;
import com.nextuple.pranay.fullstack.repo.RechargesRepo;
import com.nextuple.pranay.fullstack.repo.TransactionsRepo;
import com.nextuple.pranay.fullstack.repo.WalletsRepo;
import com.nextuple.pranay.fullstack.service.WalletService;
import com.nextuple.pranay.fullstack.TestUtil;
import com.nextuple.pranay.fullstack.utils.Globals;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WalletServiceTests {
    @Mock
    private WalletsRepo walletsRepo;
    @Mock
    private TransactionsRepo transactionsRepo;
    @Mock
    private RechargesRepo rechargesRepo;
    @Mock
    private CodeVerifier verifier;

    @InjectMocks
    private WalletService walletService;
    @Test
    public void testGetWalletDetails_Success() {
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(java.util.Optional.of(TestUtil.WalletTestData.getWallet1Response()));
        ResponseEntity<GetWalletDetailsResponse> responseEntity = walletService.getWalletDetails(TestUtil.USER1_NAME);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        GetWalletDetailsResponse getWalletDetailsResponse = responseEntity.getBody();
        assert getWalletDetailsResponse != null;
        assertEquals(TestUtil.WalletTestData.USER1_BALANCE_INIT, getWalletDetailsResponse.getBalance());
        assertFalse(getWalletDetailsResponse.isTotpEnabled());
        assertEquals(LocalDateTime.class, getWalletDetailsResponse.getResponseTime().getClass());
        assertEquals(LocalDateTime.class, getWalletDetailsResponse.getUpdated().getClass());
        assertEquals(LocalDateTime.class, getWalletDetailsResponse.getCreated().getClass());
        verify(walletsRepo, times(1)).findById(TestUtil.USER1_NAME);
    }
    @Test
    public void testGetWalletDetails_EntityNotFoundException() {
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(java.util.Optional.empty());
        assertThrows(CustomException.EntityNotFoundException.class, () -> walletService.getWalletDetails(TestUtil.USER1_NAME));
    }
    @Test
    public void testCreateTotp_Success() throws QrGenerationException {
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(java.util.Optional.of(TestUtil.WalletTestData.getWallet1Response()));
        ResponseEntity<?> responseEntity = walletService.createTotp(TestUtil.USER1_NAME);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(walletsRepo, times(1)).findById(TestUtil.USER1_NAME);
        verify(walletsRepo, times(1)).save(any());
    }
    @Test
    public void testCreateTotp_EntityNotFoundException() {
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(java.util.Optional.empty());
        assertThrows(CustomException.EntityNotFoundException.class, () -> walletService.createTotp(TestUtil.USER1_NAME));
    }
    @Test
    public void testCreateTotp_ValidationException() {
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(java.util.Optional.of(TestUtil.WalletTestData.getWallet1ResponseTotpEnabled()));
        assertThrows(CustomException.ValidationException.class, () -> walletService.createTotp(TestUtil.USER1_NAME));
    }
    @Test
    public void testCreateTotp_SaveNotSuccessfulException() {
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(java.util.Optional.of(TestUtil.WalletTestData.getWallet1Response()));
        doThrow(new RuntimeException("A database error")).when(walletsRepo).save(any());
        assertThrows(CustomException.UnableToSaveException.class, () -> walletService.createTotp(TestUtil.USER1_NAME));
    }
    @Test
    public void testConfirmTotp_Success() {
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(java.util.Optional.of(TestUtil.WalletTestData.getWallet1ResponseTotpToBeEnabled()));
        when(verifier.isValidCode(anyString(), anyString())).thenReturn(true);
        when(walletsRepo.save(any())).thenReturn(TestUtil.WalletTestData.getWallet1ResponseTotpEnabled());
        ResponseEntity<?> responseEntity = walletService.confirmTotp(TestUtil.USER1_NAME, TestUtil.WalletTestData.TOTP_CODE);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }
    @Test
    public void testConfirmTotp_EntityNotFoundException() {
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(java.util.Optional.empty());
        assertThrows(CustomException.EntityNotFoundException.class, () -> walletService.confirmTotp(TestUtil.USER1_NAME, TestUtil.WalletTestData.TOTP_CODE));
    }
    @Test
    public void testConfirmTotp_ValidationException_SecretKeyNotFound() {
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(java.util.Optional.of(TestUtil.WalletTestData.getWallet1Response()));
        assertThrows(CustomException.ValidationException.class, () -> walletService.confirmTotp(TestUtil.USER1_NAME, TestUtil.WalletTestData.TOTP_CODE));
    }
    @Test
    public void testConfirmTotp_ValidationException_TotpAlreadyEnabled() {
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(java.util.Optional.of(TestUtil.WalletTestData.getWallet1ResponseTotpEnabled()));
        assertThrows(CustomException.ValidationException.class, () -> walletService.confirmTotp(TestUtil.USER1_NAME, TestUtil.WalletTestData.TOTP_CODE));
    }
    @Test
    public void testConfirmTotp_ValidationException_InvalidCode() {
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(java.util.Optional.of(TestUtil.WalletTestData.getWallet1ResponseTotpToBeEnabled()));
        when(verifier.isValidCode(anyString(), anyString())).thenReturn(false);
        assertThrows(CustomException.ValidationException.class, () -> walletService.confirmTotp(TestUtil.USER1_NAME, TestUtil.WalletTestData.TOTP_CODE));
    }
    @Test
    public void testConfirmTotp_SaveNotSuccessfulException() {
        when(walletsRepo.findById(TestUtil.USER1_NAME)).thenReturn(java.util.Optional.of(TestUtil.WalletTestData.getWallet1ResponseTotpToBeEnabled()));
        when(verifier.isValidCode(anyString(), anyString())).thenReturn(true);
        when(walletsRepo.save(any())).thenThrow(new RuntimeException("A database error"));
        assertThrows(CustomException.UnableToSaveException.class, () -> walletService.confirmTotp(TestUtil.USER1_NAME, TestUtil.WalletTestData.TOTP_CODE));
    }

    @Test
    public void testGetStatement_Success() {
        when(transactionsRepo.findAllByFromUIdIgnoreCase(TestUtil.USER1_NAME)).thenReturn(TestUtil.TransactionTestData.getFromTransactions());
        when(transactionsRepo.findAllByToUIdIgnoreCase(TestUtil.USER1_NAME)).thenReturn(TestUtil.TransactionTestData.getToTransactions());
        when(rechargesRepo.findAllByuIdIgnoreCase(TestUtil.USER1_NAME)).thenReturn(TestUtil.RechargeTestData.getRecharges());

        ResponseEntity<GetStatementResponse> responseEntity = walletService.getStatement(TestUtil.USER1_NAME, 0);
        GetStatementResponse expectedResponse = TestUtil.StatementTestData.needStatementResponse();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody().getStatements());
        assertEquals(expectedResponse.getStatements(),responseEntity.getBody().getStatements());
        assertEquals(expectedResponse.getTotalPages(),responseEntity.getBody().getTotalPages());
        assertEquals(LocalDateTime.class, responseEntity.getBody().getResponseTime().getClass());
    }

    @Test
    public void testGetCashback_Success() {
        when(rechargesRepo.countByuId(any())).thenReturn((long)100);
        when(rechargesRepo.findAllByuIdIgnoreCaseOrderByCreatedDesc(any(), any())).thenReturn(TestUtil.RechargeTestData.getRecharges());
        Pageable pageable = PageRequest.of(0, Globals.pageSize, Sort.by(Sort.Direction.DESC,"created"));
        ResponseEntity<GetCashbackResponse> responseEntity = walletService.getCashback(TestUtil.USER1_NAME,pageable );
        GetCashbackResponse expectedResponse = TestUtil.CashbackTestData.needCashbackResponse();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getRecharges(), Objects.requireNonNull(responseEntity.getBody()).getRecharges());
    }
    @Test
    public void testGetCashback_PageFail() {
        when(rechargesRepo.countByuId(any())).thenReturn((long)100);
        when(rechargesRepo.findAllByuIdIgnoreCaseOrderByCreatedDesc(any(), any())).thenReturn(TestUtil.RechargeTestData.getRecharges());
        Pageable pageable = PageRequest.of(11, Globals.pageSize, Sort.by(Sort.Direction.DESC,"created"));

        CustomException.ValidationException validationException = assertThrows(CustomException.ValidationException.class, () ->walletService.getCashback(TestUtil.USER1_NAME,pageable ));
        assertEquals("Invalid page request", validationException.getMessage());

    }
}