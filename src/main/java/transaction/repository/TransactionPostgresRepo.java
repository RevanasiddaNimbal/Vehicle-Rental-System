package transaction.repository;


import database.DatabaseConnection;
import exception.DataAccessException;
import transaction.model.Transaction;
import transaction.model.TransactionStatus;
import transaction.model.TransactionType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionPostgresRepo implements TransactionRepo {
    private final DatabaseConnection databaseConnection;

    public TransactionPostgresRepo(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public boolean save(Transaction transaction) {
        String query = "INSERT INTO transactions (transaction_id, source_wallet_id, destination_wallet_id, amount, type, status, reference_id, description, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, transaction.getTransactionId());
            ps.setString(2, transaction.getSourceWalletId());
            ps.setString(3, transaction.getDestinationWalletId());
            ps.setDouble(4, transaction.getAmount());
            ps.setString(5, transaction.getType().name());
            ps.setString(6, transaction.getStatus().name());
            ps.setString(7, transaction.getReferenceId());
            ps.setString(8, transaction.getDescription());
            ps.setTimestamp(9, Timestamp.valueOf(transaction.getTimestamp()));

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to save transaction log.", e);
        }
    }

    @Override
    public List<Transaction> findByWalletId(String walletId) {
        String query = "SELECT * FROM transactions WHERE source_wallet_id = ? OR destination_wallet_id = ? ORDER BY timestamp DESC";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, walletId);
            ps.setString(2, walletId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(readTransactionsDbData(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch transactions for wallet.", e);
        }
        return transactions;
    }

    @Override
    public List<Transaction> findByStatus(TransactionStatus status) {
        String query = "SELECT * FROM transactions WHERE status = ? ORDER BY timestamp ASC";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, status.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(readTransactionsDbData(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch transactions by status.", e);
        }
        return transactions;
    }

    @Override
    public List<Transaction> findByReferenceId(String referenceId) {
        String query = "SELECT * FROM transactions WHERE reference_id = ? ORDER BY timestamp DESC";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, referenceId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(readTransactionsDbData(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch transactions by reference ID.", e);
        }
        return transactions;
    }

    private Transaction readTransactionsDbData(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getString("transaction_id"),
                rs.getString("source_wallet_id"),
                rs.getString("destination_wallet_id"),
                rs.getDouble("amount"),
                TransactionType.valueOf(rs.getString("type")),
                TransactionStatus.valueOf(rs.getString("status")),
                rs.getString("reference_id"),
                rs.getString("description"),
                rs.getTimestamp("timestamp").toLocalDateTime()
        );
    }
}