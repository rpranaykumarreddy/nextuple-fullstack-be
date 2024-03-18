package com.nextuple.pranay.fullstack.utils;

public class Globals {
    public static final int pageSize = 5;
    public static final int transactionTimeoutMins = 1;
    public static final String authControllerMap = "/auth";
    public static final String authController_addNewUserMap = "/register";
    public static final String authController_loginMap = "/login";
    public static final String authController_regenerateMap = "/regenerate";
    public static final String authController_checkUsernameMap = "/check-username/{username}";
    public static final String rechargeController_rechargeWalletMap = "/recharge";
    public static final String transactionControllerMap = "/transaction";
    public static final String transactionController_checkWalletMap = "/check-wallet/{username}";
    public static final String transactionController_initTransactionMap = "/init";
    public static final String transactionController_confirmTransactionMap = "/confirm/{transactionId}";
    public static final String transactionController_cancelTransactionMap = "/cancel/{transactionId}";
    public static final String walletControllerMap = "/wallet";
    public static final String walletController_getWalletDetailsMap = "/details";
    public static final String walletController_createTotpMap = "/totp";
    public static final String walletController_confirmTotpMap = "/totp/confirm";
    public static final String walletController_getStatementMap = "/statement";
    public static final String walletController_getCashbackMap = "/cashback";
}
