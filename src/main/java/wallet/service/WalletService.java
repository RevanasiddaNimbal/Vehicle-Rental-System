package wallet.service;

import util.IdGeneratorUtil;
import util.InputUtil;
import wallet.model.Wallet;
import wallet.repository.WalletRepo;

import java.util.Scanner;

public class WalletService {
    private final WalletRepo walletRepo;
    private final WalletCredentialService walletCredentialService;

    public WalletService(WalletRepo walletRepo, WalletCredentialService walletCredentialService) {
        this.walletRepo = walletRepo;
        this.walletCredentialService = walletCredentialService;
    }

    public Wallet getWalletByUserId(String userId) {
        if (userId == null) return null;
        return walletRepo.findByUserId(userId);
    }

    public double getBalanceByUserId(Scanner input, String userId) {
        Wallet wallet = getWalletByUserId(userId);
        if (wallet == null) return -1;

        if (!validatePassword(input, wallet.getWalletId())) {
            System.out.println("Invalid password. Please try again");
            return -1;
        }
        return wallet.getBalance();
    }

    public Wallet createSystemWallet() {
        Wallet wallet = walletRepo.findByUserId("SYSTEM");
        if (wallet == null) {
            Wallet newWallet = new Wallet("SYSTEM-WALLET", "SYSTEM", 2000);
            walletRepo.save(newWallet);
            return newWallet;
        }
        return wallet;
    }

    public Wallet createWallet(Scanner input, String userId) {
        if (userId == null) {
            System.out.println("UserId cannot be null");
            return null;
        }

        Wallet wallet = walletRepo.findByUserId(userId);
        if (wallet != null) {
            System.out.println("Wallet already exists");
            return wallet;
        }
        String walletId = IdGeneratorUtil.generateWalletId();
        double initialBalance = InputUtil.readDouble(input, "Enter Initial Wallet Balance Amount");
        Wallet createWallet = new Wallet(walletId, userId, initialBalance);
        walletRepo.save(createWallet);
        return createWallet;
    }

    public boolean creditAmountByWalletId(Scanner input, String walletId, double amount) {
        Wallet wallet = getValidWallet(walletId);
        if (wallet == null || !isValidAmount(amount)) return false;
        if (!validatePassword(input, walletId)) return false;
        wallet.credit(amount);
        return walletRepo.update(wallet);
    }

    public boolean withdrawAmountByWalletId(Scanner input, String walletId) {
        Wallet wallet = getValidWallet(walletId);
        if (wallet == null) return false;

        double amount = InputUtil.readDouble(input, "Enter Amount to withdraw");

        if (!validatePassword(input, wallet.getWalletId())) {
            System.out.println("Invalid Wallet Password");
            return false;
        }

        if (!isValidAmount(amount)) return false;

        if (wallet.getBalance() < amount) {
            System.out.println("Not enough balance");
            return false;
        }
        wallet.debit(amount);
        return walletRepo.update(wallet);
    }

    private Wallet getValidWallet(String walletId) {
        Wallet wallet = walletRepo.findByWalletId(walletId);
        if (wallet == null) {
            System.out.println("Wallet does not exist");
        }
        return wallet;
    }

    private boolean isValidAmount(double amount) {
        if (amount <= 0) {
            System.out.println("Amount must be more than zero");
            return false;
        }
        return true;
    }

    private boolean validatePassword(Scanner input, String walletId) {
        String password = InputUtil.readValidPassword(input, "Enter Your Wallet Password");
        return walletCredentialService.verifyWalletPassword(walletId, password);
    }
}