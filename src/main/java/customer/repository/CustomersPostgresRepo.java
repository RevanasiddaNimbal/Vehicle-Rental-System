package customer.repository;

import customer.model.Customer;
import database.DatabaseConnection;
import exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomersPostgresRepo implements CustomerRepo {

    private final DatabaseConnection databaseConnection;

    public CustomersPostgresRepo(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public boolean save(Customer customer) {

        String query = "INSERT INTO customers " +
                "(name, email, phone, address, driving_license_number, password, active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"})) {
            setDbData(customer, ps);
            
            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet id = ps.getGeneratedKeys();
                if (id.next()) {
                    customer.setId(id.getString("id"));
                }
            }

            return rows > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to save customer", e);
        }
    }

    @Override
    public boolean update(Customer customer) {

        String query = "UPDATE customers SET " +
                "name=?, email=?, phone=?, address=?, driving_license_number=?, password=?, active=? " +
                "WHERE id=?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            setDbDataForUpdate(customer, ps);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update customer", e);
        }
    }

    @Override
    public boolean deactivateById(String id) {

        String query = "UPDATE customers SET active=false WHERE id=?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, id);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to deactivate customer", e);
        }
    }

    @Override
    public boolean activateById(String id) {

        String query = "UPDATE customers SET active=true WHERE id=?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, id);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to activate customer", e);
        }
    }

    @Override
    public Customer findById(String id) {

        String query = "SELECT * FROM customers WHERE id=?";

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
            throw new DataAccessException("Failed to find customer", e);
        }
    }

    @Override
    public List<Customer> findAll() {

        List<Customer> customers = new ArrayList<>();

        String query = "SELECT * FROM customers";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                customers.add(readDbResult(rs));
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch customers", e);
        }

        return customers;
    }

    @Override
    public Customer findByEmail(String email) {

        String query = "SELECT * FROM customers WHERE email=?";

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
            throw new DataAccessException("Failed to find customer by email", e);
        }
    }

    private Customer readDbResult(ResultSet rs) {
        try {
            String id = rs.getString("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String phone = rs.getString("phone");
            String address = rs.getString("address");
            String drivingLicense = rs.getString("driving_license_number");
            String password = rs.getString("password");
            boolean active = rs.getBoolean("active");

            return new Customer(id, name, email, phone, address, drivingLicense, password, active);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to read customer", e);
        }
    }

    private void setDbData(Customer customer, PreparedStatement ps) {

        try {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhone());
            ps.setString(4, customer.getAddress());
            ps.setString(5, customer.getDrivingLicenseNumber());
            ps.setString(6, customer.getPassword());
            ps.setBoolean(7, customer.isActive());
        } catch (SQLException e) {
            throw new DataAccessException("Failed to set customer data", e);
        }
    }

    private void setDbDataForUpdate(Customer customer, PreparedStatement ps) {
        setDbData(customer, ps);
        try {
            ps.setString(8, customer.getId());
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update customer", e);
        }
    }
}