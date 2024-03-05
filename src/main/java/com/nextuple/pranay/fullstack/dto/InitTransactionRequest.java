package com.nextuple.pranay.fullstack.dto;

import com.nextuple.pranay.fullstack.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitTransactionRequest {
    private String to;
    private double amount;

    public void validate() {
        setTo(to.toLowerCase());
        if (to == null || to.isEmpty()) {
            throw new CustomException.ValidationException("To address cannot be empty");
        }
        if (amount <= 0) {
            throw new CustomException.ValidationException("Amount should be greater than 0");
        }
    }
}
