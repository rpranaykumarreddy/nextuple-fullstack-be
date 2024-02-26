package com.nextuple.pranay.fullstack.controller;

import com.nextuple.pranay.fullstack.dto.GetWalletDetailsResponse;
import com.nextuple.pranay.fullstack.service.WalletService;
import com.nextuple.pranay.fullstack.utils.AuthUserUtils;
import dev.samstevens.totp.exceptions.QrGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;
    @Autowired
    private AuthUserUtils authUserUtils;
    @GetMapping("/details")
    public ResponseEntity<GetWalletDetailsResponse> getWalletDetails(@RequestHeader("Authorization") String token) {
        String userId = authUserUtils.getUserId(token);
        return walletService.getWalletDetails(userId);
    }
    @PostMapping("/totp")
    public ResponseEntity<?> createTotp(@RequestHeader("Authorization") String token) throws QrGenerationException {
        String userId = authUserUtils.getUserId(token);
        return walletService.createTotp(userId);
    }

    @PostMapping("/totp/confirm")
    public ResponseEntity<?> confirmTotp( @RequestHeader("Authorization") String token, @RequestParam String code) {
        String userId = authUserUtils.getUserId(token);
        return walletService.confirmTotp( userId, code);
    }

//    @GetMapping("/statement")
//    public ResponseEntity<GetStatementResponse> getStatement(@RequestHeader("Authorization") String token) {
//        String userId = authUserUtils.getUserId(token);
//        return walletService.getStatement(userId);
//    }
}
