package vehicle.repository;

import database.DatabaseConnection;
import exception.DataAccessException;
import vehicle.factory.VehicleFactory;
import vehicle.models.Category;
import vehicle.models.FuelType;
import vehicle.models.Status;
import vehicle.models.Vehicle;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiclesPostgresRepo implements VehicleRepo {
    private final DatabaseConnection databaseConnection;

    public VehiclesPostgresRepo(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public boolean save(Vehicle vehicle) {
        String query = "INSERT INTO vehicles " +
                "(vehicle_type, brand, category, price_per_day, status, engine_capacity, seating_capacity, fuel_type, owner_id, id, active, deleted_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, TRUE, NULL)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            setDbData(vehicle, ps);
            ps.setString(9, vehicle.getOwnerId());
            ps.setString(10, vehicle.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to save vehicle", e);
        }
    }

    @Override
    public List<Vehicle> findAll() {
        String query = "SELECT * FROM vehicles WHERE active = TRUE";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            List<Vehicle> vehicles = new ArrayList<>();
            while (rs.next()) {
                vehicles.add(readDbResult(rs));
            }
            return vehicles;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find vehicles", e);
        }
    }

    @Override
    public Vehicle findById(String id) {
        String query = "SELECT * FROM vehicles WHERE id = ? AND active = TRUE";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? readDbResult(rs) : null;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find vehicle", e);
        }
    }

    @Override
    public List<Vehicle> findByOwnerId(String ownerId) {
        String query = "SELECT * FROM vehicles WHERE owner_id = ? AND active = TRUE";
        List<Vehicle> vehicles = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, ownerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    vehicles.add(readDbResult(rs));
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find vehicles by owner", e);
        }

        return vehicles;
    }

    @Override
    public List<Vehicle> findByStatus(Status status) {
        String query = "SELECT * FROM vehicles WHERE status = ? AND active = TRUE";
        List<Vehicle> vehicles = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, status.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    vehicles.add(readDbResult(rs));
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find vehicles by status", e);
        }

        return vehicles;
    }

    @Override
    public boolean deleteById(String id) {
        String query = "UPDATE vehicles SET active = FALSE, deleted_at = NOW() " +
                "WHERE id = ? AND active = TRUE";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete vehicle (soft delete)", e);
        }
    }

    @Override
    public boolean update(Vehicle vehicle) {
        String query = "UPDATE vehicles SET vehicle_type = ?, brand = ?, category = ?, " +
                "price_per_day = ?, status = ?, engine_capacity = ?, seating_capacity = ?, fuel_type = ? " +
                "WHERE id = ? AND active = TRUE";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            setDbData(vehicle, ps);
            ps.setString(9, vehicle.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update vehicle", e);
        }
    }

    private Vehicle readDbResult(ResultSet rs) {
        try {
            String vehicleId = rs.getString("id");
            String vehicleType = rs.getString("vehicle_type");
            String brand = rs.getString("brand");

            String categoryStr = rs.getString("category");
            Category category = categoryStr != null ? Category.valueOf(categoryStr) : null;

            BigDecimal price = rs.getBigDecimal("price_per_day");
            double pricePerDay = price != null ? price.doubleValue() : 0.0;

            String statusStr = rs.getString("status");
            Status status = (statusStr != null) ? Status.valueOf(statusStr) : null;

            int engineCapacity = rs.getInt("engine_capacity");
            if (rs.wasNull()) engineCapacity = 0;

            int seatingCapacity = rs.getInt("seating_capacity");
            if (rs.wasNull()) seatingCapacity = 0;

            String fuel = rs.getString("fuel_type");
            FuelType fuelType = fuel != null ? FuelType.valueOf(fuel) : null;

            String ownerId = rs.getString("owner_id");

            Vehicle v = VehicleFactory.createVehicle(
                    vehicleType, vehicleId, brand, category, pricePerDay, status,
                    engineCapacity, seatingCapacity, fuelType, ownerId
            );

            boolean active = rs.getBoolean("active");
            v.setActive(active);

            return v;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to read vehicle", e);
        }
    }

    private void setDbData(Vehicle vehicle, PreparedStatement ps) throws SQLException {
        ps.setString(1, vehicle.getVehicle_type());
        ps.setString(2, vehicle.getBrand());
        ps.setString(3, vehicle.getCategory().name());
        ps.setBigDecimal(4, BigDecimal.valueOf(vehicle.getPricePerDay()));
        ps.setString(5, vehicle.getStatus().name());

        Integer engine = vehicle.getEnginCapacity();
        if (engine != null) ps.setInt(6, engine);
        else ps.setNull(6, Types.INTEGER);

        Integer seating = vehicle.getSeatingCapacity();
        if (seating != null) ps.setInt(7, seating);
        else ps.setNull(7, Types.INTEGER);

        FuelType fuelType = vehicle.getFuelType();
        if (fuelType != null) ps.setString(8, fuelType.name());
        else ps.setNull(8, Types.VARCHAR);
    }
}