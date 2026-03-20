package penalty.repository;

import database.DatabaseConnection;
import exception.DataAccessException;
import penalty.model.Penalty;
import penalty.model.PenaltyReason;
import penalty.model.PenaltyType;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PenaltyPostgresRepo implements PenaltyRepo {

    private final DatabaseConnection databaseConnection;

    public PenaltyPostgresRepo(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public boolean save(Penalty penalty) {
        String sql = "INSERT INTO penalties (rental_id, vehicle_id, customer_id, amount, type, reason, issued_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"})) {

            setDbData(penalty, ps);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        penalty.setId(generatedKeys.getString(1));  // Set auto-generated ID back to object
                    }
                }
            }
            return rows > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to save penalty", e);
        }
    }

    @Override
    public List<Penalty> findAll() {
        List<Penalty> penalties = new ArrayList<>();
        String sql = "SELECT * FROM penalties";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                penalties.add(readDbData(rs));
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch penalties", e);
        }

        return penalties;
    }

    @Override
    public List<Penalty> findByVehicleId(String vehicleId) {
        List<Penalty> penalties = new ArrayList<>();
        String sql = "SELECT * FROM penalties WHERE vehicle_id = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, vehicleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    penalties.add(readDbData(rs));
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch penalties by vehicle ID", e);
        }

        return penalties;
    }

    @Override
    public List<Penalty> findByCustomerId(String customerId) {
        List<Penalty> penalties = new ArrayList<>();
        String sql = "SELECT * FROM penalties WHERE customer_id = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    penalties.add(readDbData(rs));
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch penalties by customer ID", e);
        }

        return penalties;
    }

    private Penalty readDbData(ResultSet rs) {
        try {
            String id = rs.getString("id");
            int rentalId = rs.getInt("rental_id");
            String vehicleId = rs.getString("vehicle_id");
            String customerId = rs.getString("customer_id");
            double amount = rs.getDouble("amount");
            PenaltyType type = PenaltyType.valueOf(rs.getString("type"));
            PenaltyReason reason = PenaltyReason.valueOf(rs.getString("reason"));
            LocalDate issuedDate = rs.getDate("issued_date").toLocalDate();
            return new Penalty(id, rentalId, vehicleId, customerId, amount, type, reason, issuedDate);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to readDbData.", e);
        }
    }

    private void setDbData(Penalty penalty, PreparedStatement ps) {
        try {
            ps.setInt(1, penalty.getRentalId());
            ps.setString(2, penalty.getVehicleId());
            ps.setString(3, penalty.getCustomerId());
            ps.setDouble(4, penalty.getAmount());
            ps.setString(5, penalty.getType().name());
            ps.setString(6, penalty.getReason().name());
            ps.setDate(7, Date.valueOf(penalty.getIssuedDate()));
        } catch (SQLException e) {
            throw new DataAccessException("Failed to set penalty data", e);
        }
    }
}