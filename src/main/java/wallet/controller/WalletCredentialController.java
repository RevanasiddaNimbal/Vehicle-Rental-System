package wallet.controller;

import wallet.model.Wallet;
import wallet.service.WalletCredentialService;
import wallet.service.WalletService;

import java.util.Scanner;

public class WalletCredentialController {
    private final WalletCredentialService service;
    private final WalletService walletService;
    private final Scanner input;

    public WalletCredentialController(Scanner input, WalletCredentialService service, WalletService walletService) {
        this.input = input;
        this.service = service;
        this.walletService = walletService;
    }

    public void resetPassword(String userId) {
        if (userId == null) {
            System.out.println("User id is required");
            return;
        }
        Wallet wallet = walletService.getWalletByUserId(userId);
        if (wallet == null) {
            System.out.println("User not found");
            return;
        }
        if (service.changePassword(input, wallet.getWalletId()))
            System.out.println("Password Reset successfully");
        else
            System.out.println("Failed to Reset password.Please try again");
    }

}
