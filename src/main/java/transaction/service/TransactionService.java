package transaction.service;

import transaction.model.Transaction;
import transaction.model.TransactionStatus;
import transaction.model.TransactionType;
import transaction.repository.TransactionRepo;
import util.IdGeneratorUtil;
import util.IdPrefix;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionService {
    private final TransactionRepo transactionRepo;

    public TransactionService(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;

    }

    public void logDeposit(String walletId, double amount, String description) {
        String txId = IdGeneratorUtil.generate(IdPrefix.TRX);
        Transaction transaction = new Transaction(
                txId, "BANK", walletId, amount,
                TransactionType.WALLET_DEPOSIT, TransactionStatus.SUCCESS,
                null, description, LocalDateTime.now()
        );
        transactionRepo.save(transaction);
    }

    public void logWithdrawal(String walletId, double amount, String description) {
        String txId = IdGeneratorUtil.generate(IdPrefix.TRX);
        Transaction transaction = new Transaction(
                txId, walletId, "BANK", amount,
                TransactionType.WALLET_WITHDRAWAL, TransactionStatus.SUCCESS,
                null, description, LocalDateTime.now()
        );
        transactionRepo.save(transaction);
    }

    public String logTransfer(String sourceWalletId, String destinationWalletId, double amount,
                              TransactionType type, TransactionStatus status, String referenceId, String description) {
        String txId = IdGeneratorUtil.generate(IdPrefix.TRX);
        Transaction transaction = new Transaction(
                txId, sourceWalletId, destinationWalletId, amount,
                type, status, referenceId, description, LocalDateTime.now()
        );
        transactionRepo.save(transaction);
        return txId;
    }

    public void updateTransactionStatus(String transactionId, TransactionStatus status) {
        transactionRepo.updateStatus(transactionId, status);
    }

    public void logFailedPayout(String escrowWalletId, String ownerWalletId, double amount, String rentalId, String reason) {
        String txId = IdGeneratorUtil.generate(IdPrefix.TRX);
        Transaction failedAttempt = new Transaction(
                txId, escrowWalletId, ownerWalletId, amount,
                TransactionType.OWNER_PAYOUT, TransactionStatus.FAILED,
                rentalId, "Payout Failed: " + reason, LocalDateTime.now()
        );
        transactionRepo.save(failedAttempt);
    }

    public List<Transaction> getTransactionsByWalletId(String walletId) {
        return transactionRepo.findByWalletId(walletId);
    }

    public List<Transaction> getPendingEscrowTransactions() {
        return transactionRepo.findByStatus(TransactionStatus.PENDING);
    }

    public List<Transaction> getTransactionsByWalletIdAndDays(String walletId, int daysToLookBack) {
        int daysOffset = daysToLookBack - 1;
        LocalDateTime cutoffDate = LocalDate.now().minusDays(daysOffset).atStartOfDay();

        List<Transaction> allTransactions = transactionRepo.findByWalletId(walletId);
        return allTransactions.stream()
                .filter(t -> t.getTimestamp() != null && t.getTimestamp().isAfter(cutoffDate))
                .toList();
    }

    public List<Transaction> getUsersTransactionsByWalletIdAndDays(String walletId, int daysToLookBack) {

        List<Transaction> filteredByDays = getTransactionsByWalletIdAndDays(walletId, daysToLookBack);

        return filteredByDays.stream()
                .filter(this::isUserVisible)
                .sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()))
                .toList();
    }

    public List<Transaction> getAdminTransactionsByWalletIdAndDays(String walletId, int daysToLookBack) {

        List<Transaction> filteredByDays = getTransactionsByWalletIdAndDays(walletId, daysToLookBack);

        return filteredByDays.stream()
                .filter(this::isAdminVisible)
                .sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()))
                .toList();
    }

    public List<Transaction> getUsersTransactionsByWalletId(String walletId) {

        List<Transaction> all = getTransactionsByWalletId(walletId);

        return all.stream()
                .filter(this::isUserVisible)
                .sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()))
                .toList();
    }

    public List<Transaction> getAdminTransactionsByWalletId(String walletId) {

        List<Transaction> all = getTransactionsByWalletId(walletId);

        return all.stream()
                .filter(this::isAdminVisible)
                .sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()))
                .toList();
    }

    private String getDesc(Transaction t) {
        return t.getDescription() != null ? t.getDescription().toLowerCase() : "";
    }

    private boolean isUserVisible(Transaction t) {
        String desc = getDesc(t);
        return !(desc.contains("rental fare") || desc.contains("deposit payment"));
    }

    private boolean isAdminVisible(Transaction t) {
        String desc = getDesc(t);
        return !desc.contains("wallet payment")
                && !desc.contains("rental payment");
    }
}