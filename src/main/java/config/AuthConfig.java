package config;

import authentication.controller.AuthController;
import authentication.factory.AuthStrategyFactory;
import authentication.service.AuthService;

import java.util.Scanner;

public class AuthConfig {

    private final ServiceConfig serviceConfig;
    private final NotificationConfig notificationConfig;
    private final StrategyConfig strategyConfig;
    private final MenuConfig menuConfig;
    private final Scanner input;

    private AuthStrategyFactory authStrategyFactory;
    private AuthService authService;
    private AuthController authController;

    public AuthConfig(ServiceConfig serviceConfig, NotificationConfig notificationConfig, StrategyConfig strategyConfig, MenuConfig menuConfig, Scanner input) {
        this.serviceConfig = serviceConfig;
        this.notificationConfig = notificationConfig;
        this.strategyConfig = strategyConfig;
        this.menuConfig = menuConfig;
        this.input = input;
    }

    public AuthStrategyFactory getAuthStrategyFactory() {
        if (authStrategyFactory == null) {
            authStrategyFactory = new AuthStrategyFactory(
                    input,
                    serviceConfig,
                    notificationConfig.getOtpService()
            );
        }
        return authStrategyFactory;
    }

    public AuthService getAuthService() {
        if (authService == null) {
            authService = new AuthService(getAuthStrategyFactory(), strategyConfig);
        }
        return authService;
    }

    public AuthController getAuthController() {
        if (authController == null) {
            authController = new AuthController(getAuthService(), menuConfig.getMenuFactory());
        }
        return authController;
    }
}