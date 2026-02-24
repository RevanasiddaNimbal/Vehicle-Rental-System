package application;

import UI.Menu;
import util.InputUtil;

import java.util.Map;
import java.util.Scanner;

public class Application {
    private final Scanner input;
    private final Map<Integer, Menu> menu;

    public Application(Scanner input, Map<Integer, Menu> menu) {
        this.input = input;
        this.menu = menu;
    }

    public void start() {
        System.out.println("\n========================================================================");
        System.out.println("           WELCOME TO VEHICLE RENTAL MANAGEMENT SYSTEM                 ");
        System.out.println("========================================================================");

        while (true) {
            showMainMenu();
            int choice = InputUtil.readPositiveInt(input, "Enter your choice");

            if (choice == 0) return;
            Menu selected = menu.get(choice);
            if (selected != null) selected.show();
            else System.out.println("Invalid choice.Please try again.");
        }
    }

    private static void showMainMenu() {
        System.out.println("\n⭐ AVAILABLE OPTIONS ⭐");
        System.out.println("-------------------------");

        System.out.println("1. DOCUMENTATION SECTION.");
        System.out.println("2. VEHICLE MANAGEMENT SECTION.");
        System.out.println("3. CUSTOMER MANAGEMENT SECTION.");
        System.out.println("4. RENTAL MANAGEMENT SECTION.");
        System.out.println("0. EXIT");

    }

}
