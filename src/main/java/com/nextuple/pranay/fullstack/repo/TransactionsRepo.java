package com.nextuple.pranay.fullstack.repo;

import com.nextuple.pranay.fullstack.model.Transactions;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionsRepo extends MongoRepository<Transactions, String> {
    List<Transactions> findAllByFromUId(String userId);

    List<Transactions> findAllByToUId(String userId);
}
