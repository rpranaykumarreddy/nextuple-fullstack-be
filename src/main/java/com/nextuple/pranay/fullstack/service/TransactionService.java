package com.nextuple.pranay.fullstack.service;


import com.nextuple.pranay.fullstack.dto.InitTeansactionRequest;
import com.nextuple.pranay.fullstack.repo.TransactionsRepo;
import com.nextuple.pranay.fullstack.repo.WalletsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class TransactionService {
    @Autowired
    private TransactionsRepo transactionsRepo;
    @Autowired
    private WalletsRepo walletsRepo;
    /*
   todo: Transaction Init: POST /transaction/init
       verify the receiver wallet & sender's balance
       returns: Transaction ID or error message string

   todo: Transaction Confirm: POST /transaction/confirm
       verify TOTP or OTP & debit & credit the wallets
       returns: success message string or error message string
        */
    public ResponseEntity<?> initTransaction(String userId, InitTeansactionRequest request) {
        return null;
    }
}
