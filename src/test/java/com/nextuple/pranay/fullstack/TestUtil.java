package com.nextuple.pranay.fullstack;

import com.nextuple.pranay.fullstack.dto.AddUserRequest;
import com.nextuple.pranay.fullstack.dto.GetStatementResponse;
import com.nextuple.pranay.fullstack.dto.LoginAuthRequest;
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
    public static class UserTestData {
        public static final String USER1_EMAIL = "user1@gmail.com";
        public static final String USER2_EMAIL = "user2@gmail.com";
        public static final String USER1_PASSWORD = "password1";
        public static final String USER2_PASSWORD = "password2";

        public static Users getUser1Response() {
            return new Users(USER1_NAME, USER1_EMAIL, USER1_PASSWORD, "ROLE_USER", null);
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
    }
    public static class TransactionTestData{

        public static List<Transactions> getFromTransactions() {
            return Arrays.asList(
                    new Transactions("65dcf0f339a6f47d46fe2f07", USER1_NAME,USER2_NAME, 20000.0, Transactions.TransactionStatus.CANCELLED, LocalDateTime.parse("2024-02-27T01:43:39.655")),
                    new Transactions("65dcf06a39a6f47d46fe2f05", USER1_NAME,USER2_NAME, 30000.0, Transactions.TransactionStatus.SUCCESSFUL,  LocalDateTime.parse("2024-02-27T01:41:22.79"))
            );
        }

        public static List<Transactions> getToTransactions() {
            return Arrays.asList(
                    new Transactions("65dcf0a539a6f47d46fe2f06", USER2_NAME,USER1_NAME, 40000.0, Transactions.TransactionStatus.SUCCESSFUL, LocalDateTime.parse("2024-02-27T01:42:21.873"))
            );
        }
    }
    public static class RechargeTestData{
        public static List<Recharges> getRecharges() {
            return Arrays.asList(
                    new Recharges("65dcf02d39a6f47d46fe2f03", 30000.3, 28.0,USER1_NAME, LocalDateTime.parse("2024-02-27T01:40:21.307"))
            );
        }
    }
    public static class StatementTestData{
        public static GetStatementResponse needStatementResponse() {
            GetStatementResponse response = new GetStatementResponse(
                    WalletTestData.getWallet1Response(),
                    TransactionTestData.getFromTransactions(),
                    TransactionTestData.getToTransactions(),
                    RechargeTestData.getRecharges());
            return response;
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