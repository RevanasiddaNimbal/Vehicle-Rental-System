package wallet.controller;

import exception.InsufficientFundsException;
import exception.InvalidCredentialsException;
import exception.ResourceNotFoundException;
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
        try {
            String pin = InputUtil.readValidPassword(input, "Enter Your Wallet PIN");
            double balance = walletService.getBalanceByUserId(userId, pin);
            System.out.println("Wallet Balance Amount: Rs. " + balance);
        } catch (ResourceNotFoundException | InvalidCredentialsException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("System Error: Could not retrieve balance.");
        }
    }

    public void deposit(Scanner input, String userId) {
        try {
            Wallet wallet = walletService.getWalletByUserId(userId);
            if (wallet == null) {
                System.out.println("Error: User doesn't have a wallet.");
                return;
            }

            double amount = InputUtil.readDouble(input, "Enter Amount to deposit");
            String pin = InputUtil.readValidPassword(input, "Enter Your Wallet PIN");

            walletService.creditAmountByWalletId(pin, wallet.getWalletId(), amount);
            System.out.println("Amount deposited successfully!");

        } catch (InvalidCredentialsException | IllegalArgumentException | ResourceNotFoundException e) {
            System.out.println("Deposit Failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("System Error: " + e.getMessage());
        }
    }

    public void withdraw(Scanner input, String userId) {
        try {
            Wallet wallet = walletService.getWalletByUserId(userId);
            if (wallet == null) {
                System.out.println("Error: User doesn't have a wallet.");
                return;
            }

            double amount = InputUtil.readDouble(input, "Enter Amount to withdraw");
            String pin = InputUtil.readValidPassword(input, "Enter Your Wallet PIN");

            walletService.withdrawAmountByWalletId(pin, wallet.getWalletId(), amount, false);
            System.out.println("Amount withdrawn successfully!");

        } catch (InsufficientFundsException | InvalidCredentialsException | IllegalArgumentException |
                 ResourceNotFoundException e) {
            System.out.println("Withdrawal Failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("System Error: An unexpected error occurred during withdrawal.");
        }
    }
}