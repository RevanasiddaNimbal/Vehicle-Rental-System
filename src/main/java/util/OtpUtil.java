package util;

import exception.OtpVerificationException;
import otp.service.OtpService;

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
        try {
            otpService.sendOtp(email);
            System.out.println("OTP has been sent to " + email + " successfully. (Valid for 5 mins)");

            String code = InputUtil.readString(input, "Enter OTP");

            otpService.verifyOtp(email, code);

            System.out.println("OTP verified successfully.");
            return true;

        } catch (OtpVerificationException e) {
            System.out.println("OTP Error: " + e.getMessage());
            return false;
        }
    }
}
