package wallet.controller;

import exception.InvalidCredentialsException;
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

    public void resetPassword(String userId) {
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

            String oldPassword = InputUtil.readValidPassword(input, "Enter old PIN");
            String newPassword = InputUtil.readValidPassword(input, "Enter new PIN");

            service.changePassword(wallet.getWalletId(), oldPassword, newPassword);
            System.out.println("Wallet PIN reset successfully.");

        } catch (InvalidCredentialsException | ResourceNotFoundException | IllegalStateException e) {
            System.out.println("Failed to Reset PIN: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("System Error: An unexpected error occurred.");
        }
    }
}