package com.nextuple.pranay.fullstack.service;


import com.nextuple.pranay.fullstack.dto.GetWalletDetailsResponse;
import com.nextuple.pranay.fullstack.dto.InitTransactionRequest;
import com.nextuple.pranay.fullstack.dto.InitTransactionResponse;
import com.nextuple.pranay.fullstack.dto.MessageResponse;
import com.nextuple.pranay.fullstack.exception.CustomException;
import com.nextuple.pranay.fullstack.model.Transactions;
import com.nextuple.pranay.fullstack.model.Wallets;
import com.nextuple.pranay.fullstack.repo.TransactionsRepo;
import com.nextuple.pranay.fullstack.repo.WalletsRepo;
import com.nextuple.pranay.fullstack.utils.Globals;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransactionService {
    @Autowired
    private TransactionsRepo transactionsRepo;
    @Autowired
    private WalletsRepo walletsRepo;

    TimeProvider timeProvider = new SystemTimeProvider();
    CodeGenerator codeGenerator = new DefaultCodeGenerator();
    CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
    public ResponseEntity<Boolean> checkUsername(String username) {
        return new ResponseEntity<>(walletsRepo.existsByUsernameIgnoreCase(username), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<InitTransactionResponse> initTransaction(String userId, InitTransactionRequest request) {
        String toWalletId = request.getTo();
        double amount = request.getAmount();
        if (userId.equals(toWalletId)) {
            throw new CustomException.BadRequestException("You cannot transfer money to yourself");
        }
        if (amount <= 0) {
            throw new CustomException.BadRequestException("Invalid amount");
        }
        Wallets fromWallet = walletsRepo.findById(userId).orElseThrow(
                () -> new CustomException.EntityNotFoundException("Your wallet not found. Contact support team.")
        );
        if (fromWallet.getBalance() < amount) {
            throw new CustomException.BadRequestException("Insufficient balance for the transaction");
        }
        Wallets toWallet = walletsRepo.findById(toWalletId).orElseThrow(
                () -> new CustomException.EntityNotFoundException("Receiver's wallet not found")
        );
        Transactions transaction = new Transactions();
        transaction.setFromUId(userId);
        transaction.setToUId(toWalletId);
        transaction.setAmount(amount);
        transaction.setStatus(Transactions.TransactionStatus.INIT);
        transaction.setCreated(java.time.LocalDateTime.now());
        try{
            transactionsRepo.save(transaction);
        }catch (Exception e){
            throw new CustomException.UnableToSaveException("Unable to initiate transaction");
        }
        InitTransactionResponse response = new InitTransactionResponse();
        response.copyTransactionId(transaction, transaction.getCreated().plusMinutes(Globals.transactionTimeoutMins));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<GetWalletDetailsResponse> confirmTransaction(String userId, String transactionId, String code) {
        Transactions transaction = transactionsRepo.findById(transactionId).orElseThrow(
                () -> new CustomException.EntityNotFoundException("Transaction not initiated"));
        if (!transaction.getFromUId().equals(userId)) {
            throw new CustomException.UnauthorizedException("Unauthorized action");
        }
        if (transaction.getStatus() != Transactions.TransactionStatus.INIT) {
            throw new CustomException.BadRequestException("Invalid transaction status");
        }
        if(transaction.getCreated().plusMinutes(Globals.transactionTimeoutMins).isBefore(LocalDateTime.now())){
            transaction.setStatus(Transactions.TransactionStatus.TIMEOUT);
            try{
                transactionsRepo.save(transaction);
            }catch (Exception e){
                throw new CustomException.UnableToSaveException("Transaction timeout & unable to update status");
            }
            throw new CustomException.BadRequestException("Transaction timeout");
        }
        Wallets fromWallet = walletsRepo.findById(userId).orElseThrow(
                () -> new CustomException.EntityNotFoundException("Your wallet not found. Contact support team.")
        );
        Wallets toWallet = walletsRepo.findById(transaction.getToUId()).orElseThrow(
                () -> new CustomException.EntityNotFoundException("Receiver's wallet not found")
        );
        if (fromWallet.getBalance() < transaction.getAmount()) {
            throw new CustomException.BadRequestException("Insufficient balance for the transaction");
        }
        if(fromWallet.isTotpEnabled() && (code == null || code.isBlank())){
            throw new CustomException.BadRequestException("Please enter TOTP");
        }
        if(fromWallet.isTotpEnabled() && !fromWallet.getSecretKey().isBlank()){
            String secret = fromWallet.getSecretKey();
            boolean successful = verifier.isValidCode(secret, code);
            if (!successful) {
                throw new CustomException.BadRequestException("Incorrect TOTP");
            }
        }
        fromWallet.setBalance(fromWallet.getBalance() - transaction.getAmount());
        toWallet.setBalance(toWallet.getBalance() + transaction.getAmount());
        transaction.setStatus(Transactions.TransactionStatus.SUCCESSFUL);
        try{
            walletsRepo.save(fromWallet);
            walletsRepo.save(toWallet);
            transactionsRepo.save(transaction);
        }catch (Exception e){
            throw new CustomException.UnableToSaveException("Unable to complete transaction");
        }
        GetWalletDetailsResponse response = new GetWalletDetailsResponse(fromWallet);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    public ResponseEntity<MessageResponse> cancelTransaction(String userId, String tranactionId) {
        Transactions transaction = transactionsRepo.findById(tranactionId).orElseThrow(
                () -> new CustomException.EntityNotFoundException("Transaction not initiated")
        );
        if (!transaction.getFromUId().equals(userId)) {
            throw new CustomException.UnauthorizedException("Unauthorized action");
        }
        if (transaction.getStatus() != Transactions.TransactionStatus.INIT) {
            throw new CustomException.BadRequestException("Invalid transaction status");
        }
        if(transaction.getCreated().plusMinutes(Globals.transactionTimeoutMins).isBefore(LocalDateTime.now())){
            transaction.setStatus(Transactions.TransactionStatus.TIMEOUT);
            try{
                transactionsRepo.save(transaction);
            }catch (Exception e){
                throw new CustomException.UnableToSaveException("Transaction timeout & unable to update status");
            }
            throw new CustomException.BadRequestException("Transaction timeout");
        }
        transaction.setStatus(Transactions.TransactionStatus.CANCELLED);
        transactionsRepo.save(transaction);
        return new ResponseEntity<>(new MessageResponse("Transaction cancelled"), HttpStatus.OK);
    }
}
