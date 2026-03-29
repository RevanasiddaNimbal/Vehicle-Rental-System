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

        if (email == null || email.isBlank()) {
            System.out.println("Invalid email.");
            return false;
        }

        try {
            otpService.sendOtp(email);

            System.out.println("Sending OTP to " + email + "...");

            int retries = 10;
            while (retries-- > 0) {
                if (otpService.isOtpGenerated(email)) {
                    break;
                }
                Thread.sleep(500);
            }

            if (!otpService.isOtpGenerated(email)) {
                System.out.println("Failed to generate OTP. Please try again.");
                return false;
            }

            System.out.println("OTP has been sent successfully. (Valid for 5 mins)");
            int count = 1;
            while (count <= 3) {
                String code = InputUtil.readString(input, "Enter OTP");
                if (otpService.verifyOtp(email, code)) {
                    System.out.println("OTP verified successfully.");
                    return true;
                } else {
                    System.out.println("Invalid Otp. Please try again.");
                    count++;
                }
            }
            return false;
        } catch (OtpVerificationException e) {
            System.out.println("OTP Error: " + e.getMessage());
            return false;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Process interrupted.");
            return false;

        } catch (Exception e) {
            System.out.println("Unexpected error occurred.");
            return false;
        }
    }
}
