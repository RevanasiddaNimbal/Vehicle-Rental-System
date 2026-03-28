package transaction.repository;

import transaction.model.Transaction;
import transaction.model.TransactionStatus;

import java.util.List;

public interface TransactionRepo {
    boolean save(Transaction transaction);

    List<Transaction> findByWalletId(String walletId);

    List<Transaction> findByStatus(TransactionStatus status);

    List<Transaction> findByReferenceId(String referenceId);
}