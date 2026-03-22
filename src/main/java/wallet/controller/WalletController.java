package wallet.controller;

import util.InputUtil;
import wallet.model.Wallet;
import wallet.service.WalletCredentialService;
import wallet.service.WalletService;

import java.util.Scanner;

public class WalletController {
    private final WalletCredentialService walletCredentialService;
    private final WalletService walletService;

    public WalletController(WalletCredentialService walletCredentialService, WalletService walletService) {
        this.walletCredentialService = walletCredentialService;
        this.walletService = walletService;
    }

    public void checkBalanceByUserId(Scanner input, String userId) {
        Wallet wallet = walletService.getWalletByUserId(userId);
        if (wallet == null) {
            System.out.println("User doesn't have wallet");
            return;
        }
        if (validatePassword(input, wallet.getWalletId())) {
            System.out.println("Invalid Wallet Password");
            return;
        }
        System.out.println("Balance Amount: " + wallet.getBalance());
    }

    public void deposit(Scanner input, String userId) {
        Wallet wallet = walletService.getWalletByUserId(userId);

        if (wallet == null) {
            System.out.println("User doesn't have a wallet");
            return;
        }
        double amount = InputUtil.readDouble(input, "Enter Amount to deposit");
        if (walletService.creditAmountByWalletId(wallet.getWalletId(), amount))
            System.out.println("Amount deposited successfully");
        else
            System.out.println("Failed to deposit amount.Please Try Again");
    }

    public void withdraw(Scanner input, String userId) {
        Wallet wallet = walletService.getWalletByUserId(userId);
        if (wallet == null) {
            System.out.println("User doesn't have a wallet");
            return;
        }
        double amount = InputUtil.readDouble(input, "Enter Amount to withdraw");
        if (validatePassword(input, wallet.getWalletId())) {
            System.out.println("Invalid Wallet Password");
            return;
        }
        if (walletService.withdrawAmountByWalletId(wallet.getWalletId(), amount))
            System.out.println("Amount withdrawn successfully");
        else
            System.out.println("Failed to withdraw amount.Please Try Again");
    }

    private boolean validatePassword(Scanner input, String walletId) {
        String password = InputUtil.readValidPassword(input, "Enter Your Wallet Password");
        return walletCredentialService.verifyWalletPassword(walletId, password);
    }
     
}
