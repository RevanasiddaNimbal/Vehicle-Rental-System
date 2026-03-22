package UI;

import util.InputUtil;

import java.util.Scanner;

public class VehicleOwnerMenu implements UsersMenu {
    private final Scanner input;
    private final VehicleOwnerHistoryMenu historyMenu;
    private final WalletManagementMenu walletManagementMenu;
    private final VehicleOwnerAccountMenu AccountManagementMenu;
    private final VehicleOwnerRentalsMenu rentalMenu;

    public VehicleOwnerMenu(Scanner input, VehicleOwnerRentalsMenu rentalMenu, VehicleOwnerAccountMenu accountManagementMenu, VehicleOwnerHistoryMenu historyMenu, WalletManagementMenu walletManagementMenu) {
        this.input = input;
        this.historyMenu = historyMenu;
        this.AccountManagementMenu = accountManagementMenu;
        this.walletManagementMenu = walletManagementMenu;
        this.rentalMenu = rentalMenu;
    }


    @Override
    public void show(String ownerId) {
        int choice;
        while (true) {
            System.out.println("\n========= VEHICLE OWNER PANEL =============");
            System.out.println("1. Rental Management");
            System.out.println("2. Wallet Management");
            System.out.println("3. History Management");
            System.out.println("4. Account Management");
            System.out.println("0. Logout");

            choice = InputUtil.readPositiveInt(input, "Enter your choice");

            switch (choice) {
                case 1:
                    rentalMenu.show(ownerId);
                    break;
                case 2:
                    walletManagementMenu.show(ownerId);
                    break;
                case 3:
                    historyMenu.show(ownerId);
                    break;
                case 4:
                    AccountManagementMenu.show(ownerId);
                    break;
                case 0:
                    System.out.println("Logged out from Owner panel.");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
