package com.nextuple.pranay.fullstack.dto;

import com.nextuple.pranay.fullstack.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginAuthRequest {
    private String username;
    private String password;

    public void validate() {
        setUsername(username.toLowerCase());
        if (username == null || username.isBlank()) {
            throw new CustomException.ValidationException("Username or Email cannot be empty");
        }
        if (password == null || password.isBlank()) {
            throw new CustomException.ValidationException("Password cannot be empty");
        }
    }
}
