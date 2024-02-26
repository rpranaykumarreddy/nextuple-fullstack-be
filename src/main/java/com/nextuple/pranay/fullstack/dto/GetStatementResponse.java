package com.nextuple.pranay.fullstack.dto;

import com.nextuple.pranay.fullstack.model.Recharges;
import com.nextuple.pranay.fullstack.model.Transactions;
import com.nextuple.pranay.fullstack.model.Wallets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetStatementResponse {
    LocalDateTime responseTime = LocalDateTime.now();
    List<?> statements = new ArrayList<>();
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreditDetails {
        private StatementType type = StatementType.CREDIT;
        private String id;
        private String fromUsername;
        private double amount;
        private Transactions.TransactionStatus status;
        private String message;
        private LocalDateTime created;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RechargesDetails {
        private StatementType type = StatementType.RECHARGE;
        private String id;
        private double amount;
        private double cashback;
        private String walletId;
        private LocalDateTime created;
    }
    public enum StatementType {
        RECHARGE, CREDIT, DEBIT
    }
}
