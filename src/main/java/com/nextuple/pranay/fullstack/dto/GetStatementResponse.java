package com.nextuple.pranay.fullstack.dto;

import com.nextuple.pranay.fullstack.exception.CustomException;
import com.nextuple.pranay.fullstack.model.Recharges;
import com.nextuple.pranay.fullstack.model.Transactions;
import com.nextuple.pranay.fullstack.model.Wallets;
import com.nextuple.pranay.fullstack.utils.Globals;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class GetStatementResponse {
    int totalPages = 0;
    LocalDateTime responseTime = LocalDateTime.now();
    List<StatementDetails> statements = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatementDetails {
        private String id;
        private String type;
        private String status;
        private String fromTo;
        private double amount;
        private LocalDateTime createdAt;
        public static String  statusToString(Transactions.TransactionStatus status){
            if(status == Transactions.TransactionStatus.INIT){
                return "Initiated";
            }else if(status == Transactions.TransactionStatus.SUCCESSFUL){
                return "Successful";
            }else if(status == Transactions.TransactionStatus.CANCELLED){
                return "Cancelled";
            }else if(status == Transactions.TransactionStatus.TIMEOUT){
                return "Timeout";
            }
            return "";
        }
        public static StatementDetails copyRecharge(Recharges recharge) {
            StatementDetails statementDetails = new StatementDetails();
            statementDetails.setId(recharge.getId());
            statementDetails.setType("Recharge");
            statementDetails.setStatus("Successful");
            statementDetails.setFromTo("Self");
            statementDetails.setAmount(recharge.getAmount());
            statementDetails.setCreatedAt(recharge.getCreated());
            return statementDetails;
        }

        public static StatementDetails copyFromTransactions(Transactions transactions) {
            StatementDetails statementDetails = new StatementDetails();
            statementDetails.setId(transactions.getId());
            statementDetails.setType("Debit");
            statementDetails.setStatus(statusToString(transactions.getStatus()));
            statementDetails.setFromTo(transactions.getToUId());
            statementDetails.setAmount(transactions.getAmount());
            statementDetails.setCreatedAt(transactions.getCreated());
            return statementDetails;
        }

        public static StatementDetails copyToTransactions(Transactions transactions) {
            StatementDetails statementDetails = new StatementDetails();
            statementDetails.setId(transactions.getId());
            statementDetails.setType("Credit");
            statementDetails.setStatus(statusToString(transactions.getStatus()));
            statementDetails.setFromTo(transactions.getFromUId());
            statementDetails.setAmount(transactions.getAmount());
            statementDetails.setCreatedAt(transactions.getCreated());
            return statementDetails;
        }
    }

    public GetStatementResponse( List<Transactions> fromTransactions, List<Transactions> toTransactions, List<Recharges> recharges, int pageNo) {
        long noOfDocuments = fromTransactions.size()+ toTransactions.size()+ recharges.size();
        this.totalPages = (int) Math.ceil((double) noOfDocuments / Globals.pageSize);
        if(pageNo  >= totalPages){
            throw new CustomException.ValidationException("Invalid page request");}
        List<StatementDetails> statementDetails = new ArrayList<>();
        fromTransactions.forEach(transaction -> statementDetails.add(StatementDetails.copyFromTransactions(transaction)));
        toTransactions.forEach(transaction -> statementDetails.add(StatementDetails.copyToTransactions(transaction)));
        recharges.forEach(recharge -> statementDetails.add(StatementDetails.copyRecharge(recharge)));
        statementDetails.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));
        int startIndex = (pageNo) * Globals.pageSize;
        int endIndex = Math.min(startIndex+Globals.pageSize,statementDetails.size());
        this.statements = statementDetails.subList(startIndex,endIndex);
    }
}
