package com.nextuple.pranay.fullstack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmTeansactionRequest {
    private String fromWalletId;
    private String transactionId;
    private String code;
}
