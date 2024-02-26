package com.nextuple.pranay.fullstack.dto;

import com.nextuple.pranay.fullstack.model.Transactions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitTransactionResponse {
    private String transactionId;
    private String from;
    private String to;
    private double amount;
    private LocalDateTime created;

    public void copyTransactionId(Transactions transaction) {
        this.transactionId = transaction.getId();
        this.from = transaction.getFromUId();
        this.to = transaction.getToUId();
        this.amount = transaction.getAmount();
        this.created = transaction.getCreated();
    }
}
