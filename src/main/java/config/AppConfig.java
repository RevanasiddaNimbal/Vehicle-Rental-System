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
    private ControllerConfig controllerConfig;
    private MenuConfig menuConfig;
    private AuthConfig authConfig;
    private SystemInitializer systemInitializer;
    private CommandConfig commandConfig;

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
            strategyConfig = serviceConfig.getStrategyConfig(input);
        }
        return strategyConfig;
    }

    public CommandConfig getCommandConfig() {
        if (commandConfig == null) {
            commandConfig = new CommandConfig(getPrinterConfig(), getServiceConfig());
        }
        return commandConfig;
    }

    public ControllerConfig getControllerConfig(Scanner input) {
        if (controllerConfig == null) {
            controllerConfig = new ControllerConfig(getServiceConfig(), getPrinterConfig(), getStrategyConfig(input), getCommandConfig(), input);
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
            authConfig = new AuthConfig(getServiceConfig(), getStrategyConfig(input), getMenuConfig(input));
        }
        return authConfig;
    }

    // for in memory use getSystemInitializer.
    public SystemInitializer getSystemInitializer() {
        if (systemInitializer == null) {
            systemInitializer = new SystemInitializer(getServiceConfig());
        }
        return systemInitializer;
    }

    public Application createApplication(Scanner input) {
        UserRoleMenu documentation = new Documentation();
        UserRoleMenu authMenu = new AuthMenu(input, getAuthConfig(input));
        return new Application(input, documentation, authMenu, getServiceConfig());
    }
}