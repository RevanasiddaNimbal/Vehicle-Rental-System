package database;

import java.sql.Connection;

public interface DatabaseConnection {
    Connection getConnection();
}
