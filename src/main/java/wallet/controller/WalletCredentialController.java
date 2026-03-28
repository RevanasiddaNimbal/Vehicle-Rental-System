package wallet.controller;

import exception.ResourceNotFoundException;
import util.InputUtil;
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

    public void forgotPassword(String userId) {
        if (userId == null) {
            System.out.println("User ID is required.");
            return;
        }

        try {
            Wallet wallet = walletService.getWalletByUserId(userId);
            if (wallet == null) {
                System.out.println("User doesn't have a wallet.");
                return;
            }

            String newPassword = InputUtil.readValidPassword(input, "Enter new PIN");
            String confirmPassword = InputUtil.readValidPassword(input, "Confirm new PIN");

            if (!newPassword.equals(confirmPassword)) {
                System.out.println("PINs do not match. Please try again.");
                return;
            }

            service.forgotPassword(wallet.getWalletId(), newPassword);
            System.out.println("Wallet PIN successfully recovered and updated.");

        } catch (ResourceNotFoundException | IllegalStateException e) {
            System.out.println("Failed to update PIN: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("System Error: An unexpected error occurred.");
        }
    }
}