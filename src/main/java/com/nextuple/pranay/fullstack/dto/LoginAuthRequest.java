package com.nextuple.pranay.fullstack.dto;

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
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username or email cannot be empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
    }
}
