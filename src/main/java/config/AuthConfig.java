package config;

import authentication.controller.AuthController;
import authentication.factory.AuthStrategyFactory;
import authentication.service.AuthService;
import authentication.service.UserOnBoardingService;

public class AuthConfig {

    private final StrategyConfig strategyConfig;
    private final MenuConfig menuConfig;

    private volatile AuthStrategyFactory authStrategyFactory;
    private volatile AuthService authService;
    private volatile AuthController authController;
    private volatile UserOnBoardingService userOnBoardingService;

    public AuthConfig(StrategyConfig strategyConfig, MenuConfig menuConfig) {
        this.strategyConfig = strategyConfig;
        this.menuConfig = menuConfig;
    }

    public AuthStrategyFactory getAuthStrategyFactory() {
        if (authStrategyFactory == null) {
            synchronized (this) {
                if (authStrategyFactory == null) {
                    authStrategyFactory = new AuthStrategyFactory(
                            strategyConfig.getLoginStrategies(),
                            strategyConfig.getRegisterStrategies(),
                            strategyConfig.getPasswordRecoveryStrategies()
                    );
                }
            }
        }
        return authStrategyFactory;
    }

    public AuthService getAuthService() {
        if (authService == null) {
            synchronized (this) {
                if (authService == null) {
                    authService = new AuthService(getAuthStrategyFactory());
                }
            }
        }
        return authService;
    }

    public UserOnBoardingService getUserOnBoardingService() {
        if (userOnBoardingService == null) {
            synchronized (this) {
                if (userOnBoardingService == null) {
                    userOnBoardingService =
                            new UserOnBoardingService(
                                    getAuthService(),
                                    strategyConfig.getWalletStrategy()
                            );
                }
            }
        }
        return userOnBoardingService;
    }

    public AuthController getAuthController() {
        if (authController == null) {
            synchronized (this) {
                if (authController == null) {
                    authController = new AuthController(
                            getAuthService(),
                            menuConfig.getMenuFactory(),
                            getUserOnBoardingService()
                    );
                }
            }
        }
        return authController;
    }
}