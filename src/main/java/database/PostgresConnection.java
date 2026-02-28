package database;

import config.DbConfig;
import exception.DataAccessException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection implements DatabaseConnection {
    private final DbConfig config;

    public PostgresConnection(DbConfig config) {
        this.config = config;
    }

    @Override
    public Connection getConnection() {
        try {
            String url = config.get("db.url");
            String username = config.get("db.username");
            String password = config.get("db.password");

            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
//            e.printStackTrace();
            throw new DataAccessException("Failed to create database connection", e);
        }
    }
}

