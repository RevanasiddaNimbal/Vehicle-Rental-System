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
            double balance = walletService.getBalanceByUserId(input, userId);
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

            walletService.creditAmountByWalletId(input, wallet.getWalletId(), amount);
            System.out.println("Amount deposited successfully!");

        } catch (InvalidCredentialsException | IllegalArgumentException | ResourceNotFoundException e) {
            System.out.println("Deposit Failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("System Error: An unexpected error occurred during deposit.");
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

            walletService.withdrawAmountByWalletId(input, wallet.getWalletId(), amount);
            System.out.println("Amount withdrawn successfully!");

        } catch (InsufficientFundsException | InvalidCredentialsException | IllegalArgumentException |
                 ResourceNotFoundException e) {
            System.out.println("Withdrawal Failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("System Error: An unexpected error occurred during withdrawal.");
        }
    }
}