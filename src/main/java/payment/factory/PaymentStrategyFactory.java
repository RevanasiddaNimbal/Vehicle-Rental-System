package payment.factory;

import payment.model.PaymentMethod;
import payment.strategy.PaymentStrategy;
import payment.strategy.WalletPaymentStrategy;
import transaction.service.TransactionService;
import wallet.service.WalletService;

public class PaymentStrategyFactory {
    private final WalletService walletService;
    private final TransactionService transactionService;

    public PaymentStrategyFactory(WalletService walletService, TransactionService transactionService) {
        this.walletService = walletService;
        this.transactionService = transactionService;
    }

    public PaymentStrategy getStrategy(PaymentMethod method) {
        if (method == PaymentMethod.WALLET) {
            return new WalletPaymentStrategy(walletService, transactionService);
        }
        throw new IllegalArgumentException("Payment method not supported yet.");
    }
}