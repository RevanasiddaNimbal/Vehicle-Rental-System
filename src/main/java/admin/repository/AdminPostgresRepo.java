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
    public boolean save(Admin admin) {
        String query = "INSERT INTO Admins (id, username, email, password, is_super_admin) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, admin.getId());
            ps.setString(2, admin.getUsername());
            ps.setString(3, admin.getEmail());
            ps.setString(4, admin.getPassword());
            ps.setBoolean(5, admin.isSuperAdmin());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to save admin.", e);
        }
    }

    @Override
    public Admin findById(String id) {
        String query = "SELECT * FROM Admins WHERE id=?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return readDb(rs);
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find admin by id", e);
        }
    }

    @Override
    public Admin findByEmail(String email) {
        String query = "SELECT * FROM Admins WHERE email = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return readDb(rs);
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find admin by email.", e);
        }
    }

    @Override
    public boolean update(Admin admin) {
        String query = "UPDATE Admins SET username = ?, email = ?, password = ?, is_super_admin = ? WHERE id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, admin.getUsername());
            ps.setString(2, admin.getEmail());
            ps.setString(3, admin.getPassword());
            ps.setBoolean(4, admin.isSuperAdmin());
            ps.setString(5, admin.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new exception.DuplicateResourceException("Database rejected update: The email '" + admin.getEmail() + "' is already in use.");
            }
            throw new DataAccessException("Failed to update admin.", e);
        }
    }

    private Admin readDb(ResultSet rs) {
        try {
            String id = rs.getString("id");
            String username = rs.getString("username");
            String password = rs.getString("password");
            String adminEmail = rs.getString("email");
            boolean isSuperAdmin = false;
            try {
                isSuperAdmin = rs.getBoolean("is_super_admin");
            } catch (SQLException ignored) {
                if (id.equals("ADM-001")) isSuperAdmin = true;
            }

            return new Admin(id, username, adminEmail, password, isSuperAdmin);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to read admin.", e);
        }
    }
}