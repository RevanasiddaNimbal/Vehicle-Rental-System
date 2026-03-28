package wallet.stretegy;

import util.InputUtil;
import wallet.model.Wallet;
import wallet.service.WalletCredentialService;
import wallet.service.WalletService;

import java.util.Scanner;

public class WalletSetUpStrategy implements PostRegisterationStrategy {
    private final WalletService walletService;
    private final WalletCredentialService credentialService;
    private final Scanner input;

    public WalletSetUpStrategy(Scanner input, WalletService walletService, WalletCredentialService credentialService) {
        this.walletService = walletService;
        this.credentialService = credentialService;
        this.input = input;
    }

    @Override
    public void create(String userId) {
        try {
            Wallet wallet = walletService.createWallet(userId);
            System.out.println("Wallet created successfully! You received a sign-up bonus of Rs. 2000.");

            String pin = InputUtil.readValidPassword(input, "Set a secure Wallet PIN");
            credentialService.createWalletCredential(wallet.getWalletId(), pin);

            System.out.println("Wallet created successfully.");

        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("Wallet Setup Failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("System Error: Failed to initialize wallet.");
        }
    }
}