package UI;

import util.InputUtil;
import wallet.controller.WalletController;
import wallet.controller.WalletCredentialController;

import java.util.Scanner;

public class WalletManagementMenu implements UsersMenu {
    private final WalletController walletController;
    private final WalletCredentialController walletCredentialController;
    private final Scanner input;

    public WalletManagementMenu(Scanner input, WalletController walletController, WalletCredentialController walletCredentialController) {
        this.input = input;
        this.walletController = walletController;
        this.walletCredentialController = walletCredentialController;
    }

    @Override
    public void show(String userId) {
        int choice;
        boolean isAdmin = userId.equals("ADMIN") || userId.startsWith("SYSTEM");
        while (true) {
            try {
                System.out.println("\n========= Wallet Management Menu ==============");
                System.out.println("1. Check Wallet Balance");
                System.out.println("2. Deposit Amount to Wallet");
                System.out.println("3. Withdraw Amount from Wallet");
                System.out.println("4. Forgot Wallet PIN");
                if (isAdmin) {
                    System.out.println("5. Check Escrow System Balance");
                }
                System.out.println("0. back");
                choice = InputUtil.readPositiveInt(input, "Enter your choice");
                switch (choice) {
                    case 1:
                        walletController.checkBalanceByUserId(input, userId);
                        break;
                    case 2:
                        walletController.deposit(input, userId);
                        break;
                    case 3:
                        walletController.withdraw(input, userId);
                        break;
                    case 4:
                        walletCredentialController.forgotPassword(userId);
                        break;
                    case 5:
                        if (isAdmin)
                            walletController.checkBalanceByUserId(input, "SYSTEM-ESCROW");
                        else
                            System.out.println("Invalid choice.");
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice.Please try again");
                }
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }
}
