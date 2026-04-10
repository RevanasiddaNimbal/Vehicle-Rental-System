package config;

import authentication.controller.AuthController;
import authentication.factory.AuthStrategyFactory;
import authentication.service.AuthService;
import authentication.service.UserOnBoardingService;

public class AuthConfig {

    private final StrategyConfig strategyConfig;
    private final MenuConfig menuConfig;

    private AuthStrategyFactory authStrategyFactory;
    private AuthService authService;
    private AuthController authController;
    private UserOnBoardingService userOnBoardingService;

    public AuthConfig(StrategyConfig strategyConfig, MenuConfig menuConfig) {
        this.strategyConfig = strategyConfig;
        this.menuConfig = menuConfig;
    }

    public AuthStrategyFactory getAuthStrategyFactory() {
        if (authStrategyFactory == null) {
            authStrategyFactory = new AuthStrategyFactory(strategyConfig.getLoginStrategies(), strategyConfig.getRegisterStrategies(), strategyConfig.getPasswordRecoveryStrategies()
            );
        }
        return authStrategyFactory;
    }

    public AuthService getAuthService() {
        if (authService == null) {
            authService = new AuthService(getAuthStrategyFactory());
        }
        return authService;
    }

    public UserOnBoardingService getUserOnBoardingService() {
        if (userOnBoardingService == null) {
            userOnBoardingService = new UserOnBoardingService(getAuthService(), strategyConfig.getWalletStrategy());
        }
        return userOnBoardingService;
    }

    public AuthController getAuthController() {
        if (authController == null) {
            authController = new AuthController(getAuthService(), menuConfig.getMenuFactory(), getUserOnBoardingService());
        }
        return authController;
    }
}