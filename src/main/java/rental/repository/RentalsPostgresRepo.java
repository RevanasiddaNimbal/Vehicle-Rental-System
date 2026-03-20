package rental.repository;

import database.DatabaseConnection;
import exception.DataAccessException;
import rental.model.Rental;
import rental.model.RentalStatus;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RentalsPostgresRepo implements RentalRepo {

    private final DatabaseConnection databaseConnection;

    public RentalsPostgresRepo(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public boolean save(Rental rental) {
        String query = "INSERT INTO rentals " +
                "(customer_id, vehicle_id, start_date, end_date, pickup_time, return_time, days, base_price, total_price, weekend_charge, discount, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"})) {
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
            throw new DataAccessException("Failed to save rental", e);
        }
    }

    @Override
    public boolean update(Rental rental) {
        String query = "UPDATE rentals SET " +
                "customer_id = ?, vehicle_id = ?, start_date = ?, end_date = ?, pickup_time = ?, return_time = ?, " +
                "days = ?, base_price = ?, total_price = ?, weekend_charge = ?, discount = ?, status = ? " +
                "WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            setDbData(ps, rental);
            ps.setInt(13, rental.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update rental", e);
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
            throw new DataAccessException("Failed to find rental by id", e);
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
            throw new DataAccessException("Failed to find all rentals", e);
        }
    }

    @Override
    public List<Rental> findAllByCustomerId(String customerId) {
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
            throw new DataAccessException("Failed to find rentals by customer", e);
        }

    }

    @Override
    public List<Rental> findAllByVehicleId(String vehicleId) {
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
            throw new DataAccessException("Failed to find rentals by vehicle", e);
        }

    }

    private void setDbData(PreparedStatement ps, Rental rental) {
        try {
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
            ps.setString(12, rental.getStatus().name());
        } catch (SQLException e) {
            throw new DataAccessException("Failed to set rental data", e);
        }

    }

    private Rental readDbData(ResultSet rs) {
        try {
            int id = rs.getInt("id");
            String customerId = rs.getString("customer_id");
            String vehicleId = rs.getString("vehicle_id");
            LocalDate startDate = rs.getDate("start_date").toLocalDate();
            LocalDate endDate = rs.getDate("end_date").toLocalDate();
            LocalTime startTime = rs.getTime("pickup_time").toLocalTime();
            LocalTime endTime = rs.getTime("return_time").toLocalTime();
            int days = rs.getInt("days");
            double basePrice = rs.getDouble("base_price");
            double totalPrice = rs.getDouble("total_price");
            double weekendCharge = rs.getDouble("weekend_charge");
            double discount = rs.getDouble("discount");
            RentalStatus status = RentalStatus.valueOf(rs.getString("status"));
            return new Rental(id, customerId, vehicleId, startDate, endDate, startTime, endTime, days, basePrice, totalPrice, weekendCharge, discount, status);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to map rental", e);
        }
    }
}