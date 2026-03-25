package otp.repository;

import database.DatabaseConnection;
import exception.DataAccessException;
import otp.model.OtpData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OtpPostgresRepo implements OtpRepo {
    private final DatabaseConnection databaseConnection;

    public OtpPostgresRepo(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public void save(OtpData otpData) {
        String query = "INSERT INTO otps (email, code, expiry_time) VALUES (?, ?, ?) " +
                "ON CONFLICT (email) DO UPDATE SET code = EXCLUDED.code, expiry_time = EXCLUDED.expiry_time";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, otpData.getEmail());
            ps.setString(2, otpData.getCode());
            ps.setLong(3, otpData.getExpiryTimeMillis());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Failed to save OTP data.", e);
        }
    }

    @Override
    public OtpData findByEmail(String email) {
        String query = "SELECT * FROM otps WHERE email = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new OtpData(
                            rs.getString("email"),
                            rs.getString("code"),
                            rs.getLong("expiry_time")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch OTP data.", e);
        }
    }

    @Override
    public void deleteByEmail(String email) {
        String query = "DELETE FROM otps WHERE email = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, email);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete OTP data.", e);
        }
    }
}