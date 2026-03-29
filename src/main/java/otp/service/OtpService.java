package otp.service;

import exception.OtpVerificationException;
import notification.model.EmailMessage;
import notification.service.EmailService;
import otp.model.OtpData;
import otp.repository.OtpRepo;
import util.OtpUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class OtpService {
    private final OtpRepo repository;
    private final EmailService mailService;
    private final ExecutorService executorService;

    public OtpService(OtpRepo repository, EmailService mailService) {
        this.repository = repository;
        this.mailService = mailService;
        this.executorService = Executors.newFixedThreadPool(5);
    }

    public Future<Boolean> sendOtpAsync(String email) {
        return executorService.submit(() -> {

            String code = OtpUtil.generateOtp(6);
            long expiryTime = System.currentTimeMillis() + 5 * 60 * 1000;

            OtpData otpData = new OtpData(email, code, expiryTime);
            repository.save(otpData);

            String body = "You have requested an action in the Vehicle Rental system.\n" +
                    "Your OTP is: " + code + "\n" +
                    "This code will expire in 5 minutes.";

            EmailMessage message = new EmailMessage(email, "OTP Verification", body);

            if (!mailService.sendEmail(message)) {
                throw new OtpVerificationException("Failed to send OTP email.");
            }

            return true;
        });
    }

    public void sendOtp(String email) {
        if (email == null || email.isBlank()) {
            throw new OtpVerificationException("Invalid email address.");
        }
        sendOtpAsync(email);
    }

    public boolean verifyOtp(String email, String code) {
        OtpData otpData = repository.findByEmail(email);

        if (otpData == null) {
            throw new OtpVerificationException("No OTP found for this email. Please request a new one.");
        }

        if (otpData.isExpired()) {
            repository.deleteByEmail(email);
            throw new OtpVerificationException("OTP has expired. Please request a new one.");
        }

        if (otpData.getCode().equals(code)) {
            repository.deleteByEmail(email);
            return true;
        }
        return false;
    }

    public boolean isOtpGenerated(String email) {
        return repository.findByEmail(email) != null;
    }

    public void shutdown() {
        executorService.shutdown();
    }
}