package UI;

import transaction.controller.TransactionController;
import util.InputUtil;

import java.util.Scanner;

public class TransactionMenu implements UsersMenu {
    private final Scanner input;
    private final TransactionController transactionController;

    public TransactionMenu(Scanner input, TransactionController transactionController) {
        this.input = input;
        this.transactionController = transactionController;
    }

    @Override
    public void show(String userId) {
        boolean isAdmin = userId.equals("ADMIN") || userId.startsWith("SYSTEM");
        int choice;

        while (true) {
            try {
                System.out.println("\n========= Transaction History Menu ==============");
                if (isAdmin) {
                    System.out.println("1. view Pending Escrow Transactions");
                    System.out.println("2. View System Revenue Transactions (Last X Days)");
                    System.out.println("3. View System Revenue Transactions (Full History)");
                    System.out.println("4. View System Escrow Transactions (Last X Days)");
                    System.out.println("5. View System Escrow Transactions (Full History)");
                } else {
                    System.out.println("1. View Transaction History (Last X Days)");
                    System.out.println("2. View Full Transaction History");
                }
                System.out.println("0. Back");

                choice = InputUtil.readPositiveInt(input, "Enter your choice");

                if (choice == 0) return;
                if (isAdmin) {
                    switch (choice) {
                        case 1:
                            transactionController.viewPendingEscrowTransactions();
                            break;
                        case 2:
                            transactionController.viewUserTransactionsByDays(input, userId);
                            break;
                        case 3:
                            transactionController.viewRevenueTransactions();
                            break;
                        case 4:
                            transactionController.viewUserTransactionsByDays(input, "SYSTEM-ESCROW");
                            break;
                        case 5:
                            transactionController.viewEscrowTransactions();
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } else {
                    switch (choice) {
                        case 1:
                            transactionController.viewUserTransactionsByDays(input, userId);
                            break;
                        case 2:
                            transactionController.viewUserTransactions(userId);
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }
}