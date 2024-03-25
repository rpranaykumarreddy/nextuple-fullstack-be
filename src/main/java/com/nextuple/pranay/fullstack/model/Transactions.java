package com.nextuple.pranay.fullstack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class Transactions {
    @Id
    private String id;
    private String fromUId;
    private String toUId;
    private double amount;
    private TransactionStatus status;
    private LocalDateTime created;
    private boolean TOTPVerified = false;

    public enum TransactionStatus {
        INIT, SUCCESSFUL, CANCELLED, TIMEOUT
    }
}
