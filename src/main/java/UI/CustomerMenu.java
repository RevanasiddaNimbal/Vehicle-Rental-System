package UI;


import util.InputUtil;

import java.util.Scanner;

public class CustomerMenu implements UsersMenu {
    private final Scanner input;
    private final CustomerRentalsMenu customerRentalsMenu;
    private final CustomerHistoryMenu customerHistoryMenu;
    private final WalletManagementMenu customerWalletMenu;
    private final CustomerAccountMenu accountManagementMenu;
    private final TransactionMenu transactionMenu;

    public CustomerMenu(Scanner input, CustomerRentalsMenu customerRentalsMenu, WalletManagementMenu customerWalletMenu, CustomerHistoryMenu customerHistoryMenu, CustomerAccountMenu accountManagementMenu, TransactionMenu transactionMenu) {
        this.input = input;
        this.customerRentalsMenu = customerRentalsMenu;
        this.customerWalletMenu = customerWalletMenu;
        this.customerHistoryMenu = customerHistoryMenu;
        this.accountManagementMenu = accountManagementMenu;
        this.transactionMenu = transactionMenu;
    }

    @Override
    public void show(String customerId) {
        int choice;
        while (true) {
            try {
                System.out.println("\n========= CUSTOMER PANEL ==============");
                System.out.println("1. Rental Management");
                System.out.println("2. Wallet Management");
                System.out.println("3. History Management");
                System.out.println("4. Transaction Management");
                System.out.println("5. Account Management");
                System.out.println("0. Logout");
                choice = InputUtil.readPositiveInt(input, "Enter your choice");
                switch (choice) {
                    case 1:
                        customerRentalsMenu.show(customerId);
                        break;
                    case 2:
                        customerWalletMenu.show(customerId);
                        break;
                    case 3:
                        customerHistoryMenu.show(customerId);
                        break;
                    case 4:
                        transactionMenu.show(customerId);
                        break;
                    case 5:
                        accountManagementMenu.show(customerId);
                        break;
                    case 0:
                        System.out.println("Logging out...");
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
