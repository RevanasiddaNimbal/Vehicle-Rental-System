package UI;

import util.InputUtil;

import java.util.Scanner;

public class VehicleOwnerMenu implements UsersMenu {
    private final Scanner input;
    private final VehicleOwnerHistoryMenu historyMenu;
    private final WalletManagementMenu walletManagementMenu;
    private final VehicleOwnerAccountMenu AccountManagementMenu;
    private final VehicleOwnerRentalsMenu rentalMenu;
    private final VehicleOwnerVehiclesMenu vehiclesMenu;
    private final TransactionMenu transactionMenu;

    public VehicleOwnerMenu(Scanner input, VehicleOwnerRentalsMenu rentalMenu, VehicleOwnerAccountMenu accountManagementMenu, VehicleOwnerHistoryMenu historyMenu, WalletManagementMenu walletManagementMenu, VehicleOwnerVehiclesMenu vehiclesMenu, TransactionMenu transactionMenu) {
        this.input = input;
        this.historyMenu = historyMenu;
        this.AccountManagementMenu = accountManagementMenu;
        this.walletManagementMenu = walletManagementMenu;
        this.rentalMenu = rentalMenu;
        this.vehiclesMenu = vehiclesMenu;
        this.transactionMenu = transactionMenu;
    }


    @Override
    public void show(String ownerId) {
        int choice;
        while (true) {
            try {
                System.out.println("\n========= VEHICLE OWNER PANEL =============");
                System.out.println("1. Vehicles Management");
                System.out.println("2. Rental Management");
                System.out.println("3. Wallet Management");
                System.out.println("4. History Management");
                System.out.println("5. Transaction Management");
                System.out.println("6. Account Management");
                System.out.println("0. Logout");

                choice = InputUtil.readPositiveInt(input, "Enter your choice");

                switch (choice) {
                    case 1:
                        vehiclesMenu.show(ownerId);
                        break;
                    case 2:
                        rentalMenu.show(ownerId);
                        break;
                    case 3:
                        walletManagementMenu.show(ownerId);
                        break;
                    case 4:
                        historyMenu.show(ownerId);
                        break;
                    case 5:
                        transactionMenu.show(ownerId);
                        break;
                    case 6:
                        AccountManagementMenu.show(ownerId);
                        break;
                    case 0:
                        System.out.println("Logged out from Owner panel.");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }
}
