package transaction.controller;

import UI.UserPrinter;
import transaction.model.Transaction;
import transaction.service.TransactionService;
import util.InputUtil;
import wallet.model.Wallet;
import wallet.service.WalletService;

import java.util.List;
import java.util.Scanner;

public class TransactionController {
    private final TransactionService transactionService;
    private final WalletService walletService;
    private final UserPrinter<Transaction> transactionPrinter;

    public TransactionController(TransactionService transactionService, WalletService walletService, UserPrinter<Transaction> transactionPrinter) {
        this.transactionService = transactionService;
        this.walletService = walletService;
        this.transactionPrinter = transactionPrinter;
    }

    public void viewUserTransactions(String userId) {
        try {
            Wallet wallet = walletService.getWalletByUserId(userId);
            if (wallet == null) {
                System.out.println("No wallet found for this account.");
                return;
            }
            List<Transaction> transactions = transactionService.getUsersTransactionsByWalletId(wallet.getWalletId());
            transactionPrinter.print(transactions);
        } catch (Exception e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
    }

    public void viewUserTransactionsByDays(Scanner input, String userId) {
        try {
            Wallet wallet = walletService.getWalletByUserId(userId);
            if (wallet == null) {
                System.out.println("No wallet found for this account.");
                return;
            }

            int days = InputUtil.readPositiveInt(input, "Enter the number of past days of history you want to view");

            List<Transaction> transactions = transactionService.getUsersTransactionsByWalletIdAndDays(wallet.getWalletId(), days);
            transactionPrinter.print(transactions);
        } catch (Exception e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
    }

    public void viewAdminTransactionsByDays(Scanner input, String adminId) {
        try {
            Wallet wallet = walletService.getWalletByUserId(adminId);
            if (wallet == null) {
                System.out.println("No wallet found for this account.");
                return;
            }

            int days = InputUtil.readPositiveInt(input, "Enter the number of past days of history you want to view");

            List<Transaction> transactions = transactionService.getAdminTransactionsByWalletIdAndDays(wallet.getWalletId(), days);
            transactionPrinter.print(transactions);
        } catch (Exception e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
    }

    public void viewPendingEscrowTransactions() {
        try {
            List<Transaction> transactions = transactionService.getPendingEscrowTransactions();
            if (transactions.isEmpty()) {
                System.out.println("No pending transactions found.");
                return;
            }
            transactionPrinter.print(transactions);
        } catch (Exception e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
    }

    public void viewEscrowTransactions() {
        try {
            Wallet escrowWallet = walletService.getEscrowWallet();
            if (escrowWallet == null) {
                System.out.println("System Escrow Wallet is not initialized.");
                return;
            }
            List<Transaction> transactions = transactionService.getAdminTransactionsByWalletId(escrowWallet.getWalletId());
            System.out.println("\n--- System Escrow Transaction History ---");
            transactionPrinter.print(transactions);
        } catch (Exception e) {
            System.out.println("Error loading Escrow transactions: " + e.getMessage());
        }
    }

    public void viewRevenueTransactions() {
        try {
            Wallet revenueWallet = walletService.getRevenueWallet();
            if (revenueWallet == null) {
                System.out.println("System Revenue Wallet is not initialized.");
                return;
            }
            List<Transaction> transactions = transactionService.getAdminTransactionsByWalletId(revenueWallet.getWalletId());
            System.out.println("\n--- System Revenue Transaction History ---");
            transactionPrinter.print(transactions);
        } catch (Exception e) {
            System.out.println("Error loading Revenue transactions: " + e.getMessage());
        }
    }
}