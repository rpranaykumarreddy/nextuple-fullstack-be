package com.nextuple.pranay.fullstack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitTeansactionRequest {
    private String fromWalletId;
    private String toWalletId;
    private double amount;
}
