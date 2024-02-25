package com.nextuple.pranay.fullstack.service;


import com.nextuple.pranay.fullstack.dto.RechargeWalletRequest;
import com.nextuple.pranay.fullstack.dto.RechargeWalletResponse;
import com.nextuple.pranay.fullstack.model.Recharges;
import com.nextuple.pranay.fullstack.model.Wallets;
import com.nextuple.pranay.fullstack.repo.RechargesRepo;
import com.nextuple.pranay.fullstack.repo.WalletsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RechargeService {
    @Autowired
    private RechargesRepo rechargesRepo;
    @Autowired
    private WalletsRepo walletsRepo;
@Transactional
    public RechargeWalletResponse rechargeWallet(String userId, RechargeWalletRequest rechargeWalletRequest) {
    double amount = rechargeWalletRequest.getAmount();
    String walletId = rechargeWalletRequest.getWalletId();
    Wallets wallet = walletsRepo.findById(walletId).orElseThrow(() -> new RuntimeException("Wallet not found"));
    if(!wallet.getUserId().equals(userId)){
            throw new RuntimeException("Unauthorized");
        }
        double cashback = Math.ceil(Math.random()*amount*0.01);
        Recharges recharge = new Recharges();
        recharge.setAmount(amount);
        recharge.setWalletId(walletId);
        recharge.setCreated(LocalDateTime.now());
        recharge.setCashback(cashback);
        wallet.setBalance(wallet.getBalance()+ amount + cashback);
        wallet.setUpdated(LocalDateTime.now());
        try {
            rechargesRepo.save(recharge);
            walletsRepo.save(wallet);
        } catch (Exception e) {
            throw new RuntimeException("Unable to recharge");
        }
        return new RechargeWalletResponse("Recharge Successful",wallet.getBalance());
    }

}
