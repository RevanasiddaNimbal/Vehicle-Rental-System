package UI;

import authentication.controller.AuthController;
import authentication.model.UserRole;
import util.InputUtil;

import java.util.Scanner;

public class AuthMenu implements Menu {
    private final AuthController controller;
    private final Scanner input;

    public AuthMenu(Scanner input, AuthController controller) {
        this.input = input;
        this.controller = controller;

    }

    public void show(UserRole role) {
        System.out.println("----- Authentication -----");
        System.out.println("1. Login");
        if (!role.name().equals("ADMIN"))
            System.out.println("2. Register");
        System.out.println("0. back");
        int choice = InputUtil.readPositiveInt(input, "Enter you choice");

        switch (choice) {
            case 1:
                controller.login(role);
                break;
            case 2:
                controller.register(role);
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice");
        }
    }
}
