package cancellation.repository;

import cancellation.model.CancellationRecord;
import cancellation.model.PolicyType;
import database.DatabaseConnection;
import exception.DataAccessException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CancellationPostgresRepo implements CancellationRepo {

    private final DatabaseConnection databaseConnection;

    public CancellationPostgresRepo(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public boolean save(CancellationRecord record) {
        String query = "INSERT INTO cancellations " +
                "(customer_id, owner_id, rental_id, vehicle_id,cancellation_time, reason, refund_amount) " +
                "VALUES (?, ?, ?, ?, ?, ?,?)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"})) {

            setDbData(ps, record);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        record.setId(rs.getString(1)); // 🔥 IMPORTANT
                    }
                }
            }

            return rows > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to save cancellation record", e);
        }
    }

    @Override
    public List<CancellationRecord> findAll() {
        String query = "SELECT * FROM cancellations";
        List<CancellationRecord> records = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                records.add(readDbData(rs));
            }
            return records;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch cancellations", e);
        }
    }

    @Override
    public List<CancellationRecord> findByCustomerId(String customerId) {
        String query = "SELECT * FROM cancellations WHERE customer_id = ?";
        List<CancellationRecord> records = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(readDbData(rs));
                }
            }
            return records;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch cancellations by customer", e);
        }
    }

    @Override
    public List<CancellationRecord> findByOwnerId(String ownerId) {
        String query = "SELECT * FROM cancellations WHERE owner_id = ?";
        List<CancellationRecord> records = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, ownerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(readDbData(rs));
                }
            }
            return records;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch cancellations by owner", e);
        }
    }

    private void setDbData(PreparedStatement ps, CancellationRecord record) {
        try {
            ps.setString(1, record.getCustomerId());
            ps.setString(2, record.getOwnerId());
            ps.setInt(3, record.getRentalId());
            ps.setString(4, record.getVehicleId());
            ps.setTimestamp(5, Timestamp.valueOf(record.getCanceledAt()));
            ps.setString(6, record.getPolicyApplied().name());
            ps.setDouble(7, record.getRefundAmount());

        } catch (SQLException e) {
            throw new DataAccessException("Failed to set cancellation data", e);
        }
    }

    private CancellationRecord readDbData(ResultSet rs) {
        try {
            String id = rs.getString("id");
            String customerId = rs.getString("customer_id");
            String ownerId = rs.getString("owner_id");
            int rentalId = rs.getInt("rental_id");
            String vehicleId = rs.getString("vehicle_id");
            PolicyType reason = PolicyType.valueOf(rs.getString("reason"));
            double refundAmount = rs.getDouble("refund_amount");
            LocalDateTime cancellationTime = rs.getTimestamp("cancellation_time").toLocalDateTime();

            return new CancellationRecord(id, rentalId, customerId, vehicleId, ownerId, reason, refundAmount, cancellationTime);

        } catch (SQLException e) {
            throw new DataAccessException("Failed to map cancellation record", e);
        }
    }
}