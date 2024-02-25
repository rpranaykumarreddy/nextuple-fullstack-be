package com.nextuple.pranay.fullstack.dto;

import com.nextuple.pranay.fullstack.exception.CustomException;
import com.nextuple.pranay.fullstack.model.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddUserRequest {
    private String username;
    private String email;
    private String password;

    public void validate() {
        if (username == null || username.isEmpty()) {
            throw new CustomException.ValidationException("Username cannot be empty");
        }
        if (email == null || email.isEmpty()) {
            throw new CustomException.ValidationException("Email cannot be empty");
        }
        if(email == null || !email.contains("@") || !email.contains(".") || email.indexOf("@") > email.lastIndexOf(".")){
            throw new CustomException.ValidationException("Invalid Email");
        }
        if (password == null || password.isEmpty()) {
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
