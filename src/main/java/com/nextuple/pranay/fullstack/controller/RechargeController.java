package com.nextuple.pranay.fullstack.controller;

import com.nextuple.pranay.fullstack.dto.GetWalletDetailsResponse;
import com.nextuple.pranay.fullstack.exception.CustomException;
import com.nextuple.pranay.fullstack.service.RechargeService;
import com.nextuple.pranay.fullstack.utils.AuthUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
public class RechargeController {
    @Autowired
    private RechargeService rechargeService;
    @Autowired
    private AuthUserUtils authUserUtils;

    @PostMapping("/recharge")
    public ResponseEntity<GetWalletDetailsResponse> rechargeWallet(@RequestHeader("Authorization") String token, @RequestParam double amount){
        String userId = authUserUtils.getUserId(token);
        if(amount <1){
            throw new CustomException.ValidationException("Your can only recharge with more than ₹1");
        }
        return rechargeService.rechargeWallet(userId, amount);
    }
}
