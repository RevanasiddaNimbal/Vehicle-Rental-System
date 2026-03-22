package wallet.controller;

import util.InputUtil;
import wallet.model.Wallet;
import wallet.service.WalletService;

import java.util.Scanner;

public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    public void checkBalanceByUserId(Scanner input, String userId) {
        Wallet wallet = walletService.getWalletByUserId(userId);
        if (wallet == null) {
            System.out.println("User doesn't have wallet");
            return;
        }
        double balance = walletService.getBalanceByUserId(input, userId);
        if (balance == -1) return;
        System.out.println("Wallet Balance Amount : " + balance);
    }

    public void deposit(Scanner input, String userId) {
        Wallet wallet = walletService.getWalletByUserId(userId);

        if (wallet == null) {
            System.out.println("User doesn't have a wallet");
            return;
        }
        double amount = InputUtil.readDouble(input, "Enter Amount to deposit");
        if (walletService.creditAmountByWalletId(input, wallet.getWalletId(), amount))
            System.out.println("Amount deposited successfully");
        else
            System.out.println("Failed to deposit amount. Please try again");
    }

    public void withdraw(Scanner input, String userId) {
        Wallet wallet = walletService.getWalletByUserId(userId);
        if (wallet == null) {
            System.out.println("User doesn't have a wallet");
            return;
        }

        if (walletService.withdrawAmountByWalletId(input, wallet.getWalletId()))
            System.out.println("Amount withdrawn successfully");
        else
            System.out.println("Failed to withdraw amount. Please try again");
    }
}