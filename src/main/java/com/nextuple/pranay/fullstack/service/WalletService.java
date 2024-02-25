package com.nextuple.pranay.fullstack.service;


import com.nextuple.pranay.fullstack.dto.CheckBalanceResponse;
import com.nextuple.pranay.fullstack.dto.CreateWalletResponse;
import com.nextuple.pranay.fullstack.model.Wallets;
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

import java.time.LocalDateTime;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
public class WalletService {
    @Autowired
    private WalletsRepo walletsRepo;

    public ResponseEntity<CreateWalletResponse> createWallet(String userId) {
        if(walletsRepo.existsByUserId(userId)){
            throw new RuntimeException("Wallet already exists");
        }
        Wallets wallet = new Wallets();
        wallet.setBalance(0.0);
        wallet.setUserId(userId);
        wallet.setCreated(LocalDateTime.now());
        wallet.setUpdated(LocalDateTime.now());
        try {
            wallet = walletsRepo.save(wallet);
        } catch (Exception e) {
            throw new RuntimeException("Unable to create wallet");
        }
        return new ResponseEntity<>(new CreateWalletResponse("Wallet Created Successfully", wallet.getId()), HttpStatus.CREATED);
    }

    public ResponseEntity<CheckBalanceResponse> checkBalance(String userId) {
        Wallets wallet = walletsRepo.findByUserId(userId).orElseThrow(
                () -> new RuntimeException("Wallet not found")
        );
        return new ResponseEntity<>(new CheckBalanceResponse("Wallet Balance", wallet.getBalance(), wallet.getId()), HttpStatus.OK);
    }

    public ResponseEntity<?> createTotp(String walletId, String userId) throws QrGenerationException {
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        String secret = secretGenerator.generate();
        Wallets wallet = walletsRepo.findById(walletId).orElseThrow(
                () -> new RuntimeException("Wallet not found")
        );
        if(!wallet.getUserId().equals(userId)){
            throw new RuntimeException("Unauthorized");
        }
        if(!StringUtils.isEmpty(wallet.getSecretKey())){
            throw new RuntimeException("Secret already exists");
        }
        wallet.setSecretKey(secret);
        try {
            walletsRepo.save(wallet);
        } catch (Exception e) {
            throw new RuntimeException("Unable to save secret");
        }
        QrData data = new QrData.Builder()
                .label("example@example.com")
                .secret(secret)
                .issuer("AppName")
                .algorithm(HashingAlgorithm.SHA1) // More on this below
                .digits(6)
                .period(30)
                .build();
        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData = generator.generate(data);
        String mimeType = generator.getImageMimeType();
        String dataUri = getDataUriForImage(imageData, mimeType);
        return new ResponseEntity<>(dataUri, HttpStatus.OK);
    }

    public ResponseEntity<?> confirmTotp(String walletId, String userId, String code) {
        Wallets wallet = walletsRepo.findById(walletId).orElseThrow(
                () -> new RuntimeException("Wallet not found")
        );
        if(!wallet.getUserId().equals(userId)){
            throw new RuntimeException("Unauthorized");
        }
        if(StringUtils.isEmpty(wallet.getSecretKey())){
            throw new RuntimeException("Secret not found");
        }
        String secret = wallet.getSecretKey();
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        boolean successful = verifier.isValidCode(secret, code);
        return new ResponseEntity<>(successful, HttpStatus.OK);
    }
}
