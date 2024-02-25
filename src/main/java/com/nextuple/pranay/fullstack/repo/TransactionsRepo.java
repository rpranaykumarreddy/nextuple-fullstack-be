package com.nextuple.pranay.fullstack.repo;

import com.nextuple.pranay.fullstack.model.Transactions;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionsRepo extends MongoRepository<Transactions, String> {
}
