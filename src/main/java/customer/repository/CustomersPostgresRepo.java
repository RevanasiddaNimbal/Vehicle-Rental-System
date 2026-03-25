package customer.repository;

import customer.model.Customer;
import database.DatabaseConnection;
import exception.DataAccessException;
import exception.DuplicateResourceException;

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
                "(id, name, email, phone, address, driving_license_number, password, active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, TRUE)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, customer.getId());
            setDbData(customer, ps, 2);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new DuplicateResourceException("Database rejected registration: Email or License already exists.");
            }
            throw new DataAccessException("Failed to save customer", e);
        }
    }

    @Override
    public boolean update(Customer customer) {
        String query = "UPDATE customers SET " +
                "name=?, email=?, phone=?, address=?, driving_license_number=?, password=? " +
                "WHERE id=? AND active=TRUE";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            setDbData(customer, ps, 1);
            ps.setString(7, customer.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new DuplicateResourceException("Database rejected update: Email or License already in use.");
            }
            throw new DataAccessException("Failed to update customer", e);
        }
    }

    @Override
    public boolean deactivateById(String id) {
        String query = "UPDATE customers SET active=false WHERE id=? AND active=TRUE";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to deactivate customer", e);
        }
    }

    @Override
    public boolean activateById(String id) {
        String query = "UPDATE customers SET active=true WHERE id=? AND active=FALSE";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
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
                if (rs.next()) return readDbResult(rs);
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
        String query = "SELECT * FROM customers WHERE email=? AND active=TRUE";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return readDbResult(rs);
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

            Customer customer = new Customer(id, name, email, phone, address, drivingLicense, password);
            if (!active) {
                customer.deactivate();
            }
            return customer;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to read customer", e);
        }
    }

    private void setDbData(Customer customer, PreparedStatement ps, int startIndex) throws SQLException {
        ps.setString(startIndex, customer.getName());
        ps.setString(startIndex + 1, customer.getEmail());
        ps.setString(startIndex + 2, customer.getPhone());
        ps.setString(startIndex + 3, customer.getAddress());
        ps.setString(startIndex + 4, customer.getDrivingLicenseNumber());
        ps.setString(startIndex + 5, customer.getPassword());
    }
}