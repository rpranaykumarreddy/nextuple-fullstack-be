package com.nextuple.pranay.fullstack.service;


import com.nextuple.pranay.fullstack.dto.GetCashbackResponse;
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

import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Base64;
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

    TimeProvider timeProvider = new SystemTimeProvider();
    CodeGenerator codeGenerator = new DefaultCodeGenerator();
    CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

    public ResponseEntity<GetWalletDetailsResponse> getWalletDetails(String userId) {
        Wallets wallet = walletsRepo.findById(userId).orElseThrow(
                () -> new CustomException.EntityNotFoundException("Your wallet not found. Contact support team."));
        GetWalletDetailsResponse response = new GetWalletDetailsResponse(wallet);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<MessageResponse> createTotp(String userId) throws QrGenerationException {
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        String secret = secretGenerator.generate();
        Wallets wallet = walletsRepo.findById(userId).orElseThrow(
                ()->new CustomException.EntityNotFoundException("Your wallet not found. Contact support team.")
        );
        if(wallet.isTotpEnabled()){
            throw new CustomException.ValidationException("TOTP already enabled");
        }
        String encryptedSecret = Base64.getEncoder().withoutPadding().encodeToString(secret.getBytes());
        wallet.setSecretKey(encryptedSecret);
        try {
            walletsRepo.save(wallet);
        } catch (Exception e) {
            throw new CustomException.UnableToSaveException("Issue in enabling TOTP");
        }
        QrData data = new QrData.Builder()
                .label(userId)
                .secret(secret)
                .issuer("Infinitum Bank")
                .algorithm(HashingAlgorithm.SHA1)
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
                ()->new CustomException.EntityNotFoundException("Your wallet not found. Contact support team.")
        );
        if(wallet.getSecretKey()==null || wallet.getSecretKey().isBlank()){
            throw new CustomException.ValidationException("QR Code data not found");
        }
        if(wallet.isTotpEnabled()){
            throw new CustomException.ValidationException("TOTP already enabled");
        }
        byte[] decodedBytes = Base64.getDecoder().decode(wallet.getSecretKey());
        String decodedSecret = new String(decodedBytes);
        boolean successful = verifier.isValidCode(decodedSecret, code);
        if(successful){
            try {
                wallet.setTotpEnabled(true);
                walletsRepo.save(wallet);
            } catch (Exception e) {
                throw new CustomException.UnableToSaveException("Issue in enabling TOTP");
            }
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        }else{
            throw new CustomException.ValidationException("Incorrect TOTP");
        }
    }

    public ResponseEntity<MessageResponse> disableTotp(String userId) {
        Wallets wallet = walletsRepo.findById(userId).orElseThrow(
                ()->new CustomException.EntityNotFoundException("Your wallet not found. Contact support team.")
        );
        if(!wallet.isTotpEnabled()){
            throw new CustomException.ValidationException("TOTP already disabled");
        }
        wallet.setTotpEnabled(false);
        wallet.setSecretKey(null);
        try {
            walletsRepo.save(wallet);
        } catch (Exception e) {
            throw new CustomException.UnableToSaveException("Issue in disabling TOTP");
        }
        return new ResponseEntity<>(new MessageResponse("TOTP disabled"), HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<GetStatementResponse> getStatement(String userId, int pageNo) {
        List<Transactions> fromTransactions = transactionsRepo.findAllByFromUIdIgnoreCase(userId);
        List<Transactions> toTransactions = transactionsRepo.findAllByToUIdIgnoreCase(userId);
        List<Recharges> recharges = rechargesRepo.findAllByuIdIgnoreCase(userId);
        GetStatementResponse response = new GetStatementResponse( fromTransactions, toTransactions, recharges, pageNo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<GetCashbackResponse> getCashback(String userId, Pageable pageable) {
        long noOfDocuments= rechargesRepo.countByuId(userId);
        if(pageable.getOffset() > noOfDocuments){
            throw new CustomException.ValidationException("Invalid page request");
        }
        List<Recharges> recharges = rechargesRepo.findAllByuIdIgnoreCaseOrderByCreatedDesc(userId, pageable);
        GetCashbackResponse response = new GetCashbackResponse(recharges, noOfDocuments);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
