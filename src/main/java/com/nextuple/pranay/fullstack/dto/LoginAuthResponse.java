package com.nextuple.pranay.fullstack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginAuthResponse {
    private String accessToken;
    private String tokenType="Bearer";
}
