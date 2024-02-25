package com.nextuple.pranay.fullstack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RechargeWalletResponse {
        private String message;
        private double balance;
}