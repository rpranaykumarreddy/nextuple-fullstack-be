package com.nextuple.pranay.fullstack.controller;

import com.nextuple.pranay.fullstack.service.RechargeService;
import com.nextuple.pranay.fullstack.utils.AuthUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/recharge")
public class RechargeController {
    @Autowired
    private RechargeService rechargeService;
    @Autowired
    private AuthUserUtils authUserUtils;

//    @PostMapping("wallet")
//    public ResponseEntity<RechargeWalletResponse> rechargeWallet(@RequestHeader("Authorization") String token, @RequestBody RechargeWalletRequest rechargeWalletRequest){
//        String userId = authUserUtils.getUserId(token);
//        return ResponseEntity.ok(rechargeService.rechargeWallet(userId, rechargeWalletRequest));
//    }
}
