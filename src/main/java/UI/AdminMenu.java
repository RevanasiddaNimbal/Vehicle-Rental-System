package UI;

import util.InputUtil;

import java.util.Scanner;

public class AdminMenu implements UsersMenu {
    private final Scanner input;
    private final AdminAccountMenu accountManagementMenu;
    private final AdminRentalMenu rentalMenu;
    private final AdminsCustomerMenu customerMenu;
    private final WalletManagementMenu walletManagementMenu;
    private final AdminOwnersManu ownerMenu;
    private final AdminsVehicleMenu vehicleMenu;
    private final TransactionMenu transactionMenu;

    public AdminMenu(Scanner input, AdminOwnersManu ownerMenu, AdminsVehicleMenu vehicleMenu, AdminsCustomerMenu customerMenu, AdminRentalMenu rentalMenu, AdminAccountMenu accountManagementMenu, WalletManagementMenu walletManagementMenu, TransactionMenu transactionMenu) {
        this.input = input;
        this.ownerMenu = ownerMenu;
        this.vehicleMenu = vehicleMenu;
        this.customerMenu = customerMenu;
        this.rentalMenu = rentalMenu;
        this.accountManagementMenu = accountManagementMenu;
        this.walletManagementMenu = walletManagementMenu;
        this.transactionMenu = transactionMenu;
    }

    @Override
    public void show(String adminId) {
        int choice;
        while (true) {
            try {
                System.out.println("\n========= ADMIN PANEL =========");
                System.out.println("1. Rental Management");
                System.out.println("2. Wallet Management");
                System.out.println("3. Vehicle Owners Management");
                System.out.println("4. Customers Management");
                System.out.println("5. Vehicle Management");
                System.out.println("6. Transaction Management");
                System.out.println("7. Account Management");
                System.out.println("0. Logout");

                choice = InputUtil.readPositiveInt(input, "Enter your choice");

                switch (choice) {
                    case 1:
                        rentalMenu.show(adminId);
                        break;
                    case 2:
                        walletManagementMenu.show("ADM-002");
                        break;
                    case 3:
                        ownerMenu.show(adminId);
                        break;
                    case 4:
                        customerMenu.show(adminId);
                        break;
                    case 5:
                        vehicleMenu.show(adminId);
                        break;
                    case 6:
                        transactionMenu.show("ADM-002");
                        break;
                    case 7:
                        accountManagementMenu.show(adminId);
                        break;
                    case 0:
                        System.out.println("Logged out from Admin.");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Unexpected Error: " + e.getMessage());
            }
        }
    }
}
