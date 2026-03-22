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
        Wallet wallet = walletService.createWallet(input, userId);

        String pin = InputUtil.readValidPassword(input, "Set Wallet PIN");

        credentialService.createWalletCredential(wallet.getWalletId(), pin);
        System.out.println("Wallet created successfully with walletId: " + wallet.getWalletId());
    }
}
