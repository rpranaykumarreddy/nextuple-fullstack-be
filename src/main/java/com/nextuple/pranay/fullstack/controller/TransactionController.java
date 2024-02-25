package com.nextuple.pranay.fullstack.controller;

import com.nextuple.pranay.fullstack.dto.CheckBalanceResponse;
import com.nextuple.pranay.fullstack.dto.ConfirmTeansactionRequest;
import com.nextuple.pranay.fullstack.dto.CreateWalletResponse;
import com.nextuple.pranay.fullstack.dto.InitTeansactionRequest;
import com.nextuple.pranay.fullstack.service.TransactionService;
import com.nextuple.pranay.fullstack.service.WalletService;
import com.nextuple.pranay.fullstack.utils.AuthUserUtils;
import dev.samstevens.totp.exceptions.QrGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private AuthUserUtils authUserUtils;

    /*
todo: Transaction Init: POST /transaction/init
    verify the receiver wallet & sender's balance
    returns: Transaction ID or error message string

todo: Transaction Confirm: POST /transaction/confirm
    verify TOTP or OTP & debit & credit the wallets
    returns: success message string or error message string

     */
    @PostMapping("/init")
    public ResponseEntity<?> initTransaction(@RequestHeader("Authorization") String token, @RequestBody InitTeansactionRequest request) {
        String userId = authUserUtils.getUserId(token);
        return transactionService.initTransaction(userId, request);
    }
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmTransaction(@RequestHeader("Authorization") String token, @RequestBody ConfirmTeansactionRequest request) {
        String userId = authUserUtils.getUserId(token);
        return transactionService.confirmTransaction(userId, request);
    }

    @PostMapping("/{walletId}/totp/confirm")
    public ResponseEntity<?> confirmTotp(@PathVariable String walletId, @RequestHeader("Authorization") String token, @RequestParam String code) {
        String userId = authUserUtils.getUserId(token);
        return walletService.confirmTotp(walletId, userId, code);
    }

}
