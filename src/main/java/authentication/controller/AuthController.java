package authentication.controller;

import authentication.factory.MenuFactory;
import authentication.model.AuthUser;
import authentication.model.UserRole;
import authentication.service.AuthService;

public class AuthController {
    private final AuthService service;
    private final MenuFactory menuFactory;

    public AuthController(AuthService service, MenuFactory menuFactory) {
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

    public void forgotPassword(UserRole role) {
        service.forgotPassword(role);
    }

}
