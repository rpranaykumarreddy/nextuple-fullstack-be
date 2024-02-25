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
@Document(collection = "recharges")
public class Recharges {
    @Id
    private String id;
    private double amount;
    private double cashback;
    private String walletId;
    private LocalDateTime created;
}
