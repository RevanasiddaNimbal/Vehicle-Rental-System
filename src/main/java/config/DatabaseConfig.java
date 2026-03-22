package config;

import database.DatabaseConnection;
import database.PostgresConnection;

public class DatabaseConfig {

    private DatabaseConnection postgresConnection;

    public DatabaseConnection getPostgresConnection() {
        if (postgresConnection == null) {
            postgresConnection = new PostgresConnection(new DbPropertiesConfig());
        }
        return postgresConnection;
    }
}