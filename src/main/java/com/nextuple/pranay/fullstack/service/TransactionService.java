package com.nextuple.pranay.fullstack.service;


import com.nextuple.pranay.fullstack.dto.ConfirmTeansactionRequest;
import com.nextuple.pranay.fullstack.dto.InitTeansactionRequest;
import com.nextuple.pranay.fullstack.model.Transactions;
import com.nextuple.pranay.fullstack.model.Wallets;
import com.nextuple.pranay.fullstack.repo.TransactionsRepo;
import com.nextuple.pranay.fullstack.repo.WalletsRepo;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;


@Service
public class TransactionService {
//    @Autowired
//    private TransactionsRepo transactionsRepo;
//    @Autowired
//    private WalletsRepo walletsRepo;
//
//    @Transactional
//    public ResponseEntity<?> initTransaction(String userId, InitTeansactionRequest request) {
//        String fromWalletId = request.getFromWalletId();
//        String toWalletId = request.getToWalletId();
//        double amount = request.getAmount();
//        if (fromWalletId.equals(toWalletId)) {
//            return ResponseEntity.badRequest().body("fromWalletId and toWalletId cannot be same");
//        }
//        if (amount <= 0) {
//            return ResponseEntity.badRequest().body("amount should be greater than 0");
//        }
//        Optional<Wallets> fromWallet = walletsRepo.findById(fromWalletId);
//        if (fromWallet.isEmpty()) {
//            return ResponseEntity.badRequest().body("fromWalletId does not exist");
//        }else {
//            if (fromWallet.get().getBalance() < amount) {
//                return ResponseEntity.badRequest().body("insufficient balance");
//            }
//            if (walletsRepo.findById(toWalletId).isPresent() && walletsRepo.findById(toWalletId).get().getUserId().equals(userId)) {
//                return ResponseEntity.badRequest().body("You cannot transfer money to yourself");
//            }
//        }
//        if (!walletsRepo.existsById(toWalletId)) {
//            return ResponseEntity.badRequest().body("toWalletId does not exist");
//        }
//        Transactions transaction = new Transactions();
//        transaction.setFromWalletId(fromWalletId);
//        transaction.setToWalletId(toWalletId);
//        transaction.setAmount(amount);
//        transaction.setStatus(Transactions.TransactionStatus.INIT);
//        transactionsRepo.save(transaction);
//        return ResponseEntity.ok(transaction.getId());
//    }
//    @Transactional
//    public ResponseEntity<?> confirmTransaction(String userId, ConfirmTeansactionRequest request) {
//        String fromWalletId = request.getFromWalletId();
//        String transactionId = request.getTransactionId();
//        String code = request.getCode();
//        Optional<Transactions> transactionOpt = transactionsRepo.findById(transactionId);
//        if (transactionOpt.isEmpty()) {
//            return ResponseEntity.badRequest().body("Invalid transactionId");
//        }
//        Transactions transaction = transactionOpt.get();
//        if (!transaction.getFromWalletId().equals(fromWalletId)) {
//            return ResponseEntity.badRequest().body("Invalid fromWalletId");
//        }
//        if (transaction.getStatus() != Transactions.TransactionStatus.INIT) {
//            return ResponseEntity.badRequest().body("Invalid transaction status");
//        }
//        Wallets fromWallet = walletsRepo.findById(fromWalletId).orElseThrow(
//                () -> new RuntimeException("Wallet not found")
//        );
//        Wallets toWallet = walletsRepo.findById(transaction.getToWalletId()).orElseThrow(
//                () -> new RuntimeException("Wallet not found")
//        );
//        if (fromWallet.getBalance() < transaction.getAmount()) {
//            return ResponseEntity.badRequest().body("insufficient balance");
//        }
//        if(StringUtils.isEmpty(fromWallet.getSecretKey())) throw new RuntimeException("Secret not found");
//        String secret = fromWallet.getSecretKey();
//        TimeProvider timeProvider = new SystemTimeProvider();
//        CodeGenerator codeGenerator = new DefaultCodeGenerator();
//        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
//        boolean successful = verifier.isValidCode(secret, code);
//        if (!successful) {
//            return ResponseEntity.badRequest().body("Invalid code");
//        }
//        fromWallet.setBalance(fromWallet.getBalance() - transaction.getAmount());
//        toWallet.setBalance(toWallet.getBalance() + transaction.getAmount());
//        transaction.setStatus(Transactions.TransactionStatus.SUCCESSFUL);
//        walletsRepo.save(fromWallet);
//        walletsRepo.save(toWallet);
//        transactionsRepo.save(transaction);
//        return ResponseEntity.ok("Transaction successful");
//    }
//
//    public ResponseEntity<?> cancelTransaction(String userId, String tranactionId) {
//        Optional<Transactions> transactionOpt = transactionsRepo.findById(tranactionId);
//        if (transactionOpt.isEmpty()) {
//            return ResponseEntity.badRequest().body("Invalid transactionId");
//        }
//        Transactions transaction = transactionOpt.get();
//        if (transaction.getStatus() != Transactions.TransactionStatus.INIT) {
//            return ResponseEntity.badRequest().body("Invalid transaction status");
//        }
//        if (walletsRepo.findById(transaction.getFromWalletId()).isPresent() && !walletsRepo.findById(transaction.getFromWalletId()).get().getUserId().equals(userId)) {
//            return ResponseEntity.badRequest().body("Unauthorized");
//        }
//        transaction.setStatus(Transactions.TransactionStatus.CANCELLED);
//        transactionsRepo.save(transaction);
//        return ResponseEntity.ok("Transaction cancelled");
//    }

}
