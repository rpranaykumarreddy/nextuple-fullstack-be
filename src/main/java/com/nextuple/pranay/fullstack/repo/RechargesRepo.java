package com.nextuple.pranay.fullstack.repo;

import com.nextuple.pranay.fullstack.model.Recharges;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface RechargesRepo extends MongoRepository<Recharges, String> {
    List<Recharges> findAllByuIdIgnoreCase(String userId);
    List<Recharges> findAllByuIdIgnoreCaseAndCreatedBetween(String userId, LocalDateTime startOfMonth, LocalDateTime endOfMonth);
}