package com.nextuple.pranay.fullstack.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Document(collection = "users")
public class Users {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private String roles;
    private LocalDateTime created;

    public Users(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles="USER";
        this.created = LocalDateTime.now();
    }
}
