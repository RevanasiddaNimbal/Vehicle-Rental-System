package config;

import database.DatabaseConnection;
import database.PostgresConnection;

public class DatabaseConfig {
    private volatile DatabaseConnection postgresConnection;

    public DatabaseConnection getPostgresConnection() {
        if (postgresConnection == null) {
            synchronized (this) {
                if (postgresConnection == null) {
                    postgresConnection = new PostgresConnection(new DbPropertiesConfig());
                }
            }
        }
        return postgresConnection;
    }
}