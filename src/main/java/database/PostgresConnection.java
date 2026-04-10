package database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import config.DbPropertiesConfig;
import exception.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;

public class PostgresConnection implements DatabaseConnection {
    private volatile HikariDataSource ds;
    private final DbPropertiesConfig config;

    public PostgresConnection(DbPropertiesConfig config) {
        this.config = config;
    }

    private void initDataSource() {
        try {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setPoolName("VehicleRentalPool");
            hikariConfig.setJdbcUrl(config.get("db.url"));
            hikariConfig.setMaximumPoolSize(10);
            hikariConfig.setMinimumIdle(2);
            hikariConfig.setIdleTimeout(30000);
            hikariConfig.setConnectionTimeout(30000);
            hikariConfig.setMaxLifetime(1800000);

            this.ds = new HikariDataSource(hikariConfig);
        } catch (Exception e) {
            throw new DataAccessException("Failed to create datasource", e);
        }
    }

    @Override
    public Connection getConnection() {
        if (ds == null) {
            synchronized (this) {
                if (ds == null) {
                    initDataSource();
                }
            }
        }
        try {
            return ds.getConnection();
        } catch (SQLException e) {
//            e.printStackTrace();
            throw new DataAccessException("Failed to create database connection", e);
        }
    }
}

