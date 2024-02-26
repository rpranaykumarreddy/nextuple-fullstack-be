package com.nextuple.pranay.fullstack.controller;

import com.nextuple.pranay.fullstack.service.WalletService;
import com.nextuple.pranay.fullstack.utils.AuthUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;
    @Autowired
    private AuthUserUtils authUserUtils;
//
//    @PostMapping("/{walletId}/totp")
//    public ResponseEntity<?> createTotp(@PathVariable String walletId, @RequestHeader("Authorization") String token) throws QrGenerationException {
//        String userId = authUserUtils.getUserId(token);
//        return walletService.createTotp(walletId, userId);
//    }
//
//    @PostMapping("/{walletId}/totp/confirm")
//    public ResponseEntity<?> confirmTotp(@PathVariable String walletId, @RequestHeader("Authorization") String token, @RequestParam String code) {
//        String userId = authUserUtils.getUserId(token);
//        return walletService.confirmTotp(walletId, userId, code);
//    }
//    @GetMapping("/details")
//    public ResponseEntity<GetWalletDetailsResponse> getWalletDetails(@RequestHeader("Authorization") String token) {
//        String userId = authUserUtils.getUserId(token);
//        return walletService.getWalletDetails(userId);
//    }

//    @GetMapping("/statement")
//    public ResponseEntity<GetStatementResponse> getStatement(@RequestHeader("Authorization") String token) {
//        String userId = authUserUtils.getUserId(token);
//        return walletService.getStatement(userId);
//    }



}
