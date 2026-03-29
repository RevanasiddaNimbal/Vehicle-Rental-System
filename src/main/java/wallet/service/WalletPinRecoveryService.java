package wallet.service;

import exception.ResourceNotFoundException;
import otp.service.OtpService;
import user.factory.UserResolverFactory;
import user.model.UserDetails;
import util.OtpUtil;
import wallet.model.Wallet;

import java.util.Scanner;

public class WalletPinRecoveryService {
    private final WalletService walletService;
    private final WalletCredentialService walletCredentialService;
    private final UserResolverFactory userResolverFactory;
    private final OtpService otpService;


    public WalletPinRecoveryService(WalletService walletService, WalletCredentialService walletCredentialService, OtpService otpService, UserResolverFactory userResolverFactory) {
        this.walletService = walletService;
        this.walletCredentialService = walletCredentialService;
        this.otpService = otpService;
        this.userResolverFactory = userResolverFactory;
    }

    public boolean resetPinWithOtp(String userId, String newPin, Scanner input) {

        try {
            Wallet wallet = walletService.getWalletByUserId(userId);

            if (wallet == null) {
                throw new ResourceNotFoundException("Wallet not found.");
            }

            UserDetails userDetails = userResolverFactory.resolveUserById(userId);
            String email = userDetails.getEmail();

            boolean isVerified = OtpUtil.isVerifiedOtp(input, otpService, email);

            if (!isVerified) {
                return false;
            }

            walletCredentialService.forgotPassword(wallet.getWalletId(), newPin);
            return true;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}