package application;

import UI.UserRoleMenu;
import authentication.model.UserRole;
import config.ServiceConfig;
import initializer.SystemInitializer;
import util.InputUtil;

import java.util.Scanner;

public class Application {
    private final Scanner input;
    private final UserRoleMenu documentation;
    private final UserRoleMenu authMenu;
    private final SystemInitializer systemInitializer;
    private final ServiceConfig serviceConfig;

    public Application(Scanner input, UserRoleMenu documentation, UserRoleMenu authMenu, SystemInitializer systemInitializer, ServiceConfig serviceConfig) {
        this.input = input;
        this.documentation = documentation;
        this.authMenu = authMenu;
        this.systemInitializer = systemInitializer;
        this.serviceConfig = serviceConfig;
    }

    public void start() {
        System.out.println("\n========================================================================");
        System.out.println("           WELCOME TO VEHICLE RENTAL MANAGEMENT SYSTEM                 ");
        System.out.println("========================================================================");

        while (true) {
            showMainMenu();
            int choice = InputUtil.readPositiveInt(input, "Enter your choice");
            switch (choice) {
                case 1:
                    documentation.show(null);
                    break;
                case 2:
                    systemInitializer.initialize();
                    authMenu.show(UserRole.ADMIN);
                    break;
                case 3:
                    systemInitializer.initialize();
                    authMenu.show(UserRole.OWNER);
                    break;
                case 4:
                    systemInitializer.initialize();
                    authMenu.show(UserRole.CUSTOMER);
                    break;
                case 0:
                    System.out.println("Shutting down from the code...");
                    serviceConfig.shutdownBackgroundTasks();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n⭐ AVAILABLE OPTIONS ⭐");
        System.out.println("-------------------------");

        System.out.println("1. DOCUMENTATION SECTION.");
        System.out.println("2. ADMIN PANEL.");
        System.out.println("3. VEHICLE-OWNER PANEL.");
        System.out.println("4. CUSTOMER PANEL.");
        System.out.println("0. EXIT");

    }

}
