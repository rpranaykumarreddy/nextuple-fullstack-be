package com.nextuple.pranay.fullstack.repo;

import com.nextuple.pranay.fullstack.model.Wallets;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WalletsRepo extends MongoRepository<Wallets, String> {
}
