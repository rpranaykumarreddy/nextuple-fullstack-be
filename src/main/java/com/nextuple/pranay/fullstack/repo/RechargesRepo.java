package com.nextuple.pranay.fullstack.repo;

import com.nextuple.pranay.fullstack.model.Recharges;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface RechargesRepo extends MongoRepository<Recharges, String> {
    List<Recharges> findAllByuId(String userId);
}