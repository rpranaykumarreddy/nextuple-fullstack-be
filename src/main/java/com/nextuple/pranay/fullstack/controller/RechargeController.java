package com.nextuple.pranay.fullstack.controller;

import com.nextuple.pranay.fullstack.dto.GetWalletDetailsResponse;
import com.nextuple.pranay.fullstack.exception.CustomException;
import com.nextuple.pranay.fullstack.service.RechargeService;
import com.nextuple.pranay.fullstack.utils.AuthUserUtils;
import com.nextuple.pranay.fullstack.utils.Globals;
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

    @PostMapping(Globals.rechargeController_rechargeWalletMap)
    public ResponseEntity<GetWalletDetailsResponse> rechargeWallet(@RequestHeader("Authorization") String token, @RequestParam double amount){
        String userId = authUserUtils.getUserId(token);
        if(amount <1){
            throw new CustomException.ValidationException("Amount should not be less than ₹1");
        }
        if(amount > Globals.rechargeLimit){
            throw new CustomException.ValidationException(Globals.rechargeLimitValidationError);
        }
        return rechargeService.rechargeWallet(userId, amount);
    }
}
