package com.nextuple.pranay.fullstack.dto;

import com.nextuple.pranay.fullstack.model.Recharges;
import com.nextuple.pranay.fullstack.model.Transactions;
import com.nextuple.pranay.fullstack.model.Wallets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetStatementResponse {
    LocalDateTime responseTime = LocalDateTime.now();
    GetWalletDetailsResponse wallet = new GetWalletDetailsResponse();
    List<CreditDetails> credits= new ArrayList<>();
    List<DebitDetails> debits= new ArrayList<>();
    List<RechargesDetails> recharges= new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreditDetails {
        private StatementType type = StatementType.CREDIT;
        private String id;
        private String from;
        private double amount;
        private Transactions.TransactionStatus status;
        private LocalDateTime created;

        public static CreditDetails copyTransaction(Transactions transaction) {
            CreditDetails creditDetails = new CreditDetails();
            creditDetails.setId(transaction.getId());
            creditDetails.setFrom(transaction.getFromUId());
            creditDetails.setAmount(transaction.getAmount());
            creditDetails.setStatus(transaction.getStatus());
            creditDetails.setCreated(transaction.getCreated());
            return creditDetails;
        }
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DebitDetails {
        private StatementType type = StatementType.DEBIT;
        private String id;
        private String to;
        private double amount;
        private Transactions.TransactionStatus status;
        private LocalDateTime created;

        public static DebitDetails copyTransaction(Transactions transaction) {
            DebitDetails debitDetails = new DebitDetails();
            debitDetails.setId(transaction.getId());
            debitDetails.setTo(transaction.getToUId());
            debitDetails.setAmount(transaction.getAmount());
            debitDetails.setStatus(transaction.getStatus());
            debitDetails.setCreated(transaction.getCreated());
            return debitDetails;
        }
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RechargesDetails {
        private StatementType type = StatementType.RECHARGE;
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
    public enum StatementType {
        RECHARGE, CREDIT, DEBIT
    }

    public GetStatementResponse(Wallets wallet, List<Transactions> fromTransactions, List<Transactions> toTransactions, List<Recharges> recharges) {
        this.wallet = new GetWalletDetailsResponse(wallet);
        List<CreditDetails> credits = new ArrayList<>();
        List<DebitDetails> debits = new ArrayList<>();
        List<RechargesDetails> rechargesDetails = new ArrayList<>();
        fromTransactions.forEach(transaction -> debits.add(DebitDetails.copyTransaction(transaction)));
        toTransactions.forEach(transaction -> credits.add(CreditDetails.copyTransaction(transaction)));
        recharges.forEach(recharge -> rechargesDetails.add(RechargesDetails.copyRecharge(recharge)));
        credits.sort((o1, o2) -> o2.getCreated().compareTo(o1.getCreated()));
        debits.sort((o1, o2) -> o2.getCreated().compareTo(o1.getCreated()));
        rechargesDetails.sort((o1, o2) -> o2.getCreated().compareTo(o1.getCreated()));
        this.credits = credits;
        this.debits = debits;
        this.recharges = rechargesDetails;
    }
}
