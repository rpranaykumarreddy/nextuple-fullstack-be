package com.nextuple.pranay.fullstack.controller;

import com.nextuple.pranay.fullstack.service.TransactionService;
import com.nextuple.pranay.fullstack.service.WalletService;
import com.nextuple.pranay.fullstack.utils.AuthUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
//
//    @PostMapping("/init")
//    public ResponseEntity<?> initTransaction(@RequestHeader("Authorization") String token, @RequestBody InitTeansactionRequest request) {
//        String userId = authUserUtils.getUserId(token);
//        return transactionService.initTransaction(userId, request);
//    }
//    @PostMapping("/confirm")
//    public ResponseEntity<?> confirmTransaction(@RequestHeader("Authorization") String token, @RequestBody ConfirmTeansactionRequest request) {
//        String userId = authUserUtils.getUserId(token);
//        return transactionService.confirmTransaction(userId, request);
//    }
//
//    @PostMapping("/{walletId}/totp/confirm")
//    public ResponseEntity<?> confirmTotp(@PathVariable String walletId, @RequestHeader("Authorization") String token, @RequestParam String code) {
//        String userId = authUserUtils.getUserId(token);
//        return walletService.confirmTotp(walletId, userId, code);
//    }
//
//    @PostMapping("/{tranactionId}/cancel")
//    public ResponseEntity<?> cancelTransaction(@PathVariable String tranactionId, @RequestHeader("Authorization") String token) {
//        String userId = authUserUtils.getUserId(token);
//        return transactionService.cancelTransaction(userId, tranactionId);
//    }

}
