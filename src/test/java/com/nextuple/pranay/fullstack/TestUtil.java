package com.nextuple.pranay.fullstack;

import com.nextuple.pranay.fullstack.dto.*;
import com.nextuple.pranay.fullstack.model.Recharges;
import com.nextuple.pranay.fullstack.model.Transactions;
import com.nextuple.pranay.fullstack.model.Users;
import com.nextuple.pranay.fullstack.model.Wallets;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TestUtil {

    public static final String USER1_NAME = "user1";
    public static final String USER2_NAME = "user2";
    public static final String TOKEN = "Bearer token";

    public static class UserTestData {
        public static final String USER1_EMAIL = "user1@gmail.com";
        public static final String USER1_PASSWORD = "password1";

        public static Users getUser1Response() {
            return new Users(USER1_NAME, USER1_EMAIL, USER1_PASSWORD, "ROLE_USER", LocalDateTime.now());
        }

        public static AddUserRequest getUser1Request() {
            return new AddUserRequest(USER1_NAME, USER1_EMAIL, USER1_PASSWORD);
        }

        public static LoginAuthRequest getLoginAuthRequest() {
            return new LoginAuthRequest(USER1_NAME, USER1_PASSWORD);
        }
    }
    public static class WalletTestData{
        public static final String TOTP_CODE = "123456";
        public static double USER1_BALANCE_INIT = 0.0;
        public static LocalDateTime USER1_UPDATED = LocalDateTime.now();
        public static LocalDateTime USER1_CREATED = LocalDateTime.now();
        public static Wallets getWallet1Response() {
            return new Wallets(USER1_NAME, USER1_BALANCE_INIT, null, false, USER1_UPDATED, USER1_CREATED);
        }
        public static Wallets getWallet1ResponseTotpToBeEnabled() {
            return new Wallets(USER1_NAME, USER1_BALANCE_INIT, "secret",false, USER1_UPDATED, USER1_CREATED);
        }
        public static Wallets getWallet1ResponseTotpEnabled() {
            return new Wallets(USER1_NAME, USER1_BALANCE_INIT, "secretKey", true, USER1_UPDATED, USER1_CREATED);
        }
        public static Wallets getRechargedWallet1() {
            return new Wallets(USER1_NAME, 30000.3, null, false, USER1_UPDATED, USER1_CREATED);
        }
        public static GetWalletDetailsResponse getWallet1ResponseDto() {
            return new GetWalletDetailsResponse(getRechargedWallet1());
        }
        public static Wallets getRechargedWallet1WithTotp() {
            return new Wallets(USER1_NAME, 30000.3, "secretKey", true, USER1_UPDATED, USER1_CREATED);
        }

        public static Wallets getRechargedWallet2() {
            return new Wallets(USER2_NAME, 20000.2, null, false, USER1_UPDATED, USER1_CREATED);
        }

    }

    public static class TransactionTestData{
        public static String TRANSACTION_ID = "65dcf0f339a6f47d46fe2f07";
        public static String TRANSACTION_ID2 = "65dcf06a39a6f47d46fe2f05";
        public static String TRANSACTION_ID3 = "65dcf0a539a6f47d46fe2f06";
        public static double TRANSACTION1_AMOUNT = 20000.0;
        public static double TRANSACTION2_AMOUNT = 30000.0;
        public static double TRANSACTION3_AMOUNT = 40000.0;

        public static Transactions.TransactionStatus TRANSACTION1_STATUS = Transactions.TransactionStatus.CANCELLED;
        public static Transactions.TransactionStatus TRANSACTION2_STATUS = Transactions.TransactionStatus.SUCCESSFUL;
        public static Transactions.TransactionStatus TRANSACTION3_STATUS = Transactions.TransactionStatus.SUCCESSFUL;

        public static LocalDateTime TRANSACTION1_CREATED = LocalDateTime.parse("2024-02-27T01:43:39.655");
        public static LocalDateTime TRANSACTION2_CREATED = LocalDateTime.parse("2024-02-27T01:41:22.79");
        public static LocalDateTime TRANSACTION3_CREATED = LocalDateTime.parse("2024-02-27T01:42:21.873");
        public static InitTransactionRequest getInitTransactionRequest1() {
            return new InitTransactionRequest(USER2_NAME, TRANSACTION1_AMOUNT);
        }
        public static InitTransactionRequest getInitTransactionRequest2() {
            return new InitTransactionRequest("  ", TRANSACTION1_AMOUNT);
        }
        public static InitTransactionRequest getInitTransactionRequest3() {
            return new InitTransactionRequest(USER2_NAME, 0);
        }
        public static InitTransactionRequest getInitTransactionRequest4() {
            return new InitTransactionRequest(USER2_NAME, 1_00_00_001);
        }
        public static InitTransactionResponse getInitTransactionResponse() {
            return new InitTransactionResponse(TRANSACTION_ID, USER1_NAME, USER2_NAME, TRANSACTION1_AMOUNT, TRANSACTION1_CREATED.plusMinutes(1));
        }
        public static Transactions getTransaction1_InitStatus() {
            return new Transactions(TRANSACTION_ID, USER1_NAME,USER2_NAME, TRANSACTION1_AMOUNT, Transactions.TransactionStatus.INIT, TRANSACTION1_CREATED);
        }
        public static Transactions getTransaction1_InitStatus_TimeNow() {
            return new Transactions(TRANSACTION_ID, USER1_NAME,USER2_NAME, TRANSACTION1_AMOUNT, Transactions.TransactionStatus.INIT, LocalDateTime.now());
        }
        public static Transactions getTransaction1() {
            return new Transactions(TRANSACTION_ID, USER1_NAME,USER2_NAME, TRANSACTION1_AMOUNT, TRANSACTION1_STATUS, TRANSACTION1_CREATED);
        }
        public static Transactions getTransaction2() {
            return new Transactions(TRANSACTION_ID2, USER1_NAME,USER2_NAME, TRANSACTION2_AMOUNT, TRANSACTION2_STATUS, TRANSACTION2_CREATED);
        }
        public static Transactions getTransaction3() {
            return new Transactions(TRANSACTION_ID3, USER2_NAME,USER1_NAME, TRANSACTION3_AMOUNT, TRANSACTION3_STATUS, TRANSACTION3_CREATED);
        }

        public static List<Transactions> getFromTransactions() {
            return Arrays.asList(
                    getTransaction1(),
                    getTransaction2()
            );
        }

        public static List<Transactions> getToTransactions() {
            return List.of(
                    getTransaction3()
            );
        }

        public static InitTransactionRequest getInitTransactionRequest_InvalidAmount() {
            return new InitTransactionRequest(USER2_NAME, -1);
        }

        public static InitTransactionRequest getInitTransactionRequest_InvalidUsername() {
            return new InitTransactionRequest(USER1_NAME, TRANSACTION1_AMOUNT);
        }

    }
    public static class RechargeTestData{
        public static String RECHARGE_ID = "65dcf02d39a6f47d46fe2f03";
        public static double RECHARGE_AMOUNT = 30000.3;
        public static double RECHARGE_CASHBACK = 28.0;
        public static LocalDateTime RECHARGE_CREATED = LocalDateTime.parse("2024-02-27T01:40:21.307");
        public static Recharges getRecharge() {
            return new Recharges(RECHARGE_ID, RECHARGE_AMOUNT, RECHARGE_CASHBACK, USER1_NAME, RECHARGE_CREATED);
        }

        public static List<Recharges> getRecharges() {
            return List.of(
                    getRecharge()
            );
        }
    }
    public static class StatementTestData{
        public static GetStatementResponse needStatementResponse() {
            return new GetStatementResponse(
                    WalletTestData.getWallet1Response(),
                    TransactionTestData.getFromTransactions(),
                    TransactionTestData.getToTransactions(),
                    RechargeTestData.getRecharges());
        }
    }
    public static class CashbackTestData{
        public static GetCashbackResponse needCashbackResponse() {
            return new GetCashbackResponse(RechargeTestData.getRecharges());
        }
    }
        /*
        Statement User1:
        {
            "responseTime": "2024-02-27T01:48:28.6289509",
            "credits": [
                {
                    "type": "CREDIT",
                    "id": "65dcf0a539a6f47d46fe2f06",
                    "from": "user2",
                    "amount": 40000.0,
                    "status": "SUCCESSFUL",
                    "created": "2024-02-27T01:42:21.873"
                }
            ],
            "debits": [
                {
                    "type": "DEBIT",
                    "id": "65dcf0f339a6f47d46fe2f07",
                    "to": "user2",
                    "amount": 20000.0,
                    "status": "CANCELLED",
                    "created": "2024-02-27T01:43:39.655"
                },
                {
                    "type": "DEBIT",
                    "id": "65dcf06a39a6f47d46fe2f05",
                    "to": "user2",
                    "amount": 30000.0,
                    "status": "SUCCESSFUL",
                    "created": "2024-02-27T01:41:22.79"
                }
            ],
            "recharges": [
                {
                    "type": "RECHARGE",
                    "id": "65dcf02d39a6f47d46fe2f03",
                    "amount": 30000.3,
                    "cashback": 28.0,
                    "created": "2024-02-27T01:40:21.307"
                }
            ]
        }
         */

}