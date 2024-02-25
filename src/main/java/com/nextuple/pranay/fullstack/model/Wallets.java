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
@Document(collection = "wallets")
public class Wallets {
    @Id
    private String id;
    private double balance;
    private String userId;
    private String secretKey;
    private LocalDateTime updated;
    private LocalDateTime created;
}
