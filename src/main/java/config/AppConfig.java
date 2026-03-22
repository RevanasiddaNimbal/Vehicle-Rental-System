package config;

import UI.AuthMenu;
import UI.Documentation;
import UI.UserRoleMenu;
import application.Application;
import initializer.SystemInitializer;

import java.util.Scanner;

public class AppConfig {

    private DatabaseConfig databaseConfig;
    private RepositoryConfig repositoryConfig;
    private ServiceConfig serviceConfig;
    private PrinterConfig printerConfig;
    private StrategyConfig strategyConfig;
    private NotificationConfig notificationConfig;
    private ControllerConfig controllerConfig;
    private MenuConfig menuConfig;
    private AuthConfig authConfig;
    private SystemInitializer systemInitializer;

    public DatabaseConfig getDatabaseConfig() {
        if (databaseConfig == null) {
            databaseConfig = new DatabaseConfig();
        }
        return databaseConfig;
    }

    public RepositoryConfig getRepositoryConfig() {
        if (repositoryConfig == null) {
            repositoryConfig = new RepositoryConfig(getDatabaseConfig());
        }
        return repositoryConfig;
    }

    public ServiceConfig getServiceConfig() {
        if (serviceConfig == null) {
            serviceConfig = new ServiceConfig(getRepositoryConfig());
        }
        return serviceConfig;
    }

    public PrinterConfig getPrinterConfig() {
        if (printerConfig == null) {
            printerConfig = new PrinterConfig();
        }
        return printerConfig;
    }

    public StrategyConfig getStrategyConfig(Scanner input) {
        if (strategyConfig == null) {
            strategyConfig = new StrategyConfig(getServiceConfig(), input);
        }
        return strategyConfig;
    }

    public NotificationConfig getNotificationConfig() {
        if (notificationConfig == null) {
            notificationConfig = new NotificationConfig();
        }
        return notificationConfig;
    }

    public ControllerConfig getControllerConfig(Scanner input) {
        if (controllerConfig == null) {
            controllerConfig = new ControllerConfig(getServiceConfig(), getPrinterConfig(), getStrategyConfig(input), input);
        }
        return controllerConfig;
    }

    public MenuConfig getMenuConfig(Scanner input) {
        if (menuConfig == null) {
            menuConfig = new MenuConfig(getControllerConfig(input), input);
        }
        return menuConfig;
    }

    public AuthConfig getAuthConfig(Scanner input) {
        if (authConfig == null) {
            authConfig = new AuthConfig(getServiceConfig(), getNotificationConfig(), getStrategyConfig(input), getMenuConfig(input), input);
        }
        return authConfig;
    }

    public SystemInitializer getSystemInitializer() {
        if (systemInitializer == null) {
            systemInitializer = new SystemInitializer(
                    getServiceConfig().getAdminService(),
                    getServiceConfig().getWalletService(),
                    getServiceConfig().getWalletCredentialService()
            );
        }
        return systemInitializer;
    }

    public Application createApplication(Scanner input) {
        getSystemInitializer().initialize();
        UserRoleMenu documentation = new Documentation();
        UserRoleMenu authMenu = new AuthMenu(input, getAuthConfig(input).getAuthController());
        return new Application(input, documentation, authMenu);
    }
}