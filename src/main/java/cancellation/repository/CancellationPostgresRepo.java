package cancellation.repository;

import cancellation.model.CancellationRecord;
import cancellation.model.PolicyType;
import database.DatabaseConnection;
import exception.DataAccessException;

import java.sql.*;
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
                "(id, customer_id, owner_id, rental_id, vehicle_id, cancellation_time, reason, refund_amount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, record.getId());
            ps.setString(2, record.getCustomerId());
            ps.setString(3, record.getOwnerId());
            ps.setInt(4, record.getRentalId());
            ps.setString(5, record.getVehicleId());
            ps.setTimestamp(6, Timestamp.valueOf(record.getCanceledAt()));
            ps.setString(7, record.getPolicyApplied().name());
            ps.setDouble(8, record.getRefundAmount());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to save cancellation record", e);
        }
    }

    @Override
    public List<CancellationRecord> findAll() {
        String query = "SELECT * FROM cancellations";
        return fetchRecords(query, null);
    }

    @Override
    public List<CancellationRecord> findByCustomerId(String customerId) {
        String query = "SELECT * FROM cancellations WHERE customer_id = ?";
        return fetchRecords(query, customerId);
    }

    @Override
    public List<CancellationRecord> findByOwnerId(String ownerId) {
        String query = "SELECT * FROM cancellations WHERE owner_id = ?";
        return fetchRecords(query, ownerId);
    }

    private List<CancellationRecord> fetchRecords(String query, String parameter) {
        List<CancellationRecord> records = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            if (parameter != null) {
                ps.setString(1, parameter);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(readDbData(rs));
                }
            }
            return records;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch cancellations", e);
        }
    }

    private CancellationRecord readDbData(ResultSet rs) throws SQLException {
        return new CancellationRecord(
                rs.getString("id"),
                rs.getInt("rental_id"),
                rs.getString("customer_id"),
                rs.getString("vehicle_id"),
                rs.getString("owner_id"),
                PolicyType.valueOf(rs.getString("reason")),
                rs.getDouble("refund_amount"),
                rs.getTimestamp("cancellation_time").toLocalDateTime()
        );
    }
}