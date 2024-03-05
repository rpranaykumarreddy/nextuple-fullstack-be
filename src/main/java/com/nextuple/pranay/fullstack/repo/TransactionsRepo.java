package com.nextuple.pranay.fullstack.repo;

import com.nextuple.pranay.fullstack.model.Transactions;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionsRepo extends MongoRepository<Transactions, String> {
    List<Transactions> findAllByFromUIdIgnoreCase(String userId);

    List<Transactions> findAllByToUIdIgnoreCase(String userId);
}
