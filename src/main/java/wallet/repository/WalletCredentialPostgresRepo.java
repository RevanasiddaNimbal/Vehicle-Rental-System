package wallet.repository;

import database.DatabaseConnection;
import exception.DataAccessException;
import wallet.model.WalletCredential;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WalletCredentialPostgresRepo implements WalletCredentialRepo {

    private final DatabaseConnection databaseConnection;

    public WalletCredentialPostgresRepo(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public boolean save(WalletCredential walletCredential) {
        String query = "INSERT INTO wallet_credentials (wallet_id, password) VALUES (?, ?)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, walletCredential.getWalletId());
            ps.setString(2, walletCredential.getPasswordHash());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to save wallet credential", e);
        }
    }

    @Override
    public boolean update(WalletCredential walletCredential) {
        String query = "UPDATE wallet_credentials SET password=? WHERE wallet_id=?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, walletCredential.getPasswordHash());
            ps.setString(2, walletCredential.getWalletId());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update wallet credential", e);
        }
    }

    @Override
    public WalletCredential findByWalletId(String walletId) {
        String query = "SELECT wallet_id, password FROM wallet_credentials WHERE wallet_id=?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, walletId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new WalletCredential(rs.getString("wallet_id"), rs.getString("password"));
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find wallet credential", e);
        }
    }
}