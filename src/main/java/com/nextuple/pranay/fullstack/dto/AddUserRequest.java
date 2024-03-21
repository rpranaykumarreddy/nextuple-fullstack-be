package com.nextuple.pranay.fullstack.dto;

import com.nextuple.pranay.fullstack.exception.CustomException;
import com.nextuple.pranay.fullstack.model.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddUserRequest {
    private String username;
    private String email;
    private String password;

    public void validate() {
        setUsername(username.toLowerCase());
        setEmail(email.toLowerCase());
        if (username == null || username.isBlank()) {
            throw new CustomException.ValidationException("Username cannot be empty");
        }
        if (username.length()<=3) {
            throw new CustomException.ValidationException("Username should be more than 3 characters");
        }
        if(!username.matches("^[a-z0-9]*$")){
            throw new CustomException.ValidationException("Username can only contain alphanumeric characters");
        }
        if (email == null || email.isBlank()) {
            throw new CustomException.ValidationException("Email cannot be empty");
        }
        if(!email.contains("@") || !email.contains(".") || email.indexOf("@") + 1  >= email.lastIndexOf(".") || email.lastIndexOf(".") == email.length()-1 ){
            throw new CustomException.ValidationException("Invalid Email");
        }
        if (password == null || password.isBlank()) {
            throw new CustomException.ValidationException("Password cannot be empty");
        }
        if(password.length() < 8){
            throw new CustomException.ValidationException("Password should be at least 8 characters long");
        }
    }
    public Users toUser() {
        Users user = new Users();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}
