package com.nextuple.pranay.fullstack.service;


import com.nextuple.pranay.fullstack.dto.GetStatementResponse;
import com.nextuple.pranay.fullstack.dto.GetWalletDetailsResponse;
import com.nextuple.pranay.fullstack.dto.MessageResponse;
import com.nextuple.pranay.fullstack.exception.CustomException;
import com.nextuple.pranay.fullstack.model.Recharges;
import com.nextuple.pranay.fullstack.model.Transactions;
import com.nextuple.pranay.fullstack.model.Wallets;
import com.nextuple.pranay.fullstack.repo.RechargesRepo;
import com.nextuple.pranay.fullstack.repo.TransactionsRepo;
import com.nextuple.pranay.fullstack.repo.WalletsRepo;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
public class WalletService {
    @Autowired
    private WalletsRepo walletsRepo;
    @Autowired
    private TransactionsRepo transactionsRepo;
    @Autowired
    private RechargesRepo rechargesRepo;
//todo: trim balance to 2 decimal places
    TimeProvider timeProvider = new SystemTimeProvider();
    CodeGenerator codeGenerator = new DefaultCodeGenerator();
    CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
    public ResponseEntity<GetWalletDetailsResponse> getWalletDetails(String userId) {
        Wallets wallet = walletsRepo.findById(userId).orElseThrow(
                () -> new CustomException.EntityNotFoundException("Wallet not found"));
        GetWalletDetailsResponse response = new GetWalletDetailsResponse(wallet);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<MessageResponse> createTotp(String userId) throws QrGenerationException {
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        String secret = secretGenerator.generate();
        Wallets wallet = walletsRepo.findById(userId).orElseThrow(
                ()->new CustomException.EntityNotFoundException("Wallet not found")
        );
        if(wallet.isTotpEnabled()){
            throw new CustomException.ValidationException("Totp already enabled");
        }
        wallet.setSecretKey(secret);
        try {
            walletsRepo.save(wallet);
        } catch (Exception e) {
            throw new CustomException.UnableToSaveException("Unable to save");
        }
        QrData data = new QrData.Builder()
                .label(userId)
                .secret(secret)
                .issuer("RPKR")
                .algorithm(HashingAlgorithm.SHA1) // More on this below
                .digits(6)
                .period(30)
                .build();
        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData = generator.generate(data);
        String mimeType = generator.getImageMimeType();
        String dataUri = getDataUriForImage(imageData, mimeType);
//        String dataUri ="<img src='"+getDataUriForImage(imageData, mimeType)+"' alt='Need QR' width='200' height='200'>";
        return new ResponseEntity<>(new MessageResponse(dataUri), HttpStatus.CREATED);
    }

    public ResponseEntity<Boolean> confirmTotp(String userId, String code) {
        Wallets wallet = walletsRepo.findById(userId).orElseThrow(
                ()->new CustomException.EntityNotFoundException("Wallet not found")
        );
        if(wallet.getSecretKey()==null || wallet.getSecretKey().isEmpty()){
            throw new CustomException.ValidationException("Secret key not found");
        }
        if(wallet.isTotpEnabled()){
            throw new CustomException.ValidationException("Totp already enabled");
        }
        String secret = wallet.getSecretKey();
        boolean successful = verifier.isValidCode(secret, code);
        if(successful){
            try {
                wallet.setTotpEnabled(true);
                walletsRepo.save(wallet);
            } catch (Exception e) {
                throw new CustomException.UnableToSaveException("Unable to save");
            }
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        }else{
            throw new CustomException.ValidationException("Invalid code");
        }
    }
    @Transactional
    public ResponseEntity<GetStatementResponse> getStatement(String userId, int month, int year) {
        Wallets wallet = walletsRepo.findById(userId).orElseThrow(
                ()->new CustomException.EntityNotFoundException("Wallet not found")
        );
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);
        //month notation is 1-based
        List<Transactions> fromTransactions = transactionsRepo.findAllByFromUIdIgnoreCaseAndCreatedBetween(userId, startOfMonth, endOfMonth);
        List<Transactions> toTransactions = transactionsRepo.findAllByToUIdIgnoreCaseAndCreatedBetween(userId, startOfMonth, endOfMonth);
        List<Recharges> recharges = rechargesRepo.findAllByuIdIgnoreCaseAndCreatedBetween(userId, startOfMonth, endOfMonth);
        GetStatementResponse response = new GetStatementResponse(wallet, fromTransactions, toTransactions, recharges);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
