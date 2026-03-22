package wallet.repository;

import database.DatabaseConnection;
import exception.DataAccessException;
import wallet.model.Wallet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WalletPostgresRepo implements WalletRepo {

    private final DatabaseConnection databaseConnection;

    public WalletPostgresRepo(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public boolean save(Wallet wallet) {
        String query = "INSERT INTO wallets (user_id, balance) VALUES (?, ?)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query, new String[]{"wallet_id"})) {

            ps.setString(1, wallet.getUserId());
            ps.setDouble(2, wallet.getBalance());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet id = ps.getGeneratedKeys()) {
                    if (id.next()) {
                        wallet.setWalletId(id.getString(1));
                    }
                }
            }
            return rows > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to save wallet", e);
        }
    }

    @Override
    public boolean update(Wallet wallet) {
        String query = "UPDATE wallets SET balance=? WHERE wallet_id=?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setDouble(1, wallet.getBalance());
            ps.setString(2, wallet.getWalletId());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update wallet", e);
        }
    }

    @Override
    public Wallet findByWalletId(String walletId) {
        String query = "SELECT wallet_id, user_id, balance FROM wallets WHERE wallet_id=?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, walletId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Wallet(rs.getString("wallet_id"), rs.getString("user_id"), rs.getDouble("balance"));
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find wallet by walletId", e);
        }
    }

    @Override
    public Wallet findByUserId(String userId) {
        String query = "SELECT wallet_id, user_id, balance FROM wallets WHERE user_id=?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Wallet(rs.getString("wallet_id"), rs.getString("user_id"), rs.getDouble("balance"));
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find wallet by userId", e);
        }
    }
}