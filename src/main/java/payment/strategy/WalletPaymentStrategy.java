package payment.strategy;

import exception.InsufficientFundsException;
import exception.ResourceNotFoundException;
import payment.dto.WalletPaymentDetails;
import transaction.model.TransactionStatus;
import transaction.model.TransactionType;
import transaction.service.TransactionService;
import wallet.model.Wallet;
import wallet.service.WalletService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class WalletPaymentStrategy implements PaymentStrategy<WalletPaymentDetails> {
    private static final String DEFAULT_REFERENCE_ID = "BULK-PAYMENT";
    private static final String SYSTEM_ESCROW_ID = "SYSTEM-ESCROW";

    private final WalletService walletService;
    private final TransactionService transactionService;
    private final ExecutorService executorService;

    public WalletPaymentStrategy(WalletService walletService, TransactionService transactionService, ExecutorService executorService) {
        this.walletService = walletService;
        this.transactionService = transactionService;
        this.executorService = executorService;
    }

    @Override
    public boolean pay(String customerId, double amount, WalletPaymentDetails details) {
        if (customerId == null || details == null || amount <= 0) {
            return false;
        }

        Wallet customerWallet = getValidWallet(customerId);
        validateSufficientBalance(customerId, details.getPin(), amount);

        String txId = initiatePendingTransaction(customerWallet.getWalletId(), amount);

        try {
            walletService.executeSystemTransfer(customerWallet.getWalletId(), SYSTEM_ESCROW_ID, amount);
            finalizeTransactionAsync(txId);
            return true;
        } catch (Exception e) {
            handlePaymentFailure(txId, e);
            throw new RuntimeException("Payment Authorization Failed: " + e.getMessage(), e);
        }
    }

    private Wallet getValidWallet(String customerId) {
        Wallet wallet = walletService.getWalletByUserId(customerId);
        if (wallet == null) {
            throw new ResourceNotFoundException("Wallet not found.");
        }
        return wallet;
    }

    private void validateSufficientBalance(String customerId, String pin, double amount) {
        double currentBalance = walletService.getBalanceByUserId(customerId, pin);
        if (currentBalance < amount) {
            throw new InsufficientFundsException("Insufficient wallet balance.");
        }
    }

    private String initiatePendingTransaction(String walletId, double amount) {
        return transactionService.logTransfer(
                walletId,
                SYSTEM_ESCROW_ID,
                amount,
                TransactionType.RENTAL_FARE,
                TransactionStatus.PENDING,
                DEFAULT_REFERENCE_ID,
                "Rental Payment"
        );
    }

    private void finalizeTransactionAsync(String txId) {
        CompletableFuture.runAsync(() -> {
            transactionService.updateTransactionStatus(txId, TransactionStatus.SUCCESS);
        }, executorService).exceptionally(ex -> {
            transactionService.updateTransactionStatus(txId, TransactionStatus.FAILED);
            return null;
        });
    }

    private void handlePaymentFailure(String txId, Exception e) {
        transactionService.updateTransactionStatus(txId, TransactionStatus.FAILED);
    }
}