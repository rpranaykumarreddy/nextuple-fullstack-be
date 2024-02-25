package com.nextuple.pranay.fullstack.repo;

import com.nextuple.pranay.fullstack.model.Recharges;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface RechargesRepo extends MongoRepository<Recharges, String> {
}