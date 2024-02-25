package com.nextuple.pranay.fullstack.controller;

import com.nextuple.pranay.fullstack.dto.CheckBalanceResponse;
import com.nextuple.pranay.fullstack.dto.CreateWalletResponse;
import com.nextuple.pranay.fullstack.security.JWTTokenProvider;
import com.nextuple.pranay.fullstack.service.WalletService;
import com.nextuple.pranay.fullstack.utils.AuthUserUtils;
import dev.samstevens.totp.exceptions.QrGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;
    @Autowired
    private AuthUserUtils authUserUtils;

    @PostMapping("/create")
    public ResponseEntity<CreateWalletResponse> createWallet(@RequestHeader("Authorization") String token){
        String userId = authUserUtils.getUserId(token);
        return walletService.createWallet(userId);
    }
    @PostMapping("/balance")
    public ResponseEntity<CheckBalanceResponse> checkBalance(@RequestHeader("Authorization") String token){
        String userId = authUserUtils.getUserId(token);
        return walletService.checkBalance(userId);
    }

    @PostMapping("/{walletId}/totp")
    public ResponseEntity<?> createTotp(@PathVariable String walletId, @RequestHeader("Authorization") String token) throws QrGenerationException {
        String userId = authUserUtils.getUserId(token);
        return walletService.createTotp(walletId, userId);
    }

    @PostMapping("/{walletId}/totp/confirm")
    public ResponseEntity<?> confirmTotp(@PathVariable String walletId, @RequestHeader("Authorization") String token, @RequestParam String code) {
        String userId = authUserUtils.getUserId(token);
        return walletService.confirmTotp(walletId, userId, code);
    }

}
