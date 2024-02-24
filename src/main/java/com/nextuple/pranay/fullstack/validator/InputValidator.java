package com.nextuple.pranay.fullstack.validator;

import com.nextuple.pranay.fullstack.exception.CustomException;

public class InputValidator {
    public static void IdValidator(String id){
        if(id==null || id.isBlank()){
            throw new CustomException.ValidationException("Id cannot be empty");
        }
    }
}
