package rental.repository;

import database.DatabaseConnection;
import exception.DataAccessException;
import rental.model.Rental;
import rental.model.RentalStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalsPostgresRepo implements RentalRepo {
    private final DatabaseConnection databaseConnection;

    public RentalsPostgresRepo(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public boolean save(Rental rental) {
        if (rental == null) throw new IllegalArgumentException("Cannot save a null rental.");
        
        String query = "INSERT INTO rentals (customer_id, vehicle_id, start_date, end_date, pickup_time, return_time, days, base_price, total_price, weekend_charge, discount, security_deposit, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setDbData(ps, rental);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        rental.setId(rs.getInt(1));
                    }
                }
            }
            return rows > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to save rental record to the database.", e);
        }
    }

    @Override
    public boolean update(Rental rental) {
        if (rental == null) throw new IllegalArgumentException("Cannot update a null rental.");

        String query = "UPDATE rentals SET customer_id = ?, vehicle_id = ?, start_date = ?, end_date = ?, pickup_time = ?, return_time = ?, days = ?, base_price = ?, total_price = ?, weekend_charge = ?, discount = ?, security_deposit = ?, status = ? WHERE id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            setDbData(ps, rental);
            ps.setInt(14, rental.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update rental record in the database.", e);
        }
    }

    @Override
    public Rental findById(int id) {
        String query = "SELECT * FROM rentals WHERE id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return readDbData(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch rental by ID.", e);
        }
    }

    @Override
    public List<Rental> findAll() {
        String query = "SELECT * FROM rentals";
        List<Rental> rentals = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rentals.add(readDbData(rs));
            }
            return rentals;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch all rentals.", e);
        }
    }

    @Override
    public List<Rental> findAllByCustomerId(String customerId) {
        if (customerId == null) throw new IllegalArgumentException("Customer ID cannot be null.");

        String query = "SELECT * FROM rentals WHERE customer_id = ?";
        List<Rental> rentals = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rentals.add(readDbData(rs));
                }
            }
            return rentals;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch rentals for customer.", e);
        }
    }

    @Override
    public List<Rental> findAllByVehicleId(String vehicleId) {
        if (vehicleId == null) throw new IllegalArgumentException("Vehicle ID cannot be null.");

        String query = "SELECT * FROM rentals WHERE vehicle_id = ?";
        List<Rental> rentals = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, vehicleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rentals.add(readDbData(rs));
                }
            }
            return rentals;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch rentals for vehicle.", e);
        }
    }

    private void setDbData(PreparedStatement ps, Rental rental) throws SQLException {
        ps.setString(1, rental.getCustomerId());
        ps.setString(2, rental.getVehicleId());
        ps.setDate(3, Date.valueOf(rental.getStartDate()));
        ps.setDate(4, Date.valueOf(rental.getEndDate()));
        ps.setTime(5, Time.valueOf(rental.getStartTime()));
        ps.setTime(6, Time.valueOf(rental.getEndTime()));
        ps.setInt(7, rental.getDays());
        ps.setDouble(8, rental.getBasePrice());
        ps.setDouble(9, rental.getTotalPrice());
        ps.setDouble(10, rental.getWeekendCharge());
        ps.setDouble(11, rental.getDiscount());
        ps.setDouble(12, rental.getSecurityDeposit()); // NEW FIELD
        ps.setString(13, rental.getStatus().name());   // Shifted to 13
    }

    private Rental readDbData(ResultSet rs) throws SQLException {
        return new Rental(
                rs.getInt("id"),
                rs.getString("customer_id"),
                rs.getString("vehicle_id"),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate(),
                rs.getTime("pickup_time").toLocalTime(),
                rs.getTime("return_time").toLocalTime(),
                rs.getInt("days"),
                rs.getDouble("base_price"),
                rs.getDouble("total_price"),
                rs.getDouble("weekend_charge"),
                rs.getDouble("discount"),
                rs.getDouble("security_deposit"), // NEW FIELD
                RentalStatus.valueOf(rs.getString("status"))
        );
    }
}