package transaction.model;

import java.time.LocalDateTime;

public class Transaction {
    private final String transactionId;
    private final String sourceWalletId;
    private final String destinationWalletId;
    private final double amount;
    private final TransactionType type;
    private TransactionStatus status;
    private final String referenceId;
    private final String description;
    private final LocalDateTime timestamp;

    public Transaction(String transactionId, String sourceWalletId, String destinationWalletId,
                       double amount, TransactionType type, TransactionStatus status,
                       String referenceId, String description, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.sourceWalletId = sourceWalletId;
        this.destinationWalletId = destinationWalletId;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.referenceId = referenceId;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getSourceWalletId() {
        return sourceWalletId;
    }

    public String getDestinationWalletId() {
        return destinationWalletId;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}