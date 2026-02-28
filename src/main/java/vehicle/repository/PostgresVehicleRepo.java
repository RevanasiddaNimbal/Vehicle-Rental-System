package vehicle.repository;

import database.DatabaseConnection;
import exception.DataAccessException;
import vehicle.Models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresVehicleRepo implements VehicleRepo {
    private DatabaseConnection databaseConnection;

    public PostgresVehicleRepo(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public boolean save(Vehicle vehicle) {
        String query = "INSERT INTO vehicles" +
                "(vehicle_type, brand, category, price_per_day, status," +
                " engine_capacity, seating_capacity, fuel_type)" +
                "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
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

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
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
                String id = rs.getString("id");
                String vehicleType = rs.getString("vehicle_type");
                String brand = rs.getString("brand");
                Category category = Category.valueOf(rs.getString("category"));
                double pricePerDay = rs.getDouble("price_per_day");
                Status status = Status.valueOf(rs.getString("status"));

                Integer engineCapacity = (Integer) rs.getObject("engine_capacity");
                Integer seatingCapacity = (Integer) rs.getObject("seating_capacity");
                FuelType fuelType = FuelType.valueOf(rs.getString("fuel_type"));

                Vehicle vehicle = null;
                switch (vehicleType.toUpperCase()) {
                    case "BIKE":
                        vehicle = new Bike(
                                id,
                                brand,
                                category,
                                pricePerDay,
                                fuelType,
                                engineCapacity
                        );
                        break;
                    case "CAR":
                        vehicle = new Car(
                                id,
                                brand,
                                category,
                                pricePerDay,
                                seatingCapacity,
                                fuelType
                        );
                        break;

                    case "AUTO":
                        vehicle = new Auto(
                                id,
                                brand,
                                category,
                                pricePerDay,
                                seatingCapacity
                        );
                        break;

                    default:
                        throw new RuntimeException("Unknown vehicle type: " + vehicleType);
                }
                vehicles.add(vehicle);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find vehicles", e);
        }
        return vehicles;
    }

    @Override
    public Vehicle findById(String id) {
        return null;
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }

    @Override
    public boolean update(Vehicle vehicle) {
        return false;
    }
}
