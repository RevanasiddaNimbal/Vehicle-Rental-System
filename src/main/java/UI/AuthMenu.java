package UI;

import authentication.controller.AuthController;
import authentication.model.UserRole;
import util.InputUtil;

import java.util.Scanner;

public class AuthMenu implements UserRoleMenu {
    private final AuthController controller;
    private final Scanner input;

    public AuthMenu(Scanner input, AuthController controller) {
        this.input = input;
        this.controller = controller;

    }

    @Override
    public void show(UserRole role) {
        while (true) {
            try {
                System.out.println("----- Authentication -----");
                System.out.println("1. Login");
                System.out.println("2. Forgot Password");
                if (!role.name().equals("ADMIN"))
                    System.out.println("3. Register");
                System.out.println("0. back");
                int choice = InputUtil.readPositiveInt(input, "Enter you choice");
                if (role == UserRole.ADMIN && choice == 3) {
                    System.out.println("Invalid choice.");
                    continue;
                }
                switch (choice) {
                    case 1:
                        controller.login(role);
                        break;
                    case 2:
                        controller.forgotPassword(role);
                        break;
                    case 3:
                        controller.register(role);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }
}
