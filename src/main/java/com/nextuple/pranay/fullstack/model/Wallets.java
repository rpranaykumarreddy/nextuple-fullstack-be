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
    private String username;
    private double balance;
    private String secretKey;
    private boolean totpEnabled;
    private LocalDateTime updated;
    private LocalDateTime created;
}
