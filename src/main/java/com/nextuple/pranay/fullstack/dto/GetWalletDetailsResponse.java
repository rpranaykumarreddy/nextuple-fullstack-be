package com.nextuple.pranay.fullstack.dto;

import com.nextuple.pranay.fullstack.model.Wallets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetWalletDetailsResponse {
    LocalDateTime responseTime = LocalDateTime.now();
    private double balance;
    private boolean totpEnabled;
    private LocalDateTime updated;
    private LocalDateTime created;

    public GetWalletDetailsResponse(Wallets wallets) {
        this.balance = wallets.getBalance();
        this.totpEnabled = wallets.isTotpEnabled();
        this.updated = wallets.getUpdated();
        this.created = wallets.getCreated();
    }
}
