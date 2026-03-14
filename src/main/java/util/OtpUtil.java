package util;

import authentication.service.OtpService;

import java.util.Random;
import java.util.Scanner;

public class OtpUtil {
    private static final Random random = new Random();

    public static String generateOtp(int length) {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    public static boolean isVerifiedOtp(Scanner input, OtpService otpService, String email) {
        otpService.sendOtp(email);
        for (int i = 0; i < 3; i++) {
            String userOtp = InputUtil.readString(input, "Enter OTP");
            if (otpService.verifyOtp(email, userOtp)) {
                return true;
            } else {
                System.out.println("Invalid OTP. Please try again.");
            }
        }
        return false;
    }
}
