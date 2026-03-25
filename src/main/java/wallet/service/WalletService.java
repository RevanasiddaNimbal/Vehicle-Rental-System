package wallet.service;

import exception.InsufficientFundsException;
import exception.InvalidCredentialsException;
import exception.ResourceNotFoundException;
import util.IdGeneratorUtil;
import util.IdPrefix;
import util.InputUtil;
import wallet.model.Wallet;
import wallet.repository.WalletRepo;

import java.util.Scanner;

public class WalletService {
    private final WalletRepo walletRepo;
    private final WalletCredentialService walletCredentialService;

    private static final double INITIAL_BALANCE = 2000.0;
    private static final double MIN_BALANCE_FOR_WITHDRAWAL = 1000.0;

    public WalletService(WalletRepo walletRepo, WalletCredentialService walletCredentialService) {
        this.walletRepo = walletRepo;
        this.walletCredentialService = walletCredentialService;
    }

    public Wallet getWalletByUserId(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null.");
        }
        return walletRepo.findByUserId(userId);
    }

    public double getBalanceByUserId(Scanner input, String userId) {
        Wallet wallet = getValidWalletByUserId(userId);

        if (validatePassword(input, wallet.getWalletId())) {
            throw new InvalidCredentialsException("Invalid wallet PIN. Access denied.");
        }
        return wallet.getBalance();
    }

    public Wallet createSystemWallet() {
        Wallet wallet = walletRepo.findByUserId("SYSTEM");
        if (wallet == null) {
            Wallet newWallet = new Wallet("SYSTEM-WALLET", "SYSTEM", INITIAL_BALANCE);
            walletRepo.save(newWallet);
            return newWallet;
        }
        return wallet;
    }

    public Wallet createWallet(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null.");
        }

        Wallet existingWallet = walletRepo.findByUserId(userId);
        if (existingWallet != null) {
            throw new IllegalStateException("Wallet already exists for this user.");
        }

        String walletId = IdGeneratorUtil.generate(IdPrefix.WAL);

        Wallet newWallet = new Wallet(walletId, userId, INITIAL_BALANCE);
        walletRepo.save(newWallet);
        return newWallet;
    }

    public void creditAmountByWalletId(Scanner input, String walletId, double amount) {
        Wallet wallet = getValidWallet(walletId);
        validateAmount(amount);

        if (validatePassword(input, walletId)) {
            throw new InvalidCredentialsException("Invalid Wallet PIN. Deposit denied.");
        }

        wallet.credit(amount);
        walletRepo.update(wallet);
    }

    public void withdrawAmountByWalletId(Scanner input, String walletId, double amount) {
        Wallet wallet = getValidWallet(walletId);
        validateAmount(amount);

        if (validatePassword(input, wallet.getWalletId())) {
            throw new InvalidCredentialsException("Invalid Wallet PIN. Withdrawal denied.");
        }

        if ((wallet.getBalance() - amount) < MIN_BALANCE_FOR_WITHDRAWAL) {
            throw new InsufficientFundsException("Withdrawal Denied: You must maintain a minimum balance of Rs. "
                    + MIN_BALANCE_FOR_WITHDRAWAL + " in your wallet.");
        }

        wallet.debit(amount);
        walletRepo.update(wallet);
    }
    
    private Wallet getValidWallet(String walletId) {
        Wallet wallet = walletRepo.findByWalletId(walletId);
        if (wallet == null) {
            throw new ResourceNotFoundException("Wallet does not exist.");
        }
        return wallet;
    }

    private Wallet getValidWalletByUserId(String userId) {
        Wallet wallet = walletRepo.findByUserId(userId);
        if (wallet == null) {
            throw new ResourceNotFoundException("User does not have a wallet.");
        }
        return wallet;
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
    }

    private boolean validatePassword(Scanner input, String walletId) {
        String password = InputUtil.readValidPassword(input, "Enter Your Wallet PIN");
        return !walletCredentialService.verifyWalletPassword(walletId, password);
    }
}