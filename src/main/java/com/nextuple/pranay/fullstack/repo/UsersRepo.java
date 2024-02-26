package com.nextuple.pranay.fullstack.repo;

import com.nextuple.pranay.fullstack.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsersRepo extends MongoRepository<Users, String> {
    Optional<Users> findByUsernameOrEmail(String username,String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}