package com.nextuple.pranay.fullstack.dto;

import com.nextuple.pranay.fullstack.exception.CustomException;
import com.nextuple.pranay.fullstack.utils.Globals;
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
        if (amount < 1) {
            throw new CustomException.ValidationException("Amount should not be less than ₹1");
        }
        if (amount > Globals.transactionLimit){
            throw new CustomException.ValidationException(Globals.transactionLimitValidationError);
        }
    }
}
