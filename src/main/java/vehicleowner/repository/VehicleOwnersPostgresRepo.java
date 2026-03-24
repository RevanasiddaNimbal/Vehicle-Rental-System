package vehicleowner.repository;

import database.DatabaseConnection;
import exception.DataAccessException;
import vehicleowner.models.VehicleOwner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleOwnersPostgresRepo implements VehicleOwnerRepo {
    private final DatabaseConnection databaseConnection;

    public VehicleOwnersPostgresRepo(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public boolean save(VehicleOwner owner) {
        String query = "INSERT INTO vehicle_owners " +
                "(id, name, email, phone, password, address, active) " +
                "VALUES (?, ?, ?, ?, ?, ?, TRUE)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, owner.getId());
            setDbData(owner, ps, 2);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to save vehicle owner", e);
        }
    }

    @Override
    public boolean update(VehicleOwner owner) {
        String query = "UPDATE vehicle_owners SET " +
                "name = ?, email = ?, phone = ?, password = ?, address = ? " +
                "WHERE id = ? AND active = TRUE";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            setDbData(owner, ps, 1);
            ps.setString(6, owner.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update vehicle owner", e);
        }
    }

    @Override
    public boolean deactivateById(String id) {
        String query = "UPDATE vehicle_owners SET active = FALSE WHERE id = ? AND active = TRUE";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to deactivate vehicle owner (soft delete)", e);
        }
    }

    @Override
    public boolean activateById(String id) {
        String query = "UPDATE vehicle_owners SET active = TRUE WHERE id = ? AND active = FALSE";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to activate vehicle owner", e);
        }
    }

    @Override
    public VehicleOwner findById(String id) {
        String query = "SELECT * FROM vehicle_owners WHERE id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? readDbResult(rs) : null;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find vehicle owner by id", e);
        }
    }

    @Override
    public VehicleOwner findByEmail(String email) {
        String query = "SELECT * FROM vehicle_owners WHERE email = ? AND active = TRUE";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? readDbResult(rs) : null;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find vehicle owner by email", e);
        }
    }

    @Override
    public List<VehicleOwner> findAll() {
        String query = "SELECT * FROM vehicle_owners";
        List<VehicleOwner> owners = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                owners.add(readDbResult(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find vehicle owners", e);
        }
        return owners;
    }

    private VehicleOwner readDbResult(ResultSet rs) {
        try {
            String id = rs.getString("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String phone = rs.getString("phone");
            String password = rs.getString("password");
            String address = rs.getString("address");
            boolean active = rs.getBoolean("active");

            VehicleOwner owner = new VehicleOwner(id, name, email, phone, password, address);

            if (!active) {
                owner.deactivate();
            }

            return owner;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to read vehicle owner from result set", e);
        }
    }

    private void setDbData(VehicleOwner owner, PreparedStatement ps, int startIndex) throws SQLException {
        ps.setString(startIndex, owner.getName());
        ps.setString(startIndex + 1, owner.getEmail());
        ps.setString(startIndex + 2, owner.getPhone());
        ps.setString(startIndex + 3, owner.getPassword());
        ps.setString(startIndex + 4, owner.getAddress());
    }
}