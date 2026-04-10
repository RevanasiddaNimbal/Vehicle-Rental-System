package payment.factory;

import payment.model.PaymentMethod;
import payment.strategy.PaymentStrategy;
import payment.strategy.WalletPaymentStrategy;
import transaction.service.TransactionService;
import wallet.service.WalletService;

import java.util.concurrent.ExecutorService;

public class PaymentStrategyFactory {
    private final WalletService walletService;
    private final TransactionService transactionService;
    private final ExecutorService executorService;

    public PaymentStrategyFactory(WalletService walletService, TransactionService transactionService, ExecutorService executorService) {
        this.walletService = walletService;
        this.transactionService = transactionService;
        this.executorService = executorService;
    }

    public PaymentStrategy getStrategy(PaymentMethod method) {
        if (method == PaymentMethod.WALLET) {
            return new WalletPaymentStrategy(walletService, transactionService, executorService);
        }
        throw new IllegalArgumentException("Payment method not supported yet.");
    }
}