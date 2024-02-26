package com.nextuple.pranay.fullstack.service;


import com.nextuple.pranay.fullstack.dto.GetWalletDetailsResponse;
import com.nextuple.pranay.fullstack.exception.CustomException;
import com.nextuple.pranay.fullstack.model.Recharges;
import com.nextuple.pranay.fullstack.model.Wallets;
import com.nextuple.pranay.fullstack.repo.RechargesRepo;
import com.nextuple.pranay.fullstack.repo.WalletsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<GetWalletDetailsResponse> rechargeWallet(String userId, double amount){
        Wallets wallet = walletsRepo.findById(userId).orElseThrow(
                ()-> new CustomException.EntityNotFoundException("Wallet not found")
        );
        double cashback = Math.ceil(Math.random()*amount*0.01);

        Recharges recharge = new Recharges();
        recharge.setAmount(amount);
        recharge.setUId(userId);
        recharge.setCreated(LocalDateTime.now());
        recharge.setCashback(cashback);

        wallet.setBalance(wallet.getBalance()+ amount + cashback);
        wallet.setUpdated(LocalDateTime.now());

        try {
            rechargesRepo.save(recharge);
            walletsRepo.save(wallet);
        } catch (Exception e) {
            throw new CustomException.UnableToSaveException("Unable to save recharge");
        }
        GetWalletDetailsResponse response = new GetWalletDetailsResponse();
        response.copyWallets(wallet);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
