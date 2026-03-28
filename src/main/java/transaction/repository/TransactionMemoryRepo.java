package transaction.repository;

import transaction.model.Transaction;
import transaction.model.TransactionStatus;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionMemoryRepo implements TransactionRepo {
    private final Map<String, Transaction> storage = new HashMap<>();

    @Override
    public boolean save(Transaction transaction) {
        storage.put(transaction.getTransactionId(), transaction);
        return true;
    }

    @Override
    public List<Transaction> findByWalletId(String walletId) {
        return storage.values().stream()
                .filter(t -> t.getSourceWalletId().equals(walletId) || t.getDestinationWalletId().equals(walletId))
                .sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp())) // DESC
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findByStatus(TransactionStatus status) {
        return storage.values().stream()
                .filter(t -> t.getStatus() == status)
                .sorted(Comparator.comparing(Transaction::getTimestamp))
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findByReferenceId(String referenceId) {
        return storage.values().stream()
                .filter(t -> t.getReferenceId() != null && t.getReferenceId().equals(referenceId))
                .sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp())) // DESC
                .collect(Collectors.toList());
    }
}