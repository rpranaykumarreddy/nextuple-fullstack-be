package com.nextuple.pranay.fullstack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RechargeWalletRequest {
        private String walletId;
        private double amount;
}