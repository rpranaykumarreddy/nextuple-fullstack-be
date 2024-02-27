package com.nextuple.pranay.fullstack.controller;


import com.nextuple.pranay.fullstack.TestUtil;
import com.nextuple.pranay.fullstack.service.RechargeService;
import com.nextuple.pranay.fullstack.utils.AuthUserUtils;
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
public class RechargeControllerTests {

    @Mock
    private RechargeService rechargeService;
    @Mock
    private AuthUserUtils authUserUtils;

    @InjectMocks
    private RechargeController rechargeController;

    @Test
    public void testRechargeWallet_Success_validation() {
        when(authUserUtils.getUserId(anyString())).thenReturn(TestUtil.USER1_NAME);
        when(rechargeService.rechargeWallet(anyString(), anyDouble())).thenReturn(new ResponseEntity<>(TestUtil.WalletTestData.getWallet1ResponseDto(), HttpStatus.OK));
        assertDoesNotThrow(() -> rechargeController.rechargeWallet(TestUtil.TOKEN, 30000.3));
    }

}