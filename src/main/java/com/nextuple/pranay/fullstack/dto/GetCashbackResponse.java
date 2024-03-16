package com.nextuple.pranay.fullstack.dto;

import com.nextuple.pranay.fullstack.model.Recharges;
import com.nextuple.pranay.fullstack.utils.Globals;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class GetCashbackResponse {
    int totalPages = 0;
    LocalDateTime responseTime = LocalDateTime.now();
    List<RechargesDetails> recharges = new ArrayList<>();
    @Data
    public static class RechargesDetails {
        private String id;
        private double amount;
        private double cashback;
        private LocalDateTime created;

        public static RechargesDetails copyRecharge(Recharges recharge) {
            RechargesDetails rechargesDetails = new RechargesDetails();
            rechargesDetails.setId(recharge.getId());
            rechargesDetails.setAmount(recharge.getAmount());
            rechargesDetails.setCashback(recharge.getCashback());
            rechargesDetails.setCreated(recharge.getCreated());
            return rechargesDetails;
        }
    }

    public GetCashbackResponse(List<Recharges> recharges, long noOfDocuments) {
        this.totalPages = (int) Math.ceil((double) noOfDocuments / Globals.pageSize);
        List<RechargesDetails> rechargesDetails = new ArrayList<>();
        recharges.forEach(recharge -> rechargesDetails.add(RechargesDetails.copyRecharge(recharge)));
        rechargesDetails.sort((o1, o2) -> o2.getCreated().compareTo(o1.getCreated()));
        this.recharges = rechargesDetails;
    }
}
