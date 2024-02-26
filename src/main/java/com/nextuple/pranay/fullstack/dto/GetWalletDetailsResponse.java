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
    LocalDateTime responseTime = LocalDateTime.now(); private String id;
    private double balance;
    private LocalDateTime updated;
    private LocalDateTime created;

    public void copyWallets(Wallets wallets) {
        this.id = wallets.getUsername();
        this.balance = wallets.getBalance();
        this.updated = wallets.getUpdated();
        this.created = wallets.getCreated();
    }
}
