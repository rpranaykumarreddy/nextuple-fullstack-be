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
        if (to == null || to.isBlank()) {
            throw new CustomException.ValidationException("Receiver's username cannot be empty");
        }
        if (amount <= 0) {
            throw new CustomException.ValidationException("Amount should be greater than ₹0");
        }
        if (amount > 1_00_00_000){
            throw new CustomException.ValidationException("Amount should be less than ₹1,00,00,000");
        }
    }
}
