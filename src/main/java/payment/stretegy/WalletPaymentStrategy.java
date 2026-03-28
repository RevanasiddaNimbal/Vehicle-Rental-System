package payment.stretegy;

import payment.dto.WalletPaymentDetails;
import transaction.model.TransactionStatus;
import transaction.model.TransactionType;
import transaction.service.TransactionService;
import wallet.model.Wallet;
import wallet.service.WalletService;

import java.util.concurrent.CompletableFuture;

public class WalletPaymentStrategy implements PaymentStrategy<WalletPaymentDetails> {
    private final WalletService walletService;
    private final TransactionService transactionService;

    public WalletPaymentStrategy(WalletService walletService, TransactionService transactionService) {
        this.walletService = walletService;
        this.transactionService = transactionService;
    }

    @Override
    public boolean pay(String customerId, double amount, WalletPaymentDetails details) {
        if (customerId == null) return false;

        Wallet customerWallet = walletService.getWalletByUserId(customerId);
        if (customerWallet == null) {
            throw new RuntimeException("Wallet not found.");
        }

        String pin = details.getPin();
        walletService.getBalanceByUserId(customerId, pin);

        String txId = transactionService.logTransfer(customerWallet.getWalletId(), "SYSTEM-ESCROW", amount,
                TransactionType.RENTAL_FARE, TransactionStatus.PENDING, "BULK-PAYMENT", "Initiating Wallet Payment");

        try {
            walletService.withdrawAmountByWalletId(pin, customerWallet.getWalletId(), amount, true);
            walletService.executeSystemTransfer(customerWallet.getWalletId(), "SYSTEM-ESCROW", amount);

            CompletableFuture.runAsync(() -> {
                transactionService.updateTransactionStatus(txId, TransactionStatus.SUCCESS);
            });
            return true;
        } catch (Exception e) {
            transactionService.updateTransactionStatus(txId, TransactionStatus.FAILED);
            throw new RuntimeException("Payment Authorization Failed: " + e.getMessage());
        }
    }
}