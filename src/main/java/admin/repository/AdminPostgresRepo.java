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
    public Admin findById(String id) {
        String query = "SELECT * FROM Admins WHERE id=?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return readDb(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find vehicle owner", e);
        }
    }

    @Override
    public boolean update(Admin admin) {
        String query = "UPDATE Admins SET username = ?, email = ?, password = ? WHERE id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, admin.getUsername());
            ps.setString(2, admin.getEmail());
            ps.setString(3, admin.getPassword());
            ps.setString(4, admin.getId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update admin.", e);
        }
    }

    @Override
    public Admin findByEmail(String email) {
        String query = "SELECT * FROM Admins WHERE email = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return readDb(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find admin by email.", e);
        }
    }

    private Admin readDb(ResultSet rs) {
        try {
            String id = rs.getString("id");
            String username = rs.getString("username");
            String password = rs.getString("password");
            String adminEmail = rs.getString("email");
            return new Admin(id, username, adminEmail, password);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to read admin.", e);
        }

    }
}
