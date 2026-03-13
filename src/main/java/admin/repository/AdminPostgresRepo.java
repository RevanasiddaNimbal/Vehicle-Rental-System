package admin.repository;

import admin.model.Admin;
import database.DatabaseConnection;
import exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminPostgresRepo implements AdminRepo {
    private final DatabaseConnection databaseConnection;

    public AdminPostgresRepo(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public Admin findByEmail(String email) {
        String query = "SELECT * FROM Admins WHERE email = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("id");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String adminEmail = rs.getString("email");
                    return new Admin(id, username, adminEmail, password);

                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find admin by email.", e);
        }
    }
}
