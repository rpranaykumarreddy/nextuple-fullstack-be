package com.nextuple.pranay.fullstack.service;


import org.springframework.stereotype.Service;

@Service
public class WalletService {
//    @Autowired
//    private WalletsRepo walletsRepo;
//    @Autowired
//    private TransactionsRepo transactionsRepo;
//    @Autowired
//    private RechargesRepo rechargesRepo;
//
//    public ResponseEntity<CreateWalletResponse> createWallet(String userId) {
//        if(walletsRepo.existsByUserId(userId)){
//            throw new RuntimeException("Wallet already exists");
//        }
//        Wallets wallet = new Wallets();
//        wallet.setBalance(0.0);
//        wallet.setUserId(userId);
//        wallet.setCreated(LocalDateTime.now());
//        wallet.setUpdated(LocalDateTime.now());
//        try {
//            wallet = walletsRepo.save(wallet);
//        } catch (Exception e) {
//            throw new RuntimeException("Unable to create wallet");
//        }
//        return new ResponseEntity<>(new CreateWalletResponse("Wallet Created Successfully", wallet.getId()), HttpStatus.CREATED);
//    }
//
//    public ResponseEntity<?> createTotp(String walletId, String userId) throws QrGenerationException {
//        SecretGenerator secretGenerator = new DefaultSecretGenerator();
//        String secret = secretGenerator.generate();
//        Wallets wallet = walletsRepo.findById(walletId).orElseThrow(
//                () -> new RuntimeException("Wallet not found")
//        );
//        if(!wallet.getUserId().equals(userId)){
//            throw new RuntimeException("Unauthorized");
//        }
//        if(!StringUtils.isEmpty(wallet.getSecretKey())){
//            throw new RuntimeException("Secret already exists");
//        }
//        wallet.setSecretKey(secret);
//        try {
//            walletsRepo.save(wallet);
//        } catch (Exception e) {
//            throw new RuntimeException("Unable to save secret");
//        }
//        QrData data = new QrData.Builder()
//                .label("example@example.com")
//                .secret(secret)
//                .issuer("AppName")
//                .algorithm(HashingAlgorithm.SHA1) // More on this below
//                .digits(6)
//                .period(30)
//                .build();
//        QrGenerator generator = new ZxingPngQrGenerator();
//        byte[] imageData = generator.generate(data);
//        String mimeType = generator.getImageMimeType();
//        String dataUri = getDataUriForImage(imageData, mimeType);
//        return new ResponseEntity<>(dataUri, HttpStatus.OK);
//    }
//
//    public ResponseEntity<?> confirmTotp(String walletId, String userId, String code) {
//        Wallets wallet = walletsRepo.findById(walletId).orElseThrow(
//                () -> new RuntimeException("Wallet not found")
//        );
//        if(!wallet.getUserId().equals(userId)){
//            throw new RuntimeException("Unauthorized");
//        }
//        if(StringUtils.isEmpty(wallet.getSecretKey())){
//            throw new RuntimeException("Secret not found");
//        }
//        String secret = wallet.getSecretKey();
//        TimeProvider timeProvider = new SystemTimeProvider();
//        CodeGenerator codeGenerator = new DefaultCodeGenerator();
//        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
//        boolean successful = verifier.isValidCode(secret, code);
//        return new ResponseEntity<>(successful, HttpStatus.OK);
//    }
//
//    public ResponseEntity<GetWalletDetailsResponse> getWalletDetails(String userId) {
//       Wallets wallet= walletsRepo.findByUserId(userId).orElseThrow(() -> new RuntimeException("Wallet not found"));
//        GetWalletDetailsResponse response = new GetWalletDetailsResponse();
//        response.copyWallets(wallet);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//    @Transactional
//    public ResponseEntity<GetStatementResponse> getStatement(String userId) {
//        Wallets wallet = walletsRepo.findByUserId(userId).orElseThrow(() -> new RuntimeException("Wallet not found"));
//        List<Transactions> fromTransactions = transactionsRepo.findAllByFromWalletId(wallet.getId());
//        List<Transactions> toTransactions = transactionsRepo.findAllByToWalletId(wallet.getId());
//        List<Recharges> recharges = rechargesRepo.findAllByWalletId(wallet.getId());
//        GetStatementResponse response = new GetStatementResponse();
//        response.processStatement(wallet, fromTransactions,toTransactions, recharges);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
}
