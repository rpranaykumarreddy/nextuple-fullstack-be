package com.nextuple.pranay.fullstack.controller;

import com.nextuple.pranay.fullstack.dto.GetCashbackResponse;
import com.nextuple.pranay.fullstack.dto.GetStatementResponse;
import com.nextuple.pranay.fullstack.dto.GetWalletDetailsResponse;
import com.nextuple.pranay.fullstack.dto.MessageResponse;
import com.nextuple.pranay.fullstack.exception.CustomException;
import com.nextuple.pranay.fullstack.service.WalletService;
import com.nextuple.pranay.fullstack.utils.AuthUserUtils;
import dev.samstevens.totp.exceptions.QrGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
    public ResponseEntity<MessageResponse> createTotp(@RequestHeader("Authorization") String token) throws QrGenerationException {
        String userId = authUserUtils.getUserId(token);
        return walletService.createTotp(userId);
    }

    @PostMapping("/totp/confirm")
    public ResponseEntity<Boolean> confirmTotp(@RequestHeader("Authorization") String token, @RequestParam String code) {
        String userId = authUserUtils.getUserId(token);
        return walletService.confirmTotp( userId, code);
    }

    @GetMapping("/statement")
    public ResponseEntity<GetStatementResponse> getStatement(@RequestHeader("Authorization") String token, @RequestParam int month, @RequestParam int year) {
        String userId = authUserUtils.getUserId(token);
        //month should be between 1 and 12
        if (month < 1 || month > 12 || year < 2024 || year > 9999)
            throw new CustomException.ValidationException("Invalid month or year");
        return walletService.getStatement(userId, month, year);
    }
    @GetMapping("/cashback")
    public ResponseEntity<GetCashbackResponse> getCashback(@RequestHeader("Authorization") String token, @RequestParam int month, @RequestParam int year) {
        String userId = authUserUtils.getUserId(token);
        //month should be between 1 and 12
        if (month < 1 || month > 12 || year < 2024 || year > 9999)
            throw new CustomException.ValidationException("Invalid month or year");
        return walletService.getCashback(userId, month, year);
    }
}
