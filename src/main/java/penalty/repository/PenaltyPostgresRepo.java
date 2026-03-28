package penalty.repository;

import database.DatabaseConnection;
import exception.DataAccessException;
import penalty.model.Penalty;
import penalty.model.PenaltyReason;
import penalty.model.PenaltyType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PenaltyPostgresRepo implements PenaltyRepo {

    private final DatabaseConnection databaseConnection;

    public PenaltyPostgresRepo(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public boolean save(Penalty penalty) {
        String sql = "INSERT INTO penalties (id, rental_id, vehicle_id, customer_id, amount, type, reason, issued_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setDbData(penalty, ps);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to save penalty", e);
        }
    }

    @Override
    public List<Penalty> findAll() {
        return fetchPenalties("SELECT * FROM penalties", null);
    }

    @Override
    public List<Penalty> findByVehicleId(String vehicleId) {
        return fetchPenalties("SELECT * FROM penalties WHERE vehicle_id = ?", vehicleId);
    }

    @Override
    public List<Penalty> findByCustomerId(String customerId) {
        return fetchPenalties("SELECT * FROM penalties WHERE customer_id = ?", customerId);
    }

    private List<Penalty> fetchPenalties(String sql, String param) {
        List<Penalty> penalties = new ArrayList<>();
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (param != null) {
                ps.setString(1, param);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    penalties.add(readDbData(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch penalties", e);
        }
        return penalties;
    }

    private void setDbData(Penalty penalty, PreparedStatement ps) throws SQLException {
        ps.setString(1, penalty.getId());
        ps.setInt(2, penalty.getRentalId());
        ps.setString(3, penalty.getVehicleId());
        ps.setString(4, penalty.getCustomerId());
        ps.setDouble(5, penalty.getPenaltyAmount());
        ps.setString(6, penalty.getType().name());
        ps.setString(7, penalty.getReason().name());
        ps.setDate(8, Date.valueOf(penalty.getIssuedDate()));
    }

    private Penalty readDbData(ResultSet rs) throws SQLException {
        return new Penalty(
                rs.getString("id"),
                rs.getInt("rental_id"),
                rs.getString("vehicle_id"),
                rs.getString("customer_id"),
                rs.getDouble("amount"),
                PenaltyType.valueOf(rs.getString("type")),
                PenaltyReason.valueOf(rs.getString("reason")),
                rs.getDate("issued_date").toLocalDate()
        );
    }
}