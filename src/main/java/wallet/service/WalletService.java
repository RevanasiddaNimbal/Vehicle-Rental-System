package wallet.service;

import util.IdGeneratorUtil;
import util.InputUtil;
import wallet.model.Wallet;
import wallet.repository.WalletRepo;

import java.util.Scanner;

public class WalletService {
    private final WalletRepo walletRepo;

    public WalletService(WalletRepo walletRepo) {
        this.walletRepo = walletRepo;
    }

    public Wallet getWalletByUserId(String userId) {
        if (userId == null) return null;
        return walletRepo.findByUserId(userId);

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

    public void updateWallet(Wallet wallet) {
        if (walletRepo.findByWalletId(wallet.getWalletId()) == null) {
            System.out.println("Wallet does not exist");
            return;
        }
        walletRepo.save(wallet);
    }

    public boolean creditAmountByWalletId(String walletId, double amount) {
        Wallet wallet = getWalletByUserId(walletId);
        if (wallet == null || !isValidAmount(amount)) return false;

        wallet.credit(amount);
        if (walletRepo.update(wallet)) return true;
        else return false;
    }

    public boolean withdrawAmountByWalletId(String walletId, double amount) {
        Wallet wallet = getValidWallet(walletId);
        if (wallet == null || !isValidAmount(amount)) return false;

        if (wallet.getBalance() < amount) {
            System.out.println("Not enough balance");
            return false;
        }
        wallet.debit(amount);
        if (walletRepo.update(wallet)) return true;
        else
            return false;
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
            System.out.println("Amount must be positive");
            return false;
        }
        return true;
    }

}
