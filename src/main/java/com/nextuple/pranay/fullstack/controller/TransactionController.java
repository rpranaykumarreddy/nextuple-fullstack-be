package com.nextuple.pranay.fullstack.controller;

import com.nextuple.pranay.fullstack.dto.GetWalletDetailsResponse;
import com.nextuple.pranay.fullstack.dto.InitTransactionRequest;
import com.nextuple.pranay.fullstack.dto.InitTransactionResponse;
import com.nextuple.pranay.fullstack.dto.MessageResponse;
import com.nextuple.pranay.fullstack.service.TransactionService;
import com.nextuple.pranay.fullstack.utils.AuthUserUtils;
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
    private AuthUserUtils authUserUtils;

    @GetMapping("/check-wallet/{username}")
    public ResponseEntity<Boolean> checkWallet(@PathVariable String username){
        String usernameIgnoreCase = username.toLowerCase();
        return transactionService.checkUsername(usernameIgnoreCase);
    }

    @PostMapping("/init")
    public ResponseEntity<InitTransactionResponse> initTransaction(@RequestHeader("Authorization") String token, @RequestBody InitTransactionRequest request) {
        String userId = authUserUtils.getUserId(token);
        request.validate();
        return transactionService.initTransaction(userId, request);}

    @PostMapping("/confirm/{transactionId}")
    public ResponseEntity<GetWalletDetailsResponse> confirmTransaction(@RequestHeader("Authorization") String token, @PathVariable String transactionId, @RequestParam String code) {
        String userId = authUserUtils.getUserId(token);
        return transactionService.confirmTransaction(userId, transactionId, code);}

    @PostMapping("/cancel/{transactionId}")
    public ResponseEntity<MessageResponse> cancelTransaction(@PathVariable String transactionId, @RequestHeader("Authorization") String token) {
        String userId = authUserUtils.getUserId(token);
        return transactionService.cancelTransaction(userId, transactionId);}
}