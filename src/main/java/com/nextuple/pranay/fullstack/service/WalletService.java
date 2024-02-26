package com.nextuple.pranay.fullstack.service;


import com.nextuple.pranay.fullstack.dto.GetWalletDetailsResponse;
import com.nextuple.pranay.fullstack.exception.CustomException;
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
import org.springframework.util.StringUtils;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
public class WalletService {
    @Autowired
    private WalletsRepo walletsRepo;
    @Autowired
    private TransactionsRepo transactionsRepo;
    @Autowired
    private RechargesRepo rechargesRepo;

    public ResponseEntity<GetWalletDetailsResponse> getWalletDetails(String userId) {
        Wallets wallet = walletsRepo.findById(userId).orElseThrow(() -> new CustomException.EntityNotFoundException("Wallet not found"));
        GetWalletDetailsResponse response = new GetWalletDetailsResponse();
        response.copyWallets(wallet);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> createTotp(String userId) throws QrGenerationException {
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
//        String dataUri = getDataUriForImage(imageData, mimeType);
        String dataUri ="<img src='"+getDataUriForImage(imageData, mimeType)+"' alt='Need QR' width='200' height='200'>";
        return new ResponseEntity<>(dataUri, HttpStatus.CREATED);
    }

    public ResponseEntity<?> confirmTotp(String userId, String code) {
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
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        boolean successful = verifier.isValidCode(secret, code);
        if(successful){
            try {
                wallet.setTotpEnabled(true);
                walletsRepo.save(wallet);
            } catch (Exception e) {
                throw new CustomException.UnableToSaveException("Unable to save");
            }
            return new ResponseEntity<>(successful, HttpStatus.CREATED);
        }else{
            throw new CustomException.ValidationException("Invalid code");
        }
    }
//    @Transactional
//    public ResponseEntity<GetStatementResponse> getStatement(String userId) {
//        Wallets wallet = walletsRepo.findByUserId(userId).orElseThrow(() -> new RuntimeException("Wallet not found"));
//        List<Transactions> fromTransactions = transactionsRepo.findAllByFromWalletId(wallet.getId());
//        List<Transactions> toTransactions = transactionsRepo.findAllByToWalletId(wallet.getId());
//        List<Recharges> recharges = rechargesRepo.findAllByWalletId(wallet.getId());
//        GetStatementResponse response = new GetStatementResponse();
//        response.processStatement(wallet, fromTransactions,toTransactions, recharges);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
}
