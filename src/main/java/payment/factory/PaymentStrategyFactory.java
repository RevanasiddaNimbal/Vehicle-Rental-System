package payment.factory;


import payment.model.PaymentMethod;
import payment.stretegy.PaymentStrategy;
import payment.stretegy.WalletPaymentStrategy;
import wallet.service.WalletService;

public class PaymentStrategyFactory {
    private final WalletService walletService;

    public PaymentStrategyFactory(WalletService walletService) {
        this.walletService = walletService;
    }

    public PaymentStrategy getStrategy(PaymentMethod method) {
        switch (method) {
            case WALLET:
                return new WalletPaymentStrategy(walletService);
            default:
                throw new IllegalArgumentException("Payment method not supported yet.");
        }
    }
}