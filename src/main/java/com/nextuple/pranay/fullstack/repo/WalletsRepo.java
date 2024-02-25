package com.nextuple.pranay.fullstack.repo;

import com.nextuple.pranay.fullstack.model.Wallets;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface WalletsRepo extends MongoRepository<Wallets, String> {
    boolean existsByUserId(String id);

    Optional<Wallets> findByUserId(String userId);
}
