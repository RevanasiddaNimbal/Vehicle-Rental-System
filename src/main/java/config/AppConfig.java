package config;

import UI.AuthMenu;
import UI.Documentation;
import UI.UserRoleMenu;
import application.Application;
import initializer.SystemInitializer;

import java.util.Scanner;

public class AppConfig {

    private volatile DatabaseConfig databaseConfig;
    private volatile RepositoryConfig repositoryConfig;
    private volatile ServiceConfig serviceConfig;
    private volatile PrinterConfig printerConfig;
    private volatile StrategyConfig strategyConfig;
    private volatile ControllerConfig controllerConfig;
    private volatile MenuConfig menuConfig;
    private volatile AuthConfig authConfig;
    private volatile SystemInitializer systemInitializer;
    private volatile CommandConfig commandConfig;

    public DatabaseConfig getDatabaseConfig() {
        if (databaseConfig == null) {
            synchronized (this) {
                if (databaseConfig == null) {
                    databaseConfig = new DatabaseConfig();
                }
            }
        }
        return databaseConfig;
    }

    public RepositoryConfig getRepositoryConfig() {
        if (repositoryConfig == null) {
            synchronized (this) {
                if (repositoryConfig == null) {
                    repositoryConfig = new RepositoryConfig(getDatabaseConfig());
                }
            }
        }
        return repositoryConfig;
    }

    public ServiceConfig getServiceConfig() {
        if (serviceConfig == null) {
            synchronized (this) {
                if (serviceConfig == null) {
                    serviceConfig = new ServiceConfig(getRepositoryConfig());
                }
            }
        }
        return serviceConfig;
    }

    public PrinterConfig getPrinterConfig() {
        if (printerConfig == null) {
            synchronized (this) {
                if (printerConfig == null) {
                    printerConfig = new PrinterConfig();
                }
            }
        }
        return printerConfig;
    }

    public StrategyConfig getStrategyConfig(Scanner input) {
        if (strategyConfig == null) {
            synchronized (this) {
                if (strategyConfig == null) {
                    strategyConfig = getServiceConfig().getStrategyConfig(input);
                }
            }
        }
        return strategyConfig;
    }

    public CommandConfig getCommandConfig() {
        if (commandConfig == null) {
            synchronized (this) {
                if (commandConfig == null) {
                    commandConfig = new CommandConfig(getPrinterConfig(), getServiceConfig());
                }
            }
        }
        return commandConfig;
    }

    public ControllerConfig getControllerConfig(Scanner input) {
        if (controllerConfig == null) {
            synchronized (this) {
                if (controllerConfig == null) {
                    controllerConfig =
                            new ControllerConfig(
                                    getServiceConfig(),
                                    getPrinterConfig(),
                                    getStrategyConfig(input),
                                    getCommandConfig(),
                                    input
                            );
                }
            }
        }
        return controllerConfig;
    }

    public MenuConfig getMenuConfig(Scanner input) {
        if (menuConfig == null) {
            synchronized (this) {
                if (menuConfig == null) {
                    menuConfig = new MenuConfig(getControllerConfig(input), input);
                }
            }
        }
        return menuConfig;
    }

    public AuthConfig getAuthConfig(Scanner input) {
        if (authConfig == null) {
            synchronized (this) {
                if (authConfig == null) {
                    authConfig = new AuthConfig(getStrategyConfig(input), getMenuConfig(input));
                }
            }
        }
        return authConfig;
    }

    public SystemInitializer getSystemInitializer() {
        if (systemInitializer == null) {
            synchronized (this) {
                if (systemInitializer == null) {
                    systemInitializer = new SystemInitializer(getServiceConfig());
                }
            }
        }
        return systemInitializer;
    }

    public Application createApplication(Scanner input) {
        UserRoleMenu documentation = new Documentation();
        UserRoleMenu authMenu = new AuthMenu(input, getAuthConfig(input));

        return new Application(
                input,
                documentation,
                authMenu,
                getServiceConfig()
        );
    }
}