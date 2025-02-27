package com.nextuple.pranay.fullstack.controller;

import com.nextuple.pranay.fullstack.dto.GetCashbackResponse;
import com.nextuple.pranay.fullstack.dto.GetStatementResponse;
import com.nextuple.pranay.fullstack.dto.GetWalletDetailsResponse;
import com.nextuple.pranay.fullstack.dto.MessageResponse;
import com.nextuple.pranay.fullstack.exception.CustomException;
import com.nextuple.pranay.fullstack.service.WalletService;
import com.nextuple.pranay.fullstack.utils.AuthUserUtils;
import com.nextuple.pranay.fullstack.utils.Globals;
import dev.samstevens.totp.exceptions.QrGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@CrossOrigin("*")
@RestController
@RequestMapping(Globals.walletControllerMap)
public class WalletController {
    @Autowired
    private WalletService walletService;
    @Autowired
    private AuthUserUtils authUserUtils;

    @GetMapping(Globals.walletController_getWalletDetailsMap)
    public ResponseEntity<GetWalletDetailsResponse> getWalletDetails(@RequestHeader("Authorization") String token) {
        String userId = authUserUtils.getUserId(token);
        return walletService.getWalletDetails(userId);
    }
    @PostMapping(Globals.walletController_createTotpMap)
    public ResponseEntity<MessageResponse> createTotp(@RequestHeader("Authorization") String token) throws QrGenerationException {
        String userId = authUserUtils.getUserId(token);
        return walletService.createTotp(userId);
    }

    @PostMapping(Globals.walletController_confirmTotpMap)
    public ResponseEntity<Boolean> confirmTotp(@RequestHeader("Authorization") String token, @RequestParam String code) {
        String userId = authUserUtils.getUserId(token);
        return walletService.confirmTotp( userId, code);
    }

    @PutMapping(Globals.walletController_disableTotpMap)
    public ResponseEntity<MessageResponse> disableTotp(@RequestHeader("Authorization") String token){
        String userId = authUserUtils.getUserId(token);
        return walletService.disableTotp(userId);
    }
    @GetMapping(Globals.walletController_getStatementMap)
    public ResponseEntity<GetStatementResponse> getStatement(@RequestHeader("Authorization") String token,@RequestParam(defaultValue = "0") int page) {
        String userId = authUserUtils.getUserId(token);
        if (page< 0)
            throw new CustomException.ValidationException("Invalid page request");
        return walletService.getStatement(userId, page);
    }

    @GetMapping(Globals.walletController_getCashbackMap)
    public ResponseEntity<GetCashbackResponse> getCashback(@RequestHeader("Authorization") String token, @RequestParam(defaultValue = "0") int page) {
        String userId = authUserUtils.getUserId(token);
        if (page< 0)
            throw new CustomException.ValidationException("Invalid page request");
        Pageable pageable = PageRequest.of(page, Globals.pageSize,Sort.by(Sort.Direction.DESC,"created"));
        return walletService.getCashback(userId, pageable);
    }
}
