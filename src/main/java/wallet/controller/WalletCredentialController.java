package wallet.controller;

import util.InputUtil;
import wallet.service.WalletPinRecoveryService;

import java.util.Scanner;

public class WalletCredentialController {
    private final WalletPinRecoveryService pinRecoveryService;
    private final Scanner input;

    public WalletCredentialController(Scanner input, WalletPinRecoveryService pinRecoveryService) {
        this.input = input;
        this.pinRecoveryService = pinRecoveryService;
    }

    public void forgotPassword(String userId) {

        if (userId == null) {
            System.out.println("User ID is required.");
            return;
        }

        try {
            String newPin = InputUtil.readValidPassword(input, "Enter new PIN");
            String confirmPin = InputUtil.readValidPassword(input, "Confirm new PIN");

            if (!newPin.equals(confirmPin)) {
                System.out.println("PINs do not match.");
                return;
            }

            boolean success = pinRecoveryService.resetPinWithOtp(userId, newPin, input);

            if (success) {
                System.out.println("Wallet PIN successfully reset.");
            } else {
                System.out.println("Failed to reset PIN.");
            }

        } catch (Exception e) {
            System.out.println("Unexpected error occurred.");
        }
    }
}