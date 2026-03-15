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
                "(name, email, phone, password, address, active) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"})) {

            setDbData(owner, ps);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet id = ps.getGeneratedKeys();
                if (id.next()) {
                    owner.setId(id.getString("id"));
                }
            }
            return rows > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to save vehicle owner", e);
        }
    }

    @Override
    public boolean update(VehicleOwner owner) {
        String query = "UPDATE vehicle_owners SET " +
                "name=?, email=?, phone=?, password=?, address=?, active=? " +
                "WHERE id=?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            setDbDataForUpdate(owner, ps);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update vehicle owner", e);
        }
    }

    @Override
    public boolean deactivateById(String id) {

        String query = "UPDATE vehicle_owners SET active=false WHERE id=?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, id);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to deactivate vehicle owner", e);
        }
    }

    @Override
    public boolean activateById(String id) {

        String query = "UPDATE vehicle_owners SET active=true WHERE id=?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, id);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to activate vehicle owner", e);
        }
    }

    @Override
    public VehicleOwner findById(String id) {

        String query = "SELECT * FROM vehicle_owners WHERE id=?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return readDbResult(rs);
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find vehicle owner", e);
        }
    }

    @Override
    public List<VehicleOwner> findAll() {

        List<VehicleOwner> owners = new ArrayList<>();

        String query = "SELECT * FROM vehicle_owners";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                owners.add(readDbResult(rs));
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch vehicle owners", e);
        }

        return owners;
    }

    @Override
    public VehicleOwner findByEmail(String email) {

        String query = "SELECT * FROM vehicle_owners WHERE email=?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return readDbResult(rs);
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find vehicle owner by email", e);
        }
    }

    private VehicleOwner readDbResult(ResultSet rs) {
        try {
            String id = rs.getString("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String phone = rs.getString("phone");
            String password = rs.getString("password");
            String address = rs.getString("address");
            boolean isActive = rs.getBoolean("active");

            VehicleOwner owner = new VehicleOwner(id, name, email, phone, password, address, isActive);

            return owner;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to read vehicle owner", e);
        }
    }

    private void setDbData(VehicleOwner owner, PreparedStatement ps) {
        try {
            ps.setString(1, owner.getName());
            ps.setString(2, owner.getEmail());
            ps.setString(3, owner.getPhone());
            ps.setString(4, owner.getPassword());
            ps.setString(5, owner.getAddress());
            ps.setBoolean(6, owner.isActive());
        } catch (SQLException e) {
            throw new DataAccessException("Failed to set vehicle owner data", e);
        }
    }

    private void setDbDataForUpdate(VehicleOwner owner, PreparedStatement ps) {
        setDbData(owner, ps);
        try {
            ps.setString(7, owner.getId());
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update vehicle owner", e);
        }
    }
}
