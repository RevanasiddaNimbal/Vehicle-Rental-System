package payment.stretegy;

import wallet.model.Wallet;
import wallet.service.WalletService;

import java.util.Scanner;

public class WalletPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;

    public WalletPaymentStrategy(WalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public boolean pay(Scanner input, String customerId, double amount) {
        if (customerId == null) return false;
        try {
            Wallet customerWallet = walletService.getWalletByUserId(customerId);
            if (customerWallet == null) return false;

            walletService.withdrawAmountByWalletId(input, customerWallet.getWalletId(), amount, true);

            walletService.executeSystemTransfer(customerWallet.getWalletId(), "SYSTEM-ESCROW", amount);
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("Payment Failed: " + e.getMessage());
            return false;
        }
    }
}