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
    /*
    Question-1: I am thinking of send a info of init transaction & also create the record of the transaction
    with the multiple enumerations like( FOUND, CONFIRMED, DEBITED, SUCCESSFUL, CANCELLED, TIMEOUT, ERROR).
    I want to update the transcastion (automatically or running a side effect in the backend) to timeout
    on not verified by otp & also redebit the amount if any error.
    Should I code multiple service functions for it or use the @Transactionals right from the confirmed to end.
    Where front-end itself won't send any confirmation OTP request & back-end also checks for the timestamp of created
    */
    @Id
    private String id;
    private String fromWalletId;
    private String toWalletId;
    private double amount;
    private TransactionStatus status;
    private String message;
    private LocalDateTime created;

    public enum TransactionStatus {
        INIT, CONFIRMED, DEBITED, SUCCESSFUL, CANCELLED, TIMEOUT, ERROR
    }
}
