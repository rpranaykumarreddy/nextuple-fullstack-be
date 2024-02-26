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
@Document(collection = "users")
public class Users {
    @Id
    private String username;
    private String email;
    private String password;
    private String roles;
    private LocalDateTime created;
}
