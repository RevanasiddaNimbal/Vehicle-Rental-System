package UI;

import util.InputUtil;
import wallet.controller.WalletController;

import java.util.Scanner;

public class WalletManagementMenu implements UsersMenu {
    private final WalletController walletController;
    private final Scanner input;

    public WalletManagementMenu(Scanner input, WalletController walletController) {
        this.input = input;
        this.walletController = walletController;
    }

    @Override
    public void show(String userId) {
        int choice;
        while (true) {
            System.out.println("\n========= Wallet Management Menu ==============");
            System.out.println("1. Check Wallet Balance");
            System.out.println("2. Deposit Amount to Wallet");
            System.out.println("3. withdraw Amount from Wallet");
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
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice.Please try again");
            }
        }
    }
}
