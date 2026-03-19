package vehicle.repository;

import database.DatabaseConnection;
import exception.DataAccessException;
import vehicle.factory.VehicleFactory;
import vehicle.models.Category;
import vehicle.models.FuelType;
import vehicle.models.Status;
import vehicle.models.Vehicle;

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
        String query = "INSERT INTO vehicles" +
                "(vehicle_type, brand, category, price_per_day, status," +
                " engine_capacity, seating_capacity, fuel_type,owner_id)" +
                "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"})) {
            setDataForSave(vehicle, ps);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet id = ps.getGeneratedKeys();
                if (id.next()) {
                    vehicle.setId(id.getString("id"));
                }
            }
            return rows > 0;
        } catch (SQLException e) {
//            e.printStackTrace();
            throw new DataAccessException("Failed to save vehicle", e);
        }
    }

    @Override
    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Vehicle vehicle = readDbResult(rs);
                vehicles.add(vehicle);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find vehicles", e);
        }
        return vehicles;
    }

    @Override
    public Vehicle findById(String id) {
        String query = "SELECT * FROM vehicles WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return readDbResult(rs);
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find vehicle", e);
        }
    }

    @Override
    public List<Vehicle> findByOwnerId(String ownerId) {

        String query = "SELECT * FROM vehicles WHERE owner_id = ?";
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
        String query = "SELECT * FROM vehicles WHERE status = ?";
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
        String query = "DELETE FROM vehicles WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete vehicle", e);
        }
    }

    @Override
    public boolean update(Vehicle vehicle) {
        String query = "UPDATE vehicles SET  vehicle_type = ?,brand = ?,category = ?," +
                "price_per_day = ?,status = ?,engine_capacity = ?,seating_capacity = ?," +
                "fuel_type = ? WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            setDbDataForUpdate(vehicle, ps);
            int rows = ps.executeUpdate();
            return rows > 0;
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

            double pricePerDay = rs.getDouble("price_per_day");

            String statusStr = rs.getString("status");
            Status status = (statusStr != null) ? Status.valueOf(statusStr) : null;

            int engineCapacity = rs.getInt("engine_capacity");
            int seatingCapacity = rs.getInt("seating_capacity");

            String fuel = rs.getString("fuel_type");
            FuelType fuelType = fuel != null ? FuelType.valueOf(fuel) : null;
            String ownerId = rs.getString("owner_id");

            return VehicleFactory.createVehicle(vehicleType, vehicleId, brand, category, pricePerDay, status, engineCapacity, seatingCapacity, fuelType, ownerId);


        } catch (SQLException e) {
            throw new DataAccessException("Failed to read vehicle", e);
        }
    }

    private void setDbDataForUpdate(Vehicle vehicle, PreparedStatement ps) {
        try {
            setDbData(vehicle, ps);
            ps.setString(9, vehicle.getId());
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update vehicle", e);
        }
    }

    private void setDataForSave(Vehicle vehicle, PreparedStatement ps) {
        try {
            setDbData(vehicle, ps);
            ps.setString(9, vehicle.getOwnerId());

        } catch (SQLException e) {
            throw new DataAccessException("Failed to save vehicle", e);
        }
    }

    private void setDbData(Vehicle vehicle, PreparedStatement ps) {
        try {
            ps.setString(1, vehicle.getVehicle_type());
            ps.setString(2, vehicle.getBrand());
            ps.setString(3, vehicle.getCategory().name());
            ps.setDouble(4, vehicle.getPricePerDay());
            ps.setString(5, vehicle.getStatus().name());
            if (vehicle.getEnginCapacity() != null)
                ps.setInt(6, vehicle.getEnginCapacity());
            else
                ps.setNull(6, Types.INTEGER);

            if (vehicle.getSeatingCapacity() != null)
                ps.setInt(7, vehicle.getSeatingCapacity());
            else
                ps.setNull(7, Types.INTEGER);

            if (vehicle.getFuelType() != null)
                ps.setString(8, vehicle.getFuelType().name());
            else
                ps.setNull(8, Types.VARCHAR);

        } catch (SQLException e) {
            throw new DataAccessException("Failed to set vehicle", e);
        }
    }
}
