package authentication.controller;

import authentication.factory.MenuFactory;
import authentication.model.AuthUser;
import authentication.service.AuthService;
import authentication.service.UserOnBoardingService;
import user.model.UserRole;

public class AuthController {
    private final AuthService service;
    private final UserOnBoardingService userOnBoardingService;
    private final MenuFactory menuFactory;

    public AuthController(AuthService service, MenuFactory menuFactory, UserOnBoardingService userOnBoardingService) {
        this.service = service;
        this.userOnBoardingService = userOnBoardingService;
        this.menuFactory = menuFactory;
    }

    public void register(UserRole role) {
        userOnBoardingService.registerNewUser(role);
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
