package authentication.controller;

import authentication.factory.MenuFactory;
import authentication.model.AuthUser;
import authentication.model.UserRole;
import authentication.service.AuthService;

import java.util.Scanner;

public class AuthController {
    private final AuthService service;
    private final Scanner input;
    private final MenuFactory menuFactory;

    public AuthController(Scanner input, AuthService service, MenuFactory menuFactory) {
        this.input = input;
        this.service = service;
        this.menuFactory = menuFactory;
    }

    public void register(UserRole role) {
        service.register(role);
    }

    public void login(UserRole role) {
        AuthUser user = service.login(role);
        if (user != null) {
            menuFactory.showMenu(role, user.getId());
        }
    }

    public void resetPassword(UserRole role) {
        service.resetPassword(role);
    }

}
